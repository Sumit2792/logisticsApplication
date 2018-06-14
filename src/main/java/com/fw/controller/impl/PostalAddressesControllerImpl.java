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
import org.springframework.web.bind.annotation.RequestParam;

import com.fw.beans.FromToAddressBean;
import com.fw.controller.PostalAddressesController;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IPostalAddressesService;

@Controller
@RequestMapping(value = "/mLogistics")
public class PostalAddressesControllerImpl implements PostalAddressesController {

	@Autowired
	IPostalAddressesService postalAddressesService;

	@Override
	@RequestMapping(value = "/private/postalAddresses/savePostalAddresses", method = { POST })
	public void savePostalAddressesInfo(@RequestBody PostalAddresses log)  throws APIExceptions{
		postalAddressesService.savePostalAddressesInfo(log);
	}

	@Override
	@RequestMapping(value = "/private/postalAddresses/getAllPostalAddresses", method = { GET })
	public ResponseEntity<List<PostalAddresses>> getAllPostalAddresses() throws APIExceptions{
		return new ResponseEntity<List<PostalAddresses>>(postalAddressesService.getPostalAddresses(), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/postalAddresses/deletePostalAddresses/{postalId}", method = { DELETE })
	public void removePostalAddresses(@RequestBody PostalAddresses postalAddressesLog) throws APIExceptions{
		postalAddressesService.deletePostalAddresses(postalAddressesLog);
	}

	@Override
	@RequestMapping(value = "/private/postalAddresses/updatePostalAddressesInfo", method = { POST })
	public void modifyPostalAddressesByPostalId(@RequestBody PostalAddresses log) throws APIExceptions {
		postalAddressesService.updatePostalAddresses(log);
	}

	@Override
	@RequestMapping(value = "/private/postalAddresses/getPostalAddressesInfoById/{postalId}", method = { GET })
	public ResponseEntity<PostalAddresses> getPostalAddressesInfoById(@PathVariable("postalId") Long log) throws APIExceptions {
		return new ResponseEntity<PostalAddresses>(postalAddressesService.getPostalAddressesInfoById(log),
				HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/postalAddresses/getPostalAddressesByUserId", method = { GET })
	public ResponseEntity<List<PostalAddresses>> getPostalAddressesByUserId(@RequestParam long userId) throws APIExceptions {
		return new ResponseEntity<List<PostalAddresses>>(postalAddressesService.getPostalAddressesByUserId(userId), HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/postalAddresses/savePostalAddressesInfo", method = { POST })
	public ResponseEntity<Long>  savePostalAddresses(@RequestBody FromToAddressBean log)  throws APIExceptions{
		return new ResponseEntity<Long>(postalAddressesService.savePostalAddresses(log),
				HttpStatus.OK);
	}

}
