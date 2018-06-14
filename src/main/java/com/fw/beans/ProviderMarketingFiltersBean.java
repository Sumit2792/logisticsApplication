package com.fw.beans;

import com.fw.enums.ContactType;
import com.fw.enums.UserRoles;

public class ProviderMarketingFiltersBean {

	private int limit;
	private int days;
	private Long loadRequestId;
	private ContactType searchType;
	private UserRoles providerType;
	private String country;
	private String state;
	private String city;
	private boolean useLocation;
	private int pageNumber;

	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(Long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public ContactType getSearchType() {
		return searchType;
	}
	public void setSearchType(ContactType searchType) {
		this.searchType = searchType;
	}
	public UserRoles getProviderType() {
		return providerType;
	}
	public void setProviderType(UserRoles providerType) {
		this.providerType = providerType;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public boolean isUseLocation() {
		return useLocation;
	}
	public void setUseLocation(boolean useLocation) {
		this.useLocation = useLocation;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
}
