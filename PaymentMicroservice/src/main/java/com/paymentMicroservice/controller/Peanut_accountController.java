
package com.paymentMicroservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.paymentMicroservice.service.Peanut_accountService;
import com.paymentMicroservice.service.TransactionService;


/**
 * @author vijetaagrawal
 *
 */


@RestController
/**
 * Peanut_accountController class provides the mapping of all Http requests for viewAccount.jsp.
 */
public class Peanut_accountController {
	@Autowired 
	private TransactionService transactionService; //Injection of transactionService
	@Autowired
	private Peanut_accountService peanut_accountService; //Injection of peanut_accountService
	
	
	@RequestMapping(value = {"/getViewAccount"}, method = RequestMethod.GET)
	/**
	 * GET Request mapping for "/getViewAccount"
	 * Takes session information from SingleSignIn Microservice using HttpServletRequest and creates a session
	 * to view user peanut account balance and user transaction.
	 * @param request
	 * @param m
	 * @return
	 */
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
			return new ModelAndView("redirect:viewAccount",m); 
		  	} 
    	}
	}
	
	@RequestMapping(value = {"/viewAccount"}, method = RequestMethod.GET)
	/**
	 * GET Request mapping for "/ViewAccount"
	 * Takes the user in session and display his account balance and application transactions
	 * @param m
	 * @param session
	 * @param request
	 * @return
	 */
	public ModelAndView getViewTransactions(ModelMap m, HttpSession session ,HttpServletRequest request) {
		
		//Takes the user in session
		int UserId = Integer.parseInt((String) session.getAttribute("userID"));
		
		//Displays the peanut account balance of the current user
		try {
			m.addAttribute("available_balance", peanut_accountService.balance(UserId));
		} catch (Exception e) {
			m.addAttribute("DataUnavailable", "Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/viewAccount");
			return mav;
		}
		//Displays the transaction list of the current user
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