package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fw.beans.ChangePasswordBean;
import com.fw.beans.LoginResultBean;
import com.fw.beans.UserDetailsBean;
import com.fw.config.AuthUserDetails;
import com.fw.config.JwtTokenUtil;
import com.fw.controller.UserController;
import com.fw.dao.IUserManager;
import com.fw.domain.Users;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.InvalidActionException;
import com.fw.exceptions.SMSServiceException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.ISmsService;
import com.fw.services.IUserService;
import com.fw.utils.LocalUtils;
import com.fw.validations.DataValidations;

/**
 * 
 * @author Faber
 *
 */
@RestController
@Controller
@RequestMapping(value = "/mLogistics")
public class UserControllerImpl implements UserController {

	@Autowired
	private AuthUserDetails authUser;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserManager userManager;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	DataValidations dataValidations;

	@Autowired
	private ISmsService smsService;

	private Logger log = Logger.getLogger(UserControllerImpl.class);

	@Override
	@RequestMapping(value = "/public/users/register", method = { POST })
	public ResponseEntity<?> saveUserInfo(@Valid @RequestBody Users user, BindingResult result) throws APIExceptions {
		try {
			validateJson(result);
			userService.createUserInfo(user);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
		}
	}

	@Override
	@RequestMapping(value = "/private/users/getUsers", method = { GET })
	public ResponseEntity<List<UserDetailsBean>> getAllUsers() throws APIExceptions {
		try {
			return new ResponseEntity<List<UserDetailsBean>>(userService.getAllUsers(), HttpStatus.OK);
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
		}
	}

	@Override
	@RequestMapping(value = "/private/users/getUserById/{userId}", method = { GET })
	public ResponseEntity<UserDetailsBean> getUserByUserId(@PathVariable(value = "userId", required = true) Long userId)
			throws APIExceptions {
		try {
			UserDetailsBean userDetails = userService.getUserByUserId(userId);
			return new ResponseEntity<UserDetailsBean>(userDetails, HttpStatus.OK);
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
		}
		
	}

	@Override
	@RequestMapping(value = "/private/users/changePassword", method = { PATCH })
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordBean changePassword) throws APIExceptions {
		try {
			userService.changePassword(changePassword);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
		}
	}

	@Override
	@RequestMapping(value = "/private/users/deleteUser/{id}", method = { DELETE })
	public ResponseEntity<?> removeUser(@PathVariable(value = "id", required = true) Long userId) throws APIExceptions {
		try {
			userService.deleteUser(userId);
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
		}	
	}

