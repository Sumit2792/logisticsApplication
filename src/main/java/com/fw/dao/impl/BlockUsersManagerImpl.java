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

import com.fw.dao.IBlockUsersManager;
import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;

@Repository
public class BlockUsersManagerImpl implements IBlockUsersManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(BlockUsersManagerImpl.class);
	
	@Override
	public BlockUsers persistBlockUsers(BlockUsers logEntity)   throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO blocked_users (user_id, reason, created_by, modified_by) " + " VALUES(?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "blocked_users_id" });
					ps.setLong(1, logEntity.getUserId());
					ps.setString(2, logEntity.getReason());
					ps.setLong(7, logEntity.getCreatedBy());
					ps.setLong(8, logEntity.getModifiedBy());
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setBlockedUsersId(logEntityId);
			return logEntity;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}

	}

	@Override
	public void updateBlockUsersById(BlockUsers logEntity)   throws APIExceptions {
		String sql = "UPDATE blocked_users  SET user_id=?, reason=?, "
				+ "created_by=?,modified_by=? ,  WHERE blocked_users_id=?;";
		jdbcTemplate.update(sql, logEntity.getUserId(), logEntity.getReason(), logEntity.getCreatedBy(),
				logEntity.getModifiedBy(), logEntity.getBlockedUsersId());
	}

	@Override
	public void deleteBlockUsersById(BlockUsers Id)  throws APIExceptions {

		String sql = "DELETE from blocked_users where blocked_users_id=?  ;";
		try {
			jdbcTemplate.update(sql, Id);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public List<BlockUsers> getAllBlockUsersRowMapper()  throws APIExceptions {
		return jdbcTemplate.query("select * from blocked_users;", new RowMapper<BlockUsers>() {
			@Override
			public BlockUsers mapRow(ResultSet rs, int rownumber) throws SQLException {
				BlockUsers logEntity = new BlockUsers();
				logEntity.setBlockedUsersId(rs.getLong("blocked_users_id"));
				logEntity.setUserId(rs.getLong("user_id"));
				logEntity.setReason(rs.getString("reason"));
				logEntity.setCreatedBy(rs.getLong("created_by"));
				logEntity.setModifiedBy(rs.getLong("modified_by"));
				logEntity.setCreatedDate(rs.getTimestamp("created_date"));
				logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
				return logEntity;
			}
		});
	}

	@Override
	public BlockUsers getBlockUsersById(long logEntityId)  throws APIExceptions {
		try {
			return jdbcTemplate.queryForObject("select * from blocked_users where blocked_users_id=?",
					new RowMapper<BlockUsers>() {
						@Override
						public BlockUsers mapRow(ResultSet rs, int rownumber) throws SQLException {
							BlockUsers logEntity = new BlockUsers();
							logEntity.setBlockedUsersId(rs.getLong("blocked_users_id"));
							logEntity.setUserId(rs.getLong("user_id"));
							logEntity.setReason(rs.getString("reason"));
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
