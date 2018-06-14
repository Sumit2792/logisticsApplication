package com.fw.services;

import java.sql.SQLException;
import java.util.List;

import com.fw.beans.FeedbackBean;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.exceptions.UnAuthorizedActionException;

public interface IFeedbackService {

	 void saveFeedbackInfo(FeedbackBean feedbackEntity) throws APIExceptions;
    
	 FeedbackBean getFeedbackDetailById(long feedbackEntity) throws SQLException;

	List<FeedbackBean> getUserFeedbacks() throws InvalidUsernameException, UnAuthorizedActionException;
}
