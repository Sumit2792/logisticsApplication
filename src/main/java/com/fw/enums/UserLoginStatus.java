package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum UserLoginStatus {

	CREATED("CREATED"), ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), BLOCKED("BLOCKED");

	private final String dbString;

	private UserLoginStatus(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static UserLoginStatus fromString(String str) {
		if ("CREATED".equalsIgnoreCase(str)) {
			return (CREATED);
		} else if ("ACTIVE".equals(str)) {
			return (ACTIVE);
		} else if ("INACTIVE".equals(str)) {
			return (INACTIVE);
		} else if ("BLOCKED".equals(str)) {
			return (BLOCKED);
		}  else {
			throw new RuntimeException("No such Status type:" + str);
		}
	}

}
