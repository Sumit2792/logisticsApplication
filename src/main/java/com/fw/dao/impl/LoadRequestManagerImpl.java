package com.fw.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.beans.AddLoadRequest;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.config.Constants;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.TruckType;
import com.fw.exceptions.APIExceptions;

@Repository
public class LoadRequestManagerImpl implements ILoadRequestManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	IUserManager userManager;

	Logger log = Logger.getLogger(LoadRequestManagerImpl.class);

	@Override
	public void updateLoadRequestById(AddLoadRequest loadRequest) throws APIExceptions {
		try {
			String sql = "UPDATE load_requests  SET bidding_end_datetime=?, bidding_start_datetime=?, insurance_needed=?, modified_by=?, truck_type=? where load_request_id=?;";
			jdbcTemplate.update(sql, loadRequest.getBidEnd(), loadRequest.getBidStart(),
					loadRequest.isInsuranceNeeded(), loadRequest.getModifiedBy(), loadRequest.getTruckType(),
					loadRequest.getLoadRequestId());
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error: Failed to update load request");
		}
	}

	@Override
	public void deleteLoadRequest(long loadRequestId) throws APIExceptions {

		try {
			String sql = "UPDATE  load_requests set is_deleted=true where load_request_id=?;";
			jdbcTemplate.update(sql, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error : Failed to delete load request.");
		}
	}

	@Override
	public List<LoadRequest> getAllLoadRequest() {
		try {
			return jdbcTemplate.query("select * from load_requests;", new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					String truckType = rs.getString("truck_type");
					if (truckType != null) {
						lr.setTruckType(TruckType.fromString(rs.getString("truck_type")));
					}
					return lr;
				}
			});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public LoadRequest getLoadRequestById(Long id) throws APIExceptions {

		try {
			return jdbcTemplate.queryForObject("select * from load_requests where load_request_id=? ;",
					new RowMapper<LoadRequest>() {
						@Override
						public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
							LoadRequest lr = new LoadRequest();
							lr.setLoadRequestId(rs.getLong("load_request_id"));
							lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
							lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
							lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
							lr.setUserId(rs.getLong("user_id"));
							lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
							lr.setCreatedBy(rs.getLong("created_by"));
							lr.setModifiedBy(rs.getLong("modified_by"));
							lr.setCreatedDate(rs.getTimestamp("created_date"));
							lr.setModifiedDate(rs.getTimestamp("modified_date"));
							String truckType = rs.getString("truck_type");
							if (truckType != null) {
								lr.setTruckType(TruckType.fromString(rs.getString("truck_type")));
							}
							return lr;
						}
					}, id);
		} catch (EmptyResultDataAccessException e) {
			log.error("No request found. " + e.getMessage(), e);
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			throw new APIExceptions("duplicate request found.");
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}

	}

	@Override
	public LoadRequest getLoadRequestByIdAndLoaderId(Long userId, Long loadRequestId) {

		return jdbcTemplate.queryForObject("select * from load_requests where load_request_id=? and user_id=?;",
				new RowMapper<LoadRequest>() {
					@Override
					public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
						LoadRequest lr = new LoadRequest();
						lr.setLoadRequestId(rs.getLong("load_request_id"));
						lr.setBiddingEndDatetime(rs.getTimestamp("bid_end_datetime"));
						lr.setBiddingStartDatetime(rs.getTimestamp("bid_start_datetime"));
						lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
						lr.setUserId(rs.getLong("user_id"));
						lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
						lr.setCreatedBy(rs.getLong("created_by"));
						lr.setModifiedBy(rs.getLong("modified_by"));
						lr.setCreatedDate(rs.getTimestamp("created_date"));
						lr.setModifiedDate(rs.getTimestamp("modified_date"));
						String truckType = rs.getString("truck_type");
						if (truckType != null && !"".equals(truckType))
							lr.setTruckType(TruckType.fromString(truckType));
						return lr;
					}
				}, loadRequestId, userId);
	}

	@Override
	public long insertLoadRequest(AddLoadRequest loadRequest) throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO load_requests (user_id,load_request_status,insurance_needed,created_by,modified_by,is_deleted,bidding_start_datetime,bidding_end_datetime,truck_type,user_attempts_json) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?);";
		Long loadRequestId;
		try {
			LocalDateTime bidtStartTime = (loadRequest.getBidStart() == null) ? null
					: LocalDateTime.ofInstant(loadRequest.getBidStart().toInstant(), ZoneId.systemDefault());
			LocalDateTime bidtEndTime = (loadRequest.getBidEnd() == null) ? null
					: LocalDateTime.ofInstant(loadRequest.getBidEnd().toInstant(), ZoneId.systemDefault());
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "load_request_id" });
					ps.setLong(1, loadRequest.getUserId());
					ps.setString(2, loadRequest.getStatus().getValue());
					ps.setBoolean(3, loadRequest.isInsuranceNeeded());
					ps.setLong(4, loadRequest.getCreatedBy());
					ps.setLong(5, loadRequest.getModifiedBy());
					ps.setBoolean(6, false);
					ps.setTimestamp(7, (bidtStartTime == null) ? null : Timestamp.valueOf(bidtStartTime));
					ps.setTimestamp(8, (bidtEndTime == null) ? null : Timestamp.valueOf(bidtEndTime));
					ps.setString(9, loadRequest.getTruckType());
					ps.setString(10, loadRequest.getUserAttemptsJson());
					return ps;
				}
			}, requestKeyHolder);
			loadRequestId = requestKeyHolder.getKey().longValue();
			if (loadRequestId != null && loadRequestId != 0) {
				return loadRequestId.longValue();
			} else {
				throw new APIExceptions("internal server error: failed to create load request.");
			}

		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error: Failed to add load request.");
		}
	}

	@Override
	public void updateLoadRequestStatus(LoadRequestStatusBean loadStatus) throws APIExceptions {

		try {
			String sql = "UPDATE  load_requests set load_request_status=? , modified_by=? where load_request_id=?;";
			jdbcTemplate.update(sql, loadStatus.getStatus(), loadStatus.getModifiedBy(), loadStatus.getLoadRequestId());
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error : Failed to change request status." + e.getMessage());
		}
	}

	@Override
	public List<LoadRequest> getAllLoadRequestByStatus(LoadRequestStatus statusValue) {
		try {
			String sql = "SELECT * FROM mlogistics.load_requests AS lr JOIN mlogistics.load_request_details "
					+ "as lrd ON lr.load_request_id = lrd.load_request_id WHERE lr.is_deleted = false "
					+ "AND lr.load_request_status IN ('" + statusValue
					+ "') AND (lr.created_date <= NOW() + INTERVAL '15 min' "
					+ "OR (lrd.shipping_start_datetime >= NOW() AND lrd.shipping_start_datetime <= NOW() + INTERVAL '7 days'));";
			return jdbcTemplate.query(sql, new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					String truckType = rs.getString("truck_type");
					if (truckType != null) {
						lr.setTruckType(TruckType.fromString(rs.getString("truck_type")));
					}
					return lr;
				}
			});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<LoadRequest> getAllLoadRequestByStatus() {
		try {
			return jdbcTemplate.query(

					"SELECT DISTINCT lr.load_request_id FROM mlogistics.load_requests AS lr JOIN mlogistics.load_request_details as lrd ON "
							+ "lr.load_request_id = lrd.load_request_id LEFT JOIN mlogistics.bid AS bd ON "
							+ "lr.load_request_id = bd.load_request_id WHERE lr.is_deleted = false "
							+ "AND lr.load_Request_status IN ('REQUESTED','BIDDING_IN_PROGRESS') "
							+ "AND (lr.created_date <= NOW() - INTERVAL '15 min' OR (lrd.shipping_start_datetime >= NOW() "
							+ "AND lrd.shipping_start_datetime <= NOW() + INTERVAL '9 days')) "
							+ "GROUP BY lr.load_request_id HAVING count(bd.bid_id) < '"
							+ Constants.LOAD_REQUEST_ALGORITHM_DAEMON_BID_COUNT + "';",
					new RowMapper<LoadRequest>() {
						@Override
						public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
							LoadRequest lr = new LoadRequest();
							lr.setLoadRequestId(rs.getLong("load_request_id"));
							return lr;
						}
					});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<Long> getAllBookedRequestIdsByUserId(long userId) {
		String sql = " SELECT load_request_id from load_requests  " + " where user_id = ? and load_request_status='"
				+ LoadRequestStatus.BOOKED.getValue() + "'";
		try {
			return jdbcTemplate.query(sql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rownumber) throws SQLException {
					return rs.getLong(1);
				}
			}, userId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}
}
