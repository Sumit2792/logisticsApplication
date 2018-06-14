package com.fw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.beans.FeedbackBean;
import com.fw.dao.IFeedbackManager;
import com.fw.domain.Feedback;
import com.fw.exceptions.APIExceptions;
import com.fw.utils.LocalUtils;

@Repository
public class FeedbackManagerImpl implements IFeedbackManager {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger log = Logger.getLogger(FeedbackManagerImpl.class);

	/**
	 * 
	 */
	private final static Logger logger = Logger.getLogger(FeedbackManagerImpl.class);

	@Override
	public long persistFeedbackInfo(Feedback feedback) throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO feedbacks(load_request_id, note, giving_user_id, for_user_id, rating, feedback_type_id, bid_id, callback_time, "
				+ "created_by, modified_by)	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		long feedbackId = 0;
		LocalDateTime callbackTime = (feedback.getCallBackTime() == null) ? null : LocalDateTime.ofInstant(feedback.getCallBackTime().toInstant(), ZoneId.systemDefault());

		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, new String[] { "feedback_id" });
				if (feedback.getLoadRequestId() <= 0) {
					ps.setNull(1, Types.BIGINT);
				} else {
					ps.setLong(1, feedback.getLoadRequestId());
				}
				ps.setString(2, feedback.getNote());
				ps.setLong(3, feedback.getGivingUserId());
				if (feedback.getForUserId() <= 0) {
					ps.setNull(4, Types.BIGINT);// null for system
				} else {
					ps.setLong(4, feedback.getForUserId());
				}
				ps.setDouble(5, feedback.getRating());
				ps.setLong(6, feedback.getFeedbackTypeId());
				ps.setLong(7, feedback.getBidId());
				if (callbackTime == null) {
					ps.setNull(8, Types.TIMESTAMP);
				} else {
					ps.setTimestamp(8, Timestamp.valueOf(callbackTime));
				}
				ps.setLong(9, feedback.getCreatedBy());
				ps.setLong(10, feedback.getModifiedBy());
				return ps;
			}
		}, requestKeyHolder);

		feedbackId = requestKeyHolder.getKey().longValue();
		if (feedbackId > 0) {
			return feedbackId;
		} else {
			throw new APIExceptions("internal server error: failed to add your feedback.");
		}
	}

	@Override
	public FeedbackBean getFeedbackInfoById(long id) throws SQLException {
		try {
			return jdbcTemplate.queryForObject("select f.* , ft.title as \"feedbackType\" from feedbacks f JOIN feedback_types ft ON ft.feedback_type_id=f.feedback_type_id where f.feedback_id = ?;", new RowMapper<FeedbackBean>() {
				@Override
				public FeedbackBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					FeedbackBean feedback = new FeedbackBean();
					feedback.setFeedbackId(rs.getInt("feedback_id"));
					feedback.setLoadRequestId(rs.getLong("load_request_id"));
					feedback.setBidId(rs.getLong("bid_id"));
					feedback.setNote(rs.getString("note"));
					feedback.setGivingUserId(rs.getLong("giving_user_id"));
					feedback.setForUserId(rs.getLong("for_user_id"));
					feedback.setRating(rs.getDouble("rating"));
					feedback.setFeedbackType(rs.getString("feedbackType"));
					feedback.setCallBackTime(rs.getDate("callback_time"));
					feedback.setCreatedBy(rs.getLong("created_by"));
					feedback.setModifiedBy(rs.getLong("modified_by"));
					feedback.setCreatedDate(rs.getDate("created_date"));
					feedback.setModifiedDate(rs.getDate("modified_date"));

					return feedback;
				}
			}, id);
		} catch (DataAccessException e) {
			try {
				// logger.info(LocalUtils.getStringLocale(language.getLanguageHeader(),
				// "invalidRequest"));
				logger.info(LocalUtils.getStringLocale("mlogistics_locale", "invalidRequest"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<FeedbackBean> getUserFeedbacksByUserId(Long fromUserId) {

		String sql = "select f.* , ft.title as \"feedbackType\" from feedbacks f JOIN feedback_types ft ON ft.feedback_type_id=f.feedback_type_id where f.for_user_id=?;";
		try {
			return jdbcTemplate.query(sql, new RowMapper<FeedbackBean>() {
				@Override
				public FeedbackBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					FeedbackBean feedback = new FeedbackBean();
					feedback.setFeedbackId(rs.getInt("feedback_id"));
					feedback.setLoadRequestId(rs.getInt("load_request_id"));
					feedback.setBidId(rs.getLong("bid_id"));
					feedback.setNote(rs.getString("note"));
					feedback.setGivingUserId(rs.getLong("giving_user_id"));
					feedback.setForUserId(rs.getLong("for_user_id"));
					feedback.setRating(rs.getDouble("rating"));
					feedback.setFeedbackType(rs.getString("feedbackType"));
					feedback.setCallBackTime(rs.getDate("callback_time"));
					feedback.setCreatedBy(rs.getInt("created_by"));
					feedback.setModifiedBy(rs.getInt("modified_by"));
					feedback.setCreatedDate(rs.getDate("created_date"));
					feedback.setModifiedDate(rs.getDate("modified_date"));
					return feedback;
				}
			}, fromUserId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}
}
