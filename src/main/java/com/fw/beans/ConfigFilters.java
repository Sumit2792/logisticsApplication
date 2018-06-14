package com.fw.beans;

public class ConfigFilters {

	private Integer limit;
	private Long propertyId;
	private String  title;
	private String  value;
	private Integer page;

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}


	public String getValue() {
		return value;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	
	

}
