package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

import com.fw.domain.UserFacts;
import com.fw.domain.Users;

public class UsersWithFactsBean extends Users {

	private List<UserFacts> facts;
	
	public UsersWithFactsBean() {
		facts = new ArrayList<UserFacts>();
	}
	
	public List<UserFacts> getFacts() {
		return facts;
	}
	public void setFacts(List<UserFacts> facts) {
		this.facts = facts;
	}
}
