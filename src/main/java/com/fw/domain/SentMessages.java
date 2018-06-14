package com.fw.domain;

import java.util.Date;

import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;

public class SentMessages {

	private long sentMessagesId;
	private long userId;
	private String contact;
	private String messageBody;
	private ContactStatus status;
	private ContactType type;
	//loadRequestId will be null in case of promotional marketing
	private long loadRequestId;
	private String emailSubject;
	private String smsGroupId;
	private String batchId;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private int statusResetCount;

	// Generate getters and Setters

	public int getStatusResetCount() {
		return statusResetCount;
	}

	public void setStatusResetCount(int statusResetCount) {
		this.statusResetCount = statusResetCount;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	public long getUserId() {
		return userId;
	}

	public long getSentMessagesId() {
		return sentMessagesId;
	}

	public void setSentMessagesId(long sentMessagesId) {
		this.sentMessagesId = sentMessagesId;
	}

	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long l) {
		this.createdBy = l;
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

	public String getSmsGroupId() {
		return smsGroupId;
	}

	public void setSmsGroupId(String smsGroupId) {
		this.smsGroupId = smsGroupId;
	}
	
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
