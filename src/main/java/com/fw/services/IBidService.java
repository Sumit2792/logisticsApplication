package com.fw.services;


import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.exceptions.APIExceptions;

 public interface IBidService {
	

		BidsBean getBidById(long bidId) throws APIExceptions;

		void deleteBidById(long requestBidId) throws APIExceptions;

		AddBidsBean addBid(AddBidsBean requestbids) throws APIExceptions;

		void updateBidById(AddBidsBean bidEntity) throws APIExceptions;

		void updateBidStatus(BidStatusBean bidStatus) throws APIExceptions;
}
