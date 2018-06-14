package com.fw.services;

import java.sql.SQLException;

import com.fw.beans.AddLoadRequest;
import com.fw.beans.LoadRequestBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.exceptions.APIExceptions;

public interface ILoadRequestService {

	LoadRequestBean getLoadRequestById(Long loadRequestId) throws APIExceptions;

	AddLoadRequest addLoadRequest(AddLoadRequest loadRequest) throws APIExceptions;

	void updateLoadRequestById(AddLoadRequest loadRequest) throws APIExceptions;

	void deleteLoadRequest(Long id) throws APIExceptions;

	void updateLoadRequestStatus(LoadRequestStatusBean loadStatus) throws APIExceptions, SQLException;
}
