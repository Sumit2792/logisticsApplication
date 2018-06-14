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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.beans.NotesBean;
import com.fw.beans.UserDetailsBean;
import com.fw.dao.IBidManager;
import com.fw.dao.IBidNotesManager;
import com.fw.dao.IUserManager;
import com.fw.domain.Bid;
import com.fw.domain.Users;
import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.utils.BidUtils;

@Repository
public class BidManagerImpl implements IBidManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	IUserManager userManager;

	@Autowired
	IBidNotesManager notesManager;

	private Logger log = Logger.getLogger(BidManagerImpl.class);

	@Override
	public AddBidsBean addBid(AddBidsBean bid) throws APIExceptions {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO bid (load_request_id,bidder_id,amount,currency,bid_status,faber_charges,created_by,modified_by,expected_delievery_time,is_deleted,user_attempts_json) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?);";
			LocalDateTime deliveredTime = (bid.getDeliveredTime() == null) ? null
					: LocalDateTime.ofInstant(bid.getDeliveredTime().toInstant(), ZoneId.systemDefault());
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "bid_id" });
					ps.setLong(1, bid.getLoadRequestId());
					ps.setLong(2, bid.getBidderUserId());
					ps.setDouble(3, bid.getAmount());
					ps.setString(4, bid.getCurrency().toDbString());
					ps.setString(5, bid.getStatus().toDbString());
					ps.setDouble(6, bid.getFaberCharges());
					ps.setLong(7, bid.getCreatedBy());
					ps.setLong(8, bid.getModifiedBy());
					if (deliveredTime == null)
						ps.setNull(9, Types.TIMESTAMP);
					else
						ps.setTimestamp(9, Timestamp.valueOf(deliveredTime));
					ps.setBoolean(10, false);
					ps.setString(11, bid.getUserAttemptsJson());
					return ps;
				}
			}, requestKeyHolder);
			long bidId = requestKeyHolder.getKey().longValue();
			bid.setBidId(bidId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Failed to add bid due to internal server error.");
		}
		return bid;

	}

	@Override
	public void updateBidById(AddBidsBean bid) throws APIExceptions {
		try {

			String sql = "UPDATE bid  SET faber_charges=?, amount=?,modified_by=?,expected_delievery_time=? WHERE bid_id=?;";
			int result = jdbcTemplate.update(sql, bid.getFaberCharges(), bid.getAmount(), bid.getModifiedBy(),
					bid.getDeliveredTime(), bid.getBidId());

			if (result != 1) {
				throw new APIExceptions("Internal server error : failed to update bid.");
			}
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Failed to upddate bid due to internal server error.");
		}
	}

	@Override
	public void deleteBidById(long bidId) throws APIExceptions {

		try {
			String sql2 = "UPDATE bid  SET is_deleted=true where bid_id=? ";
			int result = jdbcTemplate.update(sql2, bidId);

			if (result != 1) {
				throw new APIExceptions("Internal server error : failed to delete bid id:" + bidId);
			}
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Failed to delete your bid due to internal server error.");
		}

	}

	@Override
	public BidsBean getBidById(long bidId) {
		try {
			return jdbcTemplate.queryForObject("select * from bid  where bid_id=?", new RowMapper<BidsBean>() {
				@Override
				public BidsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					BidsBean bid = new BidsBean();
					bid.setBidId(rs.getLong("bid_id"));
					bid.setAmount(rs.getDouble("amount"));
					bid.setBidderUserId(rs.getLong("bidder_id"));
					bid.setFaberCharges(rs.getDouble("faber_charges"));
					bid.setCreatedBy(rs.getLong("created_by"));
					bid.setCreatedDate(rs.getTimestamp("created_date"));
					bid.setCurrency(CurrencyCodes.fromString(rs.getString("currency")));
					bid.setLoadRequestId(rs.getLong("load_request_id"));
					bid.setModifiedBy(rs.getLong("modified_by"));
					bid.setModifiedDate(rs.getTimestamp("modified_date"));
					bid.setStatus(BidStatus.fromString(rs.getString("bid_status")));
					bid.setDeliveredTime(rs.getTimestamp("expected_delievery_time"));
					return bid;
				}
			}, bidId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<BidsBean> getBidsByLoadRequestId(long loadRequestId) {
		try {
			return jdbcTemplate.query("select * from bid  where load_request_id=? ;", new RowMapper<BidsBean>() {
				@Override
				public BidsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					BidsBean bid = new BidsBean();
					bid.setBidId(rs.getLong("bid_id"));
					bid.setAmount(rs.getDouble("amount"));
					bid.setBidderUserId(rs.getLong("bidder_id"));
					bid.setFaberCharges(rs.getDouble("faber_charges"));
					bid.setCreatedBy(rs.getLong("created_by"));
					bid.setCreatedDate(rs.getTimestamp("created_date"));
					bid.setCurrency(CurrencyCodes.fromString(rs.getString("currency")));
					bid.setLoadRequestId(rs.getLong("load_request_id"));
					bid.setModifiedBy(rs.getLong("modified_by"));
					bid.setModifiedDate(rs.getTimestamp("modified_date"));
					bid.setStatus(BidStatus.fromString(rs.getString("bid_status")));
					bid.setDeliveredTime(rs.getTimestamp("expected_delievery_time"));
					bid.setOnlinePaymentCharges(
							BidUtils.getOnlineCharges(rs.getDouble("amount"), rs.getDouble("faber_charges")));
					List<NotesBean> notes = notesManager.getAllNotesByBidId(rs.getLong("bid_id"));
					bid.setNotes(notes);
					try {
						UserDetailsBean userDetials = userManager.getUserInfoByUserId(rs.getLong("bidder_id"));
						if (userDetials != null) {
							bid.setBidderRating(userDetials.getRating());
						}
					} catch (InvalidUsernameException e) {
						log.error("Error : " + e.getMessage(), e);
					}
					return bid;
				}
			}, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<BidsBean> getNotAwardedBidsByLoadRequestId(long loadRequestId) {
		try {
			return jdbcTemplate.query("select * from bid  where load_request_id=? and bid_status IN ('"
					+ BidStatus.ACTIVE.toDbString() + "','" + BidStatus.LOAD_REQUEST_CANCELLED.toDbString() + "','"
					+ BidStatus.NOT_AWARDED.toDbString() + "');", new RowMapper<BidsBean>() {
						@Override
						public BidsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
							BidsBean bid = new BidsBean();
							bid.setBidId(rs.getLong("bid_id"));
							bid.setAmount(rs.getDouble("amount"));
							bid.setBidderUserId(rs.getLong("bidder_id"));
							bid.setFaberCharges(rs.getDouble("faber_charges"));
							bid.setCreatedBy(rs.getLong("created_by"));
							bid.setCreatedDate(rs.getTimestamp("created_date"));
							bid.setCurrency(CurrencyCodes.fromString(rs.getString("currency")));
							bid.setLoadRequestId(rs.getLong("load_request_id"));
							bid.setModifiedBy(rs.getLong("modified_by"));
							bid.setModifiedDate(rs.getTimestamp("modified_date"));
							bid.setStatus(BidStatus.fromString(rs.getString("bid_status")));
							bid.setDeliveredTime(rs.getTimestamp("expected_delievery_time"));
							bid.setOnlinePaymentCharges(
									BidUtils.getOnlineCharges(rs.getDouble("amount"), rs.getDouble("faber_charges")));
							List<NotesBean> notes = notesManager.getAllNotesByBidId(rs.getLong("bid_id"));
							bid.setNotes(notes);
							try {
								UserDetailsBean userDetials = userManager.getUserInfoByUserId(rs.getLong("bidder_id"));
								if (userDetials != null) {
									bid.setBidderRating(userDetials.getRating());
								}
							} catch (InvalidUsernameException e) {
								log.error("Error : " + e.getMessage(), e);
							}
							return bid;
						}
					}, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public BidsBean getAwardedBidByLoadRequestId(Long loadRequestId) throws APIExceptions {
		try {
			String sql = "select * from bid  where  load_request_id=? and bid_status='AWARDED';";
			return jdbcTemplate.queryForObject(sql, new RowMapper<BidsBean>() {
				@Override
				public BidsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					BidsBean bid = new BidsBean();
					bid.setBidId(rs.getLong("bid_id"));
					bid.setAmount(rs.getDouble("amount"));
					bid.setBidderUserId(rs.getLong("bidder_id"));
					bid.setFaberCharges(rs.getDouble("faber_charges"));
					bid.setCreatedBy(rs.getLong("created_by"));
					bid.setCreatedDate(rs.getTimestamp("created_date"));
					bid.setCurrency(CurrencyCodes.fromString(rs.getString("currency")));
					bid.setLoadRequestId(rs.getLong("load_request_id"));
					bid.setModifiedBy(rs.getLong("modified_by"));
					bid.setModifiedDate(rs.getTimestamp("modified_date"));
					bid.setStatus(BidStatus.fromString(rs.getString("bid_status")));
					bid.setDeliveredTime(rs.getTimestamp("expected_delievery_time"));

					return bid;
				}
			}, loadRequestId);
		} catch (EmptyResultDataAccessException empty) {
			log.info("Empty result.");
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions(
					"Incorrect Result : there is more than one bid in AWARDED status for same load request.");
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Internal server error.");
		}
	}

	/**
	 * 
	 * @param userId
	 * @param loadRequestId
	 * @return true if bit exist , false if bid not exist.
	 */
	@Override
	public Boolean isBidExistForLoadRequest(long userId, long loadRequestId) {
		try {
			String sql = "select bid_id from bid  where created_by=? and load_request_id=?;";
			return jdbcTemplate.query(sql, new Object[] { userId, loadRequestId }, new ResultSetExtractor<Boolean>() {
				@Override
				public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
					Long bidId = null;
					if (rs.next()) {
						bidId = rs.getLong("bid_id");
					}
					if (bidId != null)
						return true;
					else
						return false;
				}
			});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return false;
		} catch (Exception e) {
			log.error("Error : " + e.getMessage(), e);
			return false;
		}
	}

	@Override
	public void updateBidStatus(BidStatusBean bidStatus) throws APIExceptions {
		try {
			String sql2 = "UPDATE bid  SET bid_status=? , modified_by=? where bid_id=? ";
			int result = jdbcTemplate.update(sql2, bidStatus.getStatus(), bidStatus.getModifiedBy(),
					bidStatus.getBidId());

			if (result != 1) {
				throw new APIExceptions("Internal server error : failed to update bid id:" + bidStatus.getBidId());
			}
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Failed to update bid due to internal server error.");
		}
	}

	/**
	 * btach update
	 */
	@Override
	public void batchUpdateBidStatus(List<BidStatusBean> bidStatus) throws APIExceptions {
		try {
			String sql = "UPDATE bid  SET bid_status=? , modified_by=? where bid_id=? ";

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, bidStatus.get(i).getStatus());
					ps.setLong(2, bidStatus.get(i).getModifiedBy());
					ps.setLong(3, bidStatus.get(i).getBidId());
				}

				@Override
				public int getBatchSize() {
					return bidStatus.size();
				}
			});
			log.info(rowUpdated.length + " rows updated.");
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			throw new APIExceptions("Failed to update bid due to internal server error.");
		}
	}

	@Override
	public Bid getBidByLoadRequestUserid(long userId) {
		Bid bid = null;
		try {
			String sql = "select bid_id from bid as bd left join load_requests as lr on bd.load_request_id = lr.load_request_id "
					+ "left join users as us on lr.user_id = us.user_id where us.user_id = ?";
			bid = jdbcTemplate.queryForObject(sql, new RowMapper<Bid>() {
				@Override
				public Bid mapRow(ResultSet rs, int rownumber) throws SQLException {
					Bid bid = new Bid();
					bid.setBidId(rs.getLong("bid_id"));
					return bid;
				}
			}, userId);

		} catch (DataAccessException e) {
			// logger.error("User values are not found : " + e.getMessage(), e);
			// throw new InvalidUsernameException("User is not valid.");
		}
		return bid;
	}

	@Override
	public List<String> getAllBiddersPhoneNumberByLoadRequestId(long loadRequestId) {
		String sql = " SELECT u.user_name from users u join bid b on b.bidder_id= u.user_id where b.load_request_id=?;";
		try {
			return jdbcTemplate.query(sql, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rownumber) throws SQLException {
					return rs.getString(1);
				}
			}, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<Long> getAllBiddersIdsByLoadRequestId(long loadRequestId) {
		String sql = " SELECT bidder_id from bid where load_request_id=?;";
		try {
			return jdbcTemplate.query(sql, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet rs, int rownumber) throws SQLException {
					return rs.getLong(1);
				}
			}, loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<Long> getAllAwardedBidIdsByUserId(long userId) {
		String sql = " SELECT bid_id from bid  " + " where bidder_id = ? and bid_status='"
				+ BidStatus.AWARDED.toDbString() + "'";
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

	@Override
	public List<Users> getBidderInfoByLoadRequestId(long loadRequestId) {
		try {
			return jdbcTemplate.query(" SELECT bidder_id from bid where load_request_id=?;",
					new RowMapper<Users>() {
						@Override
						public Users mapRow(ResultSet rs, int rownumber) throws SQLException {
							Users users = new Users();
							users.setUserId(rs.getLong("bidder_id"));
							return users;
						}
					},loadRequestId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

}
