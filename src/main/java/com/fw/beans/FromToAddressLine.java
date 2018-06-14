package com.fw.beans;

import java.util.ArrayList;

import javax.validation.Valid;

public class FromToAddressLine {

	@Valid
	private FromToAddressComponent[] address_components;
	private String formatted_address;
	@Valid
	private GeometryBean geometry;
	private String place_id;
	private ArrayList<String> types;


	//Generate getters and setters
	public FromToAddressComponent[] getAddress_components() {
		return address_components;
	}

	public void setAddress_components(FromToAddressComponent[] address_components) {
		this.address_components = address_components;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public GeometryBean getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryBean geometry) {
		this.geometry = geometry;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	

}
