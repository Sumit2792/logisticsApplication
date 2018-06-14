package com.fw.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.config.AuthUserDetails;
import com.fw.dao.IMessageTemplateManager;
import com.fw.domain.MessageTemplates;
import com.fw.domain.Users;
import com.fw.enums.ContactType;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IMessageTemplateService;

/**
 * Service implementation for Message template service.
 * 
 * @author Vikas Sonwal
 *
 */

@Service
public class MessageTemplateServiceImpl implements IMessageTemplateService {

	@Autowired
	IMessageTemplateManager messageTemplateManager;
	@Autowired
	private AuthUserDetails authUser;

	@Override
	@Transactional
	public List<MessageTemplates> getAllMessageTemplates() {
		return messageTemplateManager.getAllMessageTemplates();
	}

	@Override
	@Transactional
	public List<MessageTemplates> getMessageTemplates(String type) throws APIExceptions {
		if (type == null || "".equals(type)) {
			return messageTemplateManager.getAllMessageTemplates();
		} else if (isValidContactType(type)) {
			return messageTemplateManager.getMessageTemplatesByType(ContactType.fromString(type));
		} else {
			throw new APIExceptions("Invalid type in requested.");
		}
	}

	@Override
	@Transactional
	public MessageTemplates getMessageTemplateById(long templateId) {
		return messageTemplateManager.getMessageTemplateById(templateId);
	}

	/**
	 * Method to check if the type received is valid or not
	 * 
	 * @param type
	 * @return true if the type is correct against {@link ContactType}
	 */
	private boolean isValidContactType(String type) {
		try {
			ContactType.fromString(type);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void saveMessageTemplate(MessageTemplates template) throws APIExceptions {

		if (template == null)
			throw new BadRequestException("Request body is missing");
		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		
		if (AuthUserDetails.getInternalRoles().containsValue(userRole)) {
			
			template.setCreatedBy(userId);
		    template.setModifiedBy(userId);
		    messageTemplateManager.insertMessageTemplate(template);
		}else
		{
			throw new UnAuthorizedActionException("You are not authorized to add template.");
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void updateMessageTemplate(MessageTemplates template) throws APIExceptions {

		if (template == null)
			throw new BadRequestException("Request body is missing");

		if (template.getMessageTemplateId() == 0)
			throw new BadRequestException("Message template id is required.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		
		if (AuthUserDetails.getInternalRoles().containsValue(userRole)) {
			template.setCreatedBy(userId);
		    template.setModifiedBy(userId);
		    messageTemplateManager.updateMessageTemplate(template);
		}else {
			throw new UnAuthorizedActionException("You are not authorized to update template.");
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public void deleteMessageTemplate(long templateId) throws APIExceptions {

		if (templateId == 0)
			throw new BadRequestException("Message template id is required.");
		
		Users user = authUser.getAuthUserDetails();
		UserRoles userRole = user.getUserRole();
		
		if (AuthUserDetails.getInternalRoles().containsValue(userRole)) {
			messageTemplateManager.deleteMessageTemplate(templateId);
		}
		else {
			throw new UnAuthorizedActionException("You are not authorized to delete template.");
		}
	}

}
