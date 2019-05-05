package com.paymentMicroservice.service;

import com.paymentMicroservice.domain.Peanut_account;

/**
 * This interface is the part of the Business layer
 * It contains the declaration of functionalities that are performed on the peanut account
 * @author vijetaagrawal
 *
 */
public interface Peanut_accountService {
	//Creates the peanut_account for the registered user
	public Peanut_account createAccount(Peanut_account p, Object attribute);
	//returns the Peanut account balance of the user with userId=UserId
	public Integer balance(Integer UserId);
	//debits the peanut account of the user
	public void debit(Integer UserId);
	//credits the peanut accounts of ApplicationOwner/SingleSignIn/PaymentMicroservice 
	public void credit(Integer UserId);
	//returns the UserId of the ApplicationOwner
	public Integer getAppOwner(String AppName); 
}
