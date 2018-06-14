package com.fw.enums;

public enum TruckType {

	SMALL_MINI_TRUCK("SMALL_MINI_TRUCK"),
	LIGHT_MINIVAN("LIGHT_MINIVAN"),
	LIGHT_SPORT_UTILITY_VEHICLE("LIGHT_SPORT_UTILITY_VEHICLE"),
	LIGHT_CANOPY_EXPRESS("LIGHT_CANOPY_EXPRESS"),
	LIGHT_PICKUP_TRUCK("LIGHT_PICKUP_TRUCK"),
	LIGHT_PANEL_TRUCK("LIGHT_PANEL_TRUCK"),
	LIGHT_CAB_FORWARD("LIGHT_CAB_FORWARD"),
	LIGHT_TOW_TRUCK("LIGHT_TOW_TRUCK"),
	LIGHT_PANEL_VAN("LIGHT_PANEL_VAN"),
	LIGHT_SEDAN_DELIVERY("LIGHT_SEDAN_DELIVERY"),
	MEDIUM_BOX_TRUCK("MEDIUM_BOX_TRUCK"),
	MEDIUM_VAN("MEDIUM_VAN"),
	MEDIUM_CUTAWAY_VAN_CHASIS("MEDIUM_CUTAWAY_VAN_CHASIS"),
	MEDIUM_DUTY_TRUCK("MEDIUM_DUTY_TRUCK"),
	MEDIUM_STANDARD_TRUCK("MEDIUM_STANDARD_TRUCK"),
	MEDIUM_PLATFORM_TRUCK("MEDIUM_PLATFORM_TRUCK"),
	MEDIUM_FLATBED_TRUCK("MEDIUM_FLATBED_TRUCK"),
	MEDIUM_FIRE_TRUCK("MEDIUM_FIRE_TRUCK"),
	MEDIUM_MOTORHOME("MEDIUM_MOTORHOME"),
	HEAVY_CONCRETE_TRANSPORT_TRUCK("HEAVY_CONCRETE_TRANSPORT_TRUCK"),
	HEAVY_MOBILE_CRANE("HEAVY_MOBILE_CRANE"),
	HEAVY_DUMP_TRUCK("HEAVY_DUMP_TRUCK"),
	HEAVY_GARBAGE_TRUCK("HEAVY_GARBAGE_TRUCK"),
	HEAVY_LOG_CARRIER("HEAVY_LOG_CARRIER"),
	HEAVY_REFRIGERATOR_TRUCK("HEAVY_REFRIGERATOR_TRUCK"),
	HEAVY_TRACTOR_UNIT("HEAVY_TRACTOR_UNIT"),
	HEAVY_TANK_TRUCK("HEAVY_TANK_TRUCK"),
	VERY_HEAVY_ALMA_TRANSPORTER("VERY_HEAVY_ALMA_TRANSPORTER"),
	VERY_HEAVY_BALLAST_TRACTOR("VERY_HEAVY_BALLAST_TRACTOR"),
	VERY_HEAVY_HEAVY_HAULER("VERY_HEAVY_HEAVY_HAULER"),
	VERY_HEAVY_HAUL_TRUCK("VERY_HEAVY_HAUL_TRUCK");

	private final String dbString;

	private TruckType(String dbString) {
		this.dbString = dbString;
	}

