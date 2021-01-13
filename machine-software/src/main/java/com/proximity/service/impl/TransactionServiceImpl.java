package com.proximity.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proximity.model.TransactionModel;
import com.proximity.model.repository.TransactionRepository;
import com.proximity.service.ITransactionService;

@Service
public class TransactionServiceImpl implements ITransactionService {

	private final static Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class.getName());

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<TransactionModel> getTransactions() {

	    List<TransactionModel> result = new ArrayList<TransactionModel>();
	    transactionRepository.findAll().forEach(result::add);
		
		return result;
		
	}

	@Override
	public List<TransactionModel> getTransactionsToday(Date d) {
		
		return	transactionRepository.findAllWithCreationDateTimeBefore(d);
	}




}
