package com.mount.ambev.component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mount.ambev.config.RabbitConfig;
import com.mount.ambev.entity.Pedido;
import com.mount.ambev.service.PedidoService;

@Component
public class PedidoListener {
	
	private final PedidoService pedidoService;
	
    @Autowired
    public PedidoListener(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(Pedido pedido) {
        System.out.println("Pedido recebido: " + pedido);
    
        if (pedido.getProdutos() == null || pedido.getProdutos().isEmpty()) {
            System.out.println("Pedido inválido: sem produtos.");
            return;
        }

        pedido.setStatus("PROCESSADO");
        pedidoService.atualizarPedido(pedido);

        enviarMensagemPedidoAtualizado(pedido);
        
        System.out.println("Pedido processado: " + pedido);
    }

    private void enviarMensagemPedidoAtualizado(Pedido pedido) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5672/api/pedidos";
        try {
            restTemplate.postForEntity(url, pedido, String.class);
            System.out.println("Pedido processado: " + pedido);
        } catch (Exception e) {
            System.out.println("Erro ao enviar o pedido: " + e.getMessage());
        }
    }        
}
