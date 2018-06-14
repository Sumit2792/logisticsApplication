package com.fw.beans;

import com.fw.enums.ContactType;

public class RequestMarketingDetailFiltersBean {

	private Long loadRequestId;
	private int limit;
	private int pageNumber;
	private ContactType type;
	
	public Long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
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
	public ContactType getType() {
		return type;
	}
	public void setType(ContactType type) {
		this.type = type;
	}
}
