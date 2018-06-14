package com.fw.beans;

import java.util.Date;


public class LoadRequestDetailsBean
   {
	    private long loadRequestDetailId;
	    private long loadRequestId;
		private long startLocationId;
		private String startLocation;
		private long endLocationId;
		private String endLocation;
		private Date startDateTime;
		private Date expectedEndDateTime;
		private Date actualEndDateTime;
		
		
		public long getLoadRequestDetailId() {
			return loadRequestDetailId;
		}
		public void setLoadRequestDetailId(long loadRequestDetailId) {
			this.loadRequestDetailId = loadRequestDetailId;
		}
		public long getLoadRequestId() {
			return loadRequestId;
		}
		public void setLoadRequestId(long loadRequestId) {
			this.loadRequestId = loadRequestId;
		}
		public long getStartLocationId() {
			return startLocationId;
		}
		public void setStartLocationId(long startLocationId) {
			this.startLocationId = startLocationId;
		}
		public long getEndLocationId() {
			return endLocationId;
		}
		public void setEndLocationId(long endLocationId) {
			this.endLocationId = endLocationId;
		}
		public Date getStartDateTime() {
			return startDateTime;
		}
		public void setStartDateTime(Date startDateTime) {
			this.startDateTime = startDateTime;
		}
		public Date getExpectedEndDateTime() {
			return expectedEndDateTime;
		}
		public void setExpectedEndDateTime(Date expectedEndDateTime) {
			this.expectedEndDateTime = expectedEndDateTime;
		}
		public Date getActualEndDateTime() {
			return actualEndDateTime;
		}
		public void setActualEndDateTime(Date actualEndDateTime) {
			this.actualEndDateTime = actualEndDateTime;
		}
		public String getStartLocation() {
			return startLocation;
		}
		public void setStartLocation(String startLocation) {
			this.startLocation = startLocation;
		}
		public String getEndLocation() {
			return endLocation;
		}
		public void setEndLocation(String endLocation) {
			this.endLocation = endLocation;
		}
		
		
   }