package com.fw.domain;

import java.util.Date;

import com.fw.enums.ContactStatus;

public class CallHistory {

	private long callHistoryId;
	private long userId;
	//loadRequestId will be null in case of promotional marketing
	private long loadRequestId;
	private String phoneNumber;
	private String messageBody;
	private ContactStatus status;
	private String batchId;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private long feedbackId;
	
	public long getCallHistoryId() {
		return callHistoryId;
	}
	public void setCallHistoryId(long callHistoryId) {
		this.callHistoryId = callHistoryId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public ContactStatus getStatus() {
		return status;
	}
	public void setStatus(ContactStatus status) {
		this.status = status;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
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
	public long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}

}
