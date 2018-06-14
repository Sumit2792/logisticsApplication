package com.fw.domain;

import java.util.Date;

import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;

public class Bid {

	private long bidId;
	private long loadRequestId;
	private long bidderId;
	private double amount;
	private double faberCharges;
	private BidStatus bidStatus;
	private Date expectedDelieveryTime; // expected_delievery_time
	private CurrencyCodes currency;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;
	private String userAttemptsJson;

	// Generate Getters and Setters
	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public long getBidderId() {
		return bidderId;
	}

	public void setBidderId(long bidderId) {
		this.bidderId = bidderId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getFaberCharges() {
		return faberCharges;
	}

	public void setFaberCharges(double faberCharges) {
		this.faberCharges = faberCharges;
	}

	public BidStatus getBidStatus() {
		return bidStatus;
	}

	public void setBidStatus(BidStatus bidStatus) {
		this.bidStatus = bidStatus;
	}

	public Date getExpectedDelieveryTime() {
		return expectedDelieveryTime;
	}

	public void setExpectedDelieveryTime(Date expectedDelieveryTime) {
		this.expectedDelieveryTime = expectedDelieveryTime;
	}

	public CurrencyCodes getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyCodes currency) {
		this.currency = currency;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUserAttemptsJson() {
		return userAttemptsJson;
	}

	public void setUserAttemptsJson(String userAttemptsJson) {
		this.userAttemptsJson = userAttemptsJson;
	}

}
