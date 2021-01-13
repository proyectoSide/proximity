package com.proximity.machine;

public class MachineFactory {

	public static IMachine create(String model) {

		switch (model) {
		case "XYZ1":
			return new MachineXYZ1();
		case "XYZ2":
			return new MachineXYZ2();

		default:
			return new MachineXYZ1();
		}

	}

}
