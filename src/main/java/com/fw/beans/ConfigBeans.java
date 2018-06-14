package com.fw.beans;

import java.util.Date;

public class ConfigBeans {

	private Long configId;
	private Long configPropertiesId;
	private String title;
	private String value;
	
	private Long createdBy;
	private Long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	
	
	public Long getConfigPropertiesId() {
		return configPropertiesId;
	}
	public void setConfigPropertiesId(Long configPropertiesId) {
		this.configPropertiesId = configPropertiesId;
	}
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Long modifiedBy) {
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
