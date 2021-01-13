package com.proximity.exception;

public class InvalidItemException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidItemException() {
		super("El producto seleccionado es invalido o fue removido.");
	}

}
