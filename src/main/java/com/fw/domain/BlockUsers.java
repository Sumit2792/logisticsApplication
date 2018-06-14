package com.fw.domain;

import java.util.Date;

public class BlockUsers {

	private long blockedUsersId;
	private long userId;
	private String reason;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;


	// Generate Getters and Setters
	public long getBlockedUsersId() {
		return blockedUsersId;
	}

	public void setBlockedUsersId(long blockedUsersId) {
		this.blockedUsersId = blockedUsersId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
