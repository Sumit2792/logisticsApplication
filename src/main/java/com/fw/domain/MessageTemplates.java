package com.fw.domain;

import java.util.Date;

import com.fw.enums.ContactType;

public class MessageTemplates {
	
	private long messageTemplateId;
	private String subjectLine;
	private String messageBody;
	private ContactType type;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	
	public long getMessageTemplateId() {
		return messageTemplateId;
	}
	public void setMessageTemplateId(long messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}
	public String getSubjectLine() {
		return subjectLine;
	}
	public void setSubjectLine(String subjectLine) {
		this.subjectLine = subjectLine;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
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
