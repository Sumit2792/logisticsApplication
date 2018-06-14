package com.fw.beans;

import com.fw.enums.LoadRequestStatus;

/**
 * Bean to hold API response data for request marketing bean
 * @author Vikas Sonwal
 *
 */
public class RequestMarketingBean {

	private Long loadRequestId;
	private Long smsCount;
	private Long emailCount;
	private Long callCount;
	private Long bidCount;
	private LoadRequestStatus status;
    private double amount;
    private double faberCharges;
	private boolean insuranceNeeded;
	private Long userId;
	private String userName;
	
	public Long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public Long getSmsCount() {
		return smsCount;
	}
	public void setSmsCount(Long smsCount) {
		this.smsCount = smsCount;
	}
	public Long getEmailCount() {
		return emailCount;
	}
	public void setEmailCount(Long emailCount) {
		this.emailCount = emailCount;
	}
	public Long getCallCount() {
		return callCount;
	}
	public void setCallCount(Long callCount) {
		this.callCount = callCount;
	}
	public Long getBidCount() {
		return bidCount;
	}
	public void setBidCount(Long bidCount) {
		this.bidCount = bidCount;
	}
	public LoadRequestStatus getStatus() {
		return status;
	}
	public void setStatus(LoadRequestStatus status) {
		this.status = status;
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
	public boolean isInsuranceNeeded() {
		return insuranceNeeded;
	}
	public void setInsuranceNeeded(boolean insuranceNeeded) {
		this.insuranceNeeded = insuranceNeeded;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
