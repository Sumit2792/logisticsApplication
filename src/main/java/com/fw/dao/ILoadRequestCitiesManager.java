package com.fw.dao;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import com.fw.beans.LoadCitiesBean;
import com.fw.domain.LoadRequestCities;
import com.fw.exceptions.APIExceptions;

public interface ILoadRequestCitiesManager {

	/**
	 * Persist the object normally.
	 * @throws APIExceptions 
	 */
	LoadRequestCities persistLoadRequestCities(LoadRequestCities postalAddressesEntity);

	void updateLoadRequestCitiesById(LoadRequestCities postalAddressesEntity);

	void deleteLoadRequestCitiesById(LoadRequestCities id) ;

	List<LoadRequestCities> getAllLoadRequestCitiesRowMapper();

	LoadRequestCities getLoadRequestCitiesById(long id);

	void insertLoadRequestCitiesByLoadRequestId(String getCityFromAddress, String getCityToAddress,
			JSONArray inBetweenCities, Long requestId);

	HashMap<String, Object> getCitiesBetweenSourceAndDestination(String source, String destination);

	void updateLoadRequestCitiesBySourceAndDesination(long updatedBy, String source, String destination, String Cities);

}
