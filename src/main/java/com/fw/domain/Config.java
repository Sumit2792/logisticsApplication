package com.fw.domain;

import java.util.Date;

public class Config {

	private long configId;
	private long configPropertiesId;
	private String value;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;

	// Generate Getters and Setters
	public long getConfigId() {
		return configId;
	}

	public void setConfigId(long configId) {
		this.configId = configId;
	}

	public long getConfigPropertiesId() {
		return configPropertiesId;
	}

	public void setConfigPropertiesId(long configPropertiesId) {
		this.configPropertiesId = configPropertiesId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
