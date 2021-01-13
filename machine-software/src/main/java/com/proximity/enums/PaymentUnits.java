package com.proximity.enums;

public enum PaymentUnits {

	CREDIT_CARD(Integer.MAX_VALUE),

	BILL_100_DOLAR(100*100), 
	BILL_50_DOLAR(50*100), 
	BILL_20_DOLAR(20*100),
	BILL_10_DOLAR(10*100), 
	BILL_5_DOLAR(5*100),
	BILL_2_DOLAR(2*100), 
	BILL_1_DOLAR(100),
	
	COIN_05_CENTS(5), 
	COIN_10_CENTS(10), 
	COIN_25_CENTS(25),
	COIN_50_CENTS(50);

	private final Integer centsValue;

	PaymentUnits(Integer centsValue) {
		this.centsValue = centsValue;
	}

	public Integer getCentsValue() {
		return centsValue;
	}



}