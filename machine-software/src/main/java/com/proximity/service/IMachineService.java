package com.proximity.service;

import javax.transaction.Transactional;

import com.proximity.dto.ExtractionDTO;
import com.proximity.dto.ExtractionResponseDTO;
import com.proximity.dto.OrderDTO;

import com.proximity.dto.OrderResponseDTO;
import com.proximity.dto.CashAvailableResponseDTO;

@Transactional
public interface IMachineService {

	OrderResponseDTO buyItem(OrderDTO order);

	CashAvailableResponseDTO getCashInMachine();

	CashAvailableResponseDTO getCashProfit();

	ExtractionResponseDTO extractMoneyFromMachine(ExtractionDTO extracion, String pass);

	void unlockMachine(String pass);

}
