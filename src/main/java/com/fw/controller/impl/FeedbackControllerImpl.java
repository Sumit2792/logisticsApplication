package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fw.beans.FeedbackBean;
import com.fw.config.AuthUserDetails;
import com.fw.controller.FeedbackController;
import com.fw.domain.Feedback;
import com.fw.domain.FeedbackTypes;
import com.fw.domain.Users;
import com.fw.enums.CallStatus;
import com.fw.enums.RatingType;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IFeedbackService;
import com.fw.services.IFeedbackTypesService;
import com.fw.utils.LocalUtils;

@Controller
@RequestMapping(value = "/mLogistics")
public class FeedbackControllerImpl implements FeedbackController {

	Logger logger = Logger.getLogger(FeedbackControllerImpl.class);

	@Autowired
	private IFeedbackService feedbackService;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	private IFeedbackTypesService feedbackTypesService;

	@Override
	@RequestMapping(value = "/private/feedbacks/saveFeedback", method = { POST })
	public ResponseEntity<?> saveFeedback(@RequestBody FeedbackBean log, @RequestHeader(value = "mlogistics_locale") String acceptLang) throws APIExceptions {
		validateFeedbackToSave(log, acceptLang);
		feedbackService.saveFeedbackInfo(log);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/feedbacks/getFeedbackDetail/{feedbackId}", method = { GET })
	public ResponseEntity<Feedback> getFeedbackInfoById(@PathVariable("feedbackId") Long feedbackId,
			@RequestHeader(value = "mlogistics_locale") String acceptLang) throws APIExceptions, SQLException {
		logger.info("Accept Language : " + acceptLang);
		/*
		 * Feedback feedback=null; feedback.getFeedbackId()
		 */
		return new ResponseEntity<Feedback>(feedbackService.getFeedbackDetailById(feedbackId), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/feedbacks/getUserFeedbacks", method = { POST })
	public ResponseEntity<List<FeedbackBean>> getUserFeedbacks() throws APIExceptions {
		return new  ResponseEntity<List<FeedbackBean>>(feedbackService.getUserFeedbacks(), HttpStatus.OK);
	}

	/**
	 * Feedback save request validation
	 * @param log {@link FeedbackBean} to perform validation
	 * @param acceptLang 
	 * @throws APIExceptions
	 */
	private void validateFeedbackToSave(FeedbackBean log, String acceptLang) throws APIExceptions {
		if(log.getNote() == null || "".equals(log.getNote())) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "NoteRequired"));
		}
		if(log.getNote().length() > 300) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "Note300Char"));
		}
		if(log.getGivingUserId() <= 0 || log.getGivingUserId() == log.getForUserId()) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidFeedbackProvider"));
		}
		Users user = authUser.getAuthUserDetails();
		if(UserRoles.CSR.equals(user.getUserRole()) && user.getUserId() == log.getGivingUserId()) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidFeedbackProvider"));
		}
		try {
			RatingType.fromString(log.getRatingType());
		} catch (RuntimeException e) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidRating"));
		}
		if(log.getFeedbackTypeId() <= 0) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidFeedbackType"));
		}
		FeedbackTypes type = feedbackTypesService.getFeedbackTypesById(log.getFeedbackTypeId());
		if(type == null) {
			throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidFeedbackType"));
		}
		if(log.getMessageId() > 0) {
			try {
				CallStatus.fromString(log.getCallStatus());
			} catch (RuntimeException e) {
				throw new BadRequestException(LocalUtils.getStringLocale(acceptLang, "InvalidCallStatus"));
			}
		}
	}
}
