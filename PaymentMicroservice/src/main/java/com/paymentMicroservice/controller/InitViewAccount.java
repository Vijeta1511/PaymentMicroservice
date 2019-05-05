package com.paymentMicroservice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Servlet implementation class InitCheckout
 */

@WebServlet("/InitViewAccount")
public class InitViewAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitViewAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//obtain the header parameters
		String userIDpara=request.getParameter("userID");
    	String sessionIDpara=request.getParameter("sessionID");
    	String appIDpara=request.getParameter("appName");

    	
		//if one of both the parameters are null --> unauthorised user
		if(userIDpara==null || sessionIDpara==null){
    		response.sendRedirect("/get_login");  //the page which indicates unauthorised user
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
		     response.sendRedirect("getViewAccount");  //the homepage of the app
		  	}
		  	else {
    		response.sendRedirect("/index");  //the page which indicates unauthorised user
		  	}    	
	     }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
