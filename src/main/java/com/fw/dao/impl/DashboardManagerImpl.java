package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.beans.BidsBean;
import com.fw.beans.DashBoardBean;
import com.fw.beans.NotesBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.UserDetailsBean;
import com.fw.beans.PaymentHistoryBean;
import com.fw.dao.IBidManager;
import com.fw.dao.IBidNotesManager;
import com.fw.dao.IDashboardManager;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.enums.BidStatus;
import com.fw.enums.CurrencyCodes;
import com.fw.enums.InBetweenCitiesExist;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.PaymentDirection;
import com.fw.enums.PaymentReceiveMethod;
import com.fw.enums.TruckType;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.utils.MLogisticsPropertyReader;

@Repository
public class DashboardManagerImpl implements IDashboardManager {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MLogisticsPropertyReader mLogiticsPropertyReader;

	Logger log = Logger.getLogger(DashboardManagerImpl.class);

	@Autowired
	private IBidNotesManager notesManager;

	@Autowired
	private IUserManager userManager;

	@Autowired
	private IBidManager bidManager;

	@Autowired
	private ILoadRequestManager loadRequestManager;

	@Override
	public DashBoardBean getDashBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LoadRequest> getAllLoadRequest() {
		try {

			String sql = "select * from load_requests lr join load_request_details lrd on lrd.load_request_id=lr.load_request_id where lr.is_deleted=false  and "
					+ " lr.load_request_status NOT IN ('"+LoadRequestStatus.DELIVERED.getValue()+"','"+LoadRequestStatus.CANCELLED.getValue()+"','"+LoadRequestStatus.BLOCKED_REQUEST.getValue()+"','"+LoadRequestStatus.EXPIRED.getValue()+"') ORDER BY lrd.shipping_start_datetime ASC; ";

			return jdbcTemplate.query(sql, new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					String truckType = rs.getString("truck_type");
					if(truckType !=null && !"".equals(truckType))
						lr.setTruckType(TruckType.fromString(truckType));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					return lr;
				}
			});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Get bids which are active , awarded or not awarded.
	 */
	@Override
	public List<BidsBean> getBidsByLoadRequestId(long loadRequestId) {
		try {
			String sql = "SELECT * from bid  where load_request_id=? and is_deleted=false  and "
					+ " bid_status NOT IN ('"+BidStatus.LOAD_REQUEST_CANCELLED.toDbString()+"');";
			return jdbcTemplate.query(sql, new RowMapper<BidsBean>() {
				@Override
				public BidsBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					BidsBean bid = new BidsBean();
					long bidderId = rs.getLong("bidder_id");
					bid.setBidId(rs.getLong("bid_id"));
					bid.setAmount(rs.getDouble("amount"));
					bid.setBidderUserId(bidderId);
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
							getOnlineCharges(rs.getDouble("amount"), rs.getDouble("faber_charges")));
					List<NotesBean> notes = notesManager.getAllNotesByBidId(rs.getLong("bid_id"));
					bid.setNotes(notes);
					try {
						UserDetailsBean userDetials = userManager.getUserInfoByUserId(bidderId);
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

	// this should be moved in reusable component
	private double getOnlineCharges(double actualAmount, double faberCharge) {
		double onlineCharges = ((((actualAmount * 0.02) + (faberCharge * 0.02)) / 0.98) + 3) + 0.5;
		onlineCharges = Math.round(onlineCharges);
		return onlineCharges;
	}

	@Override
	public List<LoadRequest> getAllLoadRequestByUserId(long userId) {
		try {

			String sql = "select * from load_requests  where user_id=?;";

			return jdbcTemplate.query(sql, new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					String truckType = rs.getString("truck_type");
					if(truckType !=null && !"".equals(truckType))
						lr.setTruckType(TruckType.fromString(truckType));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					return lr;
				}
			}, userId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<LoadRequest> getAllBookedLoadRequestByUserId(long userId) {
		try {

			String sql = "select * from load_requests  where user_id=? and load_request_status IN ('"+LoadRequestStatus.DELIVERED.getValue()+"','"+LoadRequestStatus.BOOKED.getValue()+"');";

			return jdbcTemplate.query(sql, new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					String truckType = rs.getString("truck_type");
					if(truckType !=null && !"".equals(truckType))
						lr.setTruckType(TruckType.fromString(truckType));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					return lr;
				}
			}, userId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public BidsBean getAwardedBidByLoadRequestId(Long loadRequestId) throws APIExceptions {
		try {
			String sql = "select * from bid  where  load_request_id=? and bid_status='"+BidStatus.AWARDED.toDbString()+"';";
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

	@Override
	public List<PaymentHistoryBean> getPaymentHistory(PaymentFilters filters) {

		String Query = paymentQueryFilter(filters);
		//log.info("============="+Query);
		try {
			return jdbcTemplate.query(Query.toString(), new RowMapper<PaymentHistoryBean>() {
				@Override
				public PaymentHistoryBean mapRow(ResultSet rs, int rownumber) throws SQLException {
					PaymentHistoryBean payment = new PaymentHistoryBean();
					payment.setPaymentId(rs.getLong("payment_id"));
					payment.setAmountRecieved(rs.getDouble("amount_received"));
					payment.setAmountRequested(rs.getDouble("amount_requested"));
					payment.setCurrency(rs.getString("currency"));
					payment.setBidId(rs.getLong("bid_id"));
					payment.setLoadRequestId(rs.getLong("load_request_id"));
					payment.setReceiveMethod(PaymentReceiveMethod.fromString(rs.getString("receive_method")));
					payment.setUserName(rs.getString("user_name"));
					payment.setPaymentDirections(PaymentDirection.fromString(rs.getString("payment_directions")));
					payment.setPaymentStatus(rs.getString("payment_status"));
					payment.setPaymentDateTime(rs.getTimestamp("payment_time"));
					payment.setCreatedBy(rs.getInt("created_by"));
					payment.setModifiedBy(rs.getInt("modified_by"));
					payment.setCreatedDate(rs.getTimestamp("created_date"));
					payment.setModifiedDate(rs.getTimestamp("modified_date"));

					return payment;
				}
			});
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return new ArrayList<PaymentHistoryBean>();
		}
		catch (Exception e) {
			log.error(e.getMessage());
			return new ArrayList<PaymentHistoryBean>();
		}
	}

	private String paymentQueryFilter(PaymentFilters filters) {

		StringBuilder paymentQuery = new StringBuilder(
				" SELECT p.*, u.user_name  from payment p " + " join users u on u.user_id=p.user_id ");

		String paymentDirectionsFilter =((filters.getDirectionType() != null)? " and p.payment_directions='" + filters.getDirectionType().toDbString() + "'": " ");
		boolean betDates = (filters.getStartDate() != null && filters.getEndDate() != null);
		String betweenDatesFilter = ""+((betDates)? " and p.payment_time between '" + filters.getStartDate().toString() + "' " + " and  '"
								+ filters.getEndDate().toString() + "'": " ");
		String paymenStatusFilter = " "
				+ ((filters.getPaymentStatus() != null) ? " and payment_status='" + filters.getPaymentStatus() + "'"
						: " ");
		
		String loadRequestId = ((filters.getLoadRequestId() == null) ? " ": " and p.load_request_id IN (" + filters.getLoadRequestId() + ") ");
		
		String userId=(filters.getUserId() != null)?" and p.user_id =" + filters.getUserId():" ";
		
		if(!paymentDirectionsFilter.trim().isEmpty() || !betweenDatesFilter.trim().isEmpty() || 
				!paymenStatusFilter.trim().isEmpty() ||!loadRequestId.trim().isEmpty() ||
				!userId.trim().isEmpty()) 
		{
			paymentQuery.append(" WHERE 1=1 ");
		}
		paymentQuery.append(paymentDirectionsFilter).append(betweenDatesFilter).append(paymenStatusFilter).append(loadRequestId).append(userId);
		

		if( filters.getLimit()!= null ) {
			paymentQuery.append(" LIMIT "+filters.getLimit());
			
			if( filters.getPage() != null ) {
				
				paymentQuery.append(" OFFSET "+ ((filters.getPage()-1)*filters.getLimit()));
			}
		}
		else if( filters.getLimit() == null && filters.getPage() != null ) {
			
			paymentQuery.append(" OFFSET "+ filters.getPage());
		}

		return paymentQuery.toString();
	}

	@Override
	public List<LoadRequest> getAllLoadRequestCreateByCSRId(long userId) {
		try {

			String sql = "select * from load_requests lr join load_request_details lrd on lrd.load_request_id=lr.load_request_id where "
					+ " lr.created_by=?;";

			return jdbcTemplate.query(sql, new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					String truckType = rs.getString("truck_type");
					if(truckType !=null && !"".equals(truckType))
						lr.setTruckType(TruckType.fromString(truckType));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					return lr;
				}
			}, userId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<BidsBean> getAllBidsByUserId(long bidderUserId) {
		try {

			String sql = "SELECT * from bid where bidder_id=?;";

			return jdbcTemplate.query(sql, new RowMapper<BidsBean>() {
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
					List<NotesBean> notes = notesManager.getAllNotesByBidId(rs.getLong("bid_id"));
					bid.setNotes(notes);

					try {
						UserDetailsBean userDetials = userManager.getUserInfoByUserId(bidderUserId);
						if (userDetials != null) {
							bid.setBidderRating(userDetials.getRating());
						}
					} catch (InvalidUsernameException e) {
						log.error("Error : " + e.getMessage(), e);
					}

					return bid;
				}
			}, bidderUserId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<BidsBean> getAllBidsCreatedByCSRId(long bidderUserId) {
		try {

			String sql = "SELECT * from bid where created_by=?;";

			return jdbcTemplate.query(sql, new RowMapper<BidsBean>() {
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
					List<NotesBean> notes = notesManager.getAllNotesByBidId(rs.getLong("bid_id"));
					bid.setNotes(notes);

					try {
						UserDetailsBean userDetials = userManager.getUserInfoByUserId(bidderUserId);
						if (userDetials != null) {
							bid.setBidderRating(userDetials.getRating());
						}
					} catch (InvalidUsernameException e) {
						log.error("Error : " + e.getMessage(), e);
					}

					return bid;
				}
			}, bidderUserId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public List<LoadRequest> getAllLoadRequestByRegion(String country, String state, String city,
			InBetweenCitiesExist cityExist) {
		try {

			StringBuilder providerQuery = new StringBuilder(
					"select DISTINCT(lr.*),lrd.shipping_start_datetime from load_requests as lr left join load_request_details as lrd on lr.load_request_id = lrd.load_request_id "
							+ "  ");
			if (cityExist != InBetweenCitiesExist.ALL) {
				providerQuery.append(
						" left join postal_addresses as p on lrd.start_location_id = p.postal_addresses_id OR lrd.end_location_id = p.postal_addresses_id"
								+ " join load_request_cities city on  p.city  LIKE CONCAT('%',city.source_city,'%' ) ");
			}
			providerQuery.append(" where lr.is_deleted=false and lr.load_request_status NOT IN ('"
					+ LoadRequestStatus.DELIVERED.getValue() + "','" + LoadRequestStatus.CANCELLED.getValue() + "','"
					+ LoadRequestStatus.EXPIRED.getValue() + "','"+LoadRequestStatus.BLOCKED_REQUEST.getValue()+"')  ");
			if (cityExist != InBetweenCitiesExist.ALL) {
				if (cityExist == InBetweenCitiesExist.NOT_EXISTS)
					providerQuery.append(" and (city.cities ='[]' or city.cities ='' )");
				if (cityExist == InBetweenCitiesExist.EXISTS)
					providerQuery.append(" and (city.cities !='[]')");
			}

			/*
			 * if ((country != null && !"".equals(country)) || (state != null &&
			 * !"".equals(state)) || (city != null && !"".equals(city))) {
			 * providerQuery.append("and ("); if (country != null && !"".equals(country))
			 * providerQuery.append(" p.country='" + country + "'"); if (state != null &&
			 * !"".equals(state)) providerQuery.append(" and p.state_province='" + state +
			 * "'"); if (city != null && !"".equals(city))
			 * providerQuery.append(" and p.city='" + city + "'");
			 * providerQuery.append(")"); }
			 */

			providerQuery.append(" ORDER BY lrd.shipping_start_datetime ASC; ");
			return jdbcTemplate.query(providerQuery.toString(), new RowMapper<LoadRequest>() {
				@Override
				public LoadRequest mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequest lr = new LoadRequest();
					lr.setLoadRequestId(rs.getLong("load_request_id"));
					lr.setBiddingEndDatetime(rs.getTimestamp("bidding_end_datetime"));
					lr.setBiddingStartDatetime(rs.getTimestamp("bidding_start_datetime"));
					lr.setInsuranceNeeded(rs.getBoolean("insurance_needed"));
					String truckType = rs.getString("truck_type");
					if(truckType !=null && !"".equals(truckType))
						lr.setTruckType(TruckType.fromString(truckType));
					lr.setUserId(rs.getLong("user_id"));
					lr.setLoadRequestStatus(LoadRequestStatus.fromString(rs.getString("load_request_status")));
					lr.setCreatedBy(rs.getLong("created_by"));
					lr.setModifiedBy(rs.getLong("modified_by"));
					lr.setCreatedDate(rs.getTimestamp("created_date"));
					lr.setModifiedDate(rs.getTimestamp("modified_date"));
					return lr;
				}
			});
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
			return null;
		}
	}

}
