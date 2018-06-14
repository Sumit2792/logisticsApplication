package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fw.beans.ConfigBeans;
import com.fw.beans.ConfigFilters;
import com.fw.beans.ConfigPropertyFilters;
import com.fw.controller.ConfigController;
import com.fw.domain.Config;
import com.fw.domain.ConfigProperties;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IConfigService;

@Controller
@RequestMapping(value = "/mLogistics")
public class ConfigControllerImpl implements ConfigController {

	@Autowired
	IConfigService configService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/config/addConfig", method = { POST })
	public void addConfig(@RequestBody List<Config> config) throws APIExceptions {
		
		configService.addUpdateConfig(config);

	}

	@Override
	@RequestMapping(value = "/private/config/getAllConfig", method = { POST })
	public ResponseEntity<List<ConfigBeans>> getConfig(@RequestBody ConfigFilters filters) throws APIExceptions {

		return new ResponseEntity<List<ConfigBeans>>(configService.getAllConfig(filters), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/config/getConfigDetails/{configId}", method = { GET })
	public ResponseEntity<ConfigBeans> getConfigById(@PathVariable("configId") long blockUsedId) throws APIExceptions {
		return new ResponseEntity<ConfigBeans>(configService.getConfigById(blockUsedId), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/config/deleteConfig/{configId}", method = { DELETE })
	public void removeConfig(@PathVariable("configId") Long configId) throws APIExceptions {
		configService.deleteConfigById(configId);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/config/addConfigProperty", method = { POST })
	public void addConfigProperty(@RequestBody List <ConfigProperties> config) throws APIExceptions {

		configService.addUpdateConfigProperty(config);
	}

	@Override
	@RequestMapping(value = "/private/config/getAllConfigProperties", method = { GET })
	public ResponseEntity<List<ConfigProperties>> getConfigProperties() throws APIExceptions {

		return new ResponseEntity<List<ConfigProperties>>(configService.getAllConfigProperties(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/config/searchConfigProperties", method = { POST })
	public ResponseEntity<List<ConfigProperties>> searchConfigProperties(@RequestBody ConfigPropertyFilters filters) throws APIExceptions {

		return new ResponseEntity<List<ConfigProperties>>(configService.searchConfigProperties(filters), HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/config/getConfigPropertyDetails/{configId}", method = { GET })
	public ResponseEntity<ConfigProperties> getConfigPropertyById(@PathVariable(value="configId", required =true) Long configId) throws APIExceptions {

		return new ResponseEntity<ConfigProperties>(configService.getConfigPropertyById(configId), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/config/deleteConfigProperty/{configId}", method = { DELETE })
	public void removeConfigProperty(@PathVariable(value="configId", required =true)Long configId) throws APIExceptions {

		configService.deleteConfigProperty(configId);
	}

}
