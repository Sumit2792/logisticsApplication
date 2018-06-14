package com.fw.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.FeedbackBean;
import com.fw.domain.Feedback;
import com.fw.exceptions.APIExceptions;

public interface FeedbackController {

	ResponseEntity<?> saveFeedback(FeedbackBean feedbackForm, String acceptLang) throws APIExceptions;

//	ResponseEntity<List<Feedback>> getAllFeedbackDetails() throws APIExceptions;
//
//	void removeFeedbackDetails(Feedback feedbackForm)throws APIExceptions;
//
//	void modifyFeedbackByFeedbackId(Feedback feedbackForm)throws APIExceptions;
//
	ResponseEntity<Feedback> getFeedbackInfoById(Long feedbackId, String acceptLang)throws APIExceptions, SQLException;

	ResponseEntity<List<FeedbackBean>> getUserFeedbacks() throws APIExceptions;
}
