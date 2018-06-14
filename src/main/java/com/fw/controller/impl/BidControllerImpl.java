package com.fw.controller.impl;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.beans.AddBidsBean;
import com.fw.beans.BidStatusBean;
import com.fw.beans.BidsBean;
import com.fw.beans.DashBoardBean;
import com.fw.controller.BidController;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.services.IBidService;
import com.fw.services.IDashboardService;

@Controller
@RequestMapping(value = "/mLogistics")
public class BidControllerImpl implements BidController {

	private Logger log = Logger.getLogger(BidControllerImpl.class);
	@Autowired
	IBidService bidService;
	@Autowired
	SimpMessagingTemplate template;
	@Autowired
	IDashboardService dashboardService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/bid/addBid", method = { POST })
	public ResponseEntity<AddBidsBean> addBidRequest(@Valid @RequestBody AddBidsBean bidData, BindingResult result)
			throws APIExceptions {

		validateJson(result);
		AddBidsBean bid = bidService.addBid(bidData);

		String dashBoard = getDashboard();
		if (dashBoard != null)
			template.convertAndSend("/public/updateDashboard", dashBoard);

		return new ResponseEntity<AddBidsBean>(bid, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/private/bid/updateBid", method = { PATCH })
	public ResponseEntity<AddBidsBean> updateBidById(@Valid @RequestBody AddBidsBean bidForm, BindingResult result)
			throws APIExceptions {
		
		validateJson(result);
		bidService.updateBidById(bidForm);

		String dashBoard = getDashboard();
		if (dashBoard != null)
			template.convertAndSend("/public/updateDashboard", dashBoard);

		return new ResponseEntity<AddBidsBean>(bidForm, HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/bid/deleteBid", method = { DELETE })
	public void deleteBidById(@RequestParam(value = "bidId", required = true) Long bidId) throws APIExceptions {

		bidService.deleteBidById(bidId);

		String dashBoard = getDashboard();
		if (dashBoard != null)
			template.convertAndSend("/public/updateDashboard", dashBoard);

	}

	@Override
	@RequestMapping(value = "/private/bid/getBidById/{bidId}", method = { GET })
	public ResponseEntity<BidsBean> getBidById(@PathVariable(value = "bidId", required = true) long bidId)
			throws APIExceptions {

		return new ResponseEntity<BidsBean>(bidService.getBidById(bidId), HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/bid/changeStatus", method = { PATCH })
	public ResponseEntity<?> updateBidStatus(@RequestBody BidStatusBean bidStatus) throws APIExceptions {

		bidService.updateBidStatus(bidStatus);

		String dashBoard = getDashboard();
		if (dashBoard != null)
			template.convertAndSend("/public/updateDashboard", dashBoard);

		return new ResponseEntity<Void>(HttpStatus.OK);

	}

	private String getDashboard() {
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
			log.error("Error while getting dashboard json. " + e.getMessage(), e);
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