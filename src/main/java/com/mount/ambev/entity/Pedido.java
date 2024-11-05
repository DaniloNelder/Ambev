package com.mount.ambev.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pedido")
public class Pedido implements Serializable {
	
	private static final long serialVersionUID = 2L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    @JoinTable(
        name = "pedido_produto",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> produtos;
    private BigDecimal total;
    private String status;
    @Column(unique = true)
    private String idExterno;

    public void calcularTotal() {
        this.total = produtos.stream()
                .map(produto -> produto.getPrecoUnitario().multiply(new BigDecimal(produto.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

