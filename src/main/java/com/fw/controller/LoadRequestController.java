package com.fw.controller;


import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fw.beans.AddLoadRequest;
import com.fw.beans.LoadRequestBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.exceptions.APIExceptions;

public interface LoadRequestController {



	ResponseEntity<LoadRequestBean> getLoadRequestById(long loadRequestId) throws APIExceptions;

	ResponseEntity<?> deleteLoadRequest(Long loadRequestId) throws APIExceptions;

	ResponseEntity<?> updateLoadRequestStatus(LoadRequestStatusBean loadStatus) throws APIExceptions, SQLException;

	ResponseEntity<?> updateLoadRequestById(AddLoadRequest loadRequest, BindingResult result) throws APIExceptions;

	ResponseEntity<AddLoadRequest> addLoadRequest(AddLoadRequest loadRequest, BindingResult result)
			throws APIExceptions;
}
