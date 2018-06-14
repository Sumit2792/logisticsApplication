package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.domain.SentMessages;
import com.fw.exceptions.APIExceptions;

public interface SentMessageController {

	void addSentMessages(SentMessages unit) throws APIExceptions;

	ResponseEntity<List<SentMessages>> getSentMessages() throws APIExceptions;

	ResponseEntity<SentMessages> updateSentMessagesById(SentMessages bidForm) throws APIExceptions;

	ResponseEntity<SentMessages> getSentMessagesById(long bidId) throws APIExceptions;

	void removeUser(SentMessages deleteBlockUser) throws APIExceptions;

}
