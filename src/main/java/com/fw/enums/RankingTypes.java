package com.fw.enums;

public enum RankingTypes {

	FEEDBACK("FEEDBACK"), PAYMENT("PAYMENT"), ONTIME("ONTIME"), ONLINE("ONLINE");

	private final String dbString;

	private RankingTypes(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}
	public static RankingTypes fromString(String str) {
		if ("FEEDBACK".equalsIgnoreCase(str)) {
			return (FEEDBACK);
		} else if ("PAYMENT".equalsIgnoreCase(str)) {
			return (PAYMENT);
		} else if ("ONTIME".equalsIgnoreCase(str)) {
			return (ONTIME);
		} else if ("ONLINE".equalsIgnoreCase(str)) {
			return (ONLINE);
		} else {
			throw new RuntimeException("No such Ranking type:" + str);
		}
	}
}
