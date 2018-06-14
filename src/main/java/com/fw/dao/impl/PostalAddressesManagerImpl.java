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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fw.beans.FromToAddressBean;
import com.fw.beans.MarkatingAddressBean;
import com.fw.dao.IPostalAddressesManager;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;

@Repository
public class PostalAddressesManagerImpl implements IPostalAddressesManager {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Logger log = Logger.getLogger(PostalAddressesManagerImpl.class);
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public PostalAddresses persist(PostalAddresses postalAddresses) throws APIExceptions {

		String sql = "INSERT INTO postal_addresses(user_id, address_line_1, address_line_2, city, state_province, pin,"
				+ " country, latitude, longitude,  created_by, modified_by, is_deleted, near_by_landmark, area, location_json)	"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "postal_addresses_id" });
					ps.setLong(1, postalAddresses.getUserId());
					ps.setString(2, postalAddresses.getAddressLine1());
					ps.setString(3, postalAddresses.getAddressLine2());
					ps.setString(4, postalAddresses.getCity());
					ps.setString(5, postalAddresses.getState());
					ps.setString(6, (postalAddresses.getPin() == null) ? null : postalAddresses.getPin().trim());
					ps.setString(7, postalAddresses.getCountry());
					ps.setDouble(8, postalAddresses.getLatitude());
					ps.setDouble(9, postalAddresses.getLongitude());
					ps.setLong(10, postalAddresses.getCreatedBy());
					ps.setLong(11, postalAddresses.getModifiedBy());
					ps.setBoolean(12, postalAddresses.isDeleted());
					ps.setString(13, postalAddresses.getNearByLandmark());
					ps.setString(14, postalAddresses.getArea());
					ps.setString(15, postalAddresses.getLocationJSON());
					return ps;
				}
			}, requestKeyHolder);
			long addressId = requestKeyHolder.getKey().longValue();
			postalAddresses.setPostalAddressId(addressId);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new APIExceptions(e.getCause().getMessage());
		}
		return postalAddresses;

	}

	@Override
	public List<PostalAddresses> getAllPostalAddressesRowMapper() {

		try {
			return jdbcTemplate.query("select * from postal_addresses", new RowMapper<PostalAddresses>() {
				@Override
				public PostalAddresses mapRow(ResultSet rs, int rownumber) throws SQLException {
					PostalAddresses postalAddresses = new PostalAddresses();
					postalAddresses.setPostalAddressId(rs.getInt("postal_addresses_id"));
					postalAddresses.setUserId(rs.getInt("user_id"));
					postalAddresses.setAddressLine1(rs.getString("address_line_1"));
					postalAddresses.setAddressLine2(rs.getString("address_line_2"));
					postalAddresses.setCity(rs.getString("city"));
					postalAddresses.setState(rs.getString("state_province"));
					postalAddresses.setPin(rs.getString("pin"));
					postalAddresses.setCountry(rs.getString("country"));
					postalAddresses.setLatitude(rs.getDouble("latitude"));
					postalAddresses.setLongitude(rs.getDouble("longitude"));
					postalAddresses.setCreatedBy(rs.getInt("created_by"));
					postalAddresses.setModifiedBy(rs.getInt("modified_by"));
					postalAddresses.setCreatedDate(rs.getDate("created_date"));
					postalAddresses.setModifiedDate(rs.getDate("modified_date"));
					postalAddresses.setNearByLandmark(rs.getString("near_by_landmark"));
					postalAddresses.setArea(rs.getString("area"));
					postalAddresses.setLocationJSON(rs.getString("location_json"));

					return postalAddresses;
				}
			});
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public void deletePostalAddressesById(PostalAddresses id) throws APIExceptions {
		try {
			String sql = "DELETE from postal_addresses where postal_addresses_id = ?;";
			int result = jdbcTemplate.update(sql, id.getPostalAddressId());
			if (result != 1) {
				throw new APIExceptions("Failed to delete postal address");
			}
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new APIExceptions("Internal server error : Failed to delete postal address");
		}

	}

	@Override
	public void updatePostalAddressesById(PostalAddresses postalAddresses) throws APIExceptions {
		try {
			String sql = "UPDATE postal_addresses "
					+ " set user_id =?,  address_line_1 =?,  address_line_2 =?, city =?,  state_province =?, "
					+ "  pin =?,  country =?,  latitude =?,  longitude =?,  created_by =?, "
					+ "  modified_by =?, near_by_landmark =?, area =?, location_json =? where postal_addresses_id =?;";

			int result = jdbcTemplate.update(sql, postalAddresses.getUserId(), postalAddresses.getAddressLine1(),
					postalAddresses.getAddressLine2(), postalAddresses.getCity(), postalAddresses.getState(),
					(postalAddresses.getPin() == null) ? null : postalAddresses.getPin().trim(),
					postalAddresses.getCountry(), postalAddresses.getLatitude(), postalAddresses.getLongitude(),
					postalAddresses.getCreatedBy(), postalAddresses.getModifiedBy(),
					postalAddresses.getNearByLandmark(), postalAddresses.getArea(), postalAddresses.getLocationJSON(),
					postalAddresses.getPostalAddressId());

		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new APIExceptions("Internal server error : Failed to update postal address");
		}
	}

	@Override
	public PostalAddresses getPostalAddressesById(Long id) {
		try {
			return jdbcTemplate.queryForObject("select * from postal_addresses where postal_addresses_id = ?;",
					new RowMapper<PostalAddresses>() {
						@Override
						public PostalAddresses mapRow(ResultSet rs, int rownumber) throws SQLException {
							PostalAddresses postalAddresses = new PostalAddresses();
							postalAddresses.setPostalAddressId(rs.getInt("postal_addresses_id"));
							postalAddresses.setUserId(rs.getInt("user_id"));
							postalAddresses.setAddressLine1(rs.getString("address_line_1"));
							postalAddresses.setAddressLine2(rs.getString("address_line_2"));
							postalAddresses.setCity(rs.getString("city"));
							postalAddresses.setState(rs.getString("state_province"));
							postalAddresses.setPin(rs.getString("pin"));
							postalAddresses.setCountry(rs.getString("country"));
							postalAddresses.setLatitude(rs.getDouble("latitude"));
							postalAddresses.setLongitude(rs.getDouble("longitude"));
							postalAddresses.setCreatedBy(rs.getInt("created_by"));
							postalAddresses.setModifiedBy(rs.getInt("modified_by"));
							postalAddresses.setCreatedDate(rs.getDate("created_date"));
							postalAddresses.setModifiedDate(rs.getDate("modified_date"));
							postalAddresses.setNearByLandmark(rs.getString("near_by_landmark"));
							postalAddresses.setArea(rs.getString("area"));
							postalAddresses.setLocationJSON(rs.getString("location_json"));

							return postalAddresses;
						}
					}, id);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<PostalAddresses> getPostalAddressesByIdList(List<Long> ids) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource("postalIdsList", ids);
			String sql = "select * from postal_addresses where postal_addresses_id IN (:postalIdsList);";
			return namedParameterJdbcTemplate.query(sql, params, new RowMapper<PostalAddresses>() {
				@Override
				public PostalAddresses mapRow(ResultSet rs, int rownumber) throws SQLException {
					PostalAddresses postalAddresses = new PostalAddresses();
					postalAddresses.setPostalAddressId(rs.getInt("postal_addresses_id"));
					postalAddresses.setUserId(rs.getInt("user_id"));
					postalAddresses.setAddressLine1(rs.getString("address_line_1"));
					postalAddresses.setAddressLine2(rs.getString("address_line_2"));
					postalAddresses.setCity(rs.getString("city"));
					postalAddresses.setState(rs.getString("state_province"));
					postalAddresses.setPin(rs.getString("pin"));
					postalAddresses.setCountry(rs.getString("country"));
					postalAddresses.setLatitude(rs.getDouble("latitude"));
					postalAddresses.setLongitude(rs.getDouble("longitude"));
					postalAddresses.setCreatedBy(rs.getInt("created_by"));
					postalAddresses.setModifiedBy(rs.getInt("modified_by"));
					postalAddresses.setCreatedDate(rs.getDate("created_date"));
					postalAddresses.setModifiedDate(rs.getDate("modified_date"));
					postalAddresses.setNearByLandmark(rs.getString("near_by_landmark"));
					postalAddresses.setArea(rs.getString("area"));
					postalAddresses.setLocationJSON(rs.getString("location_json"));
					return postalAddresses;
				}
			});
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}


	@Override
	public List<PostalAddresses> getPostalAddressesByUserId(Long userId) {
		try {
			System.out.println("userId : " + userId);
			return jdbcTemplate.query("select * from postal_addresses where user_id=?",

					new RowMapper<PostalAddresses>() {
						@Override
						public PostalAddresses mapRow(ResultSet rs, int rownumber) throws SQLException {
							PostalAddresses location = new PostalAddresses();
							location.setPostalAddressId(rs.getInt("postal_addresses_id"));
							location.setUserId(rs.getInt("user_id"));
							location.setAddressLine1(rs.getString("address_line_1"));
							location.setAddressLine2(rs.getString("address_line_2"));
							location.setCity(rs.getString("city"));
							location.setState(rs.getString("state_province"));
							location.setPin(rs.getString("pin"));
							location.setCountry(rs.getString("country"));
							location.setLatitude(rs.getDouble("latitude"));
							location.setLongitude(rs.getDouble("longitude"));
							location.setCreatedBy(rs.getInt("created_by"));
							location.setModifiedBy(rs.getInt("modified_by"));
							location.setCreatedDate(rs.getDate("created_date"));
							location.setModifiedDate(rs.getDate("modified_date"));
							location.setNearByLandmark(rs.getString("near_by_landmark"));
							location.setArea(rs.getString("area"));
							location.setLocationJSON(rs.getString("location_json"));
							return location;
						}
					}, userId);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<MarkatingAddressBean> getPostalAddressesLinesByUserId(Long userId) {
		try {
			return jdbcTemplate.query("select address_line_1,location_json from postal_addresses where user_id=?",

					new RowMapper<MarkatingAddressBean>() {
						@Override
						public MarkatingAddressBean mapRow(ResultSet rs, int rownumber) throws SQLException {
							MarkatingAddressBean location = new MarkatingAddressBean();
							location.setAddressLine1(rs.getString("address_line_1"));
							location.setAddressJson(rs.getString("location_json"));
							return location;
						}
					}, userId);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
