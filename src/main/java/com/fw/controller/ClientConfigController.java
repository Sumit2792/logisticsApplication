package com.fw.controller;

import org.springframework.http.ResponseEntity;

import com.fw.beans.ClientConfigBean;
import com.google.maps.errors.ApiException;

/**
 * Controller to pass client required configurations from server.
 * @author Vikas Sonwal
 *
 */
public interface ClientConfigController {
	
	/**
	 * Method to serve configuration enums
	 * @return API response with all required configuration enums
	 * @throws ApiException
	 */
	ResponseEntity<ClientConfigBean> getClientConfigEnums() throws ApiException;
}
