package com.fw.enums;

/**
 * 
 * @author Narendra Gurjar
 *
 */
public enum AddressComponentTypes {

	postal_code("postal_code"), sublocality("sublocality"), locality("locality"), administrative_area_level_2(
			"administrative_area_level_2"), administrative_area_level_1("administrative_area_level_1"), country("country");

	private final String dbString;

	private AddressComponentTypes(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static AddressComponentTypes fromString(String str) {
		if ("sublocality".equalsIgnoreCase(str)) {
			return (sublocality);
		} else if ("locality".equalsIgnoreCase(str)) {
			return (locality);
		} else if ("administrative_area_level_2".equalsIgnoreCase(str)) {
			return (administrative_area_level_2);
		} else if ("administrative_area_level_1".equalsIgnoreCase(str)) {
			return (administrative_area_level_1);
		} else if ("country".equalsIgnoreCase(str)) {
			return (country);
		} else if ("postal_code".equalsIgnoreCase(str)) {
			return (postal_code);
		} else {
			throw new RuntimeException("No such criteria exist:" + str);
		}
	}
}
