package com.fw.dao;

import java.util.List;

import com.fw.domain.SentMessages;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;

public interface ISentMessagesManager {

	/**
	 * Persist the object normally.
	 * @throws APIExceptions 
	 */
	SentMessages persistSentMessages(SentMessages postalAddressesEntity);

	void updateSentMessagesById(SentMessages postalAddressesEntity);

	void deleteSentMessagesById(SentMessages id) ;

	List<SentMessages> getAllSentMessagesRowMapper();

	SentMessages getSentMessagesById(long id);
	
	SentMessages getLastSentMessageForUserByType(Long userId, ContactType type);
	
	List<SentMessages> getAllPendingMessages(ContactType contactType);
	
	void updateSentMessagesStatusShootId(String message, String phoneNumber, String shootId);
	
	List<SentMessages> getAllSMSAwaitedMessages(ContactType contactType);
	
	void updateSentMessagesStatusByGroupIdNumber(String phoneNumber, ContactStatus contactStatus, String smsGroupId);

	int[]  persistBatchSentMessages(List<SentMessages> msgToSend, boolean comingFromDaemon);

	List<Long> getAllSentMessagesLRId(long loadRequestId);

	void updateStatusResetCount(int resetCount, String contact, String smsGroupId);

}
