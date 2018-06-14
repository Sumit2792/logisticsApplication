package com.fw.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.dao.IConfigManager;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidIdException;

@Repository
public class ConfigManagerImpl implements IConfigManager {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger log = Logger.getLogger(ConfigManagerImpl.class);

	@Override
	public void persistConfig(List<Config> config) throws APIExceptions {

		try {

			String sql = "INSERT INTO config (config_properties_id , value, created_by, modified_by) "
					+ " VALUES(?,?,?,?);";

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, config.get(i).getConfigPropertiesId());
					ps.setString(2, config.get(i).getValue());
					ps.setLong(3, config.get(i).getCreatedBy());
					ps.setLong(4, config.get(i).getModifiedBy());
				}

				@Override
				public int getBatchSize() {
					return config.size();
				}
			});
			log.info(rowUpdated.length + " rows added.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error while adding configurations.");
		}
	}

	@Override
	public void updateConfigsById(List<Config> config) throws APIExceptions {

		String sql = "UPDATE config  SET config_properties_id=?, value=?, modified_by=? WHERE config_id=?;";
		try {

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setLong(1, config.get(i).getConfigPropertiesId());
					ps.setString(2, config.get(i).getValue());
					ps.setLong(3, config.get(i).getCreatedBy());
					ps.setLong(4, config.get(i).getConfigId());
				}

				@Override
				public int getBatchSize() {
					return config.size();
				}
			});
			log.info(rowUpdated.length + " rows updated.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error while updating configurations.");
		}
	}

	@Override
	public void deleteConfigById(Long Id) throws APIExceptions {

		try {
			String sql = "DELETE from config where config_id=? ;";
			jdbcTemplate.update(sql, Id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal Server Error , Failed to delete the configuration.");
		}

	}

	@Override
	public List<ConfigBeans> getAllConfigRowMapper() throws APIExceptions {
		try {

			StringBuilder sql = new StringBuilder(
					"SELECT c.* , cp.title FROM config  c JOIN config_properties cp ON cp.config_properties_id = c.config_properties_id ;");

			return jdbcTemplate.query(sql.toString(), new RowMapper<ConfigBeans>() {
				@Override
				public ConfigBeans mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigBeans config = new ConfigBeans();
					config.setConfigId(rs.getLong("config_id"));
					config.setConfigPropertiesId(rs.getLong("config_properties_id"));
					config.setTitle(rs.getString("title"));
					config.setValue(rs.getString("value"));
					config.setCreatedBy(rs.getLong("created_by"));
					config.setModifiedBy(rs.getLong("modified_by"));
					config.setCreatedDate(rs.getTimestamp("created_date"));
					config.setModifiedDate(rs.getTimestamp("modified_date"));
					return config;
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("No Data Found in Config Table.");
		}

	}

	@Override
	public List<ConfigBeans> getConfigsByFilters(ConfigFilters filters) throws APIExceptions {
		try {

			StringBuilder sql = new StringBuilder(
					"SELECT c.* , cp.title FROM config  c JOIN config_properties cp ON cp.config_properties_id = c.config_properties_id WHERE 1=1 ");

			if (filters.getPropertyId() != null)
				sql.append(" and c.config_properties_id =" + filters.getPropertyId());

			if (filters.getValue() != null && !"".equals(filters.getValue()))
				sql.append(" and c.value ilike '" + filters.getValue() + "%'");

			if (filters.getTitle() != null && !"".equals(filters.getTitle()))
				sql.append(" and cp.title ilike '" + filters.getTitle() + "%'");

			if (filters.getLimit() != null && filters.getLimit()>0) {
				sql.append(" LIMIT " + filters.getLimit());

				if (filters.getPage() != null && filters.getPage()!=0 ) {

					sql.append(" OFFSET " + ((filters.getPage() - 1) * filters.getLimit()));
				}
			} else if ((filters.getLimit() == null || filters.getLimit()==0) && filters.getPage() != null && filters.getPage()>=0 ) {

				sql.append(" OFFSET " + filters.getPage());
			}
			//log.info(sql.toString());
			return jdbcTemplate.query(sql.toString(), new RowMapper<ConfigBeans>() {
				@Override
				public ConfigBeans mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigBeans config = new ConfigBeans();
					config.setConfigId(rs.getLong("config_id"));
					config.setConfigPropertiesId(rs.getLong("config_properties_id"));
					config.setTitle(rs.getString("title"));
					config.setValue(rs.getString("value"));
					config.setCreatedBy(rs.getLong("created_by"));
					config.setModifiedBy(rs.getLong("modified_by"));
					config.setCreatedDate(rs.getTimestamp("created_date"));
					config.setModifiedDate(rs.getTimestamp("modified_date"));
					return config;
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("No Data Found in Config Table.");
		}

	}

	@Override
	public ConfigBeans getConfigById(long configId) throws APIExceptions {
		try {
			String sql = "SELECT c.* , cp.title FROM config  c JOIN config_properties cp ON cp.config_properties_id = c.config_properties_id where config_id=?;";
			return jdbcTemplate.queryForObject(sql, new RowMapper<ConfigBeans>() {
				@Override
				public ConfigBeans mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigBeans logEntity = new ConfigBeans();
					logEntity.setConfigId(rs.getLong("config_id"));
					logEntity.setConfigPropertiesId(rs.getLong("config_properties_id"));
					logEntity.setTitle(rs.getString("title"));
					logEntity.setValue(rs.getString("value"));
					logEntity.setCreatedBy(rs.getLong("created_by"));
					logEntity.setModifiedBy(rs.getLong("modified_by"));
					logEntity.setCreatedDate(rs.getTimestamp("created_date"));
					logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
					return logEntity;
				}
			}, configId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InvalidIdException("No data found in Config for config ID :" + configId);
		}
	}

	@Override
	public HashMap<String, String> getConfigMap() throws APIExceptions {
		try {
			String sql = "select c.value , cp.title from config  c JOIN config_properties cp ON cp.config_properties_id = c.config_properties_id;";
			return jdbcTemplate.query(sql, new ResultSetExtractor<HashMap<String, String>>() {

				@Override
				public HashMap<String, String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					HashMap<String, String> logEntity = new HashMap<String, String>();
					while (rs.next()) {
						logEntity.put(rs.getString("title"), rs.getString("value"));
					}
					return logEntity;
				}

			});
		} catch (EmptyResultDataAccessException e) {
			log.error(e.getMessage(), e);
			return new HashMap<String, String>();
		} catch (DataAccessException e) {
			log.error(e.getMessage(), e);
			return new HashMap<String, String>();
		}
	}

	@Override
	public List<ConfigProperties> getAllConfigProperties() throws APIExceptions {

		try {
			String sql = "SELECT * FROM  config_properties;";
			return jdbcTemplate.query(sql, new RowMapper<ConfigProperties>() {
				@Override
				public ConfigProperties mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigProperties logEntity = new ConfigProperties();
					logEntity.setConfigPropertiesId(rs.getLong("config_properties_id"));
					logEntity.setTitle(rs.getString("title"));
					logEntity.setCreatedBy(rs.getLong("created_by"));
					logEntity.setModifiedBy(rs.getLong("modified_by"));
					logEntity.setCreatedDate(rs.getTimestamp("created_date"));
					logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
					return logEntity;
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("No Data Found in Config Property.");
		}

	}

	@Override
	public void deleteConfigPropertyById(Long configId) throws APIExceptions {

		try {
			String sql = "DELETE from config where config_properties_id=? ;";
			jdbcTemplate.update(sql, configId);
			sql = "DELETE from config_properties where config_properties_id=? ;";
			jdbcTemplate.update(sql, configId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal Server Error , Failed to delete the configuration property name");
		}
	}

	@Override
	public ConfigProperties getConfigPropertyById(Long configId) throws InvalidIdException {
		try {
			String sql = "SELECT * FROM  config_properties  where config_properties_id=?;";
			return jdbcTemplate.queryForObject(sql, new RowMapper<ConfigProperties>() {
				@Override
				public ConfigProperties mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigProperties logEntity = new ConfigProperties();
					logEntity.setConfigPropertiesId(rs.getLong("config_properties_id"));
					logEntity.setTitle(rs.getString("title"));
					logEntity.setCreatedBy(rs.getLong("created_by"));
					logEntity.setModifiedBy(rs.getLong("modified_by"));
					logEntity.setCreatedDate(rs.getTimestamp("created_date"));
					logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
					return logEntity;
				}
			}, configId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InvalidIdException("No data found in Config property for ID :" + configId);
		}
	}

	@Override
	public void updateConfigsPropertiesById(List<ConfigProperties> config) throws APIExceptions {

		String sql = "UPDATE config_properties  SET  title = ?, modified_by = ? WHERE config_properties_id=?;";
		try {

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {

					ps.setString(1, config.get(i).getTitle());
					ps.setLong(2, config.get(i).getModifiedBy());
					ps.setLong(3, config.get(i).getConfigPropertiesId());
				}

				@Override
				public int getBatchSize() {
					return config.size();
				}
			});
			log.info(rowUpdated.length + " rows updated.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal Server Error , Failed to update the property keys.");
		}
	}

	@Override
	public void persistConfigProperties(List<ConfigProperties> config) throws APIExceptions {

		try {

			String sql = "INSERT INTO config_properties ( title, created_by, modified_by)  VALUES(?,?,?);";

			int rowUpdated[] = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setString(1, config.get(i).getTitle());
					ps.setLong(2, config.get(i).getCreatedBy());
					ps.setLong(3, config.get(i).getModifiedBy());
				}

				@Override
				public int getBatchSize() {
					return config.size();
				}
			});
			log.info(rowUpdated.length + " rows added.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("Internal server error while adding config property");
		}
	}

	@Override
	public List<ConfigProperties> searchConfigProperties(ConfigPropertyFilters filters) throws APIExceptions {
		try {
			StringBuilder sql = new StringBuilder("SELECT * FROM  config_properties WHERE 1=1 ");

			if (filters.getTitle() != null && !"".equals(filters.getTitle()))
				sql.append(" and title ilike '" + filters.getTitle() + "%'");

			if (filters.getLimit() != null && filters.getLimit()>0) {
				sql.append(" LIMIT " + filters.getLimit());

				if (filters.getPage() != null && filters.getPage()!=0 ) {

					sql.append(" OFFSET " + ((filters.getPage() - 1) * filters.getLimit()));
				}
			} else if ((filters.getLimit() == null || filters.getLimit()==0) && filters.getPage() != null && filters.getPage()>=0 ) {

				sql.append(" OFFSET " + filters.getPage());
			}
			//log.info(sql.toString());
			return jdbcTemplate.query(sql.toString(), new RowMapper<ConfigProperties>() {
				@Override
				public ConfigProperties mapRow(ResultSet rs, int rownumber) throws SQLException {
					ConfigProperties logEntity = new ConfigProperties();
					logEntity.setConfigPropertiesId(rs.getLong("config_properties_id"));
					logEntity.setTitle(rs.getString("title"));
					logEntity.setCreatedBy(rs.getLong("created_by"));
					logEntity.setModifiedBy(rs.getLong("modified_by"));
					logEntity.setCreatedDate(rs.getTimestamp("created_date"));
					logEntity.setModifiedDate(rs.getTimestamp("modified_date"));
					return logEntity;
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new APIExceptions("No Data Found in Config Property.");
		}
	}
}
