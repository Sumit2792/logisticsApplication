package com.fw.beans;

import javax.validation.constraints.Size;

import com.fw.enums.UserRoles;
/**
 * 
 * @author Faber
 *
 */
public class OnBehalfOfUserBean {

	@Size(min=10, max=13, message="Inavalid phone number.")
    private String phoneNumber;
	private UserRoles userRole;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public UserRoles getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRoles userRole) {
		this.userRole = userRole;
	}
    
	
}
