package com.proximity.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import com.proximity.enums.PaymentUnits;
import com.proximity.exception.UnsufficientMoneyException;
import com.proximity.model.ChangeModel;

public class MachineUtils {

	private static List<PaymentUnits> paymentsValidForChange = Arrays.asList(PaymentUnits.COIN_05_CENTS,
			PaymentUnits.COIN_10_CENTS, PaymentUnits.COIN_25_CENTS, PaymentUnits.COIN_50_CENTS);

	private static List<PaymentUnits> paymentsValidForExtract = Arrays.asList(PaymentUnits.COIN_05_CENTS,
			PaymentUnits.COIN_10_CENTS, PaymentUnits.COIN_25_CENTS, PaymentUnits.COIN_50_CENTS, PaymentUnits.BILL_1_DOLAR, PaymentUnits.BILL_2_DOLAR);

	
	/**
	 * Check if the user payment is enough to make the sell.
	 * 
	 * @param payment
	 * @param totalToPay
	 * @return
	 */
	public static int checkIfMoneyEnought(List<PaymentUnits> payment, int totalToPay) {

		int totalChargedInMachine = 0;
		for (PaymentUnits paymentUnits : payment) {
			totalChargedInMachine += paymentUnits.getCentsValue();
		}

		if (totalChargedInMachine < totalToPay)
			throw new UnsufficientMoneyException(totalToPay, totalChargedInMachine);

		return totalChargedInMachine;
	}

	/**
	 * 
	 * Calculates the best list of payments units to return the change to the user;
	 * 
	 * @param ammoutToReturn
	 * @param moneyChangedByUser
	 * @param moneyInMachine
	 * @return
	 */
	public static List<PaymentUnits> calculateChange(int ammoutToReturn, List<PaymentUnits> moneyChangedByUser,
			Iterable<ChangeModel> moneyInMachine, boolean isExtraction) {


		Hashtable<PaymentUnits, Integer> existences = new Hashtable<PaymentUnits, Integer>();

		// Add to hashtable the money charged
		for (PaymentUnits paymentUnits : moneyChangedByUser) {
			if (!existences.containsKey(paymentUnits))
				existences.put(paymentUnits, 1);
			else
				existences.put(paymentUnits, existences.get(paymentUnits) + 1);
		}

		// Add to hashtable the money inside machine
		for (ChangeModel paymentUnits : moneyInMachine) {
			PaymentUnits enumBd = PaymentUnits.valueOf(paymentUnits.getCode());
			if (!existences.containsKey(enumBd))
				existences.put(enumBd, paymentUnits.getQuantity());
			else
				existences.put(enumBd, existences.get(enumBd) + paymentUnits.getQuantity());
		}

		List<PaymentUnits> change = new ArrayList<PaymentUnits>();

		// Iterates picking the most proper and available coin/s 
		while (ammoutToReturn > 0) {
			List<PaymentUnits> candidateCoins = getCandidatesCoins(ammoutToReturn, isExtraction ? paymentsValidForExtract : paymentsValidForChange);
			PaymentUnits coinSelected = null;
			while (!candidateCoins.isEmpty() && coinSelected == null) {
				PaymentUnits bestCoin = candidateCoins.get(0);
				if (existences.containsKey(bestCoin) && existences.get(bestCoin) > 0) {
					coinSelected = bestCoin;
					existences.put(bestCoin, existences.get(bestCoin) - 1);
					change.add(bestCoin);
					ammoutToReturn -= bestCoin.getCentsValue();
				} else {
					candidateCoins.remove(0);
				}
			}
			if (coinSelected == null)
				throw new RuntimeException("No tengo plata para cambio");
		}

		return change;
	}

	private static List<PaymentUnits> getCandidatesCoins(int value, List<PaymentUnits> pay) {

		return pay.stream().filter(p -> p.getCentsValue() <= value).sorted(new Comparator<PaymentUnits>() {
			@Override
			public int compare(PaymentUnits arg0, PaymentUnits arg1) {
				return arg1.getCentsValue().compareTo(arg0.getCentsValue());
			}
		}).collect(Collectors.toList());

	}

}
