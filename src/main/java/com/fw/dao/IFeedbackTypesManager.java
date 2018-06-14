package com.fw.dao;

import java.util.List;

import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;

public interface IFeedbackTypesManager {

	/**
	 * Persist the object normally.
	 */
	FeedbackTypes persistFeedbackTypes(FeedbackTypes logEntity)  throws APIExceptions;

	void updateFeedbackTypesById(FeedbackTypes logEntity)  throws APIExceptions;

	List<FeedbackTypes> getAllFeedbackTypesRowMapper()  throws APIExceptions;

	FeedbackTypes getFeedbackTypesById(long bidId)  throws APIExceptions;

	void deleteFeedbackTypesById(FeedbackTypes Id)  throws APIExceptions;

}
