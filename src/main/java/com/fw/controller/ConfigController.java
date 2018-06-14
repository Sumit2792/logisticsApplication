package com.fw.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.exceptions.APIExceptions;

public interface ConfigController {

	ResponseEntity<ConfigBeans> getConfigById(long configId) throws APIExceptions;

	void removeConfig(Long configId) throws APIExceptions;
	
	
	ResponseEntity<List<ConfigProperties>> getConfigProperties() throws APIExceptions;

	ResponseEntity<ConfigProperties> getConfigPropertyById(Long configId) throws APIExceptions;

	void removeConfigProperty(Long configId) throws APIExceptions;

	void addConfig(List<Config> config) throws APIExceptions;

	void addConfigProperty(List<ConfigProperties> config) throws APIExceptions;

	ResponseEntity<List<ConfigBeans>> getConfig(ConfigFilters filters) throws APIExceptions;

	ResponseEntity<List<ConfigProperties>> searchConfigProperties(ConfigPropertyFilters filters) throws APIExceptions;
	
	

}
