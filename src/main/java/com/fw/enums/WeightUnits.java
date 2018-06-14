package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum WeightUnits {

	KILOGRAM("KILOGRAM"), TON("TON"), KILOLITRE("KILOLITRE");

	private final String dbString;

	private WeightUnits(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static WeightUnits fromString(String str) {
		if ("KILOGRAM".equalsIgnoreCase(str)) {
			return (KILOGRAM);
		} else if ("TON".equalsIgnoreCase(str)) {
			return (TON);
		} else if ("KILOLITRE".equalsIgnoreCase(str)) {
			return (KILOLITRE);
		} else {
			throw new RuntimeException("No such unit type:" + str);
		}
	}
}
