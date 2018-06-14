package com.fw.services;

import java.util.List;

import com.fw.beans.ChangePasswordBean;
import com.fw.beans.LoginResultBean;
import com.fw.beans.UserDetailsBean;
import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

public interface IUserService {

	void createUserInfo(Users users) throws InvalidUsernameException, APIExceptions;

	List<UserDetailsBean> getAllUsers() throws InvalidUsernameException, APIExceptions;

	UserDetailsBean getUserByUserId(Long userId) throws APIExceptions;

	LoginResultBean getUserByUserName(String requestedUserId) throws APIExceptions;

	Users verifyUserLoggedIn(Users user)throws APIExceptions;

	void deleteUser(long userId) throws APIExceptions;

	void changePassword(ChangePasswordBean changePassword) throws APIExceptions;

	void updateUsername(Users user, String newNumber) throws APIExceptions;

	void updateUserRole(Users user, String newUserRole);

}
