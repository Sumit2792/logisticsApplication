package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fw.beans.ClientConfigBean;
import com.fw.controller.ClientConfigController;
import com.google.maps.errors.ApiException;

@Controller
@RequestMapping(value = "/mLogistics")
public class ClientConfigControllerImpl implements ClientConfigController {
	
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/public/clientConfig/getClientConfigEnums", method = { GET })
	public ResponseEntity<ClientConfigBean> getClientConfigEnums() throws ApiException {
		
		ClientConfigBean clientConfigBean = new ClientConfigBean();
		return new ResponseEntity<ClientConfigBean>(clientConfigBean, HttpStatus.OK);
	}

}
