package com.fw.dao;

import java.util.List;

import com.fw.domain.CallHistory;
import com.fw.exceptions.APIExceptions;

public interface ICallHistoryManager {

	int[] persistBatchCalls(List<CallHistory> calls) throws APIExceptions;

	CallHistory getLastCallHistoryByUserId(long userId) throws APIExceptions;

	void updateCallHistoryById(long callHistoryId, String callStatus, long feedbackId) throws APIExceptions;

}
