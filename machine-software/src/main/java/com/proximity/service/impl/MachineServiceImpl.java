package com.proximity.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.proximity.dto.CashAvailableResponseDTO;
import com.proximity.dto.ExtractionDTO;
import com.proximity.dto.ExtractionResponseDTO;
import com.proximity.dto.OrderDTO;
import com.proximity.dto.OrderResponseDTO;
import com.proximity.enums.PaymentUnits;
import com.proximity.exception.BlockedMachineException;
import com.proximity.exception.InvalidItemException;
import com.proximity.exception.InvalidPasswordException;
import com.proximity.exception.OutOfStockProductException;
import com.proximity.machine.IMachine;
import com.proximity.machine.MachineStatus;
import com.proximity.model.CashMovementModel;
import com.proximity.model.ChangeModel;
import com.proximity.model.ItemModel;
import com.proximity.model.TransactionModel;
import com.proximity.model.repository.CashMovementsRepository;
import com.proximity.model.repository.ChangeRepository;
import com.proximity.model.repository.ItemRepository;
import com.proximity.model.repository.TransactionRepository;
import com.proximity.service.IMachineService;
import com.proximity.utils.MachineUtils;

@Service
public class MachineServiceImpl implements IMachineService {

	private final static Logger LOGGER = Logger.getLogger(MachineServiceImpl.class.getName());
	private final static SimpleDateFormat dayFormat = new SimpleDateFormat("DD-MM-YYYY");

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private TransactionRepository transactionRepository;


	@Autowired
	private ChangeRepository changeRepository;
	
	@Autowired
	private MachineStatus machineStatus;
	
	@Autowired
	private CashMovementsRepository cashMovementsRepository;
	
	@Autowired
	private Environment env;
	
	@Override
	public synchronized OrderResponseDTO buyItem(OrderDTO order) {

		// Checks item
		Optional<ItemModel> optionalItem = itemRepository.findById(order.getItemId());
		if (!optionalItem.isPresent()) {
			LOGGER.log(Level.SEVERE, "Invalid Item ID:" + order.getItemId());
			throw new InvalidItemException();
		}

		// Checks stock
		int available = optionalItem.get().getStock();
		if (available < order.getQuantity() || order.getQuantity() < 0) {
			LOGGER.log(Level.SEVERE, "OutOfStock ID:  Current Stock:" + available
					+ ". Order Stock" + order.getQuantity());
			throw new OutOfStockProductException();
		}


		int sellAmmount = order.getQuantity() * optionalItem.get().getPrice();
		Iterable<ChangeModel> moneyInMachine = changeRepository.findAll();

		IMachine machine = machineStatus.getCurrentMachine();
		OrderResponseDTO orderResponseDTO = machine.processPayment(order, sellAmmount, moneyInMachine);
				
		// Update Stocks
		optionalItem.get().setStock(available-order.getQuantity());
		itemRepository.save(optionalItem.get());
		
		// Store transaction
		TransactionModel trans = new TransactionModel();
		trans.setQuantity(order.getQuantity());
		trans.setItem(order.getItemId());
		trans.setMethod(orderResponseDTO.getPaymentMethod());
		trans.setSellDate(new Date());
		trans.setTotal(sellAmmount);
		transactionRepository.save(trans);

		// Update coins stocks
		updateCoinsStocks(order.getPayment(), orderResponseDTO.getChange());

		boolean moreThanHundredDolars = getCashInMachine().getAmmount() > 100;
		if (moreThanHundredDolars && !machineStatus.isWasNotified()) {
			createAlert();
		}
		
		return orderResponseDTO;

	}

