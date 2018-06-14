package com.fw.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fw.beans.ChangePasswordBean;
import com.fw.beans.UserDetailsBean;
import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;

public interface UserController {

	ResponseEntity<List<UserDetailsBean>> getAllUsers() throws APIExceptions;

	ResponseEntity<UserDetailsBean> getUserByUserId(Long userId) throws APIExceptions;

	//ResponseEntity<LoginResultBean> getUserByUserName(String userName) throws APIExceptions;

	ResponseEntity<?> sendOTP(Users user, String acceptLang, String newNumber) throws SQLException, APIExceptions, UnsupportedEncodingException;

	ResponseEntity<?> loginUser(Users user) throws APIExceptions;

	ResponseEntity<?> changeUserStatus(Users user) throws APIExceptions, SQLException;

	//ResponseEntity<?> verifyOTPAndLogin(Users user) throws SQLException, APIExceptions;

	boolean isUserLoggedIn();

	ResponseEntity<?> removeUser(Long userId) throws APIExceptions;

	ResponseEntity<?> changePassword(ChangePasswordBean changePassword) throws APIExceptions;

	ResponseEntity<?> verifyOTPAndLogin(Users user, BindingResult result) throws SQLException, APIExceptions;

	ResponseEntity<?> saveUserInfo(Users user, BindingResult result) throws APIExceptions;

	ResponseEntity<?> updateUsername(Users user, String acceptLang, BindingResult result, String newNumber) throws APIExceptions;

	ResponseEntity<?> generateNewPassword(Users user) throws APIExceptions;

	ResponseEntity<?> updateUserRole(Users user, String acceptLang, BindingResult result, String newNumber)
			throws APIExceptions;
}
