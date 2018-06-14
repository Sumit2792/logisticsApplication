package com.fw.services;

import java.util.HashMap;
import java.util.List;

import com.fw.beans.LoadCitiesBean;
import com.fw.domain.LoadRequestCities;
import com.fw.exceptions.APIExceptions;

public interface ILoadRequestCitiesService {

	LoadRequestCities addLoadRequestCities(LoadRequestCities logEntity);

	void updateLoadRequestCitiesById(LoadCitiesBean loadInBetweenCities) throws APIExceptions;

	List<LoadRequestCities> getAllLoadRequestCities();

	LoadRequestCities getLoadRequestCitiesById(long messageId);

	void deleteLoadRequestCitiesById(LoadRequestCities messageId);

	HashMap<String, Object> getCitiesBetweenSourceAndDestination(String sourceCity, String destCity)
			throws APIExceptions;
}
