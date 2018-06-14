package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.beans.LoadRequestDetailsBean;
import com.fw.dao.ILoadRequestDetailManager;
import com.fw.domain.LoadRequestDetail;

@Repository
public class LoadRequestDetailManagerImpl implements ILoadRequestDetailManager {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private Logger log = Logger.getLogger(LoadRequestDetailManagerImpl.class);

	@Override
	public void persist(LoadRequestDetail lrd) {
		String sql2 = "INSERT INTO load_request_details (load_request_id,start_location_id,end_location_id,shipping_start_datetime,expected_end_datetime,actual_end_datetime,created_by,modified_by) "
				+ " VALUES(?,?,?,?,?,?,?,?);";
		jdbcTemplate.update(sql2, lrd.getLoadRequestId(), lrd.getStartLocationId(), lrd.getEndLocationId(),
				lrd.getStartDateTime(), lrd.getExpectedEndDateTime(), lrd.getActualEndDateTime(), lrd.getCreatedBy(),
				lrd.getModifiedBy());
	}

	@Override
	public void updateLoadRequestDetailByRequestId(LoadRequestDetail lrd, long requestId) {

		String sql = "UPDATE load_request_details SET shipping_start_datetime=?,expected_end_datetime=?,created_by=?,modified_by=? where load_request_id=?;";
		jdbcTemplate.update(sql, lrd.getStartDateTime(), lrd.getExpectedEndDateTime(), lrd.getCreatedBy(),
				lrd.getModifiedBy(), requestId);
	}

	@Override
	public void deleteLoadRequestDetailsBy_LoadRequestId(Long loadRequestId) {

		String sql = "DELETE from load_request_details where load_request_id=? ";
		jdbcTemplate.update(sql, loadRequestId);

	}

	@Override
	public List<LoadRequestDetail> getAllLoadRequestDetails() {
		return jdbcTemplate.query("select * from load_request_details;", new RowMapper<LoadRequestDetail>() {
			@Override
			public LoadRequestDetail mapRow(ResultSet rs, int rownumber) throws SQLException {
				LoadRequestDetail lrd = new LoadRequestDetail();
				lrd.setLoadRequestDetailsId(rs.getLong("load_request_details_id"));
				lrd.setActualEndDateTime(rs.getTimestamp("actual_end_datetime"));
				lrd.setCreatedBy(rs.getLong("created_by"));
				lrd.setCreatedDate(rs.getTimestamp("created_date"));
				lrd.setEndLocationId(rs.getLong("end_location_id"));
				lrd.setExpectedEndDateTime(rs.getTimestamp("expected_end_datetime"));
				lrd.setLoadRequestId(rs.getLong("load_request_id"));
				lrd.setModifiedBy(rs.getLong("modified_by"));
				lrd.setModifiedDate(rs.getTimestamp("modified_date"));
				lrd.setStartDateTime(rs.getTimestamp("shipping_start_datetime"));
				lrd.setStartLocationId(rs.getLong("start_location_id"));
				return lrd;
			}
		});
	}

	@Override
	public LoadRequestDetail getLoadRequestDetailById(long loadRequestId) {

		String sql = "select * from load_request_details where load_request_id=?;";
		LoadRequestDetail loadRequest = null;
		try {
			loadRequest = jdbcTemplate.query(sql, new Object[] { loadRequestId },
					new ResultSetExtractor<LoadRequestDetail>() {
						@Override
						public LoadRequestDetail extractData(ResultSet rs) throws SQLException, DataAccessException {
							LoadRequestDetail lrd = new LoadRequestDetail();
							if (rs.next()) {
								lrd.setLoadRequestDetailsId(rs.getLong("load_request_details_id"));
								lrd.setActualEndDateTime(rs.getTimestamp("actual_end_datetime"));
								lrd.setEndLocationId(rs.getInt("end_location_id"));
								lrd.setExpectedEndDateTime(rs.getTimestamp("expected_end_datetime"));
								lrd.setLoadRequestId(rs.getInt("load_request_id"));
								lrd.setStartDateTime(rs.getTimestamp("shipping_start_datetime"));
								lrd.setStartLocationId(rs.getInt("start_location_id"));
							}
							return lrd;
						}
					});
			return loadRequest;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return loadRequest;
		}
	}

	@Override
	public LoadRequestDetailsBean getLoadRequestDetailByRequestId(Long loadRequestId) {

		String sql = "select * from load_request_details where load_request_id=? ORDER BY shipping_start_datetime ASC;";
		LoadRequestDetailsBean loadRequest = null;
		try {
			loadRequest = jdbcTemplate.query(sql, new Object[] { loadRequestId },
					new ResultSetExtractor<LoadRequestDetailsBean>() {
						@Override
						public LoadRequestDetailsBean extractData(ResultSet rs)
								throws SQLException, DataAccessException {
							LoadRequestDetailsBean lrd = new LoadRequestDetailsBean();
							if (rs.next()) {
								lrd.setLoadRequestDetailId(rs.getLong("load_request_details_id"));
								lrd.setActualEndDateTime(rs.getTimestamp("actual_end_datetime"));
								lrd.setEndLocationId(rs.getInt("end_location_id"));
								lrd.setExpectedEndDateTime(rs.getTimestamp("expected_end_datetime"));
								lrd.setLoadRequestId(rs.getInt("load_request_id"));
								lrd.setStartDateTime(rs.getTimestamp("shipping_start_datetime"));
								lrd.setStartLocationId(rs.getInt("start_location_id"));
							}
							return lrd;
						}
					});
			return loadRequest;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return loadRequest;
		}
	}

}
