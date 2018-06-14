package com.fw.domain;

import java.util.Date;

public class BidNotes {

	private long bidNotesId;
	private long bidId;
	private String notes;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;

	//Generate Getters and Setters
	public long getBidId() {
		return bidId;
	}

	public long getBidNotesId() {
		return bidNotesId;
	}

	public void setBidNotesId(long bidNotesId) {
		this.bidNotesId = bidNotesId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
	
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
