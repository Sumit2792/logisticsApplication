package com.fw.dao;

import java.util.List;

import com.fw.beans.LoadRequestDetailsBean;
import com.fw.beans.AddressBean;
import com.fw.beans.NotesBean;
import com.fw.domain.LoadRequestDetail;

public interface ILoadRequestDetailManager {

	/**
	 * Persist the object normally.
	 */
	void persist(LoadRequestDetail loadRequestDetail);

	void updateLoadRequestDetailByRequestId(LoadRequestDetail loadRequestDetail, long loadRequestId);

	List<LoadRequestDetail> getAllLoadRequestDetails();

	LoadRequestDetailsBean getLoadRequestDetailByRequestId(Long loadRequestId);

	LoadRequestDetail getLoadRequestDetailById(long loadRequestId);

	void deleteLoadRequestDetailsBy_LoadRequestId(Long loadRequestId);

}
