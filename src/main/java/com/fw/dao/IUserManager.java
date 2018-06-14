package com.fw.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fw.beans.LoginResultBean;
import com.fw.beans.UserDetailsBean;
import com.fw.domain.Users;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

public interface IUserManager {

	/**
	 * Persist the object normally.
	 * 
	 * @return
	 * @throws APIExceptions
	 */
	Long persist(Users entity) throws APIExceptions;

	Users getUserByUserName(String userName) throws InvalidUsernameException;

	boolean isUserExist(String userName) throws APIExceptions;

	boolean updateOTP(String userName, Integer otp);

	Integer getOTPByUserName(String userName) throws APIExceptions;

	String getUserNameById(long userId) throws APIExceptions;

	UserDetailsBean getUserInfoByUserId(Long userId) throws InvalidUsernameException;

	LoginResultBean getUserDataByUserName(String userName) throws InvalidUsernameException, APIExceptions;

	void updateLoginStatus(Users user, UserLoginStatus getActiveLoginStatus);

	boolean updateOTP(String userName, Integer otp, String userRole);

	void deleteUser(long userId, long deletedBy);

	boolean updatePassword(String userName, String pwd, Long updatedBy);

	List<Users> getUserNameByCities(Set<String> listOfCities, int remainingCitiesCount,
			List<Long> listofUsersInSentMessage) throws InvalidUsernameException;

	Map<Long, String> getUsersByIds(Set<Long> userIds);

	boolean updateUsername(Users user);

	List<Users> getUnAwardedCPUsersAfterLRBooked(List<Long> bidderUsersList) throws InvalidUsernameException;

	List<UserDetailsBean> getAllUserRowMapper(UserRoles loggedINUserRole);

	Users getUserByUserId(long userId) throws InvalidUsernameException;

	void saveAverageRating(Long userId, Double currentRating) throws InvalidUsernameException;

}
