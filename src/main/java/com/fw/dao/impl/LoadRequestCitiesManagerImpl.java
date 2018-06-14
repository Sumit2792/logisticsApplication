package com.fw.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.beans.InBetweenCitiesBean;
import com.fw.beans.LoadCitiesBean;
import com.fw.dao.ILoadRequestCitiesManager;
import com.fw.domain.LoadRequestCities;

@Repository
public class LoadRequestCitiesManagerImpl implements ILoadRequestCitiesManager {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger log = Logger.getLogger(LoadRequestCitiesManagerImpl.class);

	@Override
	public LoadRequestCities persistLoadRequestCities(LoadRequestCities logEntity) {

		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO load_request_cities (source_city, destination_city, cities, "
					+ " created_by, modified_by) " + " VALUES(?,?,?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "load_request_cities_id" });
					ps.setString(1, logEntity.getSourceCity());
					ps.setString(2, logEntity.getDestinationCity());
					ps.setString(3, logEntity.getCities());
					ps.setLong(4, logEntity.getCreatedBy());
					ps.setLong(5, logEntity.getModifiedBy());
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setLoadRequestCitiesId(logEntityId);
			return logEntity;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void updateLoadRequestCitiesById(LoadRequestCities logEntity) {
		String sql = "UPDATE load_request_cities  SET source_city=?, "
				+ "destination_city=?,cities=? ,created_by=? ,modified_by=? WHERE load_request_cities_id=?;";
		jdbcTemplate.update(sql, logEntity.getSourceCity(), logEntity.getDestinationCity(), logEntity.getCities(),
				logEntity.getCreatedBy(), logEntity.getModifiedBy(), logEntity.getLoadRequestCitiesId());
	}

	@Override
	public void deleteLoadRequestCitiesById(LoadRequestCities Id) {

		String sql = "DELETE from load_request_cities where load_request_cities_id=?  ;";
		jdbcTemplate.update(sql, Id);

	}

	@Override
	public List<LoadRequestCities> getAllLoadRequestCitiesRowMapper() {
		return jdbcTemplate.query("select * from load_request_cities;", new RowMapper<LoadRequestCities>() {
			@Override
			public LoadRequestCities mapRow(ResultSet rs, int rownumber) throws SQLException {
				LoadRequestCities logEntity = new LoadRequestCities();
				logEntity.setLoadRequestCitiesId(rs.getLong("load_request_cities_id"));
				logEntity.setSourceCity(rs.getString("source_city"));
				logEntity.setDestinationCity(rs.getString("destination_city"));
				logEntity.setCities(rs.getString("cities"));
				logEntity.setCreatedBy(rs.getLong("created_by"));
				logEntity.setModifiedBy(rs.getLong("modified_by"));
				logEntity.setCreatedDate(rs.getTimestamp("created_date"));
				logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
				return logEntity;
			}
		});
	}

	@Override
	public LoadRequestCities getLoadRequestCitiesById(long loadRequestCitiesID) {
		try {
			return jdbcTemplate.queryForObject("select * from load_request_cities where load_request_cities_id=?",
					new RowMapper<LoadRequestCities>() {
						@Override
						public LoadRequestCities mapRow(ResultSet rs, int rownumber) throws SQLException {
							LoadRequestCities logEntity = new LoadRequestCities();
							logEntity.setLoadRequestCitiesId(rs.getLong("load_request_cities_id"));
							logEntity.setSourceCity(rs.getString("source_city"));
							logEntity.setDestinationCity(rs.getString("destination_city"));
							logEntity.setCities(rs.getString("cities"));
							logEntity.setCreatedBy(rs.getLong("created_by"));
							logEntity.setModifiedBy(rs.getLong("modified_by"));
							logEntity.setCreatedDate(rs.getTimestamp("created_date"));
							logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
							return logEntity;
						}
					}, loadRequestCitiesID);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void insertLoadRequestCitiesByLoadRequestId(String getCityFromAddress, String getCityToAddress,
			JSONArray inBetweenCities, Long requestId) {
		LoadRequestCities logEntity = new LoadRequestCities();
		KeyHolder requestKeyHolder = new GeneratedKeyHolder();
		try {
			String sql = "INSERT INTO load_request_cities (load_request_id, source_city, destination_city, cities, "
					+ " created_by, modified_by) " + " VALUES(?,?,?,?,?,?);";

			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(sql, new String[] { "load_request_cities_id" });
					ps.setLong(1, requestId);
					ps.setString(2, getCityFromAddress);
					ps.setString(3, getCityToAddress);
					ps.setString(4, inBetweenCities.toString());
					ps.setLong(5, logEntity.getCreatedBy());
					ps.setLong(6, logEntity.getModifiedBy());
					return ps;
				}
			}, requestKeyHolder);
			long logEntityId = requestKeyHolder.getKey().longValue();
			logEntity.setLoadRequestCitiesId(logEntityId);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public HashMap<String, Object> getCitiesBetweenSourceAndDestination(String source, String destination) {

		String sql = "select * from load_request_cities where LOWER(source_city)=LOWER('" + source
				+ "') and LOWER(destination_city)=LOWER('" + destination + "');";

		return jdbcTemplate.query(sql, new ResultSetExtractor<HashMap<String, Object>>() {

			@Override
			public HashMap<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {

				HashMap<String, Object> cityMap = new HashMap<>();
				ObjectMapper mapper = new ObjectMapper();
				while (rs.next()) {
					cityMap.put("source", rs.getString("source_city"));
					cityMap.put("destination", rs.getString("destination_city"));
					try {
						InBetweenCitiesBean[] inBetweenCitiesBean = mapper.readValue(rs.getString("cities"),
								InBetweenCitiesBean[].class);
						cityMap.put("inBetweenCities", inBetweenCitiesBean);
					} catch (IOException e) {
						cityMap.put("inBetweenCities", new Object[] {});
						e.printStackTrace();
					}
				}
				return cityMap;
			}

		});
	}

	@Override
	public void updateLoadRequestCitiesBySourceAndDesination(long updatedBy, String source, String destination,
			String Cities) {
		String sql = "UPDATE load_request_cities  SET cities=?, modified_by=? WHERE source_city=? and destination_city=?;";
		try {
			jdbcTemplate.update(sql, Cities, updatedBy, source, destination);
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
		}

	}

}
