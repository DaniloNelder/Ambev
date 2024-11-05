package com.mount.ambev.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.mount.ambev.entity.Pedido;
import com.mount.ambev.entity.Produto;
import com.mount.ambev.exception.DuplicacaoPedidoException;
import com.mount.ambev.repository.PedidoRepository;
import com.mount.ambev.repository.ProdutoRepository;

@Service
public class PedidoService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final RabbitTemplate rabbitTemplate;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private static final String QUEUE_NAME = "pedido.criado";

    @Autowired
    private CacheManager cacheManager; 

    @Autowired
    public PedidoService(RabbitTemplate rabbitTemplate, PedidoRepository pedidoRepository, ProdutoRepository produtoRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
    }

    @Cacheable(value = "receberPedido", key = "#pedido.idExterno")
    public Pedido receberPedido(Pedido pedido) {
        validarDuplicacaoPedido(pedido);
        
        List<Produto> produtosValidos = vincularProdutosAoPedido(pedido);
        
        pedido.setProdutos(produtosValidos);
        pedido.calcularTotal();
        pedido.setStatus("PENDENTE");
        
        pedidoRepository.save(pedido);
        armazenarNoCache(pedido);
        enviarPedido(pedido);
        
        return pedido;
    }

    private void validarDuplicacaoPedido(Pedido pedido) {
        Cache cache = cacheManager.getCache("pedidoCache");
        if (cache != null && cache.get(pedido.getIdExterno()) != null) {
            throw new DuplicacaoPedidoException("Pedido já existe com ID: " + pedido.getIdExterno());
        }
    }

    private List<Produto> vincularProdutosAoPedido(Pedido pedido) {
        return pedido.getProdutos().stream()
            .map(produto -> produtoRepository.findByDescricao(produto.getDescricao())
                .orElseGet(() -> criarProduto(produto)))
            .collect(Collectors.toList());
    }

    private Produto criarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    private void armazenarNoCache(Pedido pedido) {
        Cache cache = cacheManager.getCache("pedidoCache");
        if (cache != null) {
            cache.put(pedido.getIdExterno(), pedido);
        }
    }

    private void enviarPedido(Pedido pedido) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, pedido);
        logger.info("Pedido enviado para a fila: {}", pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido atualizarPedido(Pedido pedido) {
        if (pedido.getId() == null || !pedidoRepository.existsById(pedido.getId())) {
            throw new IllegalArgumentException("Pedido não encontrado para atualização.");
        }
        
        List<Produto> produtosValidos = vincularProdutosAoPedido(pedido);
        pedido.setProdutos(produtosValidos);
        
        return pedidoRepository.save(pedido);
    }
}
