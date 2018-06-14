package com.fw.enums;

public enum LengthUnits {

	FEET("FEET"), METER("METER"), CENTIMETER("CENTIMETER");

	private final String dbString;

	private LengthUnits(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static LengthUnits fromString(String str) {
		if ("FEET".equalsIgnoreCase(str)) {
			return (FEET);
		} else if ("METER".equalsIgnoreCase(str)) {
			return (METER);
		} else if ("CENTIMETER".equalsIgnoreCase(str)) {
			return (CENTIMETER);
		} else {
			throw new RuntimeException("No such unit type:" + str);
		}
	}
}
