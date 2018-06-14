package com.fw.dao;

import java.sql.SQLException;
import java.util.List;

import com.fw.beans.FeedbackBean;
import com.fw.domain.Feedback;
import com.fw.exceptions.APIExceptions;

public interface IFeedbackManager {

	/**
	 * Persist the object normally.
	 */
	long persistFeedbackInfo(Feedback entity) throws APIExceptions;

	FeedbackBean getFeedbackInfoById(long entity) throws SQLException;

	List<FeedbackBean> getUserFeedbacksByUserId(Long fromUserId);
}
