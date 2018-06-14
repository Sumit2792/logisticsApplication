package com.fw.services;

import java.util.List;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

public interface IConfigService {


	ConfigBeans getConfigById(long bidId) throws APIExceptions;

	void deleteConfigById(Long id) throws APIExceptions;

	void addUpdateConfig(List<Config> config) throws APIExceptions;

	void addUpdateConfigProperty(List<ConfigProperties> config) throws APIExceptions;

	ConfigProperties getConfigPropertyById(Long configId) throws APIExceptions;

	void deleteConfigProperty(Long configId) throws APIExceptions;

	List<ConfigProperties> getAllConfigProperties() throws InvalidUsernameException, APIExceptions;

	List<ConfigBeans> getAllConfig(ConfigFilters filters) throws APIExceptions;

	List<ConfigProperties> searchConfigProperties(ConfigPropertyFilters filters) throws APIExceptions;

}
