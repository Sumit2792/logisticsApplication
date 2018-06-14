package com.fw.beans;

import com.fw.enums.UserRoles;
/**
 * 
 * @author Faber
 *
 */
public class LoginResultBean {

	private Long userId;
	private String userRole;
    private String userName;
    private double rating;
/*    private String firstName;
    private String middleName;
    private String lastName;
    private String companyName;*/
	private String token;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = UserRoles.fromString(userRole).toDbString();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}

	
	
	
}
