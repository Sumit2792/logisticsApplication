package com.fw.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.dao.ISentMessagesManager;
import com.fw.domain.SentMessages;
import com.fw.services.ISentMessageService;

@Service
public class SentMessageServiceImpl implements ISentMessageService {

	@Autowired
	ISentMessagesManager sentMessagesManager;
	
	@Override
	@Transactional
	public SentMessages addSentMessages(SentMessages logEntity) {
		if(logEntity!=null) {
			return sentMessagesManager.persistSentMessages(logEntity);
		}
		else
			return null;
	}

	@Override
	@Transactional
	public void updateSentMessagesById(SentMessages logEntity) {
	
		sentMessagesManager.updateSentMessagesById(logEntity);
		
	}

	@Override
	@Transactional
	public void deleteSentMessagesById(SentMessages id) {

		sentMessagesManager.deleteSentMessagesById(id);
	}

	@Override
	@Transactional
	public List<SentMessages> getAllSentMessages() {
		
		return sentMessagesManager.getAllSentMessagesRowMapper();
	}

	@Override
	@Transactional
	public SentMessages getSentMessagesById(long bidId) {
		// TODO Auto-generated method stub
		return sentMessagesManager.getSentMessagesById(bidId);
	}

}
