package com.fw.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.exceptions.APIExceptions;

public interface BidController {

	ResponseEntity<BidsBean> getBidById(long bidId) throws APIExceptions;
	void deleteBidById(Long bidId) throws APIExceptions;
	ResponseEntity<?> updateBidStatus(BidStatusBean bidStatus) throws APIExceptions;
	ResponseEntity<AddBidsBean> addBidRequest(AddBidsBean bidData, BindingResult result) throws APIExceptions;
	ResponseEntity<AddBidsBean> updateBidById(AddBidsBean bidForm, BindingResult result) throws APIExceptions;
}
