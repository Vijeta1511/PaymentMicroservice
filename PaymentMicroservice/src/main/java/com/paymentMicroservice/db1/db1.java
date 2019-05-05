package com.paymentMicroservice.db1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class db1 {
	Statement s1;
	Connection conn = null;
	public db1(){
		try {	
			
			Class.forName("com.mysql.jdbc.Driver");
		
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/common_db" ,"root", "svds2019"); //connection for server db
			//conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1?serverTimezone=EST5EDT" ,"root", "dil1234");  //connecting to local db
			s1=conn.createStatement();
	       
	        }
		catch(Exception e){
			
		}
	}
	public int getSessionID(int UserID) {
		int sessionID=0;
		try {
			ResultSet r2= s1.executeQuery("select sessionID from loggedIn where userID="+UserID+"");
			  while (r2.next()) {
				   		sessionID=r2.getInt("sessionID");
		        	}
		}
		catch (Exception e){
			
		}
		return sessionID;
	}
	
	public void logged(int userID,String username,String role) {
	     
		try
		{
			
			int j=s1.executeUpdate("insert into loggedIn(userID,username,role)values('"+userID+"','"+username+"','"+role+"')");
		}
		catch(Exception e)
		{			
			//System.out.print(e);
			e.printStackTrace();
		}
		
	}
	
	public void deleteLogged(int userID) {
		try {
			int j=s1.executeUpdate("delete from loggedIn where userID="+userID+"");
			  
		}
		catch (Exception e){
			
		}
	}
	
}