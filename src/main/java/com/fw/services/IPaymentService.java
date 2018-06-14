package com.fw.services;

import java.io.IOException;
import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fw.beans.PaymentGatewayRequestBean;
import com.fw.domain.Payment;
import com.fw.exceptions.APIExceptions;


public interface IPaymentService {

	void getPaymentInfo(Payment paymentEntity);

	List<Payment> getAllPayments();

	void deletePayments(Payment paymentEntity);

	void updatePayments(Payment paymentEntity);

	Payment getPaymentInfoById(Long paymentEntity);
	
	List<Payment> getPaymentInfoByUserId(Long paymentEntity);
	
	JSONObject createNewPaymentRequest(PaymentGatewayRequestBean pgRequestBean) throws APIExceptions;

	JSONObject getPaymentDetailsByTransactionId(String transactionId) throws JsonParseException, JsonMappingException, IOException, APIExceptions;
}
