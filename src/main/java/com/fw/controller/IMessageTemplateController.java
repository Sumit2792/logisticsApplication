package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.domain.MessageTemplates;
import com.fw.exceptions.APIExceptions;

public interface IMessageTemplateController {
	
	ResponseEntity<List<MessageTemplates>> getMessageTemplates(String type) throws APIExceptions;

	ResponseEntity<?> addMessageTemplate(MessageTemplates template) throws APIExceptions;

	ResponseEntity<?> updateMessageTemplate(MessageTemplates template) throws APIExceptions;

	ResponseEntity<?> deleteMessageTemplate(long templateId) throws APIExceptions;
}
