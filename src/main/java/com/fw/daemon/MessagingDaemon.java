package com.fw.daemon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fw.dao.ISentMessagesManager;
import com.fw.domain.SentMessages;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.exceptions.SMSServiceException;
import com.fw.utils.SMSSenderUtil;

@Service
public class MessagingDaemon {
	
	@Value("${spring.mail.username}")
	private String senderEmailAddress;
	
	@Value("${process.MessagingDaemon.isActive}")
	private boolean isActive;
	
	@Value("${process.SMS.internalMobiles}")
	private String internalMobiles;
	
	@Value("${process.SMS.onlyInternal}")
	private boolean onlyInternal;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@Autowired
	ISentMessagesManager sentMessagesManager;
	
	@Autowired
	SMSSenderUtil sMSSenderUtil;
	
	private Logger log = Logger.getLogger(MessagingDaemon.class);	
	
	@Scheduled(fixedDelayString = "${process.MessagingDaemon.fixedDelay}")				// Run in every 15 minutes
	public void sendMessage() {		
		if(isActive) {
			log.info("Fetching all messages whose status is SMS-Pending.");
			List<String> phones = null;
			try {
				phones = Arrays.asList(internalMobiles.split(","));
			} catch (Exception e) {
				phones = new ArrayList<String>();
			}
			List<SentMessages> smsMessageList = sentMessagesManager.getAllPendingMessages(ContactType.SMS);
			Map<String, String> map = new HashMap<String, String>();
			if(smsMessageList!=null && smsMessageList.size()>0) {
				for(int i=0;i<smsMessageList.size();i++) {	
					SentMessages sentMessage = smsMessageList.get(i);
					if(onlyInternal) {
						if(!phones.contains(sentMessage.getContact())) {
							sentMessage.setStatus(ContactStatus.SMS_CANCELED);
							sentMessagesManager.updateSentMessagesById(sentMessage);
							continue;
						}
					} 
					if(map.containsKey(sentMessage.getMessageBody())) {
						String tempVal = map.get(sentMessage.getMessageBody())+","+sentMessage.getContact();
						map.replace(sentMessage.getMessageBody(), tempVal);
					}
					else {
						map.put(sentMessage.getMessageBody(), sentMessage.getContact());
					}				
				}
				
				if(!map.isEmpty()) {
					for (Map.Entry<String, String> entry : map.entrySet()){
						try {
							log.info("sending sms [" + entry.getKey() + "] to mobile numbers [" + entry.getValue() + "]");
							String shootId = sMSSenderUtil.sendMessage(entry.getValue(), entry.getKey());
							if (shootId != null && shootId.contains("SMS-SHOOT-ID/")) {
								shootId = shootId.substring(shootId.lastIndexOf("/") + 1);
								sentMessagesManager.updateSentMessagesStatusShootId(entry.getValue(), entry.getKey(), shootId);							
							} else if (shootId != null && shootId.equalsIgnoreCase("Err")) {
								log.error("Error in sending messages to ["+entry.getValue()+"]");
							}
						} catch (SMSServiceException e) {
							log.error("Error in sending messages to ["+entry.getValue()+"]");
						}
					}
				}			
			}		
			
			List<SentMessages> emailMmessageList = sentMessagesManager.getAllPendingMessages(ContactType.EMAIL);
			if(emailMmessageList!=null && emailMmessageList.size()>0) {
				log.info("Total emails to send:"+emailMmessageList.size());
				for(SentMessages sendMessage : emailMmessageList) {			
					sendMail(senderEmailAddress, sendMessage.getContact(), sendMessage.getEmailSubject(), sendMessage.getMessageBody());
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			log.info(this.getClass().getName() + " is set to false for execution.");
		}
	}	
	
	public void sendMail(String from, String to, String subject, String body) {
		
		SimpleMailMessage mail = new SimpleMailMessage(); 
		mail.setFrom(from);
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(body);		
		
		log.info("Sending...");		
		javaMailSender.send(mail);		
		log.info("Done!");
	}

}
