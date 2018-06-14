package com.fw.domain;

import java.util.Date;

public class UserSources {

	private long userSourceid;
	private long userIdStart;
	private long userIdEnd;
	private String source;
	private int count;
	private double amount;
	private Date acquiredDate;
	private String note;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;

    //Generate getters and Setters
	public long getUserSourceid() {
		return userSourceid;
	}

	public void setUserSourceid(long userSourceid) {
		this.userSourceid = userSourceid;
	}

	public long getUserIdStart() {
		return userIdStart;
	}

	public void setUserIdStart(long userIdStart) {
		this.userIdStart = userIdStart;
	}

	public long getUserIdEnd() {
		return userIdEnd;
	}

	public void setUserIdEnd(long userIdEnd) {
		this.userIdEnd = userIdEnd;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getAcquiredDate() {
		return acquiredDate;
	}

	public void setAcquiredDate(Date acquiredDate) {
		this.acquiredDate = acquiredDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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