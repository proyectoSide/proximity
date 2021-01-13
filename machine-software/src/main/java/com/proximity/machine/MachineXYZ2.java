package com.proximity.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.proximity.dto.OrderDTO;
import com.proximity.dto.OrderResponseDTO;
import com.proximity.enums.PaymentUnits;
import com.proximity.exception.InvalidCardException;
import com.proximity.exception.InvalidPaymentUnitException;
import com.proximity.model.ChangeModel;

public class MachineXYZ2 extends MachineXYZ1 implements IMachine {

	
	@Override
	public OrderResponseDTO processPayment(OrderDTO order, int totalToPay,
			Iterable<ChangeModel> moneyInMachine) {

		List<PaymentUnits> paymentCharged = order.getPayment();
		
		Optional<PaymentUnits> notCashPayment = paymentCharged.stream().filter(item -> !supported.contains(item))
				.findAny();

		boolean onlyInCashAllowed = notCashPayment.isEmpty();
		boolean paymentOnlyWithCreditCard = !paymentCharged.isEmpty() && paymentCharged.size() == 1
				&& paymentCharged.get(0).equals(PaymentUnits.CREDIT_CARD);

		if (!(onlyInCashAllowed || paymentOnlyWithCreditCard))
			throw new InvalidPaymentUnitException();

		if (onlyInCashAllowed) {
			return processCashPayment(paymentCharged, totalToPay, moneyInMachine);
		} else {

			// External API to validate card
			boolean isValid = isValidCreditCard(order.getCreditCard());
			if (!isValid)
				throw new InvalidCardException();
				
			OrderResponseDTO response = new OrderResponseDTO();
			response.setPaymentMethod("CREDIT_CARD");
			response.setBill(createBill(order, response, totalToPay));
			return response;
		}

	}

	private List<String> createBill(OrderDTO order, OrderResponseDTO response, int total ) {
		
		List<String> bill = new ArrayList<String>();
		bill.add(createLine(""));
		bill.add(createLine("-------------- Bill Example  "));
		bill.add(createLine(""));
		bill.add(createLine("-- Card Number: " + order.getCreditCard() + " "));
		bill.add(createLine(""));
		bill.add(createLine("-- Item ID: " + order.getItemId() + " "));
		bill.add(createLine("-- Quantity: " + order.getQuantity() + " "));
		bill.add(createLine(""));
		bill.add(createLine("---Total: " + total + "cents."));
		bill.add(createLine(""));
		bill.add(createLine(""));

		return bill;
	}
	
	private String createLine(String content) {
		return (content + StringUtils.join(Collections.nCopies(999, "-"),'-')).substring(0, 50);
	}

	private boolean isValidCreditCard(String number) {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		HttpEntity<String> entity = new HttpEntity<>(
				"action=ajax_credit_card_validate&text=" + number + "&cc=Visa", headers);
		ResponseEntity<String> response = rest.postForEntity("https://www.tools4noobs.com/", entity, String.class);
		return response.getBody().contains("This credit card numer is valid!");
	}

}
