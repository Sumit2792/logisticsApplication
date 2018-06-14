package com.fw.controller.impl;

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

import com.fw.beans.DashBoardBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.ResetFilterBean;
import com.fw.beans.UserDashBoardBean;
import com.fw.beans.PaymentHistoryBean;
import com.fw.controller.DashboardController;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IDashboardService;

/**
 * 
 * @author Narendra Gurjar
 *
 */
@Controller
@RequestMapping(value = "/mLogistics")
public class DashboardControllerImpl implements DashboardController {

	@Autowired
	IDashboardService dashBoardService;

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/dashboard/getUserDashBoardHistory/{userId}", method = { GET })
	public ResponseEntity<UserDashBoardBean> getDashBoardforUser(
			@PathVariable(value = "userId", required = true) long userId) throws APIExceptions {
		UserDashBoardBean dashBoard = dashBoardService.getUserDashBoard(userId);
		return new ResponseEntity<UserDashBoardBean>(dashBoard, HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/public/dashboard/applyResetFilter", method = { POST })
	public ResponseEntity<DashBoardBean> getDashboardByFilters(@RequestBody ResetFilterBean resetFilterBean)
			throws APIExceptions {

		DashBoardBean dashBoard = dashBoardService.getDashboardByFilters(resetFilterBean);
		return new ResponseEntity<DashBoardBean>(dashBoard, HttpStatus.OK);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = "/private/dashboard/getPaymentHistory", method = { POST })
	public ResponseEntity<List<PaymentHistoryBean>> getPaymentHistory(@RequestBody PaymentFilters filters) throws APIExceptions {

		List<PaymentHistoryBean> paymentDashBoard = dashBoardService.getPaymentHistory(filters);
		
		return new ResponseEntity<List<PaymentHistoryBean>>(paymentDashBoard, HttpStatus.OK);
	}

}
