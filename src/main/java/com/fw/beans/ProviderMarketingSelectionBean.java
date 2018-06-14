package com.fw.beans;

import java.util.List;

import com.fw.enums.ContactType;

public class ProviderMarketingSelectionBean {

	private Long messageTemplateId;
	private Long loadRequestId;
	private ContactType type;
	private List<Long> users;
	
	public long getMessageTemplateId() {
		return messageTemplateId;
	}
	public void setMessageTemplateId(Long messageTemplateId) {
		this.messageTemplateId = messageTemplateId;
	}
	public Long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public ContactType getType() {
		return type;
	}
	public void setType(ContactType type) {
		this.type = type;
	}
	public List<Long> getUsers() {
		return users;
	}
	public void setUsers(List<Long> users) {
		this.users = users;
	}
}
