package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum BidStatus {

	//CANCELLED("CANCELLED"), 
	ACTIVE("ACTIVE"),
	AWARDED("AWARDED"), 
	NOT_AWARDED("NOT_AWARDED"),
	LOAD_REQUEST_CANCELLED("LOAD_REQUEST_CANCELLED");

	private final String dbString;

	private BidStatus(String dbString) {
		
		this.dbString = dbString;
	}

	public String toDbString() {
		
		return (this.dbString);
	}

	public static BidStatus fromString(String str) {
		
		if ("ACTIVE".equalsIgnoreCase(str)) {
			return (ACTIVE);
		} else if ("AWARDED".equalsIgnoreCase(str)) {
			return (AWARDED);
		} else if ("LOAD_REQUEST_CANCELLED".equalsIgnoreCase(str)) {
			return (LOAD_REQUEST_CANCELLED);
		} else if ("NOT_AWARDED".equalsIgnoreCase(str)) {
				return (NOT_AWARDED);
		}else {
			throw new RuntimeException("No such Status type:" + str);
		}
	}

}
