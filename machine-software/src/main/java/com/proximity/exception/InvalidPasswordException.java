package com.proximity.exception;

public class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super("La contrasena ingresada es invalida.");
	}

}
