package com.fw.dao;

import java.util.List;

import com.fw.domain.UserFacts;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

public interface IUserFactsManager {

	UserFacts getUserFactssById(long bidId)  throws APIExceptions;

	List<UserFacts> getUserFactsByUserId(Long userId)  throws APIExceptions;

	List<UserFacts> getUserFactsByUserIds(List<Long> userIds) throws APIExceptions;

	void deleteUserFactsById(long id, boolean purge) throws APIExceptions;

	void persistUserFacts(List<UserFacts> userFacts, boolean forNewNumber) throws APIExceptions;

	void updateUserFactsByUserId(List<UserFacts> userFacts) throws APIExceptions;

	UserFacts getContactNumber(String userContactNumber) throws InvalidUsernameException;

	List<UserFacts> getUserFactsByUserIdAndFactName(Long userId, String factName)  throws APIExceptions;

	boolean verifyUserEmailExist(long userId, String userFact) throws APIExceptions;

	void updateUserEmail(long userId, String userEmail, String userFact);

	void persistUserEmail(long userId, String userEmail, String userFact);
}
