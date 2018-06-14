package com.fw.beans;

import java.util.Date;
import java.util.List;


import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;

public class BidsBean {
	
    private long bidId;
    private long loadRequestId;
    private long bidderUserId;
    private double bidderRating;
    private List<NotesBean>notes;
    private double amount;
    private CurrencyCodes currency;
    private BidStatus status;
    private double faberCharges;
    private double onlinePaymentCharges;
    private Date deliveredTime;
    private long createdBy;
    private long modifiedBy;
    private Date createdDate;
    private Date modifiedDate;
    
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
	public long getLoadRequestId() {
		return loadRequestId;
	}
	public void setLoadRequestId(long loadRequestId) {
		this.loadRequestId = loadRequestId;
	}
	public long getBidderUserId() {
		return bidderUserId;
	}
	public void setBidderUserId(long bidderUserId) {
		this.bidderUserId = bidderUserId;
	}
	
	public List<NotesBean> getNotes() {
		return notes;
	}
	public void setNotes(List<NotesBean> notes) {
		this.notes = notes;
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
	public double getOnlinePaymentCharges() {
		return onlinePaymentCharges;
	}
	public void setOnlinePaymentCharges(double onlinePaymentCharges) {
		this.onlinePaymentCharges = onlinePaymentCharges;
	}
	

}
