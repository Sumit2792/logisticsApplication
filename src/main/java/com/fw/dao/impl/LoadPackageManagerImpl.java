package com.fw.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.beans.LoadPackageDetailsBean;
import com.fw.dao.ILoadPackageManager;
import com.fw.domain.LoadRequestPackage;
import com.fw.enums.LengthUnits;
import com.fw.enums.MaterialTypes;
import com.fw.enums.WeightUnits;
import com.fw.exceptions.APIExceptions;

@Repository
public class LoadPackageManagerImpl implements ILoadPackageManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private Logger log = Logger.getLogger(LoadPackageManagerImpl.class);

	@Override
	public void persist(LoadRequestPackage loadPackage) throws APIExceptions {

		try {
			String sql = "INSERT INTO load_request_packages (load_request_id,material_type,package_length,package_width,package_height,package_count,length_unit,weight,weight_unit,note,created_by,modified_by) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";

			jdbcTemplate.update(sql, loadPackage.getLoadRequestId(), loadPackage.getMaterialType().toDbString(),
					loadPackage.getPackageLength(), loadPackage.getPackageWidth(), loadPackage.getPackageHeight(),
					loadPackage.getPackageCount(),
					(loadPackage.getLengthUnit() == null) ? LengthUnits.CENTIMETER.toDbString()
							: loadPackage.getLengthUnit().toDbString(),
					loadPackage.getWeight(), loadPackage.getWeightUnit().toDbString(), loadPackage.getNote(),
					loadPackage.getCreatedBy(), loadPackage.getModifiedBy());

		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new APIExceptions("Failed to add load package");
		}

	}

	@Override
	public void updateLoadPackageByRequestId(LoadRequestPackage loadPackage) throws APIExceptions {
		try {
			String sql = "UPDATE load_request_packages  SET package_length=?,package_width=?,package_height=?,package_count=?,length_unit=?,material_type=?,weight=?,weight_unit=?,note=?,created_by=?,modified_by=? WHERE load_request_packages_id=?;";
			jdbcTemplate.update(sql, loadPackage.getPackageLength(), loadPackage.getPackageWidth(),
					loadPackage.getPackageHeight(), loadPackage.getPackageCount(),
					(loadPackage.getLengthUnit() == null) ? LengthUnits.CENTIMETER.toDbString()
							: loadPackage.getLengthUnit().toDbString(),
					loadPackage.getMaterialType().toDbString(), loadPackage.getWeight(),
					loadPackage.getWeightUnit().toDbString(), loadPackage.getNote(), loadPackage.getCreatedBy(),
					loadPackage.getModifiedBy(), loadPackage.getLoadRequestPackagesId());
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new APIExceptions("Failed to update load package");
		}
	}

	@Override
	public void deleteLoadPackages(long loadRequestId) throws APIExceptions {

		try {
			String sql = "DELETE from load_request_packages where load_request_id=? ";
			jdbcTemplate.update(sql, loadRequestId);
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Failed to delete load package");
		}

	}

	@Override
	public List<LoadRequestPackage> getAllLoadPackageRowMapper() {
		try {
			return jdbcTemplate.query("select * from load_request_packages;", new RowMapper<LoadRequestPackage>() {
				@Override
				public LoadRequestPackage mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequestPackage loadPackage = new LoadRequestPackage();
					loadPackage.setLoadRequestPackagesId(rs.getLong("load_request_packages_id"));
					loadPackage.setLoadRequestId(rs.getLong("load_request_id"));
					loadPackage.setMaterialType(MaterialTypes.fromString(rs.getString("material_type")));
					loadPackage.setPackageCount((rs.getInt("package_count")));
					loadPackage.setPackageLength((rs.getDouble("package_length")));
					loadPackage.setPackageWidth((rs.getDouble("package_width")));
					loadPackage.setPackageHeight((rs.getDouble("package_height")));
					loadPackage.setLengthUnit((LengthUnits.fromString(rs.getString("length_unit"))));
					loadPackage.setWeight(rs.getDouble("weight"));
					loadPackage.setWeightUnit(WeightUnits.fromString(rs.getString("weight_unit_id")));
					loadPackage.setCreatedBy(rs.getLong("created_by"));
					loadPackage.setCreatedDate(rs.getTimestamp("created_date"));
					loadPackage.setModifiedBy(rs.getLong("modified_by"));
					loadPackage.setModifiedDate(rs.getTimestamp("modified_date"));
					loadPackage.setNote(rs.getString("note"));
					return loadPackage;
				}
			});
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<LoadRequestPackage> getLoadPackageByRequestId(long requestId) {
		String sql = "select * from load_request_packages where load_request_id=?;";
		List<LoadRequestPackage> loadRequest = null;
		try {
			loadRequest = jdbcTemplate.query(sql, new RowMapper<LoadRequestPackage>() {

				@Override
				public LoadRequestPackage mapRow(ResultSet rs, int rowNum) throws SQLException {
					LoadRequestPackage loadPackage = new LoadRequestPackage();
					loadPackage.setLoadRequestId(rs.getLong("load_request_id"));
					loadPackage.setLoadRequestPackagesId(rs.getLong("load_request_packages_id"));
					loadPackage.setMaterialType(MaterialTypes.fromString(rs.getString("material_type")));
					loadPackage.setWeightUnit(WeightUnits.fromString(rs.getString("weight_unit")));
					loadPackage.setPackageCount((rs.getInt("package_count")));
					loadPackage.setPackageLength((rs.getDouble("package_length")));
					loadPackage.setPackageWidth((rs.getDouble("package_width")));
					loadPackage.setPackageHeight((rs.getDouble("package_height")));
					loadPackage.setLengthUnit((LengthUnits.fromString(rs.getString("length_unit"))));
					loadPackage.setWeight(rs.getDouble("weight"));
					loadPackage.setNote(rs.getString("note"));
					loadPackage.setCreatedBy(rs.getLong("created_by"));
					loadPackage.setCreatedDate(rs.getTimestamp("created_date"));
					loadPackage.setModifiedBy(rs.getLong("modified_by"));
					loadPackage.setModifiedDate(rs.getTimestamp("modified_date"));
					return loadPackage;
				}
			}, requestId);
			return loadRequest;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return loadRequest;
		}
	}

	@Override
	public List<LoadPackageDetailsBean> getLoadPackageDetailsByRequestId(long loadRequestId) {

		String sql = "select * from load_request_packages where load_request_id=?";
		List<LoadPackageDetailsBean> loadRequest = new ArrayList<LoadPackageDetailsBean>();
		try {
			loadRequest = jdbcTemplate.query(sql, new RowMapper<LoadPackageDetailsBean>() {

				@Override
				public LoadPackageDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
					LoadPackageDetailsBean loadPackage = new LoadPackageDetailsBean();
					loadPackage.setLoadRequestId(rs.getLong("load_request_id"));
					loadPackage.setLoadRequestPackagesId(rs.getLong("load_request_packages_id"));
					loadPackage.setMaterialType(MaterialTypes.fromString(rs.getString("material_type")));
					loadPackage.setWeightUnit(WeightUnits.fromString(rs.getString("weight_unit")));
					loadPackage.setPackageCount((rs.getInt("package_count")));
					loadPackage.setPackageLength((rs.getDouble("package_length")));
					loadPackage.setPackageWidth((rs.getDouble("package_width")));
					loadPackage.setPackageHeight((rs.getDouble("package_height")));
					loadPackage.setLengthUnit((LengthUnits.fromString(rs.getString("length_unit"))));
					loadPackage.setWeight(rs.getDouble("weight"));
					loadPackage.setNote(rs.getString("note"));
					loadPackage.setCreatedBy(rs.getLong("created_by"));
					loadPackage.setCreatedDate(rs.getTimestamp("created_date"));
					loadPackage.setModifiedBy(rs.getLong("modified_by"));
					loadPackage.setModifiedDate(rs.getTimestamp("modified_date"));
					return loadPackage;
				}

			}, loadRequestId);
			return loadRequest;
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			return loadRequest;
		}
	}

	@Override
	public LoadRequestPackage getSpecificLoadPackageDetailByRequestId(long loadRequestId) {
		LoadRequestPackage loadRequestPackage = null;
		try {
			String sql = "select * from load_request_packages where load_request_id=?";
			loadRequestPackage = jdbcTemplate.queryForObject(sql, new RowMapper<LoadRequestPackage>() {
				@Override
				public LoadRequestPackage mapRow(ResultSet rs, int rownumber) throws SQLException {
					LoadRequestPackage loadPackage = new LoadRequestPackage();
					loadPackage.setLoadRequestId(rs.getLong("load_request_id"));
					loadPackage.setLoadRequestPackagesId(rs.getLong("load_request_packages_id"));
					loadPackage.setMaterialType(MaterialTypes.fromString(rs.getString("material_type")));
					loadPackage.setWeightUnit(WeightUnits.fromString(rs.getString("weight_unit")));
					loadPackage.setPackageCount((rs.getInt("package_count")));
					loadPackage.setPackageLength((rs.getDouble("package_length")));
					loadPackage.setPackageWidth((rs.getDouble("package_width")));
					loadPackage.setPackageHeight((rs.getDouble("package_height")));
					loadPackage.setLengthUnit((LengthUnits.fromString(rs.getString("length_unit"))));
					loadPackage.setWeight(rs.getDouble("weight"));
					loadPackage.setNote(rs.getString("note"));
					loadPackage.setCreatedBy(rs.getLong("created_by"));
					loadPackage.setCreatedDate(rs.getTimestamp("created_date"));
					loadPackage.setModifiedBy(rs.getLong("modified_by"));
					loadPackage.setModifiedDate(rs.getTimestamp("modified_date"));
					return loadPackage;
				}
			}, loadRequestId);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
		}
		return loadRequestPackage;
	}

}
