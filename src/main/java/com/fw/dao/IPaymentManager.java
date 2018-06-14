package com.fw.dao;

import java.util.List;

import com.fw.domain.Payment;

public interface IPaymentManager {
	 
	/**
	 * Persist the object normally.
	 */
	void persist(Payment entity);

	List<Payment> getAllPaymentRowMapper();

	void deletePayment(Payment paymentId);

	void modifyPaymentsByPaymentId(Payment paymentId);
	
	Payment getPaymentById(Long id);
	
	List<Payment> getPaymentByUserId(Long id);

	void persistPaymentRequest(Payment paymentinfo);

	void updatePaymentResponse(Payment paymentinfo, String transactionId);

	Payment getPaymentDetailsByTransactionId(String transactionId);
	
}
