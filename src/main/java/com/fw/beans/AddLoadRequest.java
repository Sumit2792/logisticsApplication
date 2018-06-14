package com.fw.beans;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.fw.domain.LoadRequestDetail;
import com.fw.domain.LoadRequestPackage;
import com.fw.enums.LoadRequestStatus;

import io.swagger.annotations.ApiModelProperty;
/**
 * 
 * 
 * @author Narendra Gurjar
 *
 */
public class AddLoadRequest {

	private long loadRequestId;
	private long  userId;
	private boolean insuranceNeeded;
	@Size(max=300, message="You can type max 300 characters in notes.")
	private String note;
	private LoadRequestStatus status;
	private Date bidStart;
	private Date bidEnd;   
	private String truckType;
    private long createdBy;
    private long modifiedBy;
	private String userAttemptsJson  = " {\r\n" + 
			"   \"award\" :0,\r\n" + 
			"   \"edit\":0,\r\n" + 
			"   \"hold\" :0,\r\n" + 
			"   \"block\":0\r\n" + 
			"}";
    
    @ApiModelProperty(value = "timestamp of creatation date", required=false)
    private Date createdDate;
    @ApiModelProperty(value = "timestamp of modification date",required=false)
    private Date modifiedDate;
    @Valid
    private LoadRequestDetail loadRequestDetails;
    @Valid
    private List<LoadRequestPackage> loadPackageList= new ArrayList<LoadRequestPackage>();
    @Valid
    private AddressBean address;
    //@JsonIgnore
    private OnBehalfOfUserBean requestOnBehalfOfUser;
    
	public long getLoadRequestId() {
		return loadRequestId;
	}
	
	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
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
	
	public String getUserAttemptsJson() {
		return userAttemptsJson;
	}

	public void setUserAttemptsJson(String userAttemptsJson) {
		this.userAttemptsJson = userAttemptsJson;
	}

	public LoadRequestDetail getLoadRequestDetails() {
		return loadRequestDetails;
	}
	public void setLoadRequestDetails(LoadRequestDetail loadRequestDetails) {
		this.loadRequestDetails = loadRequestDetails;
	}

	
	public List<LoadRequestPackage> getLoadPackageList() {
		return loadPackageList;
	}
	public void setLoadPackageList(List<LoadRequestPackage> loadPackageList) {
		this.loadPackageList = loadPackageList;
	}
	public OnBehalfOfUserBean getRequestOnBehalfOfUser() {
		return requestOnBehalfOfUser;
	}
	public void setRequestOnBehalfOfUser(OnBehalfOfUserBean requestOnBehalfOfUser) {
		this.requestOnBehalfOfUser = requestOnBehalfOfUser;
	}
	public AddressBean getAddress() {
		return address;
	}
	public void setAddress(AddressBean address) {
		this.address = address;
	}
    
    

}
