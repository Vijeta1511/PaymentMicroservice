package com.paymentMicroservice.controller;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * 
 * @author vijetaagrawal
 *
 */
class UserSearch {
	Statement ss;
	Connection con;
	int count=0;
	String username;
	String role;
	
	public UserSearch() {
	
    	try{
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/common_db" ,"root", "svds2019"); //connecting to server database
    	ss= con.createStatement();
    	}
    	catch(Exception e){
    		
    	}
	}
	
	public int search(int userID,int sessionID) {
		try {
		ResultSet rs= ss.executeQuery("select * from loggedIn where userID='"+userID+"'and sessionID='"+sessionID+"'");
		  
    	while (rs.next()) {
    	         username=rs.getString("username");
			     role=rs.getString("role");
			     count++;
        	}
		}
		catch(Exception e) {
			
		}
		return count;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getRole() {
		return role;
	}
}
