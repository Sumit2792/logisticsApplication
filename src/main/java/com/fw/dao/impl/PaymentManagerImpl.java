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

import com.fw.dao.IPaymentManager;
import com.fw.domain.Payment;
import com.fw.enums.PaymentDirection;
import com.fw.enums.PaymentReceiveMethod;

@Repository
public class PaymentManagerImpl implements IPaymentManager {

	private Logger log = Logger.getLogger(PaymentManagerImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Insert login details
	 */
	@Override
	public void persist(Payment payment) {
		String sql = "INSERT INTO payment(payment_gateway_request_id, payment_gateway_payment_id, amount_received, amount_requested,"
				+ "currency, bid_id, receive_method, user_id, payment_directions, payment_status, payment_gateway_response, payment_time, "
				+ "created_by, modified_by)	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		jdbcTemplate.update(sql, payment.getPaymentGatewayRequestId(), payment.getPaymentGatewayPaymentId(),
				payment.getAmountRecieved(), payment.getAmountRequested(), payment.getCurrency(), payment.getBidId(),
				payment.getReceiveMethod(), payment.getUserId(), payment.getPaymentDirections(),
				payment.getPaymentStatus(), payment.getPaymentGatewayResponse(), payment.getPaymentTime(),
				payment.getCreatedBy(), payment.getModifiedBy());
	}

	@Override
	public List<Payment> getAllPaymentRowMapper() {
		return jdbcTemplate.query("select * from payment", new RowMapper<Payment>() {
			@Override
			public Payment mapRow(ResultSet rs, int rownumber) throws SQLException {
				Payment payment = new Payment();
				payment.setPaymentId(rs.getLong("payment_id"));
				payment.setPaymentGatewayRequestId(rs.getString("payment_gateway_request_id"));
				payment.setPaymentGatewayPaymentId(rs.getString("payment_gateway_payment_id"));
				payment.setAmountRecieved(rs.getDouble("amount_received"));
				payment.setAmountRequested(rs.getDouble("amount_requested"));
				payment.setCurrency(rs.getString("currency"));
				payment.setBidId(rs.getLong("bid_id"));
				payment.setLoadRequestId(rs.getLong("load_request_id"));
				payment.setReceiveMethod(PaymentReceiveMethod.fromString(rs.getString("receive_method")));
				payment.setUserId(rs.getLong("user_id"));
				payment.setPaymentDirections(PaymentDirection.fromString(rs.getString("payment_directions")));
				payment.setPaymentStatus(rs.getString("payment_status"));
				payment.setPaymentGatewayResponse(rs.getString("payment_gateway_response"));
				payment.setPaymentTime(rs.getTimestamp("payment_time"));
				payment.setCreatedBy(rs.getLong("created_by"));
				payment.setModifiedBy(rs.getLong("modified_by"));

				return payment;
			}
		});
	}

	@Override
	public void deletePayment(Payment payment) {
		String sql = "DELETE from payment where payment_id = ?;";
		jdbcTemplate.update(sql, payment.getPaymentId());
	}

	@Override
	public void modifyPaymentsByPaymentId(Payment payment) {
		String sql = "UPDATE payment"
				+ "set bid_id=?,set amount =?, set created_by =?, set modified_by =?, set created_date =?,"
				+ " set modified_date =? where payment_id =);";

		jdbcTemplate.update(sql, payment.getPaymentGatewayRequestId(), payment.getPaymentGatewayPaymentId(),
				payment.getAmountRecieved(), payment.getAmountRequested(), payment.getCurrency(), payment.getBidId(),
				payment.getReceiveMethod(), payment.getUserId(), payment.getPaymentDirections(),
				payment.getPaymentStatus(), payment.getPaymentGatewayResponse(), payment.getPaymentTime(),
				payment.getCreatedBy(), payment.getModifiedBy());
	}

	@Override
	public Payment getPaymentById(Long id) {
		return jdbcTemplate.queryForObject("select * from payment where payment_id = ?;", new RowMapper<Payment>() {
			@Override
			public Payment mapRow(ResultSet rs, int rownumber) throws SQLException {
				Payment payment = new Payment();
				payment.setPaymentId(rs.getLong("payment_id"));
				payment.setPaymentGatewayRequestId(rs.getString("payment_gateway_request_id"));
				payment.setPaymentGatewayPaymentId(rs.getString("payment_gateway_payment_id"));
				payment.setAmountRecieved(rs.getDouble("amount_received"));
				payment.setAmountRequested(rs.getDouble("amount_requested"));
				payment.setCurrency(rs.getString("currency"));
				payment.setBidId(rs.getLong("bid_id"));
				payment.setLoadRequestId(rs.getLong("load_request_id"));
				payment.setReceiveMethod(PaymentReceiveMethod.fromString(rs.getString("receive_method")));
				payment.setUserId(rs.getLong("user_id"));
				payment.setPaymentDirections(PaymentDirection.fromString(rs.getString("payment_directions")));
				payment.setPaymentStatus(rs.getString("payment_status"));
				payment.setPaymentGatewayResponse(rs.getString("payment_gateway_response"));
				payment.setPaymentTime(rs.getTimestamp("payment_time"));
				payment.setCreatedBy(rs.getLong("created_by"));
				payment.setModifiedBy(rs.getLong("modified_by"));

				return payment;
			}
		}, id);
	}

