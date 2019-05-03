/**
 * 
 */
package com.paymentMicroservice.controller;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.paymentMicroservice.command.peanut_accountCommand;
import com.paymentMicroservice.domain.Transaction;
import com.paymentMicroservice.service.Peanut_accountService;
import com.paymentMicroservice.service.TransactionService;

/**
 * @author vijetaagrawal
 *
 */

@RestController
public class TransactionController {
	
	@Autowired
	private Peanut_accountService peanut_accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value = {"/getCheckout"}, method = RequestMethod.GET)
	public ModelAndView checkout(ModelMap m) {
		m.addAttribute("command", new peanut_accountCommand());
		return new ModelAndView("redirect:checkout",m);
		
	}
	
	@RequestMapping(value = {"/checkout"}, method = RequestMethod.GET)
	public ModelAndView getViewTransactions(HttpSession session,  ModelMap m, HttpServletRequest request) {
//		String app_name = request.getQueryString();
//		System.out.println(app_name);
		Integer UserId = (Integer)session.getAttribute("userId");
		
		try {
			m.addAttribute("available_balance", peanut_accountService.balance(5));
		} catch (Exception e) {
			m.addAttribute("DataUnavailable", "Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		ModelAndView mav = new ModelAndView("/checkout");
		return mav;
		}

//	@RequestMapping(value = {"/paymentSuccessful"}, method = RequestMethod.GET)
//	public ModelAndView getPaymentSuccessful(Model m) {
//		m.addAttribute("command", new peanut_accountCommand());
//		ModelAndView mav = new ModelAndView("/paymentSuccessful"); // Require link for application
//		return mav;
//		
//	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public ModelAndView paymentSuccessful(ModelMap m,  HttpSession session, HttpServletRequest request) {
		
		
		Integer UserId = (Integer)session.getAttribute("userId");
		
		String app_name = request.getQueryString();
//		System.out.println(app_name);
		
		Integer balance;
		
		try {
			balance = peanut_accountService.balance(5);
			
		} catch (Exception e1) {
			
			m.addAttribute("TransactionFailed", "Transaction Failed. Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		try {
			
			if(balance<5) {
				
				m.addAttribute("err", "Insufficient Peanuts.");
				ModelAndView mav = new ModelAndView("/checkout");
				return mav;
				
			}else {

			transactionService.newTransaction(app_name , UserId);
			
			peanut_accountService.debit(UserId);
			
			Integer AppUserId = peanut_accountService.getAppOwner(app_name);
			
			peanut_accountService.credit(AppUserId);//Require UserId of Application Owner//testing
			
			
			}
		} catch (Exception e) {
			
			m.addAttribute("TransactionCreationFailed", "Transaction Not Created.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		String Username = (String) session.getAttribute("loginName");
		String role = (String) session.getAttribute("role");
		
		m.addAttribute("app_name", app_name);
		m.addAttribute("Username", Username);
		m.addAttribute("role", role);

		try {
			return new ModelAndView("redirect:http://143.167.9.201:8080/"+app_name+"/index.jsp?username="+Username+"&role="+role,m);
		} catch (Exception e) {
			m.addAttribute("AppLinkNotFound", "Application link not found.");
			return new ModelAndView("/SingleSignIn/index",m);
		}
//		ModelAndView mav = new ModelAndView("/paymentSuccessful"+app_name); // Require link for application
//		return mav;
	}
		

}
