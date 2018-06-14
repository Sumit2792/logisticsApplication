package com.fw.beans;

import javax.validation.Valid;

public class BoundsViewport {
	
	@Valid
	private LatLongDirections northeast;
	@Valid
	private LatLongDirections southwest;

	//Generate getters and setters
	public LatLongDirections getNortheast() {
		return northeast;
	}

	public void setNortheast(LatLongDirections northeast) {
		this.northeast = northeast;
	}

	public LatLongDirections getSouthwest() {
		return southwest;
	}

	public void setSouthwest(LatLongDirections southwest) {
		this.southwest = southwest;
	}

}
