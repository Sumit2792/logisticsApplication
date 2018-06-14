package com.fw.beans;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;



public class AddBidsBean {
	
	private long loadRequestId;
    private long bidId;
    private long bidderUserId;
    private double bidderRating;
    @Size(max=300, message="You can type max 300 characters in notes.")
    private String note;
    @Max(value=100000 , message="MAX BID AMOUNT UPTO 1 LAC.[RS. 100000/-]")
    @NotNull(message= "Amount can not be blank.")
    @Min(value=1, message="Amount can not be zero or negative.")
    private double amount;
    private CurrencyCodes currency;
    private BidStatus status;
    private double faberCharges;
    private Date deliveredTime;
    private long createdBy;
    private long modifiedBy;
    private Date createdDate;
    private Date modifiedDate;
	private String userAttemptsJson = "{\r\n" + 
			"   \"edit\" :0,\r\n" + 
			"   \"feedback\":0\r\n" + 
			"}";
	
    private OnBehalfOfUserBean bidOnBehalfOfUser;
    
    
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public long getBidId() {
		return bidId;
	}
	public void setBidId(long bidId) {
		this.bidId = bidId;
	}
	
	public Date getDeliveredTime() {
		return deliveredTime;
	}
	public void setDeliveredTime(Date deliveredTime) {
		this.deliveredTime = deliveredTime;
	}
	
	
	public double getBidderRating() {
		return bidderRating;
	}
	public void setBidderRating(double bidderRating) {
		this.bidderRating = bidderRating;
	}
	public long getBidderUserId() {
		return bidderUserId;
	}
	public void setBidderUserId(long bidderUserId) {
		this.bidderUserId = bidderUserId;
	}
	
	
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public CurrencyCodes getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyCodes currency) {
		this.currency = currency;
	}
	
	public BidStatus getStatus() {
		return status;
	}
	public void setStatus(BidStatus status) {
		this.status = status;
	}
	public double getFaberCharges() {
		return faberCharges;
	}
	public void setFaberCharges(double faberCharges) {
		this.faberCharges = faberCharges;
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
	public OnBehalfOfUserBean getBidOnBehalfOfUser() {
		return bidOnBehalfOfUser;
	}
	public void setBidOnBehalfOfUser(OnBehalfOfUserBean bidOnBehalfOfUser) {
		this.bidOnBehalfOfUser = bidOnBehalfOfUser;
	}
    

}
