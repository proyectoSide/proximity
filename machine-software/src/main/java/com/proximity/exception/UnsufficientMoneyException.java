package com.proximity.exception;

public class UnsufficientMoneyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnsufficientMoneyException(int minExpected, int ammount) {
		super("El saldo cargado no es suficiente. Monto a pagar: " + minExpected + " centavos . Valor ingresado: "
				+ ammount + " centavos.");
	}

}