/*	@Override
	@RequestMapping(value = "/private/users/updateUserInfo", method = { PATCH })
	public ResponseEntity<?> modifyUserByUserId(@Valid @RequestBody UserDetailsBean user, BindingResult result)
			throws APIExceptions {

		validateJson(result);
		userService.updateUser(user);
		return new ResponseEntity<>(HttpStatus.OK);
	}*/

	@Override
	@RequestMapping(value = "/public/users/doLogin", method = { POST })
	public ResponseEntity<?> loginUser(@RequestBody Users userRequest) throws APIExceptions {
		Users user = null;
		LoginResultBean userBean = null;
		HashMap<String, String> tempErroMsg = new HashMap<>();
		try {
			user = userManager.getUserByUserName(userRequest.getUserName());
		} catch (Exception e) {
			log.error("User is not authorized.", e);
			tempErroMsg.put("message", "User is not valid or not found");
			return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
		}

		if (user != null) {
			userRequest.getUserLoginStatus();
			if (UserLoginStatus.fromString(user.getUserLoginStatus().toDbString()) == UserLoginStatus.BLOCKED) {
				log.info("User Account is BLocked");
				tempErroMsg.put("message", "User Account with userName " + userRequest.getUserName() + " is BLOCKED.");
				return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);

			} else if ((UserLoginStatus.fromString(user.getUserLoginStatus().toDbString()) == UserLoginStatus.CREATED)
					|| (UserLoginStatus
							.fromString(user.getUserLoginStatus().toDbString()) == UserLoginStatus.INACTIVE)) {
				UserLoginStatus getActiveLoginStatus = UserLoginStatus.ACTIVE;
				userManager.updateLoginStatus(user, getActiveLoginStatus);
			}
			if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
				log.info("Password Didn't match.");
				tempErroMsg.put("message", "Password is not valid.");
				return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
			}
			userBean = getUserData(user);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUserName(),
					userRequest.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authToken);
			final String token = jwtTokenUtil.generateToken(user);
			userBean.setToken(token);
			return new ResponseEntity<LoginResultBean>(userBean, HttpStatus.OK);
		} else {
			log.info("User does not exist.");
			tempErroMsg.put("message", "User does not exist.");
			return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	@RequestMapping(value = "/public/users/sendOTP", method = { POST })
	public ResponseEntity<?> sendOTP(@RequestBody Users user,
			@RequestHeader(value = "mlogistics_locale") String acceptLang, @RequestParam(required = false) String newNumber) throws SQLException, APIExceptions, UnsupportedEncodingException {

		String userName = user.getUserName();
		try {
			if (userName == null) {
				throw new APIExceptions(LocalUtils.getStringLocale(acceptLang, "phoneNumberRequired"));
			}
			dataValidations.phoneNumberValidation(userName);
			user.setUserName(userName.trim());
			smsService.sendOTPSMS(user, newNumber);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (SMSServiceException e) {
			log.error("Failed to send otp." + e.getMessage());
			throw new APIExceptions("Failed to send OTP due to internal server error. We regret inconvenience caused , our team working on this.");
		}
		 catch (InvalidActionException e) {
				log.error("Invalid id ." + e.getMessage());
				throw new InvalidActionException(e.getMessage());
			}
		 catch (BadRequestException e) {
				log.error("Bad Request." + e.getMessage());
				throw new BadRequestException(e.getMessage());
			}
		 catch (UnAuthorizedActionException e) {
				log.error("Unauthorized action : " + e.getMessage());
				throw new UnAuthorizedActionException(e.getMessage());
			}
		catch (Exception e) {
			log.error("Failed to send otp." + e.getMessage());
			e.printStackTrace();
			throw new APIExceptions("Failed to send OTP due to internal server error OR might be your mobile number is not valid number.");
		}
	}

	@Override
	@RequestMapping(value = "/public/users/loginWithOTP", method = { POST })
	public ResponseEntity<?> verifyOTPAndLogin(@Valid @RequestBody Users user, BindingResult result)
			throws SQLException, APIExceptions {

		if (result.hasErrors()) {
			throw new BadRequestException("Bad Request.");
		}

		HashMap<String, String> tempErroMsg = new HashMap<>();
		String loginUser = user.getUserName();
		String otp = Integer.toString(user.getLoginOtp());

		if (otp == null || otp.trim().equals("0") || loginUser == null) {

			log.error("OTP or userName/Mobile number is empty.");
			throw new APIExceptions("OTP or user mobile number can not be empty!");
		}
		dataValidations.phoneNumberValidation(loginUser);
		boolean verifiedResult = smsService.verifyOTP(loginUser, otp);
		if (verifiedResult) {
			LoginResultBean userAuthData = null;
			try {
				user = userManager.getUserByUserName(loginUser);
			} catch (Exception e) {
				log.error("User is not authorized.", e);
				tempErroMsg.put("message", "User is not authorized.");
				return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
			}

			if (user != null && verifiedResult) {
				if ((UserLoginStatus.fromString(user.getUserLoginStatus().toDbString()) == UserLoginStatus.CREATED)
						|| (UserLoginStatus
								.fromString(user.getUserLoginStatus().toDbString()) == UserLoginStatus.INACTIVE)) {
					UserLoginStatus getActiveLoginStatus = UserLoginStatus.ACTIVE;
					userManager.updateLoginStatus(user, getActiveLoginStatus);
				}
				userAuthData = getUserData(user);
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginUser, otp);
				SecurityContextHolder.getContext().setAuthentication(authToken);
				final String token = jwtTokenUtil.generateToken(user);
				userAuthData.setToken(token);
				return new ResponseEntity<LoginResultBean>(userAuthData, HttpStatus.OK);
			} else {
				log.info("User [" + loginUser + "] does not exist.");
				tempErroMsg.put("message", "User [" + loginUser + "] does not exist.");
				return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
			}
		} else {
			log.error("OTP not matched.");
			throw new APIExceptions("OTP not matched.");
		}

	}

	@Override
	@RequestMapping(value = "/private/users/changeStatus", method = { POST })
	public ResponseEntity<?> changeUserStatus(@RequestBody Users user) throws SQLException, APIExceptions {
		HashMap<String, String> tempErroMsg = new HashMap<>();
		try {
			if (user == null)
				throw new APIExceptions("Invalid User");

			Users authuser = authUser.getAuthUserDetails();
			long userId = authuser.getUserId();
			UserRoles userRole = authuser.getUserRole();
			user.setModifiedBy(userId);

			if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.CAPACITY_PROVIDER
					|| userRole == UserRoles.BOTH) {
				if (user.getUserId() == userId) {
					UserLoginStatus getInActiveLoginStatus = UserLoginStatus.INACTIVE;
					userManager.updateLoginStatus(user, getInActiveLoginStatus);
				} else {
					throw new UnAuthorizedActionException("UnAuthorized Action");
				}
			} else if (userRole == UserRoles.CSR) {
				UserLoginStatus getBlockedLoginStatus = UserLoginStatus.BLOCKED;
				userManager.updateLoginStatus(user, getBlockedLoginStatus);
			} else {
				throw new UnAuthorizedActionException("UnAuthorized Action");
			}
		} catch (Exception e) {
			log.error("User is not authorized.", e);
			tempErroMsg.put("message", "User is not authorized.");
			return new ResponseEntity<HashMap<String, String>>(tempErroMsg, HttpStatus.UNAUTHORIZED);
		}
		return null;
	}

	@Override
	@RequestMapping(value = "/private/users/isUserLoggedIn", method = { GET })
	public boolean isUserLoggedIn() {
		return true;
	}

	@Override
	@RequestMapping(value = "/public/users/generateNewPassword", method = { POST })
	public ResponseEntity<?> generateNewPassword(@RequestBody Users user) throws APIExceptions {

		if(user.getUserName() == null)
			 throw new BadRequestException("User phone number is required.");
		smsService.generateNewPassword(user.getUserName());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private LoginResultBean getUserData(Users user) {
		LoginResultBean authDetails = new LoginResultBean();
		authDetails.setUserName(user.getUserName());
		authDetails.setUserId(user.getUserId());
		authDetails.setUserRole(user.getUserRole().toDbString());
		authDetails.setRating(user.getRating());
		return authDetails;
	}

	private void validateJson(BindingResult result) throws BadRequestException {

		if (result.hasErrors()) {
			List<FieldError> errors = result.getFieldErrors();
			String errorMessage = "";
			for (FieldError fieldError : errors) {
				errorMessage += fieldError.getDefaultMessage();
			}
			throw new BadRequestException(errorMessage);
		}
	}

	@Override
	@RequestMapping(value = "/private/users/updateUsername", method = { POST })
	public ResponseEntity<?> updateUsername(@Valid @RequestBody Users user, @RequestHeader(value = "mlogistics_locale") String acceptLang, 
			BindingResult result, @RequestParam(required = true) String newNumber) throws APIExceptions {
		if (result.hasErrors()) {
			throw new BadRequestException("Bad Request.");
		}
		String userName = user.getUserName();
		dataValidations.phoneNumberValidation(userName);
		dataValidations.phoneNumberValidation(newNumber);
		user.setUserName(userName.trim());
		userService.updateUsername(user, newNumber.trim());
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/users/updateUserRole", method = { POST })
	public ResponseEntity<?> updateUserRole(@Valid @RequestBody Users user, @RequestHeader(value = "mlogistics_locale") String acceptLang, 
			BindingResult result, @RequestParam(required = true) String newUserRole) throws APIExceptions {
		
		validateJson(result);
		String userName = user.getUserName();
		userService.updateUserRole(user,newUserRole);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
