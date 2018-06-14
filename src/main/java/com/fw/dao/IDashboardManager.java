package com.fw.dao;

import java.sql.SQLException;
import java.util.List;

import com.fw.beans.BidsBean;
import com.fw.beans.DashBoardBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.PaymentHistoryBean;
import com.fw.domain.Bid;
import com.fw.domain.LoadRequest;
import com.fw.enums.InBetweenCitiesExist;
import com.fw.exceptions.APIExceptions;

public interface IDashboardManager {

	DashBoardBean getDashBoard();

	List<LoadRequest> getAllLoadRequest();

	List<BidsBean> getBidsByLoadRequestId(long loadRequestId);

	List<BidsBean> getAllBidsByUserId(long userId);

	List<LoadRequest> getAllLoadRequestByRegion(String country, String state, String city,
			InBetweenCitiesExist cityExist);

	List<LoadRequest> getAllLoadRequestByUserId(long userId);

	List<LoadRequest> getAllLoadRequestCreateByCSRId(long userId);

	List<BidsBean> getAllBidsCreatedByCSRId(long bidderUserId);

	List<LoadRequest> getAllBookedLoadRequestByUserId(long userId);

	BidsBean getAwardedBidByLoadRequestId(Long loadRequestId) throws APIExceptions;

	List<PaymentHistoryBean> getPaymentHistory(PaymentFilters filters);


}
