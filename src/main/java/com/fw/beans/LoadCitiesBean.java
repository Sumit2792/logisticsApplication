package com.fw.beans;

public class LoadCitiesBean {

	private String source;
	private String destination;
	
	private InBetweenCitiesBean[] inBetweenCities;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public InBetweenCitiesBean[] getInBetweenCities() {
		return inBetweenCities;
	}

	public void setInBetweenCities(InBetweenCitiesBean[] inBetweenCities) {
		this.inBetweenCities = inBetweenCities;
	}
	

}
