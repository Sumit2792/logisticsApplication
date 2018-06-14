package com.fw.dao;

import java.util.List;

import com.fw.domain.MessageTemplates;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;

/**
 * Message template manager
 * 
 * @author Vikas Sonwal
 *
 */
public interface IMessageTemplateManager {

	/**
	 * Method to get all the message templates.
	 * 
	 * @return A list of all the message templates.
	 */
	List<MessageTemplates> getAllMessageTemplates();

	/**
	 * Method to get message templates based on type value
	 * 
	 * @param contactType
	 *            can be one of {@link ContactType} value
	 * @return
	 */
	List<MessageTemplates> getMessageTemplatesByType(ContactType contactType);

	/**
	 * Method to get message template based on templateId
	 * 
	 * @param id
	 * @return
	 */
	MessageTemplates getMessageTemplateById(long templateId);

	/**
	 * Method to get message template based on SubjectLine
	 * 
	 * @param id
	 * @return
	 */
	String getMessageBySubjectLine(String string);

	void insertMessageTemplate(MessageTemplates template) throws APIExceptions;

	void updateMessageTemplate(MessageTemplates template) throws APIExceptions;

	void deleteMessageTemplate(long templateId) throws APIExceptions;
}
