package com.fw.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;


public class LoadRequestDetail {
	
	private long loadRequestDetailsId;
	private long loadRequestId;
	private long startLocationId;
	private long endLocationId;
	@NotNull(message="start shipping date is missing.")
	private Date startDatetime;
	private Date expectedEndDateTime;
	private Date actualEndDateTime;
    private long createdBy;
    private long modifiedBy;
    private Date createdDate;
    private Date modifiedDate;
	private boolean isDeleted;
    
    //Generate Getters and setters
    
	public Date getStartDatetime() {
		return startDatetime;
	}
	public void setStartDatetime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public long getLoadRequestDetailsId() {
		return loadRequestDetailsId;
	}
	public void setLoadRequestDetailsId(long loadRequestDetailsId) {
		this.loadRequestDetailsId = loadRequestDetailsId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public long getStartLocationId() {
		return startLocationId;
	}
	public void setStartLocationId(long startLocationId) {
		this.startLocationId = startLocationId;
	}
	public long getEndLocationId() {
		return endLocationId;
	}
	public void setEndLocationId(long endLocationId) {
		this.endLocationId = endLocationId;
	}
	
	public Date getStartDateTime() {
		return startDatetime;
	}
	public void setStartDateTime(Date startDatetime) {
		this.startDatetime = startDatetime;
	}
	public Date getExpectedEndDateTime() {
		return expectedEndDateTime;
	}
	public void setExpectedEndDateTime(Date expectedEndDatetime) {
		this.expectedEndDateTime = expectedEndDatetime;
	}
	public Date getActualEndDateTime() {
		return actualEndDateTime;
	}
	public void setActualEndDateTime(Date actualEndDatetime) {
		this.actualEndDateTime = actualEndDatetime;
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
