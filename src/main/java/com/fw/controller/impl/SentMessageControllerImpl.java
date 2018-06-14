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

import com.fw.controller.SentMessageController;
import com.fw.domain.SentMessages;
import com.fw.exceptions.APIExceptions;
import com.fw.services.ISentMessageService;

@Controller
@RequestMapping(value = "/mLogistics")
public class SentMessageControllerImpl implements SentMessageController {
	@Autowired
	ISentMessageService sentMessageService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/sentMessage/addSentMessages", method = { POST })
	public void addSentMessages(@RequestBody SentMessages state) throws APIExceptions {
		sentMessageService.addSentMessages(state);

	}

	@Override
	@RequestMapping(value = "/private/sentMessage/getAllSentMessages", method = { GET })
	public ResponseEntity<List<SentMessages>> getSentMessages() throws APIExceptions{

		return new ResponseEntity<List<SentMessages>>(sentMessageService.getAllSentMessages(), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/sentMessage/updateSentMessage", method = { PATCH })
	public ResponseEntity<SentMessages> updateSentMessagesById(@RequestBody SentMessages bidForm) throws APIExceptions{
		sentMessageService.updateSentMessagesById(bidForm);
		return new ResponseEntity<SentMessages>(bidForm, HttpStatus.OK);
	}


	@Override
	@RequestMapping(value = "/private/sentMessage/getSentMessageDetails/{userID}", method = { GET })
	public ResponseEntity<SentMessages> getSentMessagesById(@PathVariable("userID") long sentMessageID) throws APIExceptions {
		return new ResponseEntity<SentMessages>(sentMessageService.getSentMessagesById(sentMessageID), HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/sentMessage/deleteSentMessage/{sentMessageID}", method = { DELETE })
	public void removeUser(@RequestBody SentMessages sentMessageID) throws APIExceptions {
		sentMessageService.deleteSentMessagesById(sentMessageID);
	}

}
