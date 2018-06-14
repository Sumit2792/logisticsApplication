package com.fw.beans;

import javax.validation.Valid;

public class GeometryBean {

	@Valid
	private BoundsViewport bounds;
	private LocationBean location;
	private String location_type;
	@Valid
	private BoundsViewport viewport;

	// Generate getters and setters
	public BoundsViewport getBounds() {
		return bounds;
	}

	public void setBounds(BoundsViewport bounds) {
		this.bounds = bounds;
	}

	public LocationBean getLocation() {
		return location;
	}

	public void setLocation(LocationBean location) {
		this.location = location;
	}

	public String getLocation_type() {
		return location_type;
	}

	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}

	public BoundsViewport getViewport() {
		return viewport;
	}

	public void setViewport(BoundsViewport viewport) {
		this.viewport = viewport;
	}

}
