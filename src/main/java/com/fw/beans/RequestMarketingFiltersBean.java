package com.fw.beans;

/**
 * Bean to hold rest API data for request marketing filters
 * @author Vikas Sonwal
 *
 */
public class RequestMarketingFiltersBean {
	
	private Long loadRequestId;
	private String status;
	private int limit;
	private int pageNumber;
	private boolean showPending;
	
	public Long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public boolean isShowPending() {
		return showPending;
	}
	public void setShowPending(boolean showPending) {
		this.showPending = showPending;
	}
}
