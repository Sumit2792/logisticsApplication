package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.beans.AddLoadRequest;
import com.fw.beans.DashBoardBean;
import com.fw.beans.LoadRequestBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.controller.LoadRequestController;
import com.fw.domain.LoadRequest;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IDashboardService;
import com.fw.services.ILoadRequestService;
import com.fw.utils.LoadRequestAlgorithm;

@Controller
@RequestMapping(value = "/mLogistics")
public class LoadRequestControllerImpl implements LoadRequestController {

	private Logger log = Logger.getLogger(BidControllerImpl.class);
	
	@Autowired
	ILoadRequestService loadRequestService;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	IDashboardService dashboardService;
	@Autowired
	private LoadRequestAlgorithm loadRequestAlgorithm;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/loadRequests/addLoadRequest", method = { POST })
	public ResponseEntity<AddLoadRequest> addLoadRequest(@Valid @Validated @RequestBody AddLoadRequest loadRequest ,BindingResult result) throws APIExceptions {
		
		validateJson(result);
		AddLoadRequest loadRequestBean = loadRequestService.addLoadRequest(loadRequest);
		
		callLoadRequestAlgorithm(loadRequestBean);
	    
		String dashBoard=getDashboard();
		if(dashBoard != null) template.convertAndSend("/public/updateDashboard",dashBoard); 
		
		return new ResponseEntity<AddLoadRequest>(loadRequestBean, HttpStatus.OK);
	}

	private void callLoadRequestAlgorithm(AddLoadRequest loadRequestBean) {
		LoadRequest request = new LoadRequest();
		request.setLoadRequestId(loadRequestBean.getLoadRequestId());
		request.setUserId(loadRequestBean.getUserId());
		List<LoadRequest> listLoadRequest = new ArrayList<LoadRequest>();
		listLoadRequest.add(request);
		loadRequestAlgorithm.processLoadRequest(listLoadRequest);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/loadRequests/getLoadRequest/{loadRequestId}", method = { GET })
	public ResponseEntity<LoadRequestBean> getLoadRequestById(@PathVariable(value="loadRequestId", required=true) long loadRequestId) throws APIExceptions{
		
		LoadRequestBean loadRequestBean=loadRequestService.getLoadRequestById(loadRequestId);
		return new ResponseEntity<LoadRequestBean>(loadRequestBean, HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/private/loadRequests/updateLoadRequest", method = { PATCH })
	public ResponseEntity<?> updateLoadRequestById(@Valid @RequestBody AddLoadRequest loadRequest , BindingResult result)throws APIExceptions {
		
		validateJson(result);
		loadRequestService.updateLoadRequestById(loadRequest);
		String dashBoard=getDashboard();
		if(dashBoard != null) template.convertAndSend("/public/updateDashboard",dashBoard); 
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/loadRequests/deleteLoadRequest", method = { DELETE })
	public ResponseEntity<?> deleteLoadRequest(@RequestParam(value="loadRequestId" , required=true) Long loadRequestId) throws APIExceptions{
		
		loadRequestService.deleteLoadRequest(loadRequestId);
		String dashBoard=getDashboard();
		if(dashBoard!=null) template.convertAndSend("/public/updateDashboard",dashBoard); 
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = "/private/loadRequests/changeStatus", method = { PATCH })
	public ResponseEntity<?> updateLoadRequestStatus(@RequestBody LoadRequestStatusBean loadStatus) throws APIExceptions, SQLException{
		
		loadRequestService.updateLoadRequestStatus(loadStatus);
		String dashBoard=getDashboard();
		if(dashBoard != null) template.convertAndSend("/public/updateDashboard",dashBoard); 
		return new ResponseEntity<Void>(HttpStatus.OK);
		
	}
	
	private String getDashboard()
	{
		try {
			DashBoardBean dashBoard = dashboardService.getDashboardByFilters(null);
			ObjectMapper mapper = new ObjectMapper();
			String json;
			json = mapper.writeValueAsString(dashBoard);
			return json;
		} catch (JsonProcessingException e) {
			log.error("Error while parsing dashboard json");
			return null;
		} catch (APIExceptions e) {
			log.error("Error while getting dashboard json. "+e.getMessage(),e);
			return null;
		}
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
