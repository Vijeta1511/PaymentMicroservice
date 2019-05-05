
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
 * TransactionController class provides the mapping of all Http requests for checkout.jsp.
 */
public class TransactionController {
	
	@Autowired
	private Peanut_accountService peanut_accountService;
	
	@Autowired
	private TransactionService transactionService;

	
	@RequestMapping(value = {"/getCheckout"}, method = RequestMethod.GET)
	/**
	 * GET Request mapping for "/getCheckout"
	 * Takes session information from SingleSignIn Microservice using HttpServletRequest and creates a session for handling checkout.
	 * Checks if the user is in session.
	 * @param m
	 * @param request
	 * @return
	 */
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
    			     
    			     
    			     return new ModelAndView("redirect:checkout",m);
    			  	}
    			  	
    			  	else {
    			  		//the page which indicates unauthorised user
    			  		return new ModelAndView("redirect:checkout",m); 
    			  	} 
    			}
		}
	
	@RequestMapping(value = {"/checkout"}, method = RequestMethod.GET)
	/**
	 * GET Request mapping for "/checkout"
	 * Displays the peanut balance for logged in user on checkout page and fixed application cost 
	 * @param session
	 * @param m
	 * @param request
	 * @return
	 */
	public ModelAndView getViewTransactions(HttpSession session,  ModelMap m, HttpServletRequest request) {
		
		//Takes the user in session
		int UserId = Integer.parseInt((String) session.getAttribute("userID"));
		
		
		try {
			m.addAttribute("available_balance", peanut_accountService.balance(UserId));
		} 
		catch (Exception e) {
			m.addAttribute("DataUnavailable", "Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		
		ModelAndView mav = new ModelAndView("/checkout");
		return mav;
		}

	
	
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	/**
	 * POST Request mapping for "/checkout"
	 * Creates the transaction for the application usage.
	 * Does peanut account balance validation.
	 * Credits the Application owner account and SingleSignIn/PaymentMicroService developer account.
	 * Debits the user account.
	 * @param m
	 * @param session1
	 * @param request
	 * @return
	 */
	public ModelAndView paymentSuccessful(ModelMap m,  HttpSession session1, HttpServletRequest request) {
		
		//Reading the session information
		int userID = Integer.parseInt((String) session1.getAttribute("userID"));
		String appName = (String)session1.getAttribute("appName");
		int sessionID = Integer.parseInt((String)session1.getAttribute("sessionID"));
		
		
		Integer balance;
		//Getting the peanut account balance for user in session
		try {
			balance = peanut_accountService.balance(userID);
			
		} catch (Exception e1) {
			
			m.addAttribute("TransactionFailed", "Transaction Failed. Peanut Account not found.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		
		//Checking for the required minimum balance for a transaction/application usage.
		try {
			
			if(balance<5) {
				
				m.addAttribute("err", "Insufficient Peanuts.");
				ModelAndView mav = new ModelAndView("/checkout");
				return mav;
				
			}else {
			//Creating a new transaction
			transactionService.newTransaction(appName , userID);
			//Debits the user account
			peanut_accountService.debit(userID);
			// Getting the userId of the Application owner
			Integer AppUserId = peanut_accountService.getAppOwner(appName);
			//Credits the Application/SingleSignIn/PaymentMicroservice accounts
			peanut_accountService.credit(AppUserId);
			
			}
		} catch (Exception e) {
			
			m.addAttribute("TransactionCreationFailed", "Transaction Not Created.");
			ModelAndView mav = new ModelAndView("/checkout");
			return mav;
		}
		//Adding the attributes for the application redirection
		m.addAttribute("appName", appName);
		m.addAttribute("sessionID", sessionID);
		m.addAttribute("userID", userID);

		//Redirecting to the selected application
		try {
			return new ModelAndView("redirect:http://143.167.9.201:8080/"+appName+"/Init?userID="+userID+"&sessionID="+sessionID,m);
		} catch (Exception e) {
			m.addAttribute("AppLinkNotFound", "Application link not found.");
			return new ModelAndView("/checkout",m);
		}

	}
		

}
