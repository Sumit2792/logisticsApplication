package com.fw.services;

import java.util.List;

import com.fw.beans.DashBoardBean;
import com.fw.beans.PaymentFilters;
import com.fw.beans.ResetFilterBean;
import com.fw.beans.UserDashBoardBean;
import com.fw.beans.PaymentHistoryBean;
import com.fw.exceptions.APIExceptions;

public interface IDashboardService {

	UserDashBoardBean getUserDashBoard(long userId) throws APIExceptions;

	DashBoardBean getDashboardByFilters(ResetFilterBean resetFilterBean) throws APIExceptions;

	List<PaymentHistoryBean> getPaymentHistory(PaymentFilters filters) throws APIExceptions;


}
