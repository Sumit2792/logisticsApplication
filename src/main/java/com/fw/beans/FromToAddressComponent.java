package com.fw.beans;

import java.util.ArrayList;

public class FromToAddressComponent {

	private String long_name;
	private String short_name;
	private ArrayList<String> types;

	// Generate getters and setters
	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	/*private long postalAddressId;
	@Size(max = 200, message = "You can type max 200 characters in Address line1.")
	private String addressLine1;
	@Size(max = 200, message = "You can type max 200 characters in Address line2.")
	private String addressLine2;
	@Size(max = 50, message = "Bad Request , City size can not be more than 50 characters. ")
	private String city;
	@Size(max = 50, message = "Bad Request , State size can not be more than 50 characters. ")
	private String state;
	@Size(min = 5, max = 6, message = "Invalid pin code.")
	private String pin;
	@Size(max = 50, message = "Bad Request , Country size can not be more than 50 characters. ")
	private String country;
	private double latitude;
	private double longitude;
	private String area;
	private Object locatonJSON;

	// Generte getters and setters
	public long getPostalAddressId() {
		return postalAddressId;
	}

	public void setPostalAddressId(long postalAddressId) {
		this.postalAddressId = postalAddressId;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Object getLocatonJSON() {
		return locatonJSON;
	}

	public void setLocatonJSON(Object locatonJSON) {
		this.locatonJSON = locatonJSON;
	}*/

}
