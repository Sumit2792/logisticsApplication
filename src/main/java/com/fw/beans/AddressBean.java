package com.fw.beans;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AddressBean {

	@JsonIgnore
	private int userId;
	@Valid
	private FromToAddressBean from;
	@Valid
	private FromToAddressBean to ;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public FromToAddressBean getFrom() {
		return from;
	}

	public void setFrom(FromToAddressBean from) {
		this.from = from;
	}

	public FromToAddressBean getTo() {
		return to;
	}

	public void setTo(FromToAddressBean to) {
		this.to = to;
	}

}
