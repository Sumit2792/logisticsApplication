package com.fw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fw.beans.ConfigBeans;
import com.fw.beans.InBetweenCitiesBean;
import com.fw.dao.IBidManager;
import com.fw.dao.IConfigManager;
import com.fw.dao.ILoadRequestCitiesManager;
import com.fw.dao.IMessageTemplateManager;
import com.fw.dao.ISentMessagesManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.domain.SentMessages;
import com.fw.domain.Users;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

@Service
public class LoadRequestAlgorithm {

	private Logger log = Logger.getLogger(LoadRequestAlgorithm.class);

	@Autowired
	private ILoadRequestCitiesManager loadRequestCitiesManager;

	@Autowired
	private IConfigManager configManager;

	@Autowired
	private IMessageTemplateManager messageTemplateManager;

	@Autowired
	private ISentMessagesManager sentMessagesManager;

	@Autowired
	private IUserManager userManager;

	@Autowired
	MessageTemplateUtils messageUtils;
	
	@Autowired
	IBidManager bidManager;

	public void processLoadRequest(List<LoadRequest> listLoadRequest) {
		List<Users> usersList = new ArrayList<Users>();
		List<Users> bidderUsersList = new ArrayList<Users>();
		List<ConfigBeans> configList = null;
		try {
			configList = configManager.getAllConfigRowMapper();
		} catch (APIExceptions e) {
			e.printStackTrace();
		}

		int noOfMessageToSent = 500;
		for (ConfigBeans config : configList) {
			if (config.getValue().equalsIgnoreCase("LOAD_REQUEST_DEFAULT_MESSAGE_COUNT")) {
				try {
					noOfMessageToSent = Integer.parseInt(config.getValue());
				} catch (NumberFormatException e) {
					log.info("LOAD_REQUEST_DEFAULT_MESSAGE_COUNT is not a integer.");
				}
			}
		}
		int noOfMessagesToSD = (int) (noOfMessageToSent * .85);
		int destCount = (int) (noOfMessagesToSD * .6);
		int sourceCount = noOfMessagesToSD - destCount;
		int remainingCitiesCount = noOfMessageToSent - noOfMessagesToSD;

		if (listLoadRequest != null && listLoadRequest.size() > 0) {
			for (LoadRequest loadRequest : listLoadRequest) {
				log.info("Running LoadRequestAlgorithm for [" + loadRequest.getLoadRequestId() + "]");

				String source = "", destination = "";
				try {
					Map<String, String> cityMapSD = messageUtils.getCitiesByLoadRequest(loadRequest.getLoadRequestId());
					source = cityMapSD.get(messageUtils.getSource());
					destination = cityMapSD.get(messageUtils.getDestination());
				} catch (APIExceptions e) {
					log.error("Skipping LoadRequestAlgorithm due to " + e.getMessage());
					continue;
				}

				if (source.equals("") || destination.equals("")) {
					log.error("Skipping LoadRequestAlgorithm for load request [" + loadRequest.getLoadRequestId()
							+ "], as either source or destination is not available.");
					continue;
				}

				List<Long> listofUsersInSentMessage = sentMessagesManager
						.getAllSentMessagesLRId(loadRequest.getLoadRequestId());

				Set<String> selectedCities = new LinkedHashSet<String>();
				try {
					selectedCities.add(destination);
					usersList.addAll(
							userManager.getUserNameByCities(selectedCities, destCount, listofUsersInSentMessage));
				} catch (InvalidUsernameException e) {
					log.error(
							"Looks like there is some issue with database operation to get users for the following cities = "
									+ Arrays.toString(selectedCities.toArray()));
				}
				selectedCities.clear();
				try {
					selectedCities.add(source);
					usersList.addAll(
							userManager.getUserNameByCities(selectedCities, sourceCount, listofUsersInSentMessage));
				} catch (InvalidUsernameException e) {
					log.error(
							"Looks like there is some issue with database operation to get users for the following cities = "
									+ Arrays.toString(selectedCities.toArray()));
				}
				selectedCities.clear();

				HashMap<String, Object> mapOfCities = loadRequestCitiesManager
						.getCitiesBetweenSourceAndDestination(source, destination);

				InBetweenCitiesBean[] inBetweenCitiesBeanArray = (InBetweenCitiesBean[]) mapOfCities
						.get("inBetweenCities");

				if (inBetweenCitiesBeanArray != null && inBetweenCitiesBeanArray.length > 0) {
					for (int i = inBetweenCitiesBeanArray.length - 1; i >= 0; i--) {
						selectedCities.add(inBetweenCitiesBeanArray[i].getName());
					}
					try {
						usersList.addAll(userManager.getUserNameByCities(selectedCities, remainingCitiesCount,
								listofUsersInSentMessage));
					} catch (InvalidUsernameException e) {
						log.error(
								"Looks like there is some issue with database operation to get users for the following cities = "
										+ Arrays.toString(selectedCities.toArray()));
					}
				}
				
				bidderUsersList = bidManager.getBidderInfoByLoadRequestId(loadRequest.getLoadRequestId());

				log.info("===bidderUsersList [" + bidderUsersList + "].");
				
				if (!usersList.isEmpty()) {
					usersList.removeAll(bidderUsersList);
					
					String message = messageTemplateManager.getMessageBySubjectLine(
							MessageTemplateConstants.CAPACITY_PROVIDERS_AFTER_LOAD_REQUEST_COMES);
					message = messageUtils.getLoadRequestFinalSMSBody(message, loadRequest.getLoadRequestId(), source,
							destination);
					log.info("===message [" + message + "].");

					String uuid = String.valueOf(UUID.randomUUID());
					List<SentMessages> msgList = new ArrayList<SentMessages>();
					ListIterator<Users> iter = usersList.listIterator();
					while (iter.hasNext()) {
						Users user = iter.next();
						if (user.getUserId() == loadRequest.getUserId()) {
							iter.remove();
							continue;
						}
						SentMessages msg = new SentMessages();
						log.info("===selected user to send message [" + user.getUserId() + "] and mobile number is ["
								+ user.getUserName() + "]");
						msg.setUserId(user.getUserId());
						msg.setContact(user.getUserName());
						msg.setMessageBody(message);
						msg.setLoadRequestId(loadRequest.getLoadRequestId());
						msg.setStatus(ContactStatus.PENDING);
						msg.setType(ContactType.SMS);
						msg.setBatchId(uuid);
						msgList.add(msg);
					}
					log.info("Load request [" + loadRequest.getLoadRequestId() + "]===total users [" + usersList.size()
							+ "]");
					sentMessagesManager.persistBatchSentMessages(msgList, true);
				} else {
					log.info(
							"No users found to send message for load request [" + loadRequest.getLoadRequestId() + "]");
				}
			}
		}
	}
}
