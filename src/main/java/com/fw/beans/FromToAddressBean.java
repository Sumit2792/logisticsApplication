package com.fw.beans;


public class FromToAddressBean {

	private long userId;
	private FromToAddressLine locationJSON;
	private String addressLine1;
	private Long postalAddressId;
	private String nearByLandmark;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public FromToAddressLine getLocationJSON() {
		return locationJSON;
	}

	public void setLocationJSON(FromToAddressLine locationJSON) {
		this.locationJSON = locationJSON;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public Long getPostalAddressId() {
		return postalAddressId;
	}

	public void setPostalAddressId(Long postalAddressId) {
		this.postalAddressId = postalAddressId;
	}

	public String getNearByLandmark() {
		return nearByLandmark;
	}

	public void setNearByLandmark(String nearByLandmark) {
		this.nearByLandmark = nearByLandmark;
	}

}
