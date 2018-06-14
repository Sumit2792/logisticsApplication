package com.fw.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.config.AuthUserDetails;
import com.fw.dao.IConfigManager;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.domain.Users;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.DuplicateIdFoundException;
import com.fw.exceptions.InvalidUsernameException;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.IConfigService;

@Service
public class ConfigServiceImpl implements IConfigService {

	@Autowired
	IConfigManager configManager;

	@Autowired
	private AuthUserDetails authUser;
	
	Logger log = Logger.getLogger(ConfigServiceImpl.class);

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUpdateConfig(List<Config> configList) throws APIExceptions {

		if (configList == null)
			throw new BadRequestException("Invalid request.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (userRole != UserRoles.ADMIN || userRole != UserRoles.SUPER_ADMIN)
		{

			HashMap<String, List<Config>> configMap = convertToConfigMap(userId, configList);
			Set<Entry<String, List<Config>>> configSet = configMap.entrySet();
	
			for (Entry<String, List<Config>> entry : configSet) {
				String key = entry.getKey();
				List<Config> configs = entry.getValue();
				if (key.contains("ADD")) {
					checkDuplicateConfigValue(configs);
					configManager.persistConfig(configs);
				} else {
						checkDuplicateConfigValue(configs);
						configManager.updateConfigsById(configs);
					}
			}
		}else {
			throw new UnAuthorizedActionException("UnAuthorized Action. You are not allowed to add configurations.");
		}
	}

	/**
	 * method to check duplicate entry for same property if duplicate is found then
	 * it will return the error.
	 * 
	 * @param configs
	 * @throws DuplicateIdFoundException
	 * 
	 */
	private void checkDuplicateConfigValue(List<Config> configs) throws DuplicateIdFoundException {

		try {
			List<ConfigBeans> allConfigs = configManager.getAllConfigRowMapper();

			for (Config config : configs) {

				for (ConfigBeans configBeans : allConfigs) {

					if (configBeans.getConfigPropertiesId().longValue() == config.getConfigPropertiesId()) {
						throw new DuplicateIdFoundException(
								"Duplicate config found. value : " + config.getValue());
					}
				}
			}
		} catch (DuplicateIdFoundException d) {
			throw new DuplicateIdFoundException(d.getMessage());
		} catch (APIExceptions e) {
			e.printStackTrace();
		}

	}

	/**
	 * this method will be used to separate add and update configs values , also
	 * will validate require things
	 * 
	 * @param loggedInUserId
	 * @param configList
	 * @return
	 * @throws APIExceptions
	 */
	private HashMap<String, List<Config>> convertToConfigMap(long loggedInUserId, List<Config> configList)
			throws APIExceptions {

		HashMap<String, List<Config>> map = new HashMap<>();
		List<Config> addConfigList = new ArrayList<>();
		List<Config> updateConfigList = new ArrayList<>();

		for (Config config : configList) {

			if (config.getValue() == null)
				throw new BadRequestException("config value is required.");
			if (config.getConfigPropertiesId() == 0)
				throw new BadRequestException("Config Properties ID is required.");

			config.setCreatedBy(loggedInUserId);
			config.setModifiedBy(loggedInUserId);

			if (config.getConfigId() != 0) {
				updateConfigList.add(config);
				map.put("EDIT", updateConfigList);
			} else {
				addConfigList.add(config);
				map.put("ADD", addConfigList);
			}
		}
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteConfigById(Long id) throws APIExceptions {

		if (id == null)
			throw new APIExceptions("Config Id is required.");
		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (userRole != UserRoles.ADMIN || userRole != UserRoles.SUPER_ADMIN) {
			configManager.deleteConfigById(id);
		}else {
			throw new UnAuthorizedActionException("UnAuthorized Action. You are not allowed to delete configurations.");
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<ConfigBeans> getAllConfig(ConfigFilters filters) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (!AuthUserDetails.getInternalRoles().containsValue(userRole))
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		return configManager.getConfigsByFilters(filters);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ConfigBeans getConfigById(long bidId) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (!AuthUserDetails.getInternalRoles().containsValue(userRole))
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		return configManager.getConfigById(bidId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUpdateConfigProperty(List<ConfigProperties> config) throws APIExceptions {

		if (config == null)
			throw new BadRequestException("Invalid request.");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (userRole != UserRoles.ADMIN || userRole != UserRoles.SUPER_ADMIN) {

		HashMap<String, List<ConfigProperties>> configMap = convertToConfigPropertyMap(userId, config);
		Set<Entry<String, List<ConfigProperties>>> configSet = configMap.entrySet();

		for (Entry<String, List<ConfigProperties>> entry : configSet) {
			String key = entry.getKey();
			List<ConfigProperties> propertyList = entry.getValue();
			if (key.contains("ADD")) {
				checkDuplicatePropertyName(propertyList);
				configManager.persistConfigProperties(propertyList);
			} else {
				checkDuplicatePropertyName(propertyList);
				configManager.updateConfigsPropertiesById(propertyList);
			}
		}
		}else {
			throw new UnAuthorizedActionException("UnAuthorized Action. You are not allowed to add configurations.");
		}
		
	}

	/**
	 * method to check duplicate entry for same property if duplicate is found then
	 * it will return the error.
	 * 
	 * @param configs
	 * @throws DuplicateIdFoundException
	 * 
	 */
	private void checkDuplicatePropertyName(List<ConfigProperties> properties) throws DuplicateIdFoundException {

		try {
			List<ConfigProperties> allProperties = configManager.getAllConfigProperties();

			for (ConfigProperties property : properties) {

				for (ConfigProperties propertydb : allProperties) {

					if (propertydb.getTitle().equalsIgnoreCase(property.getTitle())) {
						throw new DuplicateIdFoundException(
								"Duplicate property found. Property : " + property.getTitle());
					}
				}
			}
		} catch (DuplicateIdFoundException d) {
			throw new DuplicateIdFoundException(d.getMessage());
		} catch (APIExceptions e) {
			log.error("error while checking duplicate value for config property" ,e);
		}

	}
	/**
	 * this method will be used to separate add and update configs keys[Property
	 * names] , also will validate require things
	 * 
	 * @param loggedInUserId
	 * @param configList
	 * @return
	 * @throws APIExceptions
	 */
	private HashMap<String, List<ConfigProperties>> convertToConfigPropertyMap(long loggedInUserId,
			List<ConfigProperties> configList) throws APIExceptions {

		HashMap<String, List<ConfigProperties>> map = new HashMap<>();
		List<ConfigProperties> addConfigList = new ArrayList<>();
		List<ConfigProperties> updateConfigList = new ArrayList<>();

		for (ConfigProperties config : configList) {

			if (config.getTitle() == null)
				throw new BadRequestException("property title is required.");

			config.setCreatedBy(loggedInUserId);
			config.setModifiedBy(loggedInUserId);

			if (config.getConfigPropertiesId() != 0) {
				updateConfigList.add(config);
				map.put("EDIT", updateConfigList);
			} else {
				addConfigList.add(config);
				map.put("ADD", addConfigList);
			}
		}
		return map;
	}

	@Override
	public ConfigProperties getConfigPropertyById(Long configId) throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (!AuthUserDetails.getInternalRoles().containsValue(userRole))
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		return configManager.getConfigPropertyById(configId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteConfigProperty(Long configId) throws APIExceptions {

		if (configId == null)
			throw new BadRequestException("Config Property Id is required.");
		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (userRole != UserRoles.ADMIN || userRole != UserRoles.SUPER_ADMIN) {
		configManager.deleteConfigPropertyById(configId);
		}else {
		throw new UnAuthorizedActionException("UnAuthorized Action. You are not allowed to delete configurations.");
		}

	}

	@Override
	public List<ConfigProperties> getAllConfigProperties() throws APIExceptions {

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (!AuthUserDetails.getInternalRoles().containsValue(userRole))
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		return configManager.getAllConfigProperties();
	}

	@Override
	public List<ConfigProperties> searchConfigProperties(ConfigPropertyFilters filters) throws APIExceptions {
		
		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();
		if (!AuthUserDetails.getInternalRoles().containsValue(userRole))
			throw new UnAuthorizedActionException("UnAuthorized Action.");
		return configManager.searchConfigProperties(filters);
	}


}
