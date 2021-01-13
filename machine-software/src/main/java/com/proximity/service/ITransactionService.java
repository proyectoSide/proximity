package com.proximity.service;

import java.util.Date;
import java.util.List;

import com.proximity.model.TransactionModel;

public interface ITransactionService {



	List<TransactionModel> getTransactions();

	List<TransactionModel> getTransactionsToday(Date d);

}
