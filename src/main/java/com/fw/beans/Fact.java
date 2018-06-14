package com.fw.beans;

import javax.validation.constraints.Size;

public class Fact {
	private long userFactsId;
	@Size(max=50, message="Bad Request, not allowed more than 100 characters for fact key .")
	private String fact;
	@Size(max=100, message="Bad Request, not allowed more than 100 characters for fact value.")
	private String value;

	
	public String getFact() {
		return fact;
	}

	public void setFact(String fact) {
		this.fact = fact;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getUserFactsId() {
		return userFactsId;
	}

	public void setUserFactsId(long userFactsId) {
		this.userFactsId = userFactsId;
	}
	
}