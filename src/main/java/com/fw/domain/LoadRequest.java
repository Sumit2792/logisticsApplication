package com.fw.domain;

import java.util.Date;

import com.fw.enums.LoadRequestStatus;
import com.fw.enums.TruckType;

public class LoadRequest {

	private long loadRequestId;
	private Date biddingStartDatetime;
	private Date biddingEndDatetime;
	private long userId;
	private LoadRequestStatus loadRequestStatus;
	private boolean insuranceNeeded;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;
	private TruckType truckType;
	private String userAttemptsJson; 

	// Generate Getters and Setters
	public long getLoadRequestId() {
		return loadRequestId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public Date getBiddingStartDatetime() {
		return biddingStartDatetime;
	}

	public void setBiddingStartDatetime(Date biddingStartDatetime) {
		this.biddingStartDatetime = biddingStartDatetime;
	}

	public Date getBiddingEndDatetime() {
		return biddingEndDatetime;
	}

	public void setBiddingEndDatetime(Date biddingEndDatetime) {
		this.biddingEndDatetime = biddingEndDatetime;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public LoadRequestStatus getLoadRequestStatus() {
		return loadRequestStatus;
	}

	public void setLoadRequestStatus(LoadRequestStatus loadRequestStatus) {
		this.loadRequestStatus = loadRequestStatus;
	}

	public boolean isInsuranceNeeded() {
		return insuranceNeeded;
	}

	public void setInsuranceNeeded(boolean insuranceNeeded) {
		this.insuranceNeeded = insuranceNeeded;
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

	public TruckType getTruckType() {
		return truckType;
	}

	public void setTruckType(TruckType truckType) {
		this.truckType = truckType;
	}

	public String getUserAttemptsJson() {
		return userAttemptsJson;
	}

	public void setUserAttemptsJson(String userAttemptsJson) {
		this.userAttemptsJson = userAttemptsJson;
	}
	
}
