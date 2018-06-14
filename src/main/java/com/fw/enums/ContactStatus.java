package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum ContactStatus {

	PENDING((int) 1, "PENDING"), 
	SMS_CANCELED((int) 2, "SMS_CANCELED"), 
	SMS_SUCCESS((int) 3, "SMS_SUCCESS"), 
	SMS_FAILURE((int) 4, "SMS_FAILURE"), 
	CALL_SUCCESS((int) 5, "CALL_SUCCESS"), 
	CALL_FAILURE((int) 6, "CALL_FAILURE"), 
	MISSED_CALL((int) 7, "MISSED_CALL"), 
	INTERESTED((int) 8, "INTERESTED"), 
	NOT_INTERESTED((int) 9, "NOT_INTERESTED"), 
	NO_CALL((int) 10, "NO_CALL"), 
	HANG_UP((int) 11, "HANG_UP"), 
	EMAIL_SENT((int) 12, "EMAIL_SENT"), 
	EMAIL_FAILURE((int) 13, "EMAIL_FAILURE"), 
	GOT_EMAIL_REPLIED((int) 14, "GOT_EMAIL_REPLIED"), 
	SMS_STATUS_AWAITED((int) 15, "SMS_STATUS_AWAITED");

	private final int val;
	private final String dbString;

	private ContactStatus(int val, String dbString) {
		this.val = val;
		this.dbString = dbString;
	}

	public int getValue() {
		return val;
	}

	public static ContactStatus fromInt(int intVal) {
		switch (intVal) {
		case 1:
			return (PENDING);
		case 2:
			return (SMS_CANCELED);
		case 3:
			return (SMS_SUCCESS);
		case 4:
			return (SMS_FAILURE);
		case 5:
			return (CALL_SUCCESS);
		case 6:
			return (CALL_FAILURE);
		case 7:
			return (MISSED_CALL);
		case 8:
			return (INTERESTED);
		case 9:
			return (NOT_INTERESTED);
		case 10:
			return (NO_CALL);
		case 11:
			return (HANG_UP);
		case 12:
			return (EMAIL_SENT);
		case 13:
			return (EMAIL_FAILURE);
		case 14:
			return (GOT_EMAIL_REPLIED);
		case 15:
			return (SMS_STATUS_AWAITED);

		}
		throw new RuntimeException("InValid Bid Status: " + intVal);
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static ContactStatus fromString(String str) {
		if ("PENDING".equalsIgnoreCase(str)) {
			return (PENDING);
		} else if ("SMS_CANCELED".equalsIgnoreCase(str)) {
			return (SMS_CANCELED);
		} else if ("SMS_SUCCESS".equalsIgnoreCase(str)) {
			return (SMS_SUCCESS);
		} else if ("SMS_FAILURE".equalsIgnoreCase(str)) {
			return (SMS_FAILURE);
		} else if ("CALL_SUCCESS".equalsIgnoreCase(str)) {
			return (CALL_SUCCESS);
		} else if ("CALL_FAILURE".equalsIgnoreCase(str)) {
			return (CALL_FAILURE);
		} else if ("MISSED_CALL".equalsIgnoreCase(str)) {
			return (MISSED_CALL);
		} else if ("INTERESTED".equalsIgnoreCase(str)) {
			return (INTERESTED);
		} else if ("NOT_INTERESTED".equalsIgnoreCase(str)) {
			return (NOT_INTERESTED);
		} else if ("NO_CALL".equalsIgnoreCase(str)) {
			return (NO_CALL);
		} else if ("HANG_UP".equalsIgnoreCase(str)) {
			return (HANG_UP);
		} else if ("EMAIL_SENT".equalsIgnoreCase(str)) {
			return (EMAIL_SENT);
		} else if ("EMAIL_FAILURE".equalsIgnoreCase(str)) {
			return (EMAIL_FAILURE);
		} else if ("GOT_EMAIL_REPLIED".equalsIgnoreCase(str)) {
			return (GOT_EMAIL_REPLIED);
		} else if ("SMS_STATUS_AWAITED".equalsIgnoreCase(str)) {
			return (SMS_STATUS_AWAITED);
		} else {
			throw new RuntimeException("No such Status type:" + str);
		}
	}

}
