package com.fw.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.fw.dao.ICallHistoryManager;
import com.fw.domain.CallHistory;
import com.fw.enums.ContactStatus;
import com.fw.exceptions.APIExceptions;

@Repository
public class CallHistoryManagerImpl implements ICallHistoryManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private Logger log = Logger.getLogger(CallHistoryManagerImpl.class);

	@Override
	public int[] persistBatchCalls(List<CallHistory> calls) throws APIExceptions {
		try {
			return jdbcTemplate.batchUpdate(
					"INSERT INTO call_history (user_id, load_request_id, phone_number, message_body, status, created_by, modified_by, batch_id) "
							+ " VALUES(?,?,?,?,?,?,?,?)",
					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							CallHistory logEntity = calls.get(i);
							ps.setLong(1, logEntity.getUserId());
							if(logEntity.getLoadRequestId() <= 0) {
								ps.setNull(2, java.sql.Types.BIGINT);
							} else {
								ps.setLong(2, logEntity.getLoadRequestId());
							}
							ps.setString(3, logEntity.getPhoneNumber());
							ps.setString(4, logEntity.getMessageBody());
							ps.setString(5, logEntity.getStatus().toDbString());
							ps.setLong(6, logEntity.getCreatedBy());
							ps.setLong(7, logEntity.getModifiedBy());
							ps.setString(8, logEntity.getBatchId());
						}

						@Override
						public int getBatchSize() {
							return calls.size();
						}
					});
		} catch (DataAccessException e) {
			log.error("Exception occurred while inserting batch to call history table", e);
			return new int[0];
		}
	}

	@Override
	public CallHistory getLastCallHistoryByUserId(long userId) throws APIExceptions {
		try {
			return jdbcTemplate.query("select * from call_history where user_id=? order by created_date desc limit 1",
					new ResultSetExtractor<CallHistory>() {
						@Override
						public CallHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs.next()) {
								CallHistory logEntity = new CallHistory();
								logEntity.setCallHistoryId(rs.getLong("call_history_id"));
								logEntity.setUserId(rs.getLong("user_id"));
								logEntity.setLoadRequestId(rs.getLong("load_request_id"));
								logEntity.setPhoneNumber(rs.getString("phone_number"));
								logEntity.setBatchId(rs.getString("batch_id"));
								logEntity.setMessageBody(rs.getString("message_body"));
								logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
								logEntity.setCreatedBy(rs.getLong("created_by"));
								logEntity.setModifiedBy(rs.getLong("modified_by"));
								logEntity.setCreatedDate(rs.getTimestamp("created_date"));
								logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
								logEntity.setFeedbackId(rs.getLong("feedback_id"));
								return logEntity;
							} else {
								return null;
							}
						}
					}, userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateCallHistoryById(long callHistoryId, String callStatus, long feedbackId) throws APIExceptions {
		try {
			int result = jdbcTemplate.update("update call_history set status=?, feedback_id=? where call_history_id=?", callStatus, callHistoryId);
			if(result <= 0) {
				throw new APIExceptions("Looks like there is an issue with database operation. Please try again.");
			}
		} catch (DataAccessException e) {
			log.error("Exception occurred while call history table for id=[" + callHistoryId + "] and status=[" + callStatus + "]", e);
			throw new APIExceptions("Looks like there is an issue with database operation. Please try again.");
		}
	}

}
