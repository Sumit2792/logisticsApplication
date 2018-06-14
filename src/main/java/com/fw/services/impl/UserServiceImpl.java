package com.fw.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.ChangePasswordBean;
import com.fw.beans.LoginResultBean;
import com.fw.beans.UserDetailsBean;
import com.fw.config.AuthUserDetails;
import com.fw.config.Constants;
import com.fw.dao.IUserFactsManager;
import com.fw.dao.IUserManager;
import com.fw.domain.UserFacts;
import com.fw.domain.Users;
import com.fw.enums.UserLoginStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidIdException;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IUserService;
import com.fw.utils.LocalUtils;
import com.fw.validations.DataValidations;

/**
 * @author Narendra
 *
 */
@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserManager userManager;
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	DataValidations dataValidations;
	@Autowired
	private IUserFactsManager userFactsManager;

	Logger log = Logger.getLogger(UserServiceImpl.class);

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUserInfo(Users user) throws InvalidUsernameException, APIExceptions {

		if (user != null && user.getUserName() != null) {
			String userName = user.getUserName();
			dataValidations.phoneNumberValidation(userName);
			if (user.getPassword() != null)
				dataValidations.passwordValidation(user.getPassword());

			if (!userManager.isUserExist(userName)) {
				String pass = bcryptEncoder.encode(user.getPassword());
				user.setPassword(pass);
				UserRoles userRole = user.getUserRole();
				if (userRole != UserRoles.CSR && userRole != UserRoles.SUPER_ADMIN && userRole != UserRoles.ADMIN)
					userManager.persist(user);
				else {
					log.error(" User type :" + userRole + " not allowed to registered.");
					throw new APIExceptions("Unauthorized Action.");
				}
			} else {
				throw new InvalidUsernameException("Username already exist:" + userName);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<UserDetailsBean> getAllUsers() throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		UserRoles userRole = user.getUserRole();

		List<UserDetailsBean> usersList = new ArrayList<UserDetailsBean>();

		if (AuthUserDetails.getInternalRoles().containsValue(userRole)) {
			usersList = userManager.getAllUserRowMapper(userRole);
		}
		return usersList;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(long userId) throws APIExceptions {

		if (userId == 0)
			throw new APIExceptions("Invalid request. User ID is required.");

		Users user = authUser.getAuthUserDetails();
		UserRoles userRole = user.getUserRole();

		if (userId != user.getUserId() && userRole != UserRoles.CSR) {
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		}

		if (userRole == UserRoles.CSR || userId == user.getUserId()) {
			userManager.deleteUser(userId, user.getUserId());
		}
	}

	/*
	 * @Override
	 * 
	 * @Transactional(rollbackFor = Exception.class) public void
	 * updateUser(UserDetailsBean userRequest) throws APIExceptions { if
	 * (userRequest == null ) throw new APIExceptions("Invalid request"); if
	 * (userRequest.getUserId() == null) throw new
	 * APIExceptions("User id is required.");
	 * 
	 * Users user = authUser.getAuthUserDetails(); long userId = user.getUserId();
	 * UserRoles loggedInuserRole = user.getUserRole();
	 * userRequest.setModifiedBy(userId);
	 * 
	 * UserRoles requestedUserRole =
	 * UserRoles.fromString(userRequest.getUserRole()); // if request having user
	 * type = CSR/ADMIN/Super admin then we will not process // it if
	 * (requestedUserRole == UserRoles.CSR || requestedUserRole == UserRoles.ADMIN
	 * || requestedUserRole == UserRoles.SUPER_ADMIN) { throw new
	 * UnAuthorizedActionException("Unauthorized Action."); }
	 * dataValidations.phoneNumberValidation(userRequest.getUserName());
	 * 
	 * 
	 * userManager.modifyUserByUserId(userRequest); }
	 */

	@Override
	public UserDetailsBean getUserByUserId(Long requestedUserId) throws APIExceptions {
		if (requestedUserId == null)
			throw new InvalidIdException("Invalid userId");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.CAPACITY_PROVIDER
				|| userRole == UserRoles.BOTH) {
			if (requestedUserId == userId) {

				return userManager.getUserInfoByUserId(requestedUserId);
			} else {
				throw new UnAuthorizedActionException("UnAuthorized Action.");
			}
		} else if (userRole == UserRoles.CSR) {
			return userManager.getUserInfoByUserId(requestedUserId);
		} else {
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		}
	}

	@Override
	public LoginResultBean getUserByUserName(String requestedUserId) throws APIExceptions {
		if (requestedUserId == null)
			throw new InvalidIdException("Invalid userId.");

		Users user = authUser.getAuthUserDetails();
		String userId = user.getUserName();
		UserRoles userRole = user.getUserRole();

		if (userRole == UserRoles.LOAD_PROVIDER || userRole == UserRoles.CAPACITY_PROVIDER
				|| userRole == UserRoles.BOTH) {

			if (requestedUserId == userId) {
				return userManager.getUserDataByUserName(requestedUserId);
			} else {
				throw new UnAuthorizedActionException("UnAuthorized Action");
			}
		} else if (userRole == UserRoles.CSR) {
			return userManager.getUserDataByUserName(requestedUserId);
		} else {
			throw new UnAuthorizedActionException("UnAuthorized Action");
		}
	}

	@Override
	public Users verifyUserLoggedIn(Users user) throws APIExceptions {
		String userName = user.getUserName();
		String userLoginStatus = user.getUserLoginStatus().toDbString();
		if (userName != null && user.getUserName() != null
				&& (UserLoginStatus.fromString(userLoginStatus) == UserLoginStatus.ACTIVE)) {
			return user;
		} else {
			throw new APIExceptions("User is not logged in.");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changePassword(ChangePasswordBean changePassword) throws APIExceptions {

		if (changePassword == null)
			throw new APIExceptions("Invalid request.");

		if (changePassword.getUserId() == null)
			throw new APIExceptions("User ID is required.");
		if (changePassword.getNewPassword() == null || changePassword.getOldPassword() == null)
			throw new APIExceptions("old and new password is required.");

		dataValidations.passwordValidation(changePassword.getNewPassword());
		// dataValidations.passwordValidation(changePassword.getOldPassword());

		Users user = authUser.getAuthUserDetails();
		String loggedInUserName = user.getUserName();
		Long loggedInUserId = user.getUserId();
		String oldPassword = user.getPassword();

		log.info("******Target user Id : " + changePassword.getUserId());
		log.info("******Logged-in Id : " + loggedInUserId);
		if (!loggedInUserId.equals(changePassword.getUserId()))
			throw new UnAuthorizedActionException(
					LocalUtils.getStringLocale("mlogistics_locale", "unauthorizedAccess"));

		if (!bcryptEncoder.matches(changePassword.getOldPassword(), oldPassword)) {
			log.info("Old Password didn't match.");
			throw new UnAuthorizedActionException("Old Password didn't match.");
		}
		boolean isUpdated = userManager.updatePassword(loggedInUserName,
				bcryptEncoder.encode(changePassword.getNewPassword()), loggedInUserId);
		if (!isUpdated)
			throw new APIExceptions("Internal server error.");

	}

	@Override
	public void updateUsername(Users data, String newNumber) throws APIExceptions {
		
		Users user = authUser.getAuthUserDetails();
		List<UserFacts> userFacts = userFactsManager.getUserFactsByUserIdAndFactName(user.getUserId(),
				Constants.USER_FACT_NEW_NUMBER);
		if (userFacts.isEmpty() || user.getLoginOtp() <= 0) {
			throw new APIExceptions("No change request found");
		}
		UserFacts fact = userFacts.get(0);
		if (!newNumber.equals(fact.getValue())) {
			throw new APIExceptions("Please provide the correct number for otp validation.");
		}
		if (user.getLoginOtp() <= 0 || user.getLoginOtp() != data.getLoginOtp()) {
			throw new APIExceptions("OTP not matched");
		} else {
			user.setUserName(newNumber);
			boolean updateStatus = userManager.updateUsername(user);
			if (updateStatus) {
				try {
					userManager.updateOTP(user.getUserName(), null);
				} catch (Exception e) {
					log.error(
							"**********Failed to reset OTP for change username request. user id = " + user.getUserId());
				}
				try {
					userFactsManager.deleteUserFactsById(fact.getUserFactsId(), true);
				} catch (Exception e) {
					log.error("**********Failed to remove change username request for user id = " + user.getUserId());
				}
			}
		}
	}

	@Override
	public void updateUserRole(Users user, String newUserRole) {
		

	}

}
