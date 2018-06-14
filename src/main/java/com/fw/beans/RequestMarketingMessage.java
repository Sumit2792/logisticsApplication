package com.fw.beans;

import com.fw.enums.ContactStatus;

public class RequestMarketingMessage {

	private Long messageId;
	private String contact;
	private String message;
	private ContactStatus status;
	private Long createdBy;
	private String sender;
	private String batchId;
	private String userId;
	private String loadRequestId;
	
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ContactStatus getStatus() {
		return status;
	}
	public void setStatus(ContactStatus status) {
		this.status = status;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(String loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
}
