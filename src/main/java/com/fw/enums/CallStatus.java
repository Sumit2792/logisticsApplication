package com.fw.enums;

public enum CallStatus {

	CALL_SUCCESS("CALL_SUCCESS"), 
	CALL_FAILURE("CALL_FAILURE"), 
	MISSED_CALL("MISSED_CALL"), 
	NO_CALL("NO_CALL"), 
	HANG_UP("HANG_UP");

	private final String dbString;

	private CallStatus(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static CallStatus fromString(String str) {
		if ("CALL_SUCCESS".equalsIgnoreCase(str)) {
			return (CALL_SUCCESS);
		} else if ("CALL_FAILURE".equalsIgnoreCase(str)) {
			return (CALL_FAILURE);
		} else if ("MISSED_CALL".equalsIgnoreCase(str)) {
			return (MISSED_CALL);
		} else if ("NO_CALL".equalsIgnoreCase(str)) {
			return (NO_CALL);
		} else if ("HANG_UP".equalsIgnoreCase(str)) {
			return (HANG_UP);
		} else {
			throw new RuntimeException("No such Status type:" + str);
		}
	}
}
