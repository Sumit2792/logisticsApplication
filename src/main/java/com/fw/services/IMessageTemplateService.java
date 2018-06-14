package com.fw.services;

import java.util.List;

import com.fw.domain.MessageTemplates;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;

/**
 * Message template service
 * @author Vikas Sonwal
 *
 */
public interface IMessageTemplateService {

	/**
	 * Method to get all the message templates.
	 * @return A list of all the message templates.
	 */
	List<MessageTemplates> getAllMessageTemplates();
	
	/**
	 * Method to get message templates based on type value
	 * @param type can be one of {@link ContactType} value
	 * @return
	 * @throws APIExceptions 
	 */
	List<MessageTemplates> getMessageTemplates(String type) throws APIExceptions;
	
	/**
	 * Method to get message template based on templateId
	 * @param id 
	 * @return
	 */
	MessageTemplates getMessageTemplateById(long templateId);

	void saveMessageTemplate(MessageTemplates template) throws APIExceptions;

	void updateMessageTemplate(MessageTemplates template) throws APIExceptions;

	void deleteMessageTemplate(long templateId) throws APIExceptions;
}
