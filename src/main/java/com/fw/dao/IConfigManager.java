package com.fw.dao;

import java.util.List;
import java.util.Map;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidIdException;

public interface IConfigManager {

	/**
	 * Persist the object normally.
	 */
	void persistConfig(List<Config> logEntity)  throws APIExceptions;

	void updateConfigsById(List<Config> logEntity) throws APIExceptions;

	ConfigBeans getConfigById(long bidId)  throws APIExceptions;

	void deleteConfigById(Long Id)  throws APIExceptions;

	Map<String, String> getConfigMap() throws APIExceptions;

	List<ConfigProperties> getAllConfigProperties() throws APIExceptions;

	void deleteConfigPropertyById(Long configId) throws APIExceptions;

	ConfigProperties getConfigPropertyById(Long configId) throws InvalidIdException;

	void persistConfigProperties(List<ConfigProperties> config) throws APIExceptions;

	void updateConfigsPropertiesById(List<ConfigProperties> propertyList) throws APIExceptions;

	List<ConfigBeans> getAllConfigRowMapper() throws APIExceptions;

	List<ConfigBeans> getConfigsByFilters(ConfigFilters filters) throws APIExceptions;

	List<ConfigProperties> searchConfigProperties(ConfigPropertyFilters filters) throws APIExceptions;


}
