package com.fw.controller.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.fw.beans.LoadCitiesBean;
import com.fw.controller.LoadRequestCitiesController;
import com.fw.domain.LoadRequestCities;
import com.fw.exceptions.APIExceptions;
import com.fw.services.ILoadRequestCitiesService;

@Controller
@RequestMapping(value = "/mLogistics")
public class LoadRequestCitiesControllerImpl implements LoadRequestCitiesController {
	@Autowired
	ILoadRequestCitiesService loadRequestCitiesService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/loadRequestCities/addLoadRequestCities", method = { POST })
	public void addLoadRequestCities(@RequestBody LoadRequestCities state) throws APIExceptions {
		loadRequestCitiesService.addLoadRequestCities(state);

	}

	@Override
	@RequestMapping(value = "/private/loadRequestCities/getAllLoadRequestCities", method = { GET })
	public ResponseEntity<List<LoadRequestCities>> getLoadRequestCities() throws APIExceptions {

		return new ResponseEntity<List<LoadRequestCities>>(loadRequestCitiesService.getAllLoadRequestCities(),
				HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/loadRequestCities/getInBetweenCities", method = { GET })
	public ResponseEntity<HashMap<String, Object>> getAllCitiesByLoadId(
			@RequestParam(value = "source", required = true) String sourceCity,
			@RequestParam(value = "destination", required = true) String destinationCity) throws APIExceptions {

		return new ResponseEntity<HashMap<String, Object>>(
				loadRequestCitiesService.getCitiesBetweenSourceAndDestination(sourceCity, destinationCity),
				HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/loadRequestCities/updateLoadRequestCities", method = { PATCH })
	public ResponseEntity<LoadCitiesBean> updateLoadRequestCitiesById(@RequestBody LoadCitiesBean loadInBetweenCities)
			throws APIExceptions {
		loadRequestCitiesService.updateLoadRequestCitiesById(loadInBetweenCities);
		return new ResponseEntity<LoadCitiesBean>(loadInBetweenCities, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/loadRequestCities/getLoadRequestCitiesDetails/{userID}", method = { GET })
	public ResponseEntity<LoadRequestCities> getLoadRequestCitiesById(@PathVariable("userID") long loadRequestCitiesID)
			throws APIExceptions {
		return new ResponseEntity<LoadRequestCities>(
				loadRequestCitiesService.getLoadRequestCitiesById(loadRequestCitiesID), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/loadRequestCities/deleteLoadRequestCities/{loadRequestCitiesID}", method = {
			DELETE })
	public void removeUser(@RequestBody LoadRequestCities loadRequestCitiesID) throws APIExceptions {
		loadRequestCitiesService.deleteLoadRequestCitiesById(loadRequestCitiesID);
	}

}
