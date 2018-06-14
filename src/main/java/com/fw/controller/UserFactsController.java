package com.fw.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fw.beans.UserFactsBean;
import com.fw.domain.UserFacts;
import com.fw.exceptions.APIExceptions;

public interface UserFactsController {

	ResponseEntity<List<UserFacts>> getUserFactsByUserId(long bidId) throws APIExceptions;

	void removeUser(long userId) throws APIExceptions;

	void addUserFacts(UserFactsBean facts, BindingResult result) throws APIExceptions;

}
