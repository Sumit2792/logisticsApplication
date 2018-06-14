package com.fw.controller.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import com.fw.beans.UserFactsBean;
import com.fw.controller.UserFactsController;
import com.fw.domain.UserFacts;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IUserFactsService;

@Controller
@RequestMapping(value = "/mLogistics")
public class UserFactsControllerImpl implements UserFactsController {

	@Autowired
	IUserFactsService userFactsService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/userFacts/addUserFacts", method = { POST })
	public void addUserFacts(@Valid @RequestBody UserFactsBean facts, BindingResult result) throws APIExceptions {
		validateJson(result);
		userFactsService.addUserFacts(facts);

	}

	@Override
	@RequestMapping(value = "/private/userFacts/getUserFactsDetails/{userId}", method = { GET })
	public ResponseEntity<List<UserFacts>> getUserFactsByUserId(
			@PathVariable(value = "userId", required = true) long userId) throws APIExceptions {

		return new ResponseEntity<List<UserFacts>>(userFactsService.getUserFactsById(userId), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/userFacts/deleteUserFacts/{factId}", method = { DELETE })
	public void removeUser(@PathVariable(value = "factId", required = true) long factId) throws APIExceptions {

		userFactsService.deleteUserFactsById(factId);
	}

	private void validateJson(BindingResult result) throws BadRequestException {

		if (result.hasErrors()) {
			List<FieldError> errors = result.getFieldErrors();
			String errorMessage = "";
			for (FieldError fieldError : errors) {
				errorMessage += fieldError.getDefaultMessage();
			}
			throw new BadRequestException(errorMessage);
		}
	}

}
