package com.proximity.machine;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MachineStatus {

	private String model;
	private String password;
	private String passwordSystem;

	private boolean activeAlert;
	private boolean wasNotified;

	private int countIntent;
	private String dayIntent;

	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private IMachine machine = null;

	public IMachine getCurrentMachine() {
		if (Objects.isNull(machine))
			machine = MachineFactory.create(model);
		return machine;
	}

}
