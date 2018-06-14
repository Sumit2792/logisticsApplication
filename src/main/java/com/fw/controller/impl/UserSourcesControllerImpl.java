package com.fw.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.fw.controller.UserSourcesController;
import com.fw.domain.UserSources;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IUserSourcesService;

@Controller
@RequestMapping(value = "/mLogistics")
public class UserSourcesControllerImpl implements UserSourcesController {
	
	@Autowired
	IUserSourcesService UserSourcesService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/userSources/addUserSources", method = { POST })
	public void addUserSources(@RequestBody UserSources state) throws APIExceptions {
		UserSourcesService.addUserSources(state);

	}

	@Override
	@RequestMapping(value = "/private/userSources/getaddUserSources", method = { GET })
	public ResponseEntity<List<UserSources>> getUserSources() throws APIExceptions{

		return new ResponseEntity<List<UserSources>>(UserSourcesService.getAllUserSources(), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/userSources/updateUserSources", method = { PATCH })
	public ResponseEntity<UserSources> updateUserSourcesById(@RequestBody UserSources bidForm) throws APIExceptions{
		UserSourcesService.updateUserSourcesById(bidForm);
		return new ResponseEntity<UserSources>(bidForm, HttpStatus.OK);
	}


	@Override
	@RequestMapping(value = "/private/userSources/getUserSourcesDetails/{userSourceID}", method = { GET })
	public ResponseEntity<UserSources> getUserSourcesById(@PathVariable("userSourceID") long userSourceID) throws APIExceptions {
		return new ResponseEntity<UserSources>(UserSourcesService.getUserSourcesById(userSourceID), HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/userSources/deleteUserSources/{userSourceID}", method = { DELETE })
	public void removeUser(@RequestBody UserSources userSourceID) throws APIExceptions {
		UserSourcesService.deleteUserSourcesById(userSourceID);
	}

}
