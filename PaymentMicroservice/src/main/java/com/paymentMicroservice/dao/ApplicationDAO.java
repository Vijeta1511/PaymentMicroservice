package com.paymentMicroservice.dao;

import com.paymentMicroservice.domain.Application;

/**
 * ApplicationDAO interface for dao methods
 * @author vijetaagrawal
 *
 */

public interface ApplicationDAO {

	public Application save(Application a); // save the user object
	public Application findById(Integer ApplicationId); // find the user by its userId
	public Application findByProperty(String propName, String propValue); // find the user by its property name and its value
}