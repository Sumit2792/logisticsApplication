package com.fw.services;

import java.util.List;

import com.fw.beans.UserFactsBean;
import com.fw.domain.UserFacts;
import com.fw.exceptions.APIExceptions;

public interface IUserFactsService {

	void addUserFacts(UserFactsBean facts) throws APIExceptions;

	List<UserFacts> getUserFactsById(long bidId) throws APIExceptions;

	void deleteUserFactsById(long factId) throws APIExceptions;

}
