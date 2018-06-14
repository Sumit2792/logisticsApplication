package com.fw.services;

import java.util.List;

import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;

public interface IUserSourcesService {

	UserSources addUserSources(UserSources bidEntity) throws APIExceptions;

	void updateUserSourcesById(UserSources bidEntity) throws APIExceptions;

	List<UserSources> getAllUserSources() throws APIExceptions;

	UserSources getUserSourcesById(long bidId) throws APIExceptions;

	void deleteUserSourcesById(UserSources userId) throws APIExceptions;
}
