package com.fw.domain;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.fw.enums.LengthUnits;
import com.fw.enums.MaterialTypes;
import com.fw.enums.WeightUnits;

public class LoadRequestPackage {

	private long loadRequestPackagesId;
	@Max(value = 2147483647, message = "Package count is out of range.")
	private int packageCount;
	@Max(value = 2000, message = "Package Length is out of range. MAX allowed value is : 2000")
	private double packageLength;
	@Max(value = 2000, message = "Package Width is out of range. MAX allowed value is : 2000")
	private double packageWidth;
	@Max(value = 2000, message = "Package Height is out of range. MAX allowed value is : 2000")
	private double packageHeight;
	private LengthUnits lengthUnit;
	private long loadRequestId;
	private MaterialTypes materialType;
	private double weight;
	private WeightUnits weightUnit;
	@Size(max = 300, message = "You can type max 300 characters in notes.")
	private String note;
	private long createdBy;
	private long modifiedBy;
	private Date createdDate;
	private Date modifiedDate;
	private boolean isDeleted;

	// Generate Getters and Setters
	public long getLoadRequestId() {
		return loadRequestId;
	}

	public long getLoadRequestPackagesId() {
		return loadRequestPackagesId;
	}

	public void setLoadRequestPackagesId(long loadRequestPackagesId) {
		this.loadRequestPackagesId = loadRequestPackagesId;
	}

	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}

	public int getPackageCount() {
		return packageCount;
	}

	public void setPackageCount(int packageCount) {
		this.packageCount = packageCount;
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

	public MaterialTypes getMaterialType() {
		return materialType;
	}

	public void setMaterialType(MaterialTypes materialType) {
		this.materialType = materialType;
	}

	public WeightUnits getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(WeightUnits weightUnit) {
		this.weightUnit = weightUnit;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}
