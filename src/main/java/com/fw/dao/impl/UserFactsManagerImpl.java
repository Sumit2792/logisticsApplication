package com.fw.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fw.dao.IUserFactsManager;
import com.fw.domain.UserFacts;
import com.fw.enums.Facts;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

@Repository
public class UserFactsManagerImpl implements IUserFactsManager {

	private Logger logger = Logger.getLogger(UserFactsManagerImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void persistUserFacts(final List<UserFacts> userFacts, boolean forNewNumber) throws APIExceptions {

		try {
			String sql = "INSERT INTO user_facts (user_id, fact_name, fact_value, created_by, modified_by,is_deleted) "
					+ " VALUES(?,?,?,?,?,?);";

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, userFacts.get(i).getUserId());
					ps.setString(2, userFacts.get(i).getFact());
					ps.setString(3, userFacts.get(i).getValue());
					if (forNewNumber) {
						ps.setNull(4, Types.BIGINT);
						ps.setNull(5, Types.BIGINT);
					} else {
						ps.setLong(4, userFacts.get(i).getCreatedBy());
						ps.setLong(5, userFacts.get(i).getModifiedBy());
					}
					ps.setBoolean(6, forNewNumber);
				}

				@Override
				public int getBatchSize() {
					return userFacts.size();
				}
			});
			logger.info(rowUpdated.length + " rows updated.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error.");
		}

	}

	@Override
	public void updateUserFactsByUserId(List<UserFacts> userFacts) throws APIExceptions {
		String sql = "UPDATE user_facts  SET  fact_name=?, fact_value=?,  "
				+ " modified_by=?  WHERE user_facts_id=? and user_id=? ;";
		try {
			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, userFacts.get(i).getFact());
					ps.setString(2, userFacts.get(i).getValue());
					if (userFacts.get(i).getModifiedBy() <= 0) {
						ps.setNull(3, Types.BIGINT);
					} else {
						ps.setLong(3, userFacts.get(i).getModifiedBy());
					}
					ps.setLong(4, userFacts.get(i).getUserFactsId());
					ps.setLong(5, userFacts.get(i).getUserId());
				}

				@Override
				public int getBatchSize() {
					return userFacts.size();
				}
			});
			logger.info(rowUpdated.length + " rows updated.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error.");
		}
	}

	@Override
	public void deleteUserFactsById(long id, boolean purge) throws APIExceptions {
		try {
			String sql = "UPDATE user_facts  SET is_deleted=true where user_facts_id=?  ;";
			if (purge) {
				sql = "delete from user_facts where user_facts_id=? ;";
			}
			jdbcTemplate.update(sql, id);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
		}

	}

	@Override
	public UserFacts getUserFactssById(long logEntityId) throws APIExceptions {
		try {
			return jdbcTemplate.queryForObject(
					"select * from user_facts where user_facts_id=? and COALESCE(is_deleted, FALSE) = FALSE",
					new RowMapper<UserFacts>() {
						@Override
						public UserFacts mapRow(ResultSet rs, int rownumber) throws SQLException {
							UserFacts logEntity = new UserFacts();
							logEntity.setUserFactsId(rs.getLong("user_facts_id"));
							logEntity.setUserId(rs.getLong("user_id"));
							logEntity.setFact(rs.getString("fact_name"));
							logEntity.setValue(rs.getString("fact_value"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							return logEntity;
						}
					}, logEntityId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<UserFacts> getUserFactsByUserId(Long userId) throws APIExceptions {
		try {
			String sql = "select * from user_facts where user_id=? and COALESCE(is_deleted, FALSE) = FALSE";
			return jdbcTemplate.query(sql, new RowMapper<UserFacts>() {
				@Override
				public UserFacts mapRow(ResultSet rs, int rownumber) throws SQLException {
					UserFacts userFact = new UserFacts();
					userFact.setUserFactsId(rs.getLong("user_facts_id"));
					userFact.setUserId(rs.getLong("user_id"));
					userFact.setFact(rs.getString("fact_name"));
					userFact.setValue(rs.getString("fact_value"));
					userFact.setCreatedBy(rs.getLong("created_by"));
					userFact.setModifiedBy(rs.getLong("modified_by"));
					userFact.setCreatedDate(rs.getTimestamp("created_date"));
					userFact.setModifiedDate(rs.getTimestamp("modified_date"));
					return userFact;
				}
			}, userId);
		} catch (EmptyResultDataAccessException e) {
			logger.info("No user facts found for user id:" + userId);
			return new ArrayList<UserFacts>(); // return empty list
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error.");
		}
	}

	@Override
	public List<UserFacts> getUserFactsByUserIds(List<Long> userIds) throws APIExceptions {
		try {
			MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource("ids", userIds);
			return namedParameterJdbcTemplate.query(
					"select * from user_facts where user_id in (:ids) and COALESCE(is_deleted, FALSE) = FALSE",
					sqlParameterSource, new RowMapper<UserFacts>() {
						@Override
						public UserFacts mapRow(ResultSet rs, int rownumber) throws SQLException {
							UserFacts userFact = new UserFacts();
							userFact.setUserFactsId(rs.getLong("user_facts_id"));
							userFact.setUserId(rs.getLong("user_id"));
							userFact.setFact(rs.getString("fact_name"));
							userFact.setValue(rs.getString("fact_value"));
							userFact.setCreatedBy(rs.getLong("created_by"));
							userFact.setModifiedBy(rs.getLong("modified_by"));
							userFact.setCreatedDate(rs.getTimestamp("created_date"));
							userFact.setModifiedDate(rs.getTimestamp("modified_date"));
							return userFact;
						}
					});
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public UserFacts getContactNumber(String userContactNumber) throws InvalidUsernameException {
		try {
			return jdbcTemplate.queryForObject(
					"select * from user_facts where fact_value=? and COALESCE(is_deleted, FALSE) = FALSE",
					new RowMapper<UserFacts>() {
						@Override
						public UserFacts mapRow(ResultSet rs, int rownumber) throws SQLException {
							UserFacts logEntity = new UserFacts();
							logEntity.setUserFactsId(rs.getLong("user_facts_id"));
							logEntity.setUserId(rs.getLong("user_id"));
							logEntity.setFact(rs.getString("fact_name"));
							logEntity.setValue(rs.getString("fact_value"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							return logEntity;
						}
					}, userContactNumber);
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<UserFacts> getUserFactsByUserIdAndFactName(Long userId, String factName) throws APIExceptions {
		try {
			String sql = "select * from user_facts where user_id=? and fact_name=?";
			return jdbcTemplate.query(sql, new RowMapper<UserFacts>() {
				@Override
				public UserFacts mapRow(ResultSet rs, int rownumber) throws SQLException {
					UserFacts userFact = new UserFacts();
					userFact.setUserFactsId(rs.getLong("user_facts_id"));
					userFact.setUserId(rs.getLong("user_id"));
					userFact.setFact(rs.getString("fact_name"));
					userFact.setValue(rs.getString("fact_value"));
					userFact.setCreatedBy(rs.getLong("created_by"));
					userFact.setModifiedBy(rs.getLong("modified_by"));
					userFact.setCreatedDate(rs.getTimestamp("created_date"));
					userFact.setModifiedDate(rs.getTimestamp("modified_date"));
					return userFact;
				}
			}, userId, factName);
		} catch (EmptyResultDataAccessException e) {
			logger.info("No fact_name=" + factName + " found for user id:" + userId);
			return new ArrayList<UserFacts>(); // return empty list
		} catch (DataAccessException e) {
			logger.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error.");
		}
	}

	@Override
	public boolean verifyUserEmailExist(long userId, String userFact) throws APIExceptions {
		List<String> userEmailDB = jdbcTemplate
				.queryForList("select fact_value from user_facts where fact_name = '" + userFact + "' AND user_id=" + userId + "", String.class);
		if (userEmailDB.size() > 0)
			return true;
		else if (userEmailDB.size() > 1)
			throw new APIExceptions("Duplicate user Email.");
		else
			return false;

	}

	@Override
	public void updateUserEmail(long userId, String userEmail, String userFact) {
		String sql = "UPDATE user_facts  SET  fact_value=?,  " + " modified_by=?  WHERE user_id=? and fact_name=?;";
		jdbcTemplate.update(sql, userEmail, userId, userId, userFact);

	}

	@Override
	public void persistUserEmail(long userId, String userEmail, String userFact) {
		try {
			String sql = "INSERT INTO user_facts (user_id, fact_name, fact_value, created_by, modified_by ) "
					+ " VALUES(?,?,?,?,?);";
			jdbcTemplate.update(sql, userId, Facts.EMAIL_ID.toDbString(), userEmail, userId, userId);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

	}

}
