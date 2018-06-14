package com.fw.dao;

import java.util.List;
import com.fw.beans.AddLoadRequest;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.domain.LoadRequest;
import com.fw.enums.LoadRequestStatus;
import com.fw.exceptions.APIExceptions;

public interface ILoadRequestManager {

	/**
	 * Persist the object normally.
	 * @throws APIExceptions 
	 */

	void updateLoadRequestById(AddLoadRequest loadRequest) throws APIExceptions;

	List<LoadRequest> getAllLoadRequest();

	LoadRequest getLoadRequestById(Long id) throws APIExceptions ;

	LoadRequest getLoadRequestByIdAndLoaderId(Long userId, Long loadRequestId);

	void deleteLoadRequest(long loadRequestId) throws APIExceptions;

	long insertLoadRequest(AddLoadRequest loadRequest) throws APIExceptions;

	void updateLoadRequestStatus(LoadRequestStatusBean loadStatus) throws APIExceptions;
	
	List<LoadRequest> getAllLoadRequestByStatus(LoadRequestStatus statusValue);

	List<LoadRequest> getAllLoadRequestByStatus();

	List<Long> getAllBookedRequestIdsByUserId(long userId);	
}
