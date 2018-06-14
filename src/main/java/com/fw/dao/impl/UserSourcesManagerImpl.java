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

import com.fw.dao.IUserSourcesManager;
import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;

@Repository
public class UserSourcesManagerImpl implements IUserSourcesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(UserSourcesManagerImpl.class);

	@Override
	public UserSources persistUserSources(UserSources logEntity) throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO user_sources (user_id_start, user_id_end, source, count, amount, note,"
					+ " created_by, modified_by) VALUES(?,?,?,?,?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "user_sources_id" });
					ps.setLong(1, logEntity.getUserIdStart());
					ps.setLong(2, logEntity.getUserIdEnd());
					ps.setString(3, logEntity.getSource());
					ps.setInt(4, logEntity.getCount());
					ps.setDouble(5, logEntity.getAmount());
					ps.setString(6, logEntity.getNote());
					ps.setLong(7, logEntity.getCreatedBy());
					ps.setLong(8, logEntity.getModifiedBy());
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setUserSourceid(logEntityId);
			return logEntity;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}

	}

	@Override
	public void updateUserSourcesById(UserSources logEntity) throws APIExceptions {
		String sql = "UPDATE user_sources  SET user_id_start=?,  user_id_end=?, source=?, count=?, amount=?, note=?, "
				+ "created_by=?, modified_by=?  WHERE user_sources_id=?;";
		jdbcTemplate.update(sql, logEntity.getUserIdStart(), logEntity.getUserIdEnd(), logEntity.getSource(),
				logEntity.getCount(), logEntity.getAmount(), logEntity.getNote(), logEntity.getCreatedBy(),
				logEntity.getModifiedBy(), logEntity.getUserSourceid());
	}

	@Override
	public void deleteUserSourcesById(UserSources Id) throws APIExceptions {

		String sql = "DELETE from user_sources where user_sources_id=?  ;";
		try {
			jdbcTemplate.update(sql, Id);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public List<UserSources> getAllUserSourcesRowMapper() throws APIExceptions {
		return jdbcTemplate.query("select * from user_sources;", new RowMapper<UserSources>() {
			@Override
			public UserSources mapRow(ResultSet rs, int rownumber) throws SQLException {
				UserSources logEntity = new UserSources();
				logEntity.setUserSourceid(rs.getLong("user_sources_id"));
				logEntity.setUserIdStart(rs.getLong("user_id_start"));
				logEntity.setUserIdEnd(rs.getLong("user_id_end"));
				logEntity.setSource(rs.getString("source"));
				logEntity.setCount(rs.getInt("count"));
				logEntity.setAmount(rs.getLong("amount"));
				logEntity.setAcquiredDate(rs.getDate("acquired_date"));
				logEntity.setNote(rs.getString("note"));
				logEntity.setCreatedBy(rs.getLong("created_by"));
				logEntity.setModifiedBy(rs.getLong("modified_by"));
				logEntity.setCreatedDate(rs.getTimestamp("created_date"));
				logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
				return logEntity;
			}
		});
	}

	@Override
	public UserSources getUserSourcesById(long logEntityId) throws APIExceptions {
		try {
			return jdbcTemplate.queryForObject("select * from user_sources where user_sources_id=?",
					new RowMapper<UserSources>() {
						@Override
						public UserSources mapRow(ResultSet rs, int rownumber) throws SQLException {
							UserSources logEntity = new UserSources();
							logEntity.setUserSourceid(rs.getLong("user_sources_id"));
							logEntity.setUserIdStart(rs.getLong("user_id_start"));
							logEntity.setUserIdEnd(rs.getLong("user_id_end"));
							logEntity.setSource(rs.getString("source"));
							logEntity.setCount(rs.getInt("count"));
							logEntity.setAmount(rs.getLong("amount"));
							logEntity.setAcquiredDate(rs.getDate("acquired_date"));
							logEntity.setNote(rs.getString("note"));
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
