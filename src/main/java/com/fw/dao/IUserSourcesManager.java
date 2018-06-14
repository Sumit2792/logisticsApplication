package com.fw.dao;

import java.util.List;

import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;

public interface IUserSourcesManager {

	/**
	 * Persist the object normally.
	 */
	UserSources persistUserSources(UserSources logEntity)  throws APIExceptions;

	void updateUserSourcesById(UserSources logEntity)  throws APIExceptions;

	List<UserSources> getAllUserSourcesRowMapper()  throws APIExceptions;

	UserSources getUserSourcesById(long bidId)  throws APIExceptions;

	void deleteUserSourcesById(UserSources Id)  throws APIExceptions;

}
