package com.proximity.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.proximity.model.TransactionModel;
import com.proximity.service.ITransactionService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/transaction")
public class TransactionController {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		return handle(e);
	}

	@Autowired
	private ITransactionService transacionService;

	@GetMapping("/transactions")
	@ResponseBody
	public List<TransactionModel> getTransactions() {
		List<TransactionModel> response = transacionService.getTransactions();
		return response;
	}

	@GetMapping("/transactions/today")
	@ResponseBody
	public List<TransactionModel> getTransactionsToday() {

		LocalDateTime aa = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
		Instant instant = aa.atZone(ZoneId.systemDefault()).toInstant();
		List<TransactionModel> response = transacionService.getTransactionsToday(Date.from(instant));
		return response;
	}

	public static ResponseEntity<String> handle(Exception e) {
		String dummy = "{ message : " + e.getMessage() + "  }";
		e.printStackTrace();
		return new ResponseEntity<String>(dummy, HttpStatus.BAD_REQUEST);
	}

}
