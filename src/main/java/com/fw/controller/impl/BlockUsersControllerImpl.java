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

import com.fw.controller.BlockUsersController;
import com.fw.domain.BlockUsers;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IBlockUsersService;

@Controller
@RequestMapping(value = "/mLogistics")
public class BlockUsersControllerImpl implements BlockUsersController {
	
	@Autowired
	IBlockUsersService blockUsersService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/blockedUsers/addBlockUsers", method = { POST })
	public void addBlockUsers(@RequestBody BlockUsers state) throws APIExceptions {
		blockUsersService.addBlockUsers(state);

	}

	@Override
	@RequestMapping(value = "/private/blockedUsers/getaddBlockUsers", method = { GET })
	public ResponseEntity<List<BlockUsers>> getBlockUsers() throws APIExceptions{

		return new ResponseEntity<List<BlockUsers>>(blockUsersService.getAllBlockUsers(), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/blockedUsers/updateBlockUser", method = { PATCH })
	public ResponseEntity<BlockUsers> updateBlockUsersById(@RequestBody BlockUsers bidForm) throws APIExceptions{
		blockUsersService.updateBlockUsersById(bidForm);
		return new ResponseEntity<BlockUsers>(bidForm, HttpStatus.OK);
	}


	@Override
	@RequestMapping(value = "/private/blockedUsers/getBlockUserDetails/{userID}", method = { GET })
	public ResponseEntity<BlockUsers> getBlockUsersById(@PathVariable("userID") long blockUsedId) throws APIExceptions {
		return new ResponseEntity<BlockUsers>(blockUsersService.getBlockUsersById(blockUsedId), HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(value = "/private/blockedUsers/deleteBlockUser/{userID}", method = { DELETE })
	public void removeUser(@RequestBody BlockUsers blockUsedId) throws APIExceptions {
		blockUsersService.deleteBlockUsersById(blockUsedId);
	}

}
