package com.fw.enums;

public enum RatingType {

    NOT_RATED(0.0, "NOT_RATED"),
    CRAP(1.0, "CRAP"),
    WORST(2.0, "WORST"),
    BAD(3.0, "BAD"),
    OK(4.0, "OK"),
    GOOD(4.2, "GOOD"),
    BETTER(4.5, "BETTER"),
    BEST(4.8, "BEST"),
    EXCELLENT(5.0, "EXCELLENT");

	private final double val;
	private final String dbString;

	private RatingType(double val, String dbString) {
		this.val = val;
		this.dbString = dbString;
	}

	public double getVal() {
		return val;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static RatingType fromString(String str) {
		if ("NOT_RATED".equalsIgnoreCase(str)) {
			return NOT_RATED;
		} else if ("CRAP".equalsIgnoreCase(str)) {
			return CRAP;
		} else if ("WORST".equalsIgnoreCase(str)) {
			return WORST;
		} else if ("BAD".equalsIgnoreCase(str)) {
			return BAD;
		} else if ("OK".equalsIgnoreCase(str)) {
			return OK;
		} else if ("GOOD".equalsIgnoreCase(str)) {
			return GOOD;
		} else if ("BETTER".equalsIgnoreCase(str)) {
			return BETTER;
		} else if ("BEST".equalsIgnoreCase(str)) {
			return BEST;
		} else if ("EXCELLENT".equalsIgnoreCase(str)) {
			return EXCELLENT;
		} else {
			throw new RuntimeException("No such rating type:" + str);
		}
	}

	public static RatingType fromVal(double val) {
		if(val < 1) {
			return NOT_RATED;
		} else if(val >= 1 && val < 2) {
			return CRAP;
		} else if(val >= 2 && val < 3) {
			return WORST;
		} else if(val >= 3 && val < 4) {
			return BAD;
		} else if(val >= 4 && val < 4.2) {
			return OK;
		} else if(val >= 4.2 && val < 4.5) {
			return GOOD;
		} else if(val >= 4.5 && val < 4.8) {
			return BETTER;
		} else if(val >= 4.8 && val < 5) {
			return BEST;
		} else if(val > 5) {
			return EXCELLENT;
		} else {
			return NOT_RATED;
		}
	}
}
