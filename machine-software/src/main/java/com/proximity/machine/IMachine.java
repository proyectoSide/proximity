package com.proximity.machine;

import com.proximity.dto.OrderDTO;
import com.proximity.dto.OrderResponseDTO;
import com.proximity.model.ChangeModel;

public interface IMachine {

	public OrderResponseDTO processPayment(OrderDTO order, int totalToCharge, Iterable<ChangeModel> moneyInMachine);
	
}