	/**
	 * Creates an alert and tries to send it to the admin system. If the
	 * communications fails the sell will continue anyway.
	 */
	private void createAlert() {
		machineStatus.setActiveAlert(true);

		try {

			String adminUrlSystem = env.getProperty("general.adminsystem.url");
			ResponseEntity<String> response = new RestTemplate().postForEntity(adminUrlSystem + "/alert", "",
					String.class);

			if (response.getStatusCode().equals(HttpStatus.OK)) {
				machineStatus.setWasNotified(true);
			}
				
			
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void updateCoinsStocks(List<PaymentUnits> chargedMoney, List<PaymentUnits> toReturnMoney) {
		
		Set<PaymentUnits> coinToModify = new HashSet<PaymentUnits>();
		coinToModify.addAll(chargedMoney);
		if (Objects.nonNull(toReturnMoney)) {
			coinToModify.addAll(toReturnMoney);
		}
		for (PaymentUnits paymentUnit : coinToModify) {
			Optional<ChangeModel> change = changeRepository.findById(paymentUnit.name());

			int cantiToAdd = (int) chargedMoney.stream().filter(c -> c.equals(paymentUnit)).count();
			int cantiToRemove = (int) (Objects.isNull(toReturnMoney) ? 0
					: toReturnMoney.stream().filter(c -> c.equals(paymentUnit)).count());

			int cant = cantiToAdd - cantiToRemove;

			if (!PaymentUnits.CREDIT_CARD.equals(paymentUnit)) {
				if (change.isEmpty()) {
					ChangeModel changeModel = new ChangeModel();
					changeModel.setCode(paymentUnit.name());
					changeModel.setQuantity(cant);
					changeRepository.save(changeModel);
				} else {
					change.get().setQuantity(change.get().getQuantity() + cant);
					changeRepository.save(change.get());

				}
			}

		}
	}

	@Override
	public CashAvailableResponseDTO getCashInMachine() {

		Iterable<ChangeModel> cashInside = changeRepository.findAll();
		int total = 0;
		for (ChangeModel changeModel : cashInside) {
			PaymentUnits paymentUnit = PaymentUnits.valueOf(changeModel.getCode());
			total += paymentUnit.getCentsValue() * changeModel.getQuantity();
		}

		return new CashAvailableResponseDTO(total);
	}

	@Override
	public CashAvailableResponseDTO getCashProfit() {

		Iterable<TransactionModel> transactionsList = transactionRepository.findAll();
		int sum = 0;
		for (TransactionModel transactionModel : transactionsList) {
			sum += transactionModel.getTotal();
		}
		return new CashAvailableResponseDTO(sum);
		
		/*
		CashAvailableResponseDTO cash = getCashInMachine();
		Iterable<CashMovementModel> movements = cashMovementsRepository.findAll();
		
		int alreadyExtractedMoney = 0;
		for (CashMovementModel cashMovementModel : movements) {
			// Ignoring the cases when maintenance add coins the have change in the machine.
			if (cashMovementModel.getAmmount() < 0) {
				alreadyExtractedMoney += (cashMovementModel.getAmmount() * -1);
			}
		} 
		
		return new CashAvailableResponseDTO(cash.getAmmount() + alreadyExtractedMoney);	
		 */
	}

	@Override
	public ExtractionResponseDTO extractMoneyFromMachine(ExtractionDTO extracion, String pass) {
		
		// If the last login was in a previous day, the intents are cleaned.
		String currentDateIntent = dayFormat.format(new Date());
		if (!machineStatus.getDayIntent().equals(currentDateIntent)) {
			machineStatus.setDayIntent(currentDateIntent);
			machineStatus.setCountIntent(0);
		}

		// After 3 intents the machine is blocked
		if (machineStatus.getCountIntent() > 2)
			throw new BlockedMachineException();

		// After login success, the counting is set in zero. 
		if (machineStatus.getPassword().equals(pass)) {
			machineStatus.setCountIntent(0);
		} else {
			machineStatus.setCountIntent(machineStatus.getCountIntent() + 1);
			throw new InvalidPasswordException();
		}
		
		Iterable<ChangeModel> changeAvailable = changeRepository.findAll();
		List<PaymentUnits> extraction = MachineUtils.calculateChange(extracion.getAmmount(), Lists.newArrayList(), changeAvailable, true);
		
		CashMovementModel cashMovement = new CashMovementModel();
		cashMovement.setAmmount(-1 * extracion.getAmmount());
		cashMovementsRepository.save(cashMovement);
		
		updateCoinsStocks(Lists.newArrayList(), extraction);	
		
		// Cleaning alert
		machineStatus.setActiveAlert(false);
		machineStatus.setWasNotified(false);
		
		return new ExtractionResponseDTO(extraction);
	}

	@Override
	public void unlockMachine(String pass) {

		if (!machineStatus.getPasswordSystem().equals(pass)) {
			throw new InvalidPasswordException();
		}

		machineStatus.setCountIntent(0);

	}

}
