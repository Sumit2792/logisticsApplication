package com.fw.services;


import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.exceptions.UnAuthorizedActionException;

public interface ISmsService {

	String sendOTPSMS(Users user, String newNumber) throws  APIExceptions, SQLException, UnsupportedEncodingException;

	boolean verifyOTP(String user, String otp) throws APIExceptions;

	void generateNewPassword(String userName) throws InvalidUsernameException, UnAuthorizedActionException, APIExceptions;

}
