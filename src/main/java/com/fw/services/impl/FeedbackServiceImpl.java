package com.fw.services.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.FeedbackBean;
import com.fw.beans.UserDetailsBean;
import com.fw.config.AuthUserDetails;
import com.fw.dao.ICallHistoryManager;
import com.fw.dao.IFeedbackManager;
import com.fw.dao.IUserManager;
import com.fw.domain.Users;
import com.fw.enums.RatingType;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IFeedbackService;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

	@Autowired
	private IFeedbackManager feedbackManager;
	@Autowired
	private AuthUserDetails authUser;
	@Autowired
	private ICallHistoryManager callHistoryManager;
	@Autowired
	private IUserManager userManager;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveFeedbackInfo(FeedbackBean feedbackEntity) throws APIExceptions {
		Users user = authUser.getAuthUserDetails();
		feedbackEntity.setCreatedBy(user.getUserId());
		feedbackEntity.setModifiedBy(user.getUserId());
		RatingType ratingType = RatingType.fromString(feedbackEntity.getRatingType());
		feedbackEntity.setRating(ratingType.getVal());
		long feedbackId = feedbackManager.persistFeedbackInfo(feedbackEntity);
		if (feedbackEntity.getMessageId() > 0) {
			callHistoryManager.updateCallHistoryById(feedbackEntity.getMessageId(), feedbackEntity.getCallStatus(),
					feedbackId);
		}
		if (feedbackId > 0 && feedbackEntity.getForUserId() != 0) {
			UserDetailsBean forUser = userManager.getUserInfoByUserId(feedbackEntity.getForUserId());
			if (forUser != null && forUser.getUserRole() != UserRoles.CSR.toDbString())
				userManager.saveAverageRating(feedbackEntity.getForUserId(), feedbackEntity.getRating());
		}
	}

	@Override
	@Transactional
	public FeedbackBean getFeedbackDetailById(long feedbackEntity) throws SQLException {
		FeedbackBean bean = feedbackManager.getFeedbackInfoById(feedbackEntity);
		RatingType ratingType = RatingType.fromVal(bean.getRating());
		bean.setRatingType(ratingType.toDbString());
		bean.setRating(0);
		return bean;
	}

	@Override
	@Transactional
	public List<FeedbackBean> getUserFeedbacks() throws InvalidUsernameException, UnAuthorizedActionException {

		Users user = authUser.getAuthUserDetails();
		List<FeedbackBean> list = feedbackManager.getUserFeedbacksByUserId(user.getUserId());
		for (FeedbackBean bean : list) {
			RatingType ratingType = RatingType.fromVal(bean.getRating());
			bean.setRatingType(ratingType.toDbString());
			bean.setRating(0);
		}
		return list;
	}

}
