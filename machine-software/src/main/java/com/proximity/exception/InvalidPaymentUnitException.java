package com.proximity.exception;

public class InvalidPaymentUnitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPaymentUnitException() {
		super("Se ha cargado un pago invalido");
	}

}
