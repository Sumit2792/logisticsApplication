package com.fw.enums;

/**
 * 
 * @author Sumit Srivastava
 *
 */
public enum ContactType {

	EMAIL((int) 1, "EMAIL"), SMS((int) 2, "SMS"), CALL((int) 3,
			"CALL"), WHATSAPP((int) 2, "WHATSAPP");

	private final int val;
	private final String dbString;

	private ContactType(int val, String dbString) {
		this.val = val;
		this.dbString = dbString;
	}

	public int getValue() {
		return val;
	}

	public static ContactType fromInt(int intVal) {
		switch (intVal) {
		case 1:
			return (EMAIL);
		case 2:
			return (SMS);
		case 3:
			return (CALL);
		case 4:
			return (WHATSAPP);

		}
		throw new RuntimeException("InValid Bid Status: " + intVal);
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static ContactType fromString(String str) {
		if ("EMAIL".equalsIgnoreCase(str)) {
			return (EMAIL);
		} else if ("SMS".equalsIgnoreCase(str)) {
			return (SMS);
		} else if ("CALL".equalsIgnoreCase(str)) {
			return (CALL);
		} else if ("WHATSAPP".equalsIgnoreCase(str)) {
			return (WHATSAPP);
		} else {
			throw new RuntimeException("No such Status type:" + str);
		}
	}

}
