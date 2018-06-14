package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

import com.fw.enums.ContactType;

public class RequestMarketingMessageDetailBean {
	
	private ContactType type;
	private Long loadRequestId;
	private List<RequestMarketingMessage> messageList;
	
	public RequestMarketingMessageDetailBean() {
		messageList = new ArrayList<RequestMarketingMessage>();
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public Long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public List<RequestMarketingMessage> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<RequestMarketingMessage> messageList) {
		this.messageList = messageList;
	}
	
}
