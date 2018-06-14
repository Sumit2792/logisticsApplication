package com.fw.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fw.beans.DashBoardBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.ResetFilterBean;
import com.fw.beans.UserDashBoardBean;
import com.fw.beans.PaymentHistoryBean;
import com.fw.exceptions.APIExceptions;

public interface DashboardController {

	ResponseEntity<UserDashBoardBean> getDashBoardforUser(long userId) throws APIExceptions;
	
	ResponseEntity<DashBoardBean> getDashboardByFilters(ResetFilterBean resetFilterBean) throws APIExceptions;

	ResponseEntity<List<PaymentHistoryBean>> getPaymentHistory(PaymentFilters filters) throws APIExceptions;
	

}