	public String toDbString() {
		return (this.dbString);
	}

	
	public static TruckType fromString(String userType) {
		if ("SMALL_MINI_TRUCK".equalsIgnoreCase(userType)) {
			return (SMALL_MINI_TRUCK);
		} else if ("LIGHT_MINIVAN".equalsIgnoreCase(userType)) {
			return (LIGHT_MINIVAN);
		} else if ("LIGHT_SPORT_UTILITY_VEHICLE".equalsIgnoreCase(userType)) {
			return (LIGHT_SPORT_UTILITY_VEHICLE);
		} else if ("LIGHT_CANOPY_EXPRESS".equalsIgnoreCase(userType)) {
			return (LIGHT_CANOPY_EXPRESS);
		} else if ("LIGHT_PICKUP_TRUCK".equalsIgnoreCase(userType)) {
			return (LIGHT_PICKUP_TRUCK);
		} else if ("LIGHT_PANEL_TRUCK".equalsIgnoreCase(userType)) {
			return (LIGHT_PANEL_TRUCK);
		} else if ("LIGHT_CAB_FORWARD".equalsIgnoreCase(userType)) {
			return (LIGHT_CAB_FORWARD);
		} else if ("LIGHT_TOW_TRUCK".equalsIgnoreCase(userType)) {
			return (LIGHT_TOW_TRUCK);
		} else if ("LIGHT_PANEL_VAN".equalsIgnoreCase(userType)) {
			return (LIGHT_PANEL_VAN);
		} else if ("LIGHT_SEDAN_DELIVERY".equalsIgnoreCase(userType)) {
			return (LIGHT_SEDAN_DELIVERY);
		} else if ("MEDIUM_BOX_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_BOX_TRUCK);
		} else if ("MEDIUM_VAN".equalsIgnoreCase(userType)) {
			return (MEDIUM_VAN);
		} else if ("MEDIUM_CUTAWAY_VAN_CHASIS".equalsIgnoreCase(userType)) {
			return (MEDIUM_CUTAWAY_VAN_CHASIS);
		} else if ("MEDIUM_DUTY_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_DUTY_TRUCK);
		} else if ("MEDIUM_STANDARD_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_STANDARD_TRUCK);
		} else if ("MEDIUM_PLATFORM_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_PLATFORM_TRUCK);
		} else if ("MEDIUM_FLATBED_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_FLATBED_TRUCK);
		} else if ("MEDIUM_FIRE_TRUCK".equalsIgnoreCase(userType)) {
			return (MEDIUM_FIRE_TRUCK);
		} else if ("MEDIUM_MOTORHOME".equalsIgnoreCase(userType)) {
			return (MEDIUM_MOTORHOME);
		} else if ("HEAVY_CONCRETE_TRANSPORT_TRUCK".equalsIgnoreCase(userType)) {
			return (HEAVY_CONCRETE_TRANSPORT_TRUCK);
		} else if ("HEAVY_MOBILE_CRANE".equalsIgnoreCase(userType)) {
			return (HEAVY_MOBILE_CRANE);
		} else if ("HEAVY_DUMP_TRUCK".equalsIgnoreCase(userType)) {
			return (HEAVY_DUMP_TRUCK);
		} else if ("HEAVY_GARBAGE_TRUCK".equalsIgnoreCase(userType)) {
			return (HEAVY_GARBAGE_TRUCK);
		} else if ("HEAVY_LOG_CARRIER".equalsIgnoreCase(userType)) {
			return (HEAVY_LOG_CARRIER);
		} else if ("HEAVY_REFRIGERATOR_TRUCK".equalsIgnoreCase(userType)) {
			return (HEAVY_REFRIGERATOR_TRUCK);
		} else if ("HEAVY_TRACTOR_UNIT".equalsIgnoreCase(userType)) {
			return (HEAVY_TRACTOR_UNIT);
		} else if ("HEAVY_TANK_TRUCK".equalsIgnoreCase(userType)) {
			return (HEAVY_TANK_TRUCK);
		} else if ("VERY_HEAVY_ALMA_TRANSPORTER".equalsIgnoreCase(userType)) {
			return (VERY_HEAVY_ALMA_TRANSPORTER);
		} else if ("VERY_HEAVY_BALLAST_TRACTOR".equalsIgnoreCase(userType)) {
			return (VERY_HEAVY_BALLAST_TRACTOR);
		} else if ("VERY_HEAVY_HEAVY_HAULER".equalsIgnoreCase(userType)) {
			return (VERY_HEAVY_HEAVY_HAULER);
		} else if ("VERY_HEAVY_HAUL_TRUCK".equalsIgnoreCase(userType)) {
			return (VERY_HEAVY_HAUL_TRUCK);
		} else {
			throw new RuntimeException("Invalid Truck Type");
		}
	}
}
