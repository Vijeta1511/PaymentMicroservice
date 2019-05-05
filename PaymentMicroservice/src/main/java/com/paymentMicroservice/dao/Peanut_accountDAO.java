package com.paymentMicroservice.dao;

import java.util.List;
import com.paymentMicroservice.domain.Peanut_account;

/**
 * @author vijetaagrawal
 *This interface declares the various methods used for handling payment in the platform.
 */

public interface Peanut_accountDAO {

	public Peanut_account save(Peanut_account p, Object attribute); // save the user object
	public void updateSignIn(Integer UserId); // updates SingleSignIn microservice admin account
	public void updatePayment(Integer UserId); // updates the Payment microservice admin account
	public void updateAppOwner(Integer UserId); //updates the Application owner account
	public void debitAccount(Integer UserId); // updates the user account
	public void delete(Peanut_account p); // delete the user object
	public void delete(Integer Peanut_accountId); // delete the user by its userId
	public Peanut_account findById(Integer Peanut_accountId); // find the user by its userId
	public List<Peanut_account> findAll(); // find the user by its userId
	public Peanut_account findByProperty(String propName, Integer UserId); // find the user by its property name and its value
}