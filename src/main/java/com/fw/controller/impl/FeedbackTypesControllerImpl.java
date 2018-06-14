package com.fw.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.fw.controller.FeedbackTypesController;
import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IFeedbackTypesService;

@Controller
@RequestMapping(value = "/mLogistics")
public class FeedbackTypesControllerImpl implements FeedbackTypesController {
	
	@Autowired
	IFeedbackTypesService feedbackTypesService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/feedbackTypes/addFeedbackTypes", method = { POST })
	public void addFeedbackTypes(@RequestBody FeedbackTypes state) throws APIExceptions {
		feedbackTypesService.addFeedbackTypes(state);

	}

	@Override
	@RequestMapping(value = "/private/feedbackTypes/getallFeedbackTypes", method = { GET })
	public ResponseEntity<List<FeedbackTypes>> getFeedbackTypes() throws APIExceptions{

		return new ResponseEntity<List<FeedbackTypes>>(feedbackTypesService.getAllFeedbackTypes(), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/feedbackTypes/updateFeedbackTypes", method = { PATCH })
	public ResponseEntity<FeedbackTypes> updateFeedbackTypesById(@RequestBody FeedbackTypes bidForm) throws APIExceptions{
		feedbackTypesService.updateFeedbackTypesById(bidForm);
		return new ResponseEntity<FeedbackTypes>(bidForm, HttpStatus.OK);
	}


	@Override
	@RequestMapping(value = "/private/feedbackTypes/getFeedbackTypesDetails/{feedbackTypesId}", method = { GET })
	public ResponseEntity<FeedbackTypes> getFeedbackTypesById(@PathVariable("feedbackTypeId") long feedbackTypesId) throws APIExceptions {
		return new ResponseEntity<FeedbackTypes>(feedbackTypesService.getFeedbackTypesById(feedbackTypesId), HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/feedbackTypes/deleteFeedbackTypes/{feedbackTypesId}", method = { DELETE })
	public void removeUser(@RequestBody FeedbackTypes feedbackTypesId) throws APIExceptions {
		feedbackTypesService.deleteFeedbackTypesById(feedbackTypesId);
	}

}
