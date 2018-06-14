package com.fw.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.dao.IFeedbackTypesManager;
import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IFeedbackTypesService;

@Service
public class FeedbackTypesServiceImpl implements IFeedbackTypesService {

	@Autowired
	IFeedbackTypesManager feedbackTypesManager;
	
	@Override
	@Transactional
	public FeedbackTypes addFeedbackTypes(FeedbackTypes logEntity)  throws APIExceptions {
		if(logEntity!=null) {
			return feedbackTypesManager.persistFeedbackTypes(logEntity);
		}
		else
			return null;
	}

	@Override
	@Transactional
	public void updateFeedbackTypesById(FeedbackTypes logEntity)  throws APIExceptions {
	
		feedbackTypesManager.updateFeedbackTypesById(logEntity);
		
	}

	@Override
	@Transactional
	public void deleteFeedbackTypesById(FeedbackTypes id)  throws APIExceptions{

		feedbackTypesManager.deleteFeedbackTypesById(id);
	}

	@Override
	@Transactional
	public List<FeedbackTypes> getAllFeedbackTypes() throws APIExceptions {
		
		return feedbackTypesManager.getAllFeedbackTypesRowMapper();
	}

	@Override
	@Transactional
	public FeedbackTypes getFeedbackTypesById(long bidId) throws APIExceptions {
		// TODO Auto-generated method stub
		return feedbackTypesManager.getFeedbackTypesById(bidId);
	}

}
