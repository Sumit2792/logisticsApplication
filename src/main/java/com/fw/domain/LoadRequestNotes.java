package com.fw.domain;

import java.util.Date;

public class LoadRequestNotes {

	private long loadRequestNotesId;
	private long loadRequestId;
	private String notes;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;

	// Generate Getters and Setters

	public long getLoadRequestId() {
		return loadRequestId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public long getLoadRequestNotesId() {
		return loadRequestNotesId;
	}

	public void setLoadRequestNotesId(long loadRequestNotesId) {
		this.loadRequestNotesId = loadRequestNotesId;
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


}
