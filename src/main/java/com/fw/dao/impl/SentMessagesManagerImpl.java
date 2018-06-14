package com.fw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.dao.ISentMessagesManager;
import com.fw.domain.SentMessages;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;

@Repository
public class SentMessagesManagerImpl implements ISentMessagesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private Logger log = Logger.getLogger(SentMessagesManagerImpl.class);

	@Override
	public SentMessages persistSentMessages(SentMessages logEntity) {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO sent_messages (userId, contact, messageBody, status, type, load_request_id,  "
					+ "email_subject, sms_goup_id, batch_id, created_by, modified_by, status_reset_count ) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "sent_messages_id" });
					ps.setLong(1, logEntity.getUserId());
					ps.setString(2, logEntity.getContact());
					ps.setString(3, logEntity.getMessageBody());
					ps.setString(4, logEntity.getStatus().toDbString());
					ps.setString(5, logEntity.getType().toDbString());
					ps.setLong(6, logEntity.getLoadRequestId());
					ps.setString(7, logEntity.getEmailSubject());
					ps.setString(8, logEntity.getSmsGroupId());
					ps.setString(9, logEntity.getBatchId());
					ps.setLong(10, logEntity.getCreatedBy());
					ps.setLong(11, logEntity.getModifiedBy());
					ps.setInt(12, logEntity.getStatusResetCount());
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setSentMessagesId(logEntityId);
			return logEntity;
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void updateSentMessagesById(SentMessages logEntity) {
		String sql = "UPDATE sent_messages  SET user_id=?, email_subject=?, sms_group_id=?, batch_id=?,  contact=?, "
				+ "message_body=?,status=? ,contact_type=? ,created_by=? ,modified_by=? WHERE sent_messages_id=?;";
		jdbcTemplate.update(sql, logEntity.getUserId(), logEntity.getEmailSubject(), logEntity.getSmsGroupId(),
				logEntity.getBatchId(), logEntity.getContact(), logEntity.getMessageBody(), logEntity.getStatus().toDbString(),
				logEntity.getType().toDbString(), logEntity.getCreatedBy(), logEntity.getModifiedBy(),
				logEntity.getSentMessagesId());
	}

	@Override
	public void deleteSentMessagesById(SentMessages Id) {

		String sql = "DELETE from sent_messages where sent_messages_id=?  ;";
		jdbcTemplate.update(sql, Id);

	}

	@Override
	public List<SentMessages> getAllSentMessagesRowMapper() {
		return jdbcTemplate.query("select * from sent_messages;", new RowMapper<SentMessages>() {
			@Override
			public SentMessages mapRow(ResultSet rs, int rownumber) throws SQLException {
				SentMessages logEntity = new SentMessages();
				logEntity.setSentMessagesId(rs.getLong("sent_messages_id"));
				logEntity.setUserId(rs.getLong("user_id"));
				logEntity.setContact(rs.getString("contact"));
				logEntity.setMessageBody(rs.getString("message_body"));
				logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
				logEntity.setType(ContactType.fromString(rs.getString("contact_type")));
				logEntity.setEmailSubject(rs.getString("email_subject"));
				logEntity.setLoadRequestId(rs.getLong("load_request_id"));
				logEntity.setSmsGroupId(rs.getString("sms_group_id"));
				logEntity.setBatchId(rs.getString("batch_id"));
				logEntity.setCreatedBy(rs.getLong("created_by"));
				logEntity.setModifiedBy(rs.getLong("modified_by"));
				logEntity.setCreatedDate(rs.getTimestamp("created_date"));
				logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
				logEntity.setStatusResetCount(rs.getInt("status_reset_count"));
				return logEntity;
			}
		});
	}

	@Override
	public SentMessages getSentMessagesById(long logEntityId) {
		try {
			return jdbcTemplate.queryForObject("select * from sent_messages where sent_messages_id=?",
					new RowMapper<SentMessages>() {
						@Override
						public SentMessages mapRow(ResultSet rs, int rownumber) throws SQLException {
							SentMessages logEntity = new SentMessages();
							logEntity.setSentMessagesId(rs.getLong("sent_messages_id"));
							logEntity.setUserId(rs.getLong("user_id"));
							logEntity.setContact(rs.getString("contact"));
							logEntity.setMessageBody(rs.getString("message_body"));
							logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
							logEntity.setType(ContactType.fromString(rs.getString("contact_type")));
							logEntity.setEmailSubject(rs.getString("email_subject"));
							logEntity.setLoadRequestId(rs.getLong("load_request_id"));
							logEntity.setSmsGroupId(rs.getString("sms_group_id"));
							logEntity.setBatchId(rs.getString("batch_id"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							logEntity.setStatusResetCount(rs.getInt("status_reset_count"));
							return logEntity;
						}
					}, logEntityId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SentMessages getLastSentMessageForUserByType(Long userId, ContactType type) {
		try {
			String query = "select * from sent_messages where user_id=? and contact_type=? order by created_date desc limit 1";
			Object[] args = { userId, type.toDbString() };
			return jdbcTemplate.query(query, args, new ResultSetExtractor<SentMessages>() {
				@Override
				public SentMessages extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						SentMessages logEntity = new SentMessages();
						logEntity.setSentMessagesId(rs.getLong("sent_messages_id"));
						logEntity.setUserId(rs.getLong("user_id"));
						logEntity.setContact(rs.getString("contact"));
						logEntity.setMessageBody(rs.getString("message_body"));
						logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
						logEntity.setType(ContactType.fromString(rs.getString("contact_type")));
						logEntity.setEmailSubject(rs.getString("email_subject"));
						logEntity.setLoadRequestId(rs.getLong("load_request_id"));
						logEntity.setSmsGroupId(rs.getString("sms_group_id"));
						logEntity.setBatchId(rs.getString("batch_id"));
						logEntity.setCreatedBy(rs.getLong("created_by"));
						logEntity.setModifiedBy(rs.getLong("modified_by"));
						logEntity.setCreatedDate(rs.getTimestamp("created_date"));
						logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
						logEntity.setStatusResetCount(rs.getInt("status_reset_count"));
						return logEntity;
					} else {
						return null;
					}
				}
			});
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<SentMessages> getAllPendingMessages(ContactType contactType) {
		Object[] args = { ContactStatus.PENDING.toDbString(), contactType.toDbString() };
		return jdbcTemplate.query("select * from sent_messages where status=? and contact_type=?", args,
				new RowMapper<SentMessages>() {
					@Override
					public SentMessages mapRow(ResultSet rs, int rownumber) throws SQLException {
						SentMessages logEntity = new SentMessages();
						logEntity.setSentMessagesId(rs.getLong("sent_messages_id"));
						logEntity.setUserId(rs.getLong("user_id"));
						logEntity.setContact(rs.getString("contact"));
						logEntity.setMessageBody(rs.getString("message_body"));
						logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
						logEntity.setType(ContactType.fromString(rs.getString("contact_type")));
						logEntity.setEmailSubject(rs.getString("email_subject"));
						logEntity.setLoadRequestId(rs.getLong("load_request_id"));
						logEntity.setSmsGroupId(rs.getString("sms_group_id"));
						logEntity.setBatchId(rs.getString("batch_id"));
						logEntity.setCreatedBy(rs.getLong("created_by"));
						logEntity.setModifiedBy(rs.getLong("modified_by"));
						logEntity.setCreatedDate(rs.getTimestamp("created_date"));
						logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
						logEntity.setStatusResetCount(rs.getInt("status_reset_count"));
						return logEntity;
					}
				});
	}

	@Override
	public void updateSentMessagesStatusShootId(String phoneNumber, String message, String shootId) {

		List<String> al = new ArrayList<String>();

		if (phoneNumber.contains(",")) {
			al = Arrays.asList(phoneNumber.split(","));
		} else {
			al.add(phoneNumber);
		}

		Map<String, List<String>> params = Collections.singletonMap("mobileNos", al);

		String sql = "UPDATE sent_messages  SET status='" + ContactStatus.SMS_STATUS_AWAITED.toDbString() + "'"
				+ ",sms_group_id='" + shootId + "' WHERE message_body='" + message + "' AND contact in (:mobileNos)";
		namedParameterJdbcTemplate.update(sql, params);
	}

	@Override
	public List<SentMessages> getAllSMSAwaitedMessages(ContactType contactType) {
		Object[] args = { ContactStatus.SMS_STATUS_AWAITED.toDbString(), contactType.toDbString() };

		try {
			return jdbcTemplate.query("SELECT * from sent_messages where status=? AND contact_type=?",
					new RowMapper<SentMessages>() {
						@Override
						public SentMessages mapRow(ResultSet rs, int rownumber) throws SQLException {
							SentMessages logEntity = new SentMessages();
							logEntity.setSentMessagesId(rs.getLong("sent_messages_id"));
							logEntity.setUserId(rs.getLong("user_id"));
							logEntity.setContact(rs.getString("contact"));
							logEntity.setMessageBody(rs.getString("message_body"));
							logEntity.setStatus(ContactStatus.fromString(rs.getString("status")));
							logEntity.setType(ContactType.fromString(rs.getString("contact_type")));
							logEntity.setEmailSubject(rs.getString("email_subject"));
							logEntity.setLoadRequestId(rs.getLong("load_request_id"));
							logEntity.setSmsGroupId(rs.getString("sms_group_id"));
							logEntity.setBatchId(rs.getString("batch_id"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							logEntity.setStatusResetCount(rs.getInt("status_reset_count"));
							return logEntity;
						}
					}, args);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void updateSentMessagesStatusByGroupIdNumber(String phoneNumber, ContactStatus contactStatus,
			String smsGroupId) {
		String sql = "UPDATE sent_messages  SET status=? WHERE contact=? and sms_group_id=?;";
		jdbcTemplate.update(sql, contactStatus.toDbString(), phoneNumber, smsGroupId);
	}
	
	@Override
	public void updateStatusResetCount(int resetCount, String phoneNumber, String smsGroupId) {
		String sql = "UPDATE sent_messages  SET status_reset_count=? WHERE contact=? and sms_group_id=?;";
		jdbcTemplate.update(sql, resetCount, phoneNumber, smsGroupId);
	}

	@Override
	public int[] persistBatchSentMessages(List<SentMessages> msgToSend, boolean comingFromDaemon) {
		try {
			return jdbcTemplate.batchUpdate(
					"INSERT INTO sent_messages (user_id, load_request_id, contact, message_body, status, contact_type, created_by, modified_by, batch_id) "
							+ " VALUES(?,?,?,?,?,?,?,?,?)",
					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							SentMessages logEntity = msgToSend.get(i);
							ps.setLong(1, logEntity.getUserId());
							if (logEntity.getLoadRequestId() <= 0) {
								ps.setNull(2, java.sql.Types.BIGINT);
							} else {
								ps.setLong(2, logEntity.getLoadRequestId());
							}
							ps.setString(3, logEntity.getContact());
							ps.setString(4, logEntity.getMessageBody());
							ps.setString(5, logEntity.getStatus().toDbString());
							ps.setString(6, logEntity.getType().toDbString());
							if (comingFromDaemon) {
								ps.setNull(7, java.sql.Types.BIGINT);
								ps.setNull(8, java.sql.Types.BIGINT);
							} else {
								ps.setLong(7, logEntity.getCreatedBy());
								ps.setLong(8, logEntity.getModifiedBy());
							}
							ps.setString(9, logEntity.getBatchId());
						}

						@Override
						public int getBatchSize() {
							return msgToSend.size();
						}
					});
		} catch (DataAccessException e) {
			log.error("Exception occurred while inserting batch to send message table", e);
			return new int[0];
		}
	}

	@Override
	public List<Long> getAllSentMessagesLRId(long loadRequestId) {
		try {
			return jdbcTemplate.query("SELECT distinct user_id from sent_messages where load_request_id =?",
					new RowMapper<Long>() {
						@Override
						public Long mapRow(ResultSet rs, int rownumber) throws SQLException {
							return rs.getLong("user_id");
						}
					}, loadRequestId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
