package com.proximity.exception;

public class OutOfStockProductException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OutOfStockProductException() {
		super("El stock requerido es mayor que el disponible.");
	}
	
}
