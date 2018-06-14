package com.fw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.fw.dao.IFeedbackTypesManager;
import com.fw.domain.FeedbackTypes;
import com.fw.exceptions.APIExceptions;

@Repository
public class FeedbackTypesManagerImpl implements IFeedbackTypesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(FeedbackTypesManagerImpl.class);
	
	@Override
	public FeedbackTypes persistFeedbackTypes(FeedbackTypes logEntity)   throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO feedback_types (title, description, created_by, modified_by, is_deleted) " + " VALUES(?,?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "feedback_type_id" });
					ps.setString(1, logEntity.getTitle());
					ps.setString(2, logEntity.getDescription());
					ps.setLong(3, logEntity.getCreatedBy());
					ps.setLong(4, logEntity.getModifiedBy());
					ps.setBoolean(5, false);
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setFeedbackTypesId(logEntityId);
			return logEntity;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}

	}

	@Override
	public void updateFeedbackTypesById(FeedbackTypes logEntity)   throws APIExceptions {
		String sql = "UPDATE feedback_types  SET title=?, description=?, "
				+ "created_by=?,modified_by=? ,  WHERE feedback_type_id=?;";
		jdbcTemplate.update(sql, logEntity.getTitle(), logEntity.getDescription(), logEntity.getCreatedBy(),
				logEntity.getModifiedBy(), logEntity.getFeedbackTypesId());
	}

	@Override
	public void deleteFeedbackTypesById(FeedbackTypes Id)  throws APIExceptions {

		String sql = "UPDATE feedback_types set is_deleted=true where feedback_type_id=?  ;";
		try {
			jdbcTemplate.update(sql, Id);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public List<FeedbackTypes> getAllFeedbackTypesRowMapper()  throws APIExceptions {
		return jdbcTemplate.query("select * from feedback_types where COALESCE(is_deleted, FALSE) = FALSE;", new RowMapper<FeedbackTypes>() {
			@Override
			public FeedbackTypes mapRow(ResultSet rs, int rownumber) throws SQLException {
				FeedbackTypes logEntity = new FeedbackTypes();
				logEntity.setFeedbackTypesId(rs.getLong("feedback_type_id"));
				logEntity.setTitle(rs.getString("title"));
				logEntity.setDescription(rs.getString("description"));
				logEntity.setCreatedBy(rs.getLong("created_by"));
				logEntity.setModifiedBy(rs.getLong("modified_by"));
				logEntity.setCreatedDate(rs.getTimestamp("created_date"));
				logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
				return logEntity;
			}
		});
	}

	@Override
	public FeedbackTypes getFeedbackTypesById(long logEntityId)  throws APIExceptions {
		try {
			return jdbcTemplate.queryForObject("select * from feedback_types where feedback_type_id=? and COALESCE(is_deleted, FALSE) = FALSE;",
					new RowMapper<FeedbackTypes>() {
						@Override
						public FeedbackTypes mapRow(ResultSet rs, int rownumber) throws SQLException {
							FeedbackTypes logEntity = new FeedbackTypes();
							logEntity.setFeedbackTypesId(rs.getLong("feedback_type_id"));
							logEntity.setTitle(rs.getString("title"));
							logEntity.setDescription(rs.getString("description"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							return logEntity;
						}
					}, logEntityId);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
