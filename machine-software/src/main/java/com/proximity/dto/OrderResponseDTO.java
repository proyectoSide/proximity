package com.proximity.dto;
import java.util.List;

import com.proximity.enums.PaymentUnits;

import lombok.Data;

@Data
public class OrderResponseDTO {

	private List<PaymentUnits> change;
	private String paymentMethod;
	private List<String> bill;

}
