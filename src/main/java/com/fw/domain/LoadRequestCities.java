package com.fw.domain;

import java.util.Date;

public class LoadRequestCities {

	private long loadRequestCitiesId;
	private long loadRequestId;
	private String sourceCity;
	private String destinationCity;
	private String cities;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;

	//Generate Getters and Setters
	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public long getLoadRequestCitiesId() {
		return loadRequestCitiesId;
	}

	public void setLoadRequestCitiesId(long loadRequestCitiesId) {
		this.loadRequestCitiesId = loadRequestCitiesId;
	}

	public String getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
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

}
