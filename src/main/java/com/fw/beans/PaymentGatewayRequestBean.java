package com.fw.beans;

public class PaymentGatewayRequestBean {

	private long userId;
	private String userName;
	private String userEmail;
	private long bidId;
	private long loadRequestId;
	private double amountRequested;
	private double amountRecieved;
	private String paymentDescription;
	private String transactionId;
	private String redirectUrl;

	// Generate getters and setters
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

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

	public double getAmountRequested() {
		return amountRequested;
	}

	public void setAmountRequested(double amountRequested) {
		this.amountRequested = amountRequested;
	}

	public double getAmountRecieved() {
		return amountRecieved;
	}

	public void setAmountRecieved(double amountRecieved) {
		this.amountRecieved = amountRecieved;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

}
