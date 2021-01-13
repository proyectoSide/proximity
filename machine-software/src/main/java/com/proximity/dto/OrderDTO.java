package com.proximity.dto;

import java.util.List;

import javax.validation.constraints.Min;

import com.proximity.enums.PaymentUnits;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class OrderDTO {

	@NotNull
	@Min(1)
	private Integer quantity;

	@NotNull
	private Integer itemId;

	private String creditCard;
	
	private List<PaymentUnits> payment;

}
