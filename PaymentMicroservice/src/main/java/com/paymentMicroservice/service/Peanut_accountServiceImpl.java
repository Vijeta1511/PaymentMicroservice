
package com.paymentMicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.paymentMicroservice.dao.ApplicationDAO;
import com.paymentMicroservice.dao.BaseDAO;
import com.paymentMicroservice.dao.Peanut_accountDAO;
import com.paymentMicroservice.domain.Application;
import com.paymentMicroservice.domain.Peanut_account;
import com.paymentMicroservice.exception.InsufficientPeanutsException;
import com.paymentMicroservice.rm.ApplicationRowMapper;
import com.paymentMicroservice.rm.Peanut_accountRowMapper;
import com.paymentMicroservice.rm.TransactionRowMapper;

/**
 * @author vijetaagrawal
 * It extends the BaseDAO class and implements Peanut_accountService interface
 *This class contains the description of functionalities available on the Peanut Account
 *It implements the methods declared in the Peanut_accountService
 */

@Service
public class Peanut_accountServiceImpl extends BaseDAO implements Peanut_accountService {
	
	@Autowired
	private Peanut_accountDAO peanut_accountDAO; //Dependency Injection using @Autowired Annotation
	
	@Autowired ApplicationDAO applicationDAO; //Dependency Injection using @Autowired Annotation

	@Override
	public Peanut_account createAccount(Peanut_account p, Object attribute) {
		 p = peanut_accountDAO.save(p, attribute);
		 return p;
	}

	@Override
	public void debit(Integer UserId){
		peanut_accountDAO.debitAccount(UserId);
		
	}
	
	@Override
	public Integer getAppOwner(String AppName) {
		String sql = "SELECT app_id, name, userId, imageLocation, appDescription FROM application WHERE name = ?";
		Application app = getJdbcTemplate().queryForObject(sql, new ApplicationRowMapper(), AppName);
		return app.getUserId();
		
	}


	@Override
	public void credit(Integer UserId) { //requires UserID of application Owner
		Integer SignIn = 1;
		Integer Payment = 2;
		peanut_accountDAO.updateSignIn(SignIn);
		peanut_accountDAO.updatePayment(Payment);
		peanut_accountDAO.updateAppOwner(UserId);
			
	}

	@Override
	public Integer balance(Integer UserId) {
		Peanut_account p = peanut_accountDAO.findByProperty("userId", UserId);
		return p.getAvailable_peanuts();
		
	}

	
	
}
