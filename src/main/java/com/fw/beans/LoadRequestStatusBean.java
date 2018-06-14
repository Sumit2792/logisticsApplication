package com.fw.beans;


public class LoadRequestStatusBean {

	private long loadRequestId;
	private String status;
    private long createdBy;
    private long modifiedBy;
	
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
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
