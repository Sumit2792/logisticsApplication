package com.fw.services;

import java.util.List;

import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;

public interface IFeedbackTypesService {

	FeedbackTypes addFeedbackTypes(FeedbackTypes bidEntity) throws APIExceptions;

	void updateFeedbackTypesById(FeedbackTypes bidEntity) throws APIExceptions;

	List<FeedbackTypes> getAllFeedbackTypes() throws APIExceptions;

	FeedbackTypes getFeedbackTypesById(long bidId) throws APIExceptions;

	void deleteFeedbackTypesById(FeedbackTypes userId) throws APIExceptions;
}
