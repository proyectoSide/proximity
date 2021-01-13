package com.proximity.exception;

public class InvalidCardException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidCardException() {
		super("La tarjeta ingresada es invalida. Compruebe que sea emitida por Visa y que el numero sea correcto.");
	}

}
