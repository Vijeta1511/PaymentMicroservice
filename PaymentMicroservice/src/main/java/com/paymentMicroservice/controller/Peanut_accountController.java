/**
 * 
 */
package com.paymentMicroservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paymentMicroservice.command.peanut_accountCommand;
import com.paymentMicroservice.service.Peanut_accountService;
import com.paymentMicroservice.service.TransactionService;


/**
 * @author vijetaagrawal
 *
 */
@RestController
public class Peanut_accountController {
	@Autowired 
	private TransactionService transactionService;
	@Autowired
	private Peanut_accountService peanut_accountService;
	
//	@RequestMapping(value = {"/InitViewAccount"}, method = RequestMethod.GET)
	
	@RequestMapping(value = {"/","/getViewAccount"}, method = RequestMethod.GET)
	public ModelAndView viewTransactions(HttpServletRequest request, ModelMap m) {	
		String userIDpara=request.getParameter("userID");
    	String sessionIDpara=request.getParameter("sessionID");
    	
    	//if one of both the parameters are null --> unauthorised user
    			if(userIDpara==null || sessionIDpara==null){
    				return new ModelAndView("redirect:/SingleSignIn/get_login",m);
    			}else{
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
    			     return new ModelAndView("redirect:viewAccount",m);
    			  	}
		else {
			return new ModelAndView("redirect:viewAccount",m); //the page which indicates unauthorised user
		  	} 
    			}
	}
	
	@RequestMapping(value = {"/viewAccount"}, method = RequestMethod.GET)
	public ModelAndView getViewTransactions(ModelMap m, HttpSession session ,HttpServletRequest request) {
		

		
		int UserId = Integer.parseInt((String) session.getAttribute("userID"));
		
		try {
			m.addAttribute("available_balance", peanut_accountService.balance(UserId));
		} catch (Exception e) {
			m.addAttribute("DataUnavailable", "Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/viewAccount");
			return mav;
		}
		try {
			m.addAttribute("transactionList", transactionService.viewAllTransactions(UserId));
		} catch (Exception e) {
			m.addAttribute("NoTransaction", "Transactions not found.");
			ModelAndView mav = new ModelAndView("/viewAccount");
			return mav;
		}
		
		ModelAndView mav = new ModelAndView("/viewAccount");
		return mav;
	}
}