package com.fw.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.LoadCitiesBean;
import com.fw.domain.LoadRequestCities;
import com.fw.exceptions.APIExceptions;

public interface LoadRequestCitiesController {

	void addLoadRequestCities(LoadRequestCities unit) throws APIExceptions;

	ResponseEntity<List<LoadRequestCities>> getLoadRequestCities() throws APIExceptions;

	ResponseEntity<LoadRequestCities> getLoadRequestCitiesById(long bidId) throws APIExceptions;

	void removeUser(LoadRequestCities deleteBlockUser) throws APIExceptions;

	ResponseEntity<HashMap<String, Object>> getAllCitiesByLoadId(String sourceCity, String destinationCity)
			throws APIExceptions;

	ResponseEntity<LoadCitiesBean> updateLoadRequestCitiesById(LoadCitiesBean loadInBetweenCities)
			throws APIExceptions;

}
