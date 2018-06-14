package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;

public interface UserSourcesController {

	void addUserSources(UserSources unit) throws APIExceptions;

	ResponseEntity<List<UserSources>> getUserSources() throws APIExceptions;

	ResponseEntity<UserSources> updateUserSourcesById(UserSources bidForm) throws APIExceptions;

	ResponseEntity<UserSources> getUserSourcesById(long bidId) throws APIExceptions;

	void removeUser(UserSources deleteBlockUser) throws APIExceptions;

}
