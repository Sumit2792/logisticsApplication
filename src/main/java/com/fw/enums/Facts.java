package com.fw.enums;

/**
 * 
 * @author Sumit Srivastava
 *
 */
public enum Facts {

	PHONE_NO("PHONE_NO"), EMAIL_ID("EMAIL_ID"), FIRST_NAME("FIRST_NAME"), MIDDLE_NAME("MIDDLE_NAME"), LAST_NAME(
			"LAST_NAME"), COMPANY_NAME("COMPANY_NAME"), NO_OF_TRUCKS("NO_OF_TRUCKS");

	private final String dbString;

	private Facts(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static Facts fromString(String str) {
		if ("PHONE_NO".equalsIgnoreCase(str)) {
			return (PHONE_NO);
		} else if ("EMAIL_ID".equalsIgnoreCase(str)) {
			return (EMAIL_ID);
		}  else if ("FIRST_NAME".equalsIgnoreCase(str)) {
			return (FIRST_NAME);
		}  else if ("MIDDLE_NAME".equalsIgnoreCase(str)) {
			return (MIDDLE_NAME);
		}  else if ("LAST_NAME".equalsIgnoreCase(str)) {
			return (LAST_NAME);
		}  else if ("COMPANY_NAME".equalsIgnoreCase(str)) {
			return (COMPANY_NAME);
		}  else if ("NO_OF_TRUCKS".equalsIgnoreCase(str)) {
			return (NO_OF_TRUCKS);
		} else {
			throw new RuntimeException("No such user facts available:" + str);
		}
	}
}
