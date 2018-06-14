package com.fw.beans;

import java.util.ArrayList;
import java.util.List;

import com.fw.domain.CallHistory;
import com.fw.domain.SentMessages;
import com.fw.domain.UserFacts;
import com.fw.enums.UserRoles;

public class ProviderMarketingBean {

	private long userId;
	private String userName;
	private UserRoles userRole;
	private double rating;
	private List<UserFacts> facts;
	private List<MarkatingAddressBean>addressess = new ArrayList<>();
	private SentMessages lastSMS;
	private SentMessages lastEmail;
	private CallHistory lastCall;
	private SentMessages lastWhatsApp;
	
	public ProviderMarketingBean() {
		facts = new ArrayList<UserFacts>();
	}

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public UserRoles getUserRole() {
		return userRole;
	}
	public void setUserRole(UserRoles userRole) {
		this.userRole = userRole;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public List<UserFacts> getFacts() {
		return facts;
	}
	public void setFacts(List<UserFacts> facts) {
		this.facts = facts;
	}
	public SentMessages getLastSMS() {
		return lastSMS;
	}
	public void setLastSMS(SentMessages lastSMS) {
		this.lastSMS = lastSMS;
	}
	public SentMessages getLastEmail() {
		return lastEmail;
	}
	public void setLastEmail(SentMessages lastEmail) {
		this.lastEmail = lastEmail;
	}
	public CallHistory getLastCall() {
		return lastCall;
	}
	public void setLastCall(CallHistory lastCall) {
		this.lastCall = lastCall;
	}
	public SentMessages getLastWhatsApp() {
		return lastWhatsApp;
	}
	public void setLastWhatsApp(SentMessages lastWhatsApp) {
		this.lastWhatsApp = lastWhatsApp;
	}

	public List<MarkatingAddressBean> getAddressess() {
		return addressess;
	}

	public void setAddressess(List<MarkatingAddressBean> addressess) {
		if(addressess == null || addressess.size() < 1)
			this.addressess = new ArrayList<>();
		else
			this.addressess = addressess;
	}
	
}
