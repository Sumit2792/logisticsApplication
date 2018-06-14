package com.fw.services.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.beans.LoadCitiesBean;
import com.fw.config.AuthUserDetails;
import com.fw.dao.ILoadRequestCitiesManager;
import com.fw.domain.LoadRequestCities;
import com.fw.domain.Users;
import com.fw.enums.UserRoles;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.UnAuthorizedActionException;
import com.fw.services.ILoadRequestCitiesService;

@Service
public class LoadRequestCitiesServiceImpl implements ILoadRequestCitiesService {

	@Autowired
	private ILoadRequestCitiesManager loadRequestCitiesManager;
	@Autowired
	private AuthUserDetails authUser;

	@Override
	@Transactional
	public LoadRequestCities addLoadRequestCities(LoadRequestCities logEntity) {
		if (logEntity != null) {
			return loadRequestCitiesManager.persistLoadRequestCities(logEntity);
		} else
			return null;
	}

	@Override
	@Transactional
	public void deleteLoadRequestCitiesById(LoadRequestCities id) {

		loadRequestCitiesManager.deleteLoadRequestCitiesById(id);
	}

	@Override
	@Transactional
	public List<LoadRequestCities> getAllLoadRequestCities() {

		return loadRequestCitiesManager.getAllLoadRequestCitiesRowMapper();
	}

	@Override
	@Transactional
	public LoadRequestCities getLoadRequestCitiesById(long loadRequestCitiesID) {
		return loadRequestCitiesManager.getLoadRequestCitiesById(loadRequestCitiesID);
	}

	@Override
	public HashMap<String, Object> getCitiesBetweenSourceAndDestination(String sourceCity, String destCity)
			throws APIExceptions {

		if (sourceCity == null)
			throw new APIExceptions("Source city is required");
		if (destCity == null)
			throw new APIExceptions("Destination city is required");

		HashMap<String, Object> cityMap = loadRequestCitiesManager.getCitiesBetweenSourceAndDestination(sourceCity,
				destCity);
		if(cityMap == null ) cityMap= new HashMap<>();
		if (cityMap.size() < 1) {
			cityMap.put("source", sourceCity);
		    cityMap.put("destination", destCity);
			cityMap.put("inBetweenCities", new Object[] {});
		}
		return cityMap;

	}

	@Override
	public void updateLoadRequestCitiesById(LoadCitiesBean loadInBetweenCities) throws APIExceptions {

		if (loadInBetweenCities == null)
			throw new APIExceptions("Invalid request.");
		if (loadInBetweenCities.getSource() == null)
			throw new APIExceptions("Source city is required");
		if (loadInBetweenCities.getDestination() == null)
			throw new APIExceptions("Destination city is required");

		Users user = authUser.getAuthUserDetails();
		long userId = user.getUserId();
		UserRoles userRole = user.getUserRole();

		if (userRole != UserRoles.CSR)
			throw new UnAuthorizedActionException("Unauthorized Access.");

		ObjectMapper mapper = new ObjectMapper();

		String cities = "[]";
		try {
			cities = mapper.writeValueAsString(loadInBetweenCities.getInBetweenCities());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		loadRequestCitiesManager.updateLoadRequestCitiesBySourceAndDesination(userId, loadInBetweenCities.getSource(),
				loadInBetweenCities.getDestination(), cities);
	}

}
