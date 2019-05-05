package com.paymentMicroservice.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

/**
 * BaseDAO class
 * It sets the JDBC DataSource to be used by this DAO. 
 * The DataSource which is to be used is configured in mvc-dispatcher-payment-servlet.xml configuration file.
 * @author vijetaagrawal
 *
 */


abstract public class BaseDAO extends NamedParameterJdbcDaoSupport {
	@Autowired
	public void setDataSource2(DataSource ds) {
		super.setDataSource(ds);
		
	}

}
