package com.fw.domain;

import java.util.Date;

public class FeedbackTypes {

	private long feedbackTypesId;
	private String title;
	private String description;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;


	// Generate Getters and Setters
	public long getFeedbackTypesId() {
		return feedbackTypesId;
	}

	public void setFeedbackTypesId(long feedbackTypesId) {
		this.feedbackTypesId = feedbackTypesId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
