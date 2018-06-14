package com.fw.domain;

import java.util.Date;

public class PostalAddresses {

	private long postalAddressId;
	private int userId;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String pin;
	private String country;
	private double latitude;
	private double longitude;
	private int createdBy;
	private int modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;
	private String nearByLandmark;
	private String area;
	private String locationJSON;

	// Generate Getters and Setters
	public long getPostalAddressId() {
		return postalAddressId;
	}

	public void setPostalAddressId(long postalAddressId) {
		this.postalAddressId = postalAddressId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getNearByLandmark() {
		return nearByLandmark;
	}

	public void setNearByLandmark(String nearByLandmark) {
		this.nearByLandmark = nearByLandmark;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLocationJSON() {
		return locationJSON;
	}

	public void setLocationJSON(String locationJSON) {
		this.locationJSON = locationJSON;
	}

}
