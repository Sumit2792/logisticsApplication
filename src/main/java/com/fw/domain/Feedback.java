package com.fw.domain;

import java.util.Date;

public class Feedback {

	private long feedbackId;
	private long loadRequestId;
	private String note;
	private long givingUserId;
	private long forUserId;
	private double rating;
	private long feedbackTypeId;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private Date callBackTime;
	private long bidId;

	// Generate Getters and Setters
	public long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public long getFeedbackTypeId() {
		return feedbackTypeId;
	}

	public void setFeedbackTypeId(long feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}

	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public long getGivingUserId() {
		return givingUserId;
	}

	public void setGivingUserId(long givingUserId) {
		this.givingUserId = givingUserId;
	}

	public long getForUserId() {
		return forUserId;
	}

	public void setForUserId(long forUserId) {
		this.forUserId = forUserId;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
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

	public Date getCallBackTime() {
		return callBackTime;
	}

	public void setCallBackTime(Date callBackTime) {
		this.callBackTime = callBackTime;
	}
	
	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

}
