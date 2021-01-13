package com.proximity.machine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proximity.dto.OrderDTO;
import com.proximity.dto.OrderResponseDTO;
import com.proximity.enums.PaymentUnits;
import com.proximity.exception.InvalidPaymentUnitException;
import com.proximity.model.ChangeModel;
import com.proximity.utils.MachineUtils;

@Service
public class MachineXYZ1 implements IMachine {

	protected static List<PaymentUnits> supported = Arrays.asList(PaymentUnits.COIN_05_CENTS,
			PaymentUnits.COIN_10_CENTS, PaymentUnits.COIN_25_CENTS, PaymentUnits.COIN_50_CENTS,
			PaymentUnits.BILL_1_DOLAR, PaymentUnits.BILL_2_DOLAR);

	public OrderResponseDTO processPayment(OrderDTO order, int totalToPay,
			Iterable<ChangeModel> moneyInMachine) {

		List<PaymentUnits> paymentCharged = order.getPayment();
		
		// Checks if machine supports the charged payment/s
		boolean notAllowed = paymentCharged.stream().anyMatch(item -> !supported.contains(item));
		if (notAllowed)
			throw new InvalidPaymentUnitException();

		
		return processCashPayment(paymentCharged, totalToPay, moneyInMachine);
	}

	protected OrderResponseDTO processCashPayment(List<PaymentUnits> paymentCharged, int totalToPay,
			Iterable<ChangeModel> moneyInMachine) {
		int totalCharged = MachineUtils.checkIfMoneyEnought(paymentCharged, totalToPay);

		int moneyToReturn = totalCharged - totalToPay;

		List<PaymentUnits> changeToReturn = new ArrayList<PaymentUnits>();

		if (moneyToReturn > 0) {
			changeToReturn = MachineUtils.calculateChange(moneyToReturn, paymentCharged, moneyInMachine, false);
		}

		OrderResponseDTO response = new OrderResponseDTO();
		response.setChange(changeToReturn);
		response.setPaymentMethod("CASH");
		return response;
	}



}
