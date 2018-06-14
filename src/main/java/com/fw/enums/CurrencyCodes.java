package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum CurrencyCodes {

	USD("USD"), INR("INR");

	private final String dbString;

	private CurrencyCodes(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static CurrencyCodes fromString(String str) {
		if ("USD".equalsIgnoreCase(str)) {
			return (USD);
		} else if ("INR".equalsIgnoreCase(str)) {
			return (INR);
		} else {
			throw new RuntimeException("No such code type:" + str);
		}
	}
}
