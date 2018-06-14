package com.fw.services;

import java.util.List;

import com.fw.beans.ProviderMarketingBean;
import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.ProviderMarketingSelectionBean;
import com.fw.beans.RequestMarketingBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessageDetailBean;
import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;

/**
 * Service layer class to have all business logics related to Marketing module.
 * @author Vikas Sonwal
 *
 */
public interface IMarketingService {
	
	List<ProviderMarketingBean> getProviderMarketingList(ProviderMarketingFiltersBean filtersBean) throws APIExceptions;

	int[] sendSelectedMarketingMessage(ProviderMarketingSelectionBean selectionBean, Users authuser) throws APIExceptions;

	List<RequestMarketingBean> getRequestMarketingList(RequestMarketingFiltersBean filtersBean, Users authuser) throws APIExceptions;
	
	RequestMarketingMessageDetailBean getRequestMarketingMessageDetails(RequestMarketingDetailFiltersBean filtersBean, boolean showPending, Users authuser) throws APIExceptions;

}
