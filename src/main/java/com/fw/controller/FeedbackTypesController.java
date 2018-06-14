package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;

public interface FeedbackTypesController {

	void addFeedbackTypes(FeedbackTypes unit) throws APIExceptions;

	ResponseEntity<List<FeedbackTypes>> getFeedbackTypes() throws APIExceptions;

	ResponseEntity<FeedbackTypes> updateFeedbackTypesById(FeedbackTypes bidForm) throws APIExceptions;

	ResponseEntity<FeedbackTypes> getFeedbackTypesById(long bidId) throws APIExceptions;

	void removeUser(FeedbackTypes deleteBlockUser) throws APIExceptions;

}
