package com.fw.beans;

import com.fw.enums.InBetweenCitiesExist;

public class ResetFilterBean {

	private String country;
	private String state;
	private String city;
	private String inBetweenCitiesExist;
	private String bidCount;

	// Generate Getters and Setters
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getInBetweenCitiesExist() {
		return inBetweenCitiesExist;
	}

	public void setInBetweenCitiesExist(String inBetweenCitiesExist) {
		this.inBetweenCitiesExist = inBetweenCitiesExist;
	}

	public String getBidCount() {
		return bidCount;
	}

	public void setBidCount(String bidCount) {
		this.bidCount = bidCount;
	}

}
