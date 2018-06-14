package com.fw.beans;


/**
 * 
 * @author Faber
 *
 */
public class MessageToSentBean {

	private long userId;
	private long messageToSentId;
	private String mobileNo;
	private String message;
	private long loadRequestId;

	// Generate Getters and Setters
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getMessageToSentId() {
		return messageToSentId;
	}

	public void setMessageToSentId(long messageToSentId) {
		this.messageToSentId = messageToSentId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNumer) {
		this.mobileNo = mobileNumer;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

}
