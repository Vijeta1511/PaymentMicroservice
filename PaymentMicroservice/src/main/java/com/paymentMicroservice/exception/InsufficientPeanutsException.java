package com.paymentMicroservice.exception;

/**
 * 
 * @author vijetaagrawal
 *
 */
public class InsufficientPeanutsException extends Exception {
	
	public InsufficientPeanutsException() {
		
	}

	public InsufficientPeanutsException(String errDesc) {
		super(errDesc);
	}
	

}
