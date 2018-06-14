package com.fw.dao;

import java.util.List;

import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.domain.Bid;
import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;

public interface IBidManager {

	/**
	 * Persist the object normally.
	 */

	List<BidsBean> getBidsByLoadRequestId(long loadRequestId);

	BidsBean getBidById(long bidId);

	void deleteBidById(long bidId) throws APIExceptions;

	AddBidsBean addBid(AddBidsBean bid) throws APIExceptions;

	void updateBidById(AddBidsBean bid) throws APIExceptions;

	void updateBidStatus(BidStatusBean bidStatus) throws APIExceptions;

	Bid getBidByLoadRequestUserid(long userId);


	List<String> getAllBiddersPhoneNumberByLoadRequestId(long loadRequestId);

	void batchUpdateBidStatus(List<BidStatusBean> bidStatus) throws APIExceptions;

	BidsBean getAwardedBidByLoadRequestId(Long loadRequestId) throws APIExceptions;

	List<Long> getAllBiddersIdsByLoadRequestId(long loadRequestId);

	List<Long> getAllAwardedBidIdsByUserId(long userId);
	
	List<BidsBean> getNotAwardedBidsByLoadRequestId(long loadRequestId);

	Boolean isBidExistForLoadRequest(long userId, long loadRequestId);

	List<Users> getBidderInfoByLoadRequestId(long loadRequestId);

}
