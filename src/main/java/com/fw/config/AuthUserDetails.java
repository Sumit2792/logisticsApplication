package com.fw.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.fw.dao.IUserManager;
import com.fw.domain.Users;
import com.fw.enums.UserRoles;
import com.fw.exceptions.InvalidUsernameException;

/**
 * 
 * @author Narendra
 *
 */
@Service
public class AuthUserDetails {

	@Autowired
	IUserManager userManager;

	public Users getAuthUserDetails() throws InvalidUsernameException {
		Users user = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			user = userManager.getUserByUserName(currentUserName);
		}
		return user;
	}

	public String getLanguageHeader() throws InvalidUsernameException {
		System.out.println("The Authentication Details are ::::::::::"
				+ SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		CustomUser customUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String language = customUser.getLanguage();
		return language;

	}

	public static HashMap<UserRoles, UserRoles> getInternalRoles() {

		HashMap<UserRoles, UserRoles> adminRoles = new HashMap<>();

		adminRoles.put(UserRoles.CSR, UserRoles.CSR);
		adminRoles.put(UserRoles.ADMIN, UserRoles.ADMIN);
		adminRoles.put(UserRoles.SUPER_ADMIN, UserRoles.SUPER_ADMIN);
		return adminRoles;

	}

	public static HashMap<UserRoles, UserRoles> getNonAdminUserRoles() {
		HashMap<UserRoles, UserRoles> userRoles = new HashMap<>();

		userRoles.put(UserRoles.BOTH, UserRoles.BOTH);
		userRoles.put(UserRoles.CAPACITY_PROVIDER, UserRoles.CAPACITY_PROVIDER);
		userRoles.put(UserRoles.LOAD_PROVIDER, UserRoles.LOAD_PROVIDER);
		return userRoles;

	}

}
