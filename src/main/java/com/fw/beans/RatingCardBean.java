package com.fw.beans;

public class RatingCardBean {

	private long userId;
	private String userName;
	private String givenUserName;
	private String forUserName;
	private float rating;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGivenUserName() {
		return givenUserName;
	}
	public void setGivenUserName(String givenUserName) {
		this.givenUserName = givenUserName;
	}
	public String getForUserName() {
		return forUserName;
	}
	public void setForUserName(String forUserName) {
		this.forUserName = forUserName;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	
}
