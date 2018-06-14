package com.fw.domain;

import java.util.Date;

public class ConfigProperties {

	private long configPropertiesId;
	private String title;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;

	// Generate Getters and Setters

	public long getConfigPropertiesId() {
		return configPropertiesId;
	}

	public void setConfigPropertiesId(long configPropertiesId) {
		this.configPropertiesId = configPropertiesId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
