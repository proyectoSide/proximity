package com.proximity.exception;

public class BlockedMachineException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BlockedMachineException() {
		super("La maquina se encuentra bloqueada.");
	}

}
