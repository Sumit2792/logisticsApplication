package com.fw.beans;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fw.enums.LoadRequestStatus;

import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * 
 * @author Narendra Gurjar
 *
 */
public class LoadRequestBean {

	private long loadRequestId;
	private long  userId;
	private boolean insuranceNeeded;
	private String note;
	private String truckType = "";
	private List<NotesBean> notes;
	private LoadRequestStatus status;
	private Date bidStart;
	private Date bidEnd;    
    private long createdBy;
    private long modifiedBy;
    private boolean isDeleted;
    private String userAttemptsJson;
    @ApiModelProperty(value = "timestamp of creatation date", required=false)
    private Date createdDate;
    @ApiModelProperty(value = "timestamp of modification date",required=false)
    private Date modifiedDate;
    private LoadRequestDetailsBean loadRequestDetails;
    private List<LoadPackageDetailsBean> loadPackageList;
    private List<BidsBean> bids=new ArrayList<>();
    private AddressBean address;
    //@JsonIgnore
    private OnBehalfOfUserBean requestOnBehalfOfUser;
    
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public long getUserId() {
		return userId;
	}
	
	public LoadRequestStatus getStatus() {
		return status;
	}
	public void setStatus(LoadRequestStatus status) {
		this.status = status;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public boolean isInsuranceNeeded() {
		return insuranceNeeded;
	}
	public void setInsuranceNeeded(boolean insuranceNeeded) {
		this.insuranceNeeded = insuranceNeeded;
	}
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public List<NotesBean> getNotes() {
		return notes;
	}
	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
	}
	public Date getBidStart() {
		return bidStart;
	}
	public void setBidStart(Date bidStart) {
		this.bidStart = bidStart;
	}
	public Date getBidEnd() {
		return bidEnd;
	}
	public void setBidEnd(Date bidEnd) {
		this.bidEnd = bidEnd;
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
	
	public LoadRequestDetailsBean getLoadRequestDetails() {
		return loadRequestDetails;
	}
	public void setLoadRequestDetails(LoadRequestDetailsBean loadRequestDetails) {
		this.loadRequestDetails = loadRequestDetails;
	}
	public List<LoadPackageDetailsBean> getLoadPackageList() {
		return loadPackageList;
	}
	public void setLoadPackageList(List<LoadPackageDetailsBean> loadPackageList) {
		this.loadPackageList = loadPackageList;
	}
	public OnBehalfOfUserBean getRequestOnBehalfOfUser() {
		return requestOnBehalfOfUser;
	}
	public void setRequestOnBehalfOfUser(OnBehalfOfUserBean requestOnBehalfOfUser) {
		this.requestOnBehalfOfUser = requestOnBehalfOfUser;
	}
	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public AddressBean getAddress() {
		return address;
	}
	public void setAddress(AddressBean address) {
		this.address = address;
	}
	public List<BidsBean> getBids() {
		return bids;
	}
	public void setBids(List<BidsBean> bids) {
		this.bids = bids;
	}
	public String getTruckType() {
		return truckType;
	}
	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}
	public String getUserAttemptsJson() {
		return userAttemptsJson;
	}
	public void setUserAttemptsJson(String userAttemptsJson) {
		this.userAttemptsJson = userAttemptsJson;
	}
    

}
