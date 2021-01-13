package com.proximity.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.proximity.config.ApiMessages;
import com.proximity.dto.CashAvailableResponseDTO;
import com.proximity.dto.ExtractionDTO;
import com.proximity.dto.ExtractionResponseDTO;
import com.proximity.dto.OrderDTO;
import com.proximity.dto.OrderResponseDTO;
import com.proximity.service.IMachineService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/machine")
public class MachineController {

	protected final static int DEFAULT_PAGE_SIZE = 8;


	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleException(Exception e) {
		return handle(e);
	}

	@Autowired
	private IMachineService machineService;

	@ApiOperation(notes = ApiMessages.buyItem, value = "/buy")
	@PostMapping("/buy")
	@ResponseBody
	public OrderResponseDTO buyItem(@Valid @RequestBody OrderDTO order) {
		return machineService.buyItem(order);
	}

	/**
	 * It returns the current ammount of money in the machine.
	 * 
	 * @return
	 */
	@GetMapping("/cash/available")
	@ResponseBody
	public CashAvailableResponseDTO getCashInMachine() {
		CashAvailableResponseDTO response = machineService.getCashInMachine();
		return response;
	}

	@PutMapping("/cash/extraction")
	@ResponseBody
	public ExtractionResponseDTO extractMoneyFromMachine(@RequestParam String pass,
			@RequestBody ExtractionDTO extracion) {

		ExtractionResponseDTO response = machineService.extractMoneyFromMachine(extracion, pass);
		return response;
	}
	
	@PutMapping("/unlock")
	@ResponseBody
	public void unlockMachine(@RequestParam String pass) {
		machineService.unlockMachine(pass);
	}

	@GetMapping("/cash/profit")
	@ResponseBody
	public CashAvailableResponseDTO getCashProfit() {
		CashAvailableResponseDTO response = machineService.getCashProfit();
		return response;
	}

	private static ResponseEntity<String> handle(Exception e) {
		String dummy = "{ message : " + e.getMessage() + "  }";
		e.printStackTrace();
		return new ResponseEntity<String>(dummy, HttpStatus.BAD_REQUEST);
	}

}
