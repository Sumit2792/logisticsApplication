package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fw.controller.IMessageTemplateController;
import com.fw.domain.MessageTemplates;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IMessageTemplateService;

@Controller
@RequestMapping(value = "/mLogistics")
public class MessageTemplateControllerImpl implements IMessageTemplateController {

	@Autowired
	IMessageTemplateService messageTemplateService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/messageTemplate/getMessageTemplates", method = { GET })
	public ResponseEntity<List<MessageTemplates>> getMessageTemplates(@RequestParam(required = false) String type)
			throws APIExceptions {
		return new ResponseEntity<List<MessageTemplates>>(messageTemplateService.getMessageTemplates(type),
				HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/messageTemplate/addMessageTemplate", method = { POST })
	public ResponseEntity<?> addMessageTemplate(@RequestBody(required = true) MessageTemplates template)
			throws APIExceptions {
		
		messageTemplateService.saveMessageTemplate(template);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@Override
	@ResponseBody
	@RequestMapping(value = "/private/messageTemplate/updateMessageTemplate", method = { PATCH })
	public ResponseEntity<?> updateMessageTemplate(@RequestBody(required = true) MessageTemplates template)
			throws APIExceptions {
		
		messageTemplateService.updateMessageTemplate(template);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@Override
	@ResponseBody
	@RequestMapping(value = "/private/messageTemplate/deleteMessageTemplate/{templateId}", method = { DELETE })
	public ResponseEntity<?> deleteMessageTemplate(@PathVariable(value = "templateId" , required =true) long templateId)
			throws APIExceptions {
		
		messageTemplateService.deleteMessageTemplate(templateId);
		
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
