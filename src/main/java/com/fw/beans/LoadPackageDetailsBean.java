package com.fw.beans;

import java.util.Date;

import com.fw.enums.LengthUnits;
import com.fw.enums.MaterialTypes;
import com.fw.enums.WeightUnits;

public class LoadPackageDetailsBean
   {
	//
	    private long loadRequestPackagesId;         
	    private long loadRequestId;
	    private MaterialTypes materialType;
		private double packageLength ;
		private double packageWidth ;
		private double packageHeight ;
		private LengthUnits lengthUnit;
	    private double weight;
	    private WeightUnits weightUnit;
	    private int packageCount;
	    private String note;
	    private long createdBy;
		private long modifiedBy;
		private Date createdDate;
		private Date modifiedDate;
	
		
		public long getLoadRequestPackagesId() {
			return loadRequestPackagesId;
		}
		public void setLoadRequestPackagesId(long loadRequestPackagesId) {
			this.loadRequestPackagesId = loadRequestPackagesId;
		}
		public long getLoadRequestId() {
			return loadRequestId;
		}
		public void setLoadRequestId(long loadRequestId) {
			this.loadRequestId = loadRequestId;
		}
		public MaterialTypes getMaterialType() {
			return materialType;
		}
		public void setMaterialType(MaterialTypes materialType) {
			this.materialType = materialType;
		}
		public double getPackageLength() {
			return packageLength;
		}
		public void setPackageLength(double packageLength) {
			this.packageLength = packageLength;
		}
		public double getPackageWidth() {
			return packageWidth;
		}
		public void setPackageWidth(double packageWidth) {
			this.packageWidth = packageWidth;
		}
		public double getPackageHeight() {
			return packageHeight;
		}
		public void setPackageHeight(double packageHeight) {
			this.packageHeight = packageHeight;
		}
		public LengthUnits getLengthUnit() {
			return lengthUnit;
		}
		public void setLengthUnit(LengthUnits lengthUnit) {
			this.lengthUnit = lengthUnit;
		}
		public double getWeight() {
			return weight;
		}
		public void setWeight(double weight) {
			this.weight = weight;
		}
		public WeightUnits getWeightUnit() {
			return weightUnit;
		}
		public void setWeightUnit(WeightUnits weightUnit) {
			this.weightUnit = weightUnit;
		}
		public int getPackageCount() {
			return packageCount;
		}
		public void setPackageCount(int packageCount) {
			this.packageCount = packageCount;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		public long getCreatedBy() {
			return createdBy;
		}
		public void setCreatedBy(long createdBy) {
			this.createdBy = createdBy;
		}
		public long getModifiedBy() {
			return modifiedBy;
		}
		public void setModifiedBy(long modifiedBy) {
			this.modifiedBy = modifiedBy;
		}
		public Date getCreatedDate() {
			return createdDate;
		}
		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}
		public Date getModifiedDate() {
			return modifiedDate;
		}
		public void setModifiedDate(Date modifiedDate) {
			this.modifiedDate = modifiedDate;
		}
	    
		
	    
   }