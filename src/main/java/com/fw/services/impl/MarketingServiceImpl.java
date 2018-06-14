package com.fw.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.MarkatingAddressBean;
import com.fw.beans.ProviderMarketingBean;
import com.fw.beans.ProviderMarketingFiltersBean;
import com.fw.beans.ProviderMarketingSelectionBean;
import com.fw.beans.RequestMarketingBean;
import com.fw.beans.RequestMarketingDetailFiltersBean;
import com.fw.beans.RequestMarketingFiltersBean;
import com.fw.beans.RequestMarketingMessage;
import com.fw.beans.RequestMarketingMessageDetailBean;
import com.fw.beans.UsersWithFactsBean;
import com.fw.dao.ICallHistoryManager;
import com.fw.dao.IMarketingManager;
import com.fw.dao.IMessageTemplateManager;
import com.fw.dao.IPostalAddressesManager;
import com.fw.dao.ISentMessagesManager;
import com.fw.dao.IUserFactsManager;
import com.fw.dao.IUserManager;
import com.fw.domain.CallHistory;
import com.fw.domain.LoadRequest;
import com.fw.domain.MessageTemplates;
import com.fw.domain.SentMessages;
import com.fw.domain.UserFacts;
import com.fw.domain.Users;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.enums.Facts;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IMarketingService;
import com.fw.utils.MessageTemplateUtils;

@Service
public class MarketingServiceImpl implements IMarketingService {

	@Autowired
	private IMarketingManager marketingManager;

	@Autowired
	private IUserFactsManager userFactsManager;

	@Autowired
	private IUserManager userManager;

	@Autowired
	private ISentMessagesManager sentMessagesManager;

	@Autowired
	private IMessageTemplateManager messageTemplateManager;

	@Autowired
	private ICallHistoryManager callHistoryManager;

	@Autowired
	private MessageTemplateUtils messageUtils;

	@Autowired
	private IPostalAddressesManager postalAddressesManager;

	private Logger log = Logger.getLogger(MarketingServiceImpl.class);

	@Override
	@Transactional
	public List<ProviderMarketingBean> getProviderMarketingList(ProviderMarketingFiltersBean filtersBean) 
			throws APIExceptions {
		
		List<Users> filteredUsers = marketingManager.getAllNotContectedUsers(filtersBean);
		if(filteredUsers == null || filteredUsers.isEmpty()) {
			return new ArrayList<ProviderMarketingBean>();
		} 
		List<ProviderMarketingBean> providers = new ArrayList<ProviderMarketingBean>();
		for(Users user: filteredUsers) {
			try {
				ProviderMarketingBean marketingBean = new ProviderMarketingBean();
				marketingBean.setUserId(user.getUserId());
				marketingBean.setUserRole(user.getUserRole());
				marketingBean.setUserName(user.getUserName());
				marketingBean.setRating(user.getRating());
				List<UserFacts> facts = userFactsManager.getUserFactsByUserId(user.getUserId());
				if(ContactType.EMAIL.equals(filtersBean.getSearchType())) {
					getUserEmail(facts, user.getUserId());
				}
				marketingBean.setAddressess(getAddressses(postalAddressesManager.getPostalAddressesLinesByUserId(user.getUserId())));
				marketingBean.setFacts(facts);
				marketingBean.setLastSMS(sentMessagesManager.getLastSentMessageForUserByType(user.getUserId(), ContactType.SMS));
				marketingBean.setLastEmail(sentMessagesManager.getLastSentMessageForUserByType(user.getUserId(), ContactType.EMAIL));
				marketingBean.setLastWhatsApp(sentMessagesManager.getLastSentMessageForUserByType(user.getUserId(), ContactType.WHATSAPP));
				marketingBean.setLastCall(callHistoryManager.getLastCallHistoryByUserId(user.getUserId()));
				providers.add(marketingBean);
			} catch (RuntimeException e) {
				log.info("Skipping the record due to, "+e.getMessage());
			}
		}
		return providers;
	}

	@Override
	@Transactional(rollbackFor = APIExceptions.class)
	public int[] sendSelectedMarketingMessage(ProviderMarketingSelectionBean selectionBean, Users authuser)
			throws APIExceptions {
		MessageTemplates template = messageTemplateManager.getMessageTemplateById(selectionBean.getMessageTemplateId());
		if (template != null) {
			List<UsersWithFactsBean> users = marketingManager.getSelectedUsers(selectionBean.getUsers());
			try {
				if (selectionBean.getType().equals(ContactType.CALL)) {
					List<CallHistory> calls = prepareCallList(users, template, selectionBean, authuser);
					if (!calls.isEmpty()) {
						return callHistoryManager.persistBatchCalls(calls);
					} else {
						throw new BadRequestException("Invalid user list");
					}
				} else {
					List<SentMessages> msgToSend = prepareMessageList(users, template, selectionBean, authuser);
					if (!msgToSend.isEmpty()) {
						return sentMessagesManager.persistBatchSentMessages(msgToSend, false);
					} else {
						throw new BadRequestException("Invalid user list");
					}
				}
			} catch (RuntimeException e) {
				return null;
			}
		} else {
			throw new BadRequestException("Invalid template");
		}
	}

	@Override
	@Transactional
	public List<RequestMarketingBean> getRequestMarketingList(RequestMarketingFiltersBean filtersBean, Users authuser)
			throws APIExceptions {
		List<LoadRequest> requests = marketingManager.getLoadRequestsForMarketing(filtersBean);
		if (requests == null || requests.isEmpty()) {
			return new ArrayList<RequestMarketingBean>();
		}
		Set<Long> userIds = new HashSet<Long>();
		for (LoadRequest req : requests) {
			Long id = req.getUserId();
			if (id != null && id > 0) {
				userIds.add(id);
			}
		}
		Map<Long, String> usersMap = userManager.getUsersByIds(userIds);
		List<RequestMarketingBean> beans = new ArrayList<RequestMarketingBean>();
		for (LoadRequest req : requests) {
			try {
				RequestMarketingBean res = new RequestMarketingBean();
				res.setLoadRequestId(req.getLoadRequestId());
				res.setSmsCount(getRequestMessageCount(ContactType.SMS, req.getLoadRequestId()));
				res.setEmailCount(getRequestMessageCount(ContactType.EMAIL, req.getLoadRequestId()));
				res.setCallCount(getRequestMessageCount(ContactType.CALL, req.getLoadRequestId()));
				res.setBidCount(getRequestBidCount(req.getLoadRequestId()));
				res.setStatus(req.getLoadRequestStatus());
				if (UserRoles.SUPER_ADMIN.equals(authuser.getUserRole())
						&& checkStatusForCharges(req.getLoadRequestStatus())) {
					res.setAmount(getRequestBidCharges(req.getLoadRequestId(), "amount"));
					res.setFaberCharges(getRequestBidCharges(req.getLoadRequestId(), "faber_charges"));
				}
				res.setInsuranceNeeded(req.isInsuranceNeeded());
				res.setUserId(req.getUserId());
				res.setUserName(usersMap.get(req.getUserId()));
				beans.add(res);
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		}
		return beans;
	}

	@Override
	@Transactional
	public RequestMarketingMessageDetailBean getRequestMarketingMessageDetails(
			RequestMarketingDetailFiltersBean filtersBean, boolean showPending, Users authuser) throws APIExceptions {
		RequestMarketingMessageDetailBean detailBean = new RequestMarketingMessageDetailBean();
		detailBean.setLoadRequestId(filtersBean.getLoadRequestId());
		detailBean.setType(filtersBean.getType());
		List<RequestMarketingMessage> messageList = null;
		if (filtersBean.getType().equals(ContactType.CALL)) {
			messageList = marketingManager.getRequestMarketingCallDetails(filtersBean, showPending, authuser);
		} else {
			messageList = marketingManager.getRequestMarketingMessageDetails(filtersBean);
		}
		if (messageList != null) {
			detailBean.setMessageList(messageList);
		}
		return detailBean;
	}

	/**
	 * Method to check allowed load request status to get money values
	 * 
	 * @param loadRequestStatus
	 * @return
	 */
	private boolean checkStatusForCharges(LoadRequestStatus loadRequestStatus) {
		switch (loadRequestStatus) {
		case AWARDED:
			return true;
		case BOOKED:
			return true;
		/*
		 * case IN_TRANSIT: return true;
		 */
		case DELIVERED:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Method to fetch lowest bid charges for the supplied load request id
	 * 
	 * @param loadRequestId
	 * @param charge
	 *            can be either "amount" or "fw_charges"
	 * @return
	 * @throws APIExceptions
	 */
	private double getRequestBidCharges(long loadRequestId, String charge) throws APIExceptions {
		if (charge != null && loadRequestId > 0) {
			return marketingManager.getRequestBidCharges(loadRequestId, charge);
		} else {
			throw new APIExceptions("Bad call for this method");
		}
	}

	/**
	 * Method to fetch total bid count for the supplied load request id
	 * 
	 * @param requestId
	 * @return
	 * @throws APIExceptions
	 */
	private Long getRequestBidCount(long requestId) throws APIExceptions {
		if (requestId > 0) {
			return (long) marketingManager.getRequestBidCount(requestId);
		} else {
			throw new APIExceptions("Bad call for this method");
		}
	}

	/**
	 * Method to fetch count of sms/email/call for the supplied load request id
	 * 
	 * @param type
	 *            can be either sms/email/call
	 * @param requestId
	 * @return total count available in the database
	 * @throws APIExceptions
	 */
	private Long getRequestMessageCount(ContactType type, long requestId) throws APIExceptions {
		if (requestId > 0) {
			return (long) marketingManager.getRequestMessageCount(type, requestId);
		} else {
			throw new APIExceptions("Bad call for this method");
		}
	}

	/**
	 * Prepare final call list to batch insert into the call_history table
	 * 
	 * @param users
	 *            selected user list with facts
	 * @param template
	 *            message template
	 * @param selectionBean
	 *            selected filters
	 * @param authuser
	 *            current logged-in user
	 * @return a list of messages to batch insert into the call_history table
	 */
	private List<CallHistory> prepareCallList(List<UsersWithFactsBean> users, MessageTemplates template,
			ProviderMarketingSelectionBean selectionBean, Users authuser) throws RuntimeException {
		int skippedCount = 0;
		List<CallHistory> messages = new ArrayList<CallHistory>();
		String uuid = String.valueOf(UUID.randomUUID());
		// user_id, load_request_id, contact, message_body, status, type, created_by,
		// modified_by
		for (UsersWithFactsBean user : users) {
			try {
				CallHistory message = new CallHistory();
				message.setLoadRequestId(selectionBean.getLoadRequestId());
				message.setUserId(user.getUserId());
				message.setStatus(ContactStatus.PENDING);
				message.setPhoneNumber(user.getUserName());
				message.setMessageBody(template.getMessageBody());
				message.setBatchId(uuid);
				message.setCreatedBy(authuser.getUserId());
				message.setModifiedBy(authuser.getUserId());
				messages.add(message);
			} catch (RuntimeException e) {
				log.info("Skipping the record due to, " + e.getMessage());
				skippedCount++;
			}
		}
		if (skippedCount == users.size()) {
			throw new RuntimeException("No user is having any email");
		}
		return messages;
	}

	/**
	 * Prepare final message list to batch insert into the sent_messages table
	 * 
	 * @param users
	 *            selected user list with facts
	 * @param template
	 *            message template
	 * @param selectionBean
	 *            selected filters
	 * @param authuser
	 *            current logged-in user
	 * @return a list of messages to batch insert into the sent_messages table
	 */
	private List<SentMessages> prepareMessageList(List<UsersWithFactsBean> users, MessageTemplates template,
			ProviderMarketingSelectionBean selectionBean, Users authuser) throws RuntimeException {
		int skippedCount = 0;
		List<SentMessages> messages = new ArrayList<SentMessages>();
		String uuid = String.valueOf(UUID.randomUUID());
		// user_id, load_request_id, contact, message_body, status, type, created_by,
		// modified_by
		for (UsersWithFactsBean user : users) {
			try {
				SentMessages message = new SentMessages();
				message.setLoadRequestId(selectionBean.getLoadRequestId());
				message.setUserId(user.getUserId());
				message.setStatus(ContactStatus.PENDING);
				if (ContactType.SMS.equals(selectionBean.getType())) {
					message.setContact(user.getUserName());
					message.setMessageBody(
							getFinalSMSBody(template, user.getFacts(), selectionBean.getLoadRequestId()));
					message.setType(ContactType.SMS);
				} else if (ContactType.EMAIL.equals(selectionBean.getType())) {
					message.setContact(getUserEmail(user.getFacts(), user.getUserId()));
					message.setMessageBody(
							getFinalEmailBody(template, user.getFacts(), selectionBean.getLoadRequestId()));
					message.setType(ContactType.EMAIL);
				}
				message.setBatchId(uuid);
				message.setCreatedBy(authuser.getUserId());
				message.setModifiedBy(authuser.getUserId());
				messages.add(message);
			} catch (RuntimeException e) {
				log.info("Skipping the record due to, " + e.getMessage());
				skippedCount++;
			}
		}
		if (skippedCount == users.size()) {
			throw new RuntimeException("No user is having any email");
		}
		return messages;
	}

	/**
	 * Method to return email from user facts.
	 * 
	 * @param facts
	 *            list of user facts
	 * @param userId
	 *            for which these facts are saved
	 * @return the first found email available for user
	 * @throws Exception
	 *             if list is empty or no email found in user facts
	 */
	private String getUserEmail(List<UserFacts> facts, long userId) throws RuntimeException {
		if (facts != null && !facts.isEmpty()) {
			String email = "";
			for (UserFacts fact : facts) {
				if (fact.getFact().equals(Facts.EMAIL_ID.toDbString())) {
					email = fact.getValue();
					break;
				}
			}
			if ("".equals(email)) {
				throw new RuntimeException("No email found for user_id=" + userId);
			}
			return email;
		} else {
			throw new RuntimeException("No facts available for user_id=" + userId);
		}
	}

	/**
	 * Method to replace email placeholder in the message-body with actual content
	 * and return final message-body.
	 * 
	 * @param template
	 *            selected email template
	 * @param facts
	 *            user data available
	 * @param loadRequestId
	 * @return final message-body
	 */
	private String getFinalEmailBody(MessageTemplates template, List<UserFacts> facts, Long loadRequestId) {
		return template.getMessageBody();
	}

	/**
	 * Method to replace SMS placeholder in the message-body with actual content and
	 * return final message-body.
	 * 
	 * @param template
	 *            selected SMS template
	 * @param facts
	 *            user data available
	 * @param loadRequestId
	 * @return final message-body
	 */
	private String getFinalSMSBody(MessageTemplates template, List<UserFacts> facts, Long loadRequestId)
			throws RuntimeException {
		if (loadRequestId > 0 && "CAPACITY PROVIDERS AFTER LOAD REQUEST COMES".equals(template.getSubjectLine())) {
			String source = "", destination = "";
			try {
				Map<String, String> cityMapSD = messageUtils.getCitiesByLoadRequest(loadRequestId);
				source = cityMapSD.get(messageUtils.getSource());
				destination = cityMapSD.get(messageUtils.getDestination());
			} catch (APIExceptions e) {
				throw new RuntimeException(e.getMessage());
			}
			return messageUtils.getLoadRequestFinalSMSBody(template.getMessageBody(), loadRequestId, source,
					destination);
		}
		return template.getMessageBody();
	}

	/**
	 * 
	 * @param address
	 * @return
	 */
	private List<MarkatingAddressBean> getAddressses(List<MarkatingAddressBean> addressList) {

		ListIterator<MarkatingAddressBean> lstItr = addressList.listIterator();

		while (lstItr.hasNext()) {

			MarkatingAddressBean address = lstItr.next();

			if (address.getAddressLine1() == null || address.getAddressLine1().trim().equals("")) {
				lstItr.remove();
			}
		}
		return addressList;

	}

}
