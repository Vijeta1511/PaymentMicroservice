/**
 * 
 */
package com.paymentMicroservice.controller;

import java.net.URL;

import javax.servlet.ServletContext;
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
	public ModelAndView checkout(ModelMap m, HttpServletRequest request) {
		
		String userIDpara=request.getParameter("userID");
    	String sessionIDpara=request.getParameter("sessionID");
    	String appIDpara=request.getParameter("appName");
    	
    	//if one of both the parameters are null --> unauthorised user
    			if(userIDpara==null || sessionIDpara==null){
    				return new ModelAndView("redirect:/SingleSignIn/get_login",m);
    			}
    			else{
    			   	//int conversion					    
    			  	int userID=Integer.parseInt(userIDpara);
    			  	int sessionID=Integer.parseInt(sessionIDpara);
    			  	
    			  	UserSearch us1=new UserSearch();
    			  	
    			  	//if the IDs are valid->if they are in the LoggedIn table
    			  	if(us1.search(userID, sessionID)==1) {
    	    		HttpSession session=request.getSession();	
    	    		 session.setAttribute("userID", userIDpara);
    	    		 session.setAttribute("sessionID", sessionIDpara);
    	    		 session.setAttribute("username",us1.getUsername());
    			     session.setAttribute("role",us1.getRole());
    			     session.setAttribute("appName", appIDpara);
//		Integer UserId = (Integer)request.getAttribute("userID");
    			     
    			     
    			     return new ModelAndView("redirect:checkout",m);
    			  	}
		else {
			return new ModelAndView("redirect:checkout",m); //the page which indicates unauthorised user
		  	} 
    			}
//		m.addAttribute("command", new peanut_accountCommand());
//		return new ModelAndView("redirect:checkout",m);
		
	}
	
	@RequestMapping(value = {"/checkout"}, method = RequestMethod.GET)
	public ModelAndView getViewTransactions(HttpSession session,  ModelMap m, HttpServletRequest request) {
		
		
		int UserId = Integer.parseInt((String) session.getAttribute("userID"));
		
		
		try {
			m.addAttribute("available_balance", peanut_accountService.balance(UserId));
		} catch (Exception e) {
			m.addAttribute("DataUnavailable", "Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		ModelAndView mav = new ModelAndView("/checkout");
		return mav;
		}

	
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public ModelAndView paymentSuccessful(ModelMap m,  HttpSession session1, HttpServletRequest request) {
		
		
		int userID = Integer.parseInt((String) session1.getAttribute("userID"));
		String appName = (String)session1.getAttribute("appName");
		int sessionID = Integer.parseInt((String)session1.getAttribute("sessionID"));
		
		
		Integer balance;
		
		try {
			balance = peanut_accountService.balance(userID);
			
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

			transactionService.newTransaction(appName , userID);
			
			peanut_accountService.debit(userID);
			
			Integer AppUserId = peanut_accountService.getAppOwner(appName);
			
			peanut_accountService.credit(AppUserId);//Require UserId of Application Owner//testing
			
			
			}
		} catch (Exception e) {
			
			m.addAttribute("TransactionCreationFailed", "Transaction Not Created.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
//
		m.addAttribute("appName", appName);
		m.addAttribute("sessionID", sessionID);
		m.addAttribute("userID", userID);

		try {
			return new ModelAndView("redirect:http://143.167.9.201:8080/"+appName+"/Init?userID="+userID+"&sessionID="+sessionID,m);
		} catch (Exception e) {
			m.addAttribute("AppLinkNotFound", "Application link not found.");
			return new ModelAndView("/checkout",m);
		}

	}
		

}
