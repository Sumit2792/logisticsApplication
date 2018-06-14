package com.fw.beans;

public class BidStatusBean {

	private long bidId;
	private String status;
	private long createdBy;
	private long modifiedBy;

	public long getBidId() {
		return bidId;
	}

	public void setBidId(long bidId) {
		this.bidId = bidId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	

}