	@Override
	public List<Payment> getPaymentByUserId(Long userId) {

		return jdbcTemplate.query("select * from payment where user_id = ?;", new RowMapper<Payment>() {
			@Override
			public Payment mapRow(ResultSet rs, int rownumber) throws SQLException {
				Payment payment = new Payment();
				payment.setPaymentId(rs.getLong("payment_id"));
				payment.setPaymentGatewayRequestId(rs.getString("payment_gateway_request_id"));
				payment.setPaymentGatewayPaymentId(rs.getString("payment_gateway_payment_id"));
				payment.setAmountRecieved(rs.getDouble("amount_received"));
				payment.setAmountRequested(rs.getDouble("amount_requested"));
				payment.setCurrency(rs.getString("currency"));
				payment.setBidId(rs.getLong("bid_id"));
				payment.setLoadRequestId(rs.getLong("load_request_id"));
				payment.setReceiveMethod(PaymentReceiveMethod.fromString(rs.getString("receive_method")));
				payment.setUserId(rs.getLong("user_id"));
				payment.setPaymentDirections(PaymentDirection.fromString(rs.getString("payment_directions")));
				payment.setPaymentStatus(rs.getString("payment_status"));
				payment.setPaymentGatewayResponse(rs.getString("payment_gateway_response"));
				payment.setPaymentTime(rs.getTimestamp("payment_time"));
				payment.setCreatedBy(rs.getLong("created_by"));
				payment.setModifiedBy(rs.getLong("modified_by"));

				return payment;
			}
		}, userId);
	}

	@Override
	public void persistPaymentRequest(Payment paymentinfo) {
		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			
			
			String sql = "INSERT INTO payment(payment_gateway_request_id, payment_gateway_payment_id, amount_received, amount_requested,"
					+ "currency, bid_id, load_request_id, receive_method, user_id, payment_directions,"
					+ " payment_status, payment_gateway_response, payment_time, created_by, modified_by)	"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			LocalDateTime paymentTime = (paymentinfo.getPaymentTime() == null) ? null
					: LocalDateTime.ofInstant(paymentinfo.getPaymentTime().toInstant(), ZoneId.systemDefault());
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "payment_id" });
					ps.setString(1, paymentinfo.getPaymentGatewayRequestId());
					ps.setString(2, paymentinfo.getPaymentGatewayPaymentId());
					ps.setDouble(3, paymentinfo.getAmountRecieved());
					ps.setDouble(4, paymentinfo.getAmountRequested());
					ps.setString(5, paymentinfo.getCurrency());
					ps.setLong(6, paymentinfo.getBidId());
					ps.setLong(7, paymentinfo.getLoadRequestId());
					ps.setString(8, paymentinfo.getReceiveMethod().toDbString());
					ps.setLong(9, paymentinfo.getUserId());
					ps.setString(10, paymentinfo.getPaymentDirections().toDbString());
					ps.setString(11, paymentinfo.getPaymentStatus());
					ps.setString(12, paymentinfo.getPaymentGatewayResponse());
					if (paymentTime == null)
						ps.setNull(13, Types.TIMESTAMP);
					else
						ps.setTimestamp(13, Timestamp.valueOf(paymentTime));
					ps.setLong(14, paymentinfo.getCreatedBy());
					ps.setLong(15, paymentinfo.getModifiedBy());
					return ps;
				}
			}, requestKeyHolder);
			long paymentId = requestKeyHolder.getKey().longValue();
			paymentinfo.setBidId(paymentId);
		} catch (DataAccessException e) {
			log.error("Error : " + e.getMessage(), e);
		}

	}

	@Override
	public void updatePaymentResponse(Payment paymentinfo, String transactionId) {
		String sql = "UPDATE payment set amount_received=?, payment_status =?, payment_gateway_response =?, payment_time =?,"
				+ " modified_by =? where payment_gateway_payment_id =?;";

		jdbcTemplate.update(sql, paymentinfo.getAmountRecieved(), paymentinfo.getPaymentStatus(),
				paymentinfo.getPaymentGatewayResponse(), paymentinfo.getPaymentTime() , paymentinfo.getModifiedBy(), transactionId);

	}

	@Override
	public Payment getPaymentDetailsByTransactionId(String transactionId) {
		return jdbcTemplate.queryForObject("select * from payment where payment_gateway_payment_id = ?;",
				new RowMapper<Payment>() {
					@Override
					public Payment mapRow(ResultSet rs, int rownumber) throws SQLException {
						Payment payment = new Payment();
						payment.setPaymentId(rs.getLong("payment_id"));
						payment.setPaymentGatewayRequestId(rs.getString("payment_gateway_request_id"));
						payment.setPaymentGatewayPaymentId(rs.getString("payment_gateway_payment_id"));
						payment.setAmountRecieved(rs.getDouble("amount_received"));
						payment.setAmountRequested(rs.getDouble("amount_requested"));
						payment.setCurrency(rs.getString("currency"));
						payment.setBidId(rs.getLong("bid_id"));
						payment.setLoadRequestId(rs.getLong("load_request_id"));
						payment.setReceiveMethod(PaymentReceiveMethod.fromString(rs.getString("receive_method")));
						payment.setUserId(rs.getLong("user_id"));
						payment.setPaymentDirections(PaymentDirection.fromString(rs.getString("payment_directions")));
						payment.setPaymentStatus(rs.getString("payment_status"));
						payment.setPaymentGatewayResponse(rs.getString("payment_gateway_response"));
						payment.setPaymentTime(rs.getTimestamp("payment_time"));
						payment.setCreatedBy(rs.getLong("created_by"));
						payment.setModifiedBy(rs.getLong("modified_by"));

						return payment;
					}
				}, transactionId);
	}
}
