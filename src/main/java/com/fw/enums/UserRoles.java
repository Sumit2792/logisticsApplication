package com.fw.enums;

public enum UserRoles {

	SUPER_ADMIN("SUPER_ADMIN"), ADMIN("ADMIN"), CSR("CSR"), LOAD_PROVIDER("LOAD_PROVIDER"), CAPACITY_PROVIDER(
			"CAPACITY_PROVIDER"), BOTH("BOTH");

	private final String dbString;

	private UserRoles(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static UserRoles fromString(String userType) {
		if ("CSR".equalsIgnoreCase(userType)) {
			return (CSR);
		} else if ("LOAD_PROVIDER".equalsIgnoreCase(userType)) {
			return (LOAD_PROVIDER);
		} else if ("CAPACITY_PROVIDER".equalsIgnoreCase(userType)) {
			return (CAPACITY_PROVIDER);
		} else if ("BOTH".equalsIgnoreCase(userType)) {
			return (BOTH);
		} else if ("SUPER_ADMIN".equalsIgnoreCase(userType)) {
			return (SUPER_ADMIN);
		} else if ("ADMIN".equalsIgnoreCase(userType)) {
			return (ADMIN);
		} else {
			throw new RuntimeException("Invalid User Type");
		}
	}
}
