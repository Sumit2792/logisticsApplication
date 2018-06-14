package com.fw.services;

import java.util.List;

import com.fw.domain.SentMessages;

public interface ISentMessageService {

	SentMessages addSentMessages(SentMessages logEntity);

	void updateSentMessagesById(SentMessages logEntity);

	List<SentMessages> getAllSentMessages();

	SentMessages getSentMessagesById(long messageId);

	void deleteSentMessagesById(SentMessages messageId);
}
