package com.paymentMicroservice.domain;

public class Application {
	private Integer app_id; //PK
	private String name;
	private Integer userId; //FK
	private String imageLocation; // for storing location of app icon
	private String appDesc; // for providing description to the application
	

	//Constructor
	public Application() {
		
	}
	
	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public Integer getApp_id() {
		return app_id;
	}
	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Application [app_id=" + app_id + ", name=" + name + ", userId=" + userId + ", imageLocation="
				+ imageLocation + ", appDesc=" + appDesc + "]";
	}
	
	

}