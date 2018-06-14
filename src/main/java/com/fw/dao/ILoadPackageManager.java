package com.fw.dao;

import java.util.List;

import com.fw.beans.LoadPackageDetailsBean;
import com.fw.domain.LoadRequestPackage;
import com.fw.exceptions.APIExceptions;

public interface ILoadPackageManager {

	/**
	 * Persist the object normally.
	 * 
	 * @throws APIExceptions
	 */

	void persist(LoadRequestPackage loadPackageEntity) throws APIExceptions;


	List<LoadRequestPackage> getAllLoadPackageRowMapper();

	List<LoadPackageDetailsBean> getLoadPackageDetailsByRequestId(long loadRequestId);

	List<LoadRequestPackage> getLoadPackageByRequestId(long requestId);

	void updateLoadPackageByRequestId(LoadRequestPackage loadPackage) throws APIExceptions;

	void deleteLoadPackages(long loadRequestId) throws APIExceptions;

	LoadRequestPackage getSpecificLoadPackageDetailByRequestId(long loadRequestId);

}
