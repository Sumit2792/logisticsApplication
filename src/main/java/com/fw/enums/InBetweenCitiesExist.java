package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum InBetweenCitiesExist {

	ALL("ALL"), EXISTS("EXISTS"), NOT_EXISTS("NOT EXISTS");

	private final String dbString;

	private InBetweenCitiesExist(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static InBetweenCitiesExist fromString(String str) {
		if ("ALL".equalsIgnoreCase(str)) {
			return (ALL);
		} else if ("EXISTS".equalsIgnoreCase(str)) {
			return (EXISTS);
		}  else if ("NOT EXISTS".equalsIgnoreCase(str)) {
			return (NOT_EXISTS);
		} else {
			throw new RuntimeException("No such criteria exist:" + str);
		}
	}
}
