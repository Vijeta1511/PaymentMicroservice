
package com.paymentMicroservice.command;



public class applicationCommand {

	private String name;
	private Integer userId;
	private String imageLocation;
	private String appDescription;
	
	@Override
	public String toString() {
		return "applicationCommand [name=" + name + ", userId=" + userId + ", imageLocation=" + imageLocation
				+ ", appDescription=" + appDescription + "]";
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getAppDescription() {
		return appDescription;
	}

	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}