package com.mount.ambev.exception;

public class DuplicacaoPedidoException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public DuplicacaoPedidoException(String message) {
        super(message);
    }

    public DuplicacaoPedidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
