package com.fw.daemon;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fw.dao.ISentMessagesManager;
import com.fw.domain.SentMessages;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.exceptions.SMSServiceException;
import com.fw.utils.SMSSenderUtil;


@RunWith(MockitoJUnitRunner.class)
public class MessagingDaemonTest {

	@InjectMocks
	MessagingDaemon messagingDaemon;
	
	@Mock
	ISentMessagesManager sentMessagesManager;
	
	@Mock
	SMSSenderUtil sMSSenderUtil;
	
	@Test
	public void messagingDaemonTest1() {
		
		org.springframework.test.util.ReflectionTestUtils.setField(messagingDaemon, "isActive", true);		
		
		List<SentMessages> al = getSentMessagesData();
		when(sentMessagesManager.getAllPendingMessages(ContactType.SMS)).thenReturn(al);
		
		try {
			when(sMSSenderUtil.sendMessage("9982718071","Hiiii")).thenReturn("SMS-SHOOT-ID/shoot1");
		} catch (SMSServiceException e) {
			e.printStackTrace();
		}
		
		doNothing().when(sentMessagesManager).updateSentMessagesStatusShootId("9982718071","Hiiii","shoot1");		
		
		messagingDaemon.sendMessage();		
	}
	
	
	private List<SentMessages> getSentMessagesData(){
		
		List<SentMessages> al = new ArrayList<SentMessages>();
		SentMessages sm = new SentMessages();
		sm.setContact("9982718071");
		sm.setStatus(ContactStatus.PENDING);
		sm.setMessageBody("Hiiii");
		al.add(sm);
		return al;
	}
	
	
	
}
