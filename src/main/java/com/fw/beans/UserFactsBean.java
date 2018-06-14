package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

public class UserFactsBean {

	private long userId;
	@Valid
	private List<Fact>facts= new ArrayList<>();

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<Fact> getFacts() {
		return facts;
	}

	public void setFacts(List<Fact> facts) {
		this.facts = facts;
	}

}

