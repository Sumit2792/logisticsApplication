package com.fw.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;

public class Users {

	private long userId;
	private String userName;
	@Size(max = 20, message = "Max 20 characters are allowed for password.")
	private String password;
	private UserRoles userRole;
	private int loginOtp;
	private int failedAttempts;
	private Date lastLoginDate;
	private UserLoginStatus userLoginStatus;
	// @Size (max=500 , message="Max 500 characters are allowed for authToken.")
	private String authToken;
	// @Size (max=500 , message="Max 500 characters are allowed for resetToken.")
	private String resetToken;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;
	private double rating;
	private int reviewCount;

	// Generate Getters and Setters
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName2) {

		this.userName = userName2;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRoles getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoles userRole) {
		this.userRole = userRole;
	}

	public int getLoginOtp() {
		return loginOtp;
	}

	public void setLoginOtp(int loginOtp) {
		this.loginOtp = loginOtp;
	}

	public int getFailedAttempts() {
		return failedAttempts;
	}

	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public UserLoginStatus getUserLoginStatus() {
		return userLoginStatus;
	}

	public void setUserLoginStatus(UserLoginStatus userLoginStatus) {
		this.userLoginStatus = userLoginStatus;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}