package com.fw.dao;

import java.util.List;

import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessage;
import com.fw.beans.UsersWithFactsBean;
import com.fw.domain.LoadRequest;
import com.fw.domain.Users;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;

public interface IMarketingManager {

	List<Users> getAllNotContectedUsers(ProviderMarketingFiltersBean filtersBean) throws APIExceptions;
	
	List<UsersWithFactsBean> getSelectedUsers(List<Long> userIds) throws APIExceptions;

	List<LoadRequest> getLoadRequestsForMarketing(RequestMarketingFiltersBean filtersBean) throws APIExceptions;

	int getRequestMessageCount(ContactType type, long requestId) throws APIExceptions;

	int getRequestBidCount(long requestId) throws APIExceptions;

	double getRequestBidCharges(long loadRequestId, String colName) throws APIExceptions;

	List<RequestMarketingMessage> getRequestMarketingMessageDetails(RequestMarketingDetailFiltersBean filtersBean) throws APIExceptions;

	List<RequestMarketingMessage> getRequestMarketingCallDetails(RequestMarketingDetailFiltersBean filtersBean, boolean showPending, Users authuser) throws APIExceptions;
}
