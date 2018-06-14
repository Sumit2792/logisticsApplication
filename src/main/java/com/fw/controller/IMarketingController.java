package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.ProviderMarketingBean;
import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.ProviderMarketingSelectionBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessageDetailBean;
import com.fw.exceptions.APIExceptions;

public interface IMarketingController {

	ResponseEntity<List<ProviderMarketingBean>> getProvidersForMarketing(ProviderMarketingFiltersBean filtersBean) throws APIExceptions;
	
	ResponseEntity<?> sendSelectedMessage(ProviderMarketingSelectionBean selectionBean) throws APIExceptions;

	ResponseEntity<?> getRequestMarketingData(RequestMarketingFiltersBean filtersBean) throws APIExceptions;
	
	ResponseEntity<RequestMarketingMessageDetailBean> getRequestMarketingMessageDetails(RequestMarketingDetailFiltersBean filtersBean) throws APIExceptions;
}
