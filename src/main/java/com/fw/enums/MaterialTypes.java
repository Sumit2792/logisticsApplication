package com.fw.enums;

public enum MaterialTypes {

	SOLID("SOLID"), 
	FRAGILE("FRAGILE"), 
	LIQUID("LIQUID"),
	BIOMATERIALS("BIOMATERIALS"),
	CERAMICS("CERAMICS"),
	COMPOSITES("COMPOSITES"),
	CONCRETE("CONCRETE"),
	ELECTRONIC("ELECTRONIC"), 
	OPTICAL("OPTICAL"),
	GLASS("GLASS"),
	METALS("METALS"),
	META_MATERIALS("META_MATERIALS"),
	NANO_MATERIALS("NANO_MATERIALS"),
	POLYMERS("POLYMERS"), 
	PLASTICS("PLASTICS"),
	SEMICONDUCTORS("SEMICONDUCTORS"),
	WOOD("WOOD"),
	TEXTILES("TEXTILES"),
	ADHESIVE("ADHESIVE"),
	DOCUMENTS("DOCUMENTS"),
	CHEMICALS("CHEMICALS"),
	RADIOACTIVE("RADIOACTIVE"),
	OTHER("OTHER");
	
	private final String dbString;

	private MaterialTypes(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	public static MaterialTypes fromString(String str) {
		if ("SOLID".equalsIgnoreCase(str)) {
			return (SOLID);
		} else if ("FRAGILE".equalsIgnoreCase(str)) {
			return (FRAGILE);
		} else if ("LIQUID".equalsIgnoreCase(str)) {
			return (LIQUID);
		} else if ("BIOMATERIALS".equalsIgnoreCase(str)) {
			return (BIOMATERIALS);
		} else if ("CERAMICS".equalsIgnoreCase(str)) {
			return (CERAMICS);
		} else if ("COMPOSITES".equalsIgnoreCase(str)) {
			return (COMPOSITES);
		} else if ("CONCRETE".equalsIgnoreCase(str)) {
			return (CONCRETE);
		} else if ("ELECTRONIC".equalsIgnoreCase(str)) {
			return (ELECTRONIC);
		} else if ("OPTICAL".equalsIgnoreCase(str)) {
			return (OPTICAL);
		} else if ("GLASS".equalsIgnoreCase(str)) {
			return (GLASS);
		} else if ("METALS".equalsIgnoreCase(str)) {
			return (METALS);
		} else if ("META_MATERIALS".equalsIgnoreCase(str)) {
			return (META_MATERIALS);
		} else if ("NANO_MATERIALS".equalsIgnoreCase(str)) {
			return (NANO_MATERIALS);
		} else if ("POLYMERS".equalsIgnoreCase(str)) {
			return (POLYMERS);
		} else if ("PLASTICS".equalsIgnoreCase(str)) {
			return (PLASTICS);
		} else if ("SEMICONDUCTORS".equalsIgnoreCase(str)) {
			return (SEMICONDUCTORS);
		} else if ("WOOD".equalsIgnoreCase(str)) {
			return (WOOD);
		} else if ("TEXTILES".equalsIgnoreCase(str)) {
			return (TEXTILES);
		} else if ("ADHESIVE".equalsIgnoreCase(str)) {
			return (ADHESIVE);
		} else if ("DOCUMENTS".equalsIgnoreCase(str)) {
			return (DOCUMENTS);
		} else if ("CHEMICALS".equalsIgnoreCase(str)) {
			return (CHEMICALS);
		} else if ("RADIOACTIVE".equalsIgnoreCase(str)) {
			return (RADIOACTIVE);
		} else if ("OTHER".equalsIgnoreCase(str)) {
			return (OTHER);
		} else {
			throw new RuntimeException("No such unit type:" + str);
		}
	}
}
