package com.fw.daemon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fw.dao.ISentMessagesManager;
import com.fw.domain.SentMessages;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.exceptions.SMSServiceException;
import com.fw.utils.SMSSenderUtil;

@Service
@PropertySource("classpath:mlogistics.properties")
public class MessagesResponseDaemon {

	@Autowired
	ISentMessagesManager sentMessagesManager;

	@Autowired
	SMSSenderUtil sMSSenderUtil;

	@Value("${process.MessagesResponseDaemon.isActive}")
	private boolean isActive;
	
	@Value("${process.SMS.Status.Reset.Count}")
	private int resetCountLimit;
	
	private Logger log = Logger.getLogger(MessagesResponseDaemon.class);

	@Scheduled(fixedDelayString = "${process.MessagesResponseDaemon.fixedDelay}")	 // Run in every 30 minutes
	public void checkMessageStatus() throws JSONException, SMSServiceException {

		if(isActive) {
			log.info("Fetching all messages whose status is SMS-Awaited.");
			List<SentMessages> smsMessageList = sentMessagesManager.getAllSMSAwaitedMessages(ContactType.SMS);
			
			log.info("fetched details :" + smsMessageList);
			
			Map<String, List<SentMessages>> mapListMessages = new HashMap<>();
			
			if (smsMessageList != null && smsMessageList.size() > 0) {
				log.info("Number of messages whose status is SMS-Awaited=[" + smsMessageList.size() + "]");
				
				for (SentMessages sentMessageTemp : smsMessageList) {
					if (mapListMessages.containsKey(sentMessageTemp.getSmsGroupId())) {
						List<SentMessages> messageList = mapListMessages.get(sentMessageTemp.getSmsGroupId());
						messageList.add(sentMessageTemp);
						// mapListMessages.put(smsMessageList.get(i).getSmsGroupId(), messageList);
					} else {
						List<SentMessages> messageList = new ArrayList<SentMessages>();
						messageList.add(sentMessageTemp);
						mapListMessages.put(sentMessageTemp.getSmsGroupId(), messageList);
					}
				}
				
				for (Map.Entry<String, List<SentMessages>> entry : mapListMessages.entrySet()) {
					
					String response = sMSSenderUtil.getDeliveryStatus(entry.getKey());
					Map<String, ContactStatus> map = parseMessage(response);
					List<String> list = new ArrayList<String>(map.keySet());
					
					for (SentMessages sentMsg : entry.getValue()) {
						if (list.contains(sentMsg.getContact())) {					
							ContactStatus scTemp = map.get(sentMsg.getContact());				
							if(scTemp==ContactStatus.SMS_STATUS_AWAITED) {
								if(sentMsg.getStatusResetCount() < resetCountLimit) {
									sentMessagesManager.updateStatusResetCount(sentMsg.getStatusResetCount()+1, sentMsg.getContact(), entry.getKey());
									
								} else {
									sentMessagesManager.updateSentMessagesStatusByGroupIdNumber(sentMsg.getContact(),
											ContactStatus.SMS_FAILURE, entry.getKey());
								}
							}
							else {							
								sentMessagesManager.updateSentMessagesStatusByGroupIdNumber(sentMsg.getContact(),
									map.get(sentMsg.getContact()), entry.getKey());
							}
						} else {
							sentMessagesManager.updateSentMessagesStatusByGroupIdNumber(sentMsg.getContact(),
									ContactStatus.SMS_FAILURE, entry.getKey());
						}
					}
				}
			}
		} else {
			log.info(this.getClass().getName() + " is set to false for execution.");
		}
	}

	private Map<String, ContactStatus> parseMessage(String response) throws JSONException {

		// Code to parse response and return map containing phoneNumber as key and
		// ContactStatus as value.
		Map<String, ContactStatus> map = new LinkedHashMap<String, ContactStatus>();

		JSONArray values = new JSONArray(response);
		for (int i = 0; i < values.length(); i++) {
			JSONObject conDetails = values.getJSONObject(i);
			String conValue = conDetails.getString("MSISDN");
			String status = conDetails.getString("DLR");

			if (status.equalsIgnoreCase("Delivered"))
				map.put(conValue, ContactStatus.SMS_SUCCESS);

			else if (status.equalsIgnoreCase("Operator Submitted"))
				map.put(conValue, ContactStatus.SMS_STATUS_AWAITED);
		}
		return map;
	}

}
