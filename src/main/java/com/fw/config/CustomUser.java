package com.fw.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String language;
	private String userrole;
	
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String language, String userrole) {
		super(username, password, authorities);
		this.language = language;
		this.userrole = userrole;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

}
