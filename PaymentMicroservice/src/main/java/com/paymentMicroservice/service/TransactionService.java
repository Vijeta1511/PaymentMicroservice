package com.paymentMicroservice.service;

import java.util.List;

/**
 * This interface is the part of the Business layer
 * It contains the declaration of functionalities that are performed on the transactions
 * @author vijetaagrawal
 *
 */

import com.paymentMicroservice.domain.Transaction;

public interface TransactionService {

	//Creates a new transaction
	public void newTransaction(String AppName, Integer UserId); 
	//returns the List of transaction
	public List<Transaction> viewAllTransactions(Integer UserId);
}
