package com.fw.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fw.beans.BidsBean;
import com.fw.beans.LoadRequestStatusBean;
import com.fw.beans.PaymentGatewayRequestBean;
import com.fw.beans.PaymentResponseBean;
import com.fw.beans.PaymentResponseTransactionBean;
import com.fw.beans.UserDetailsBean;
import com.fw.dao.IBidManager;
import com.fw.dao.ILoadRequestManager;
import com.fw.dao.IMessageTemplateManager;
import com.fw.dao.IPaymentManager;
import com.fw.dao.IUserFactsManager;
import com.fw.dao.IUserManager;
import com.fw.domain.Payment;
import com.fw.domain.Users;
import com.fw.enums.Facts;
import com.fw.enums.LoadRequestStatus;
import com.fw.enums.PaymentDirection;
import com.fw.enums.PaymentReceiveMethod;
import com.fw.exceptions.APIExceptions;
import com.fw.services.IPaymentService;
import com.fw.utils.InstamojoPaymentGateway;
import com.fw.utils.LocalUtils;
import com.fw.utils.MessageTemplateConstants;
import com.fw.utils.MessageTemplateUtils;
import com.fw.utils.SMSSenderUtil;

@Service
public class PaymentServiceImpl implements IPaymentService {

	public final static Logger log = Logger.getLogger(PaymentServiceImpl.class);

	@Autowired
	private IPaymentManager paymentManager;

	@Autowired
	private ILoadRequestManager loadRequestManager;

	@Autowired
	private IBidManager bidManager;

	@Autowired
	private IUserManager userManager;

	@Autowired
	private IUserFactsManager userFactsManager;

	@Autowired
	private IMessageTemplateManager messageTemplateManager;

	@Autowired
	private MessageTemplateUtils messageUtils;

	@Autowired
	private SMSSenderUtil sMSSenderUtil;

	@Override
	@Transactional
	public List<Payment> getAllPayments() {

		return paymentManager.getAllPaymentRowMapper();
	}

	@Override
	@Transactional
	public void deletePayments(Payment payment) {
		paymentManager.deletePayment(payment);
	}

	@Override
	@Transactional
	public void updatePayments(Payment payment) {
		paymentManager.modifyPaymentsByPaymentId(payment);
	}

	@Override
	@Transactional
	public void getPaymentInfo(Payment paymentEntity) {
		paymentManager.persist(paymentEntity);

	}

	@Override
	@Transactional
	public Payment getPaymentInfoById(Long paymentEntity) {
		return paymentManager.getPaymentById(paymentEntity);
	}

	@Override
	@Transactional
	public List<Payment> getPaymentInfoByUserId(Long paymentEntity) {
		return paymentManager.getPaymentByUserId(paymentEntity);
	}

	Payment paymentinfo = new Payment();
	UserDetailsBean users = new UserDetailsBean();

	@Override
	@Transactional
	public JSONObject createNewPaymentRequest(PaymentGatewayRequestBean pgRequestBean) throws APIExceptions {
		JSONObject jsonObject = null;
		Users users = userManager.getUserByUserId(pgRequestBean.getUserId());
		boolean userEmailExist = userFactsManager.verifyUserEmailExist(pgRequestBean.getUserId(),
				Facts.EMAIL_ID.toDbString());
		if (userEmailExist) {
			userFactsManager.updateUserEmail(pgRequestBean.getUserId(), pgRequestBean.getUserEmail(),
					Facts.EMAIL_ID.toDbString());
		} else {
			userFactsManager.persistUserEmail(pgRequestBean.getUserId(), pgRequestBean.getUserEmail(),
					Facts.EMAIL_ID.toDbString());
		}
		try {
			jsonObject = InstamojoPaymentGateway.paymentOrder(pgRequestBean, users);
			if (jsonObject != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				PaymentResponseBean paymentResponse = objectMapper.readValue(jsonObject.toString(),
						PaymentResponseBean.class);
				paymentinfo.setPaymentGatewayRequestId(paymentResponse.getOrder().getId());
				paymentinfo.setPaymentGatewayPaymentId(paymentResponse.getOrder().getTransaction_id());
				paymentinfo.setAmountRecieved(paymentResponse.getOrder().getAmount());
				paymentinfo.setAmountRequested(pgRequestBean.getAmountRequested());
				paymentinfo.setCurrency(paymentResponse.getOrder().getCurrency());
				paymentinfo.setBidId(pgRequestBean.getBidId());
				paymentinfo.setLoadRequestId(pgRequestBean.getLoadRequestId());
				paymentinfo.setReceiveMethod(PaymentReceiveMethod.PAYMENT_GATEWAY);
				paymentinfo.setUserId(pgRequestBean.getUserId());
				paymentinfo.setPaymentDirections(PaymentDirection.CREDIT);
				paymentinfo.setPaymentStatus(paymentResponse.getOrder().getStatus());
				paymentinfo.setPaymentTime(paymentResponse.getOrder().getCreated_at());
				paymentinfo.setCreatedBy(pgRequestBean.getUserId());
				paymentinfo.setModifiedBy(pgRequestBean.getUserId());

				String getPaymentGatewayResponse = paymentResponse.toString();
				paymentinfo.setPaymentGatewayResponse(getPaymentGatewayResponse);

				paymentManager.persistPaymentRequest(paymentinfo);
				return jsonObject;
			} else {
				throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "PaymentFailure"));
			}
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "PaymentFailure"));
		}
	}

	@Override
	public JSONObject getPaymentDetailsByTransactionId(String transactionId)
			throws JsonParseException, JsonMappingException, IOException, APIExceptions {
		try {
			JSONObject getTranasactionJsonObject = null;
			LoadRequestStatusBean loadRequest = new LoadRequestStatusBean();
			getTranasactionJsonObject = InstamojoPaymentGateway.getDetailsByTransactionId(transactionId);
			if (getTranasactionJsonObject != null) {
				paymentinfo = paymentManager.getPaymentDetailsByTransactionId(transactionId);
				ObjectMapper objectMapper = new ObjectMapper();
				PaymentResponseTransactionBean paymentTransactionResponse = objectMapper
						.readValue(getTranasactionJsonObject.toString(), PaymentResponseTransactionBean.class);
				if (paymentTransactionResponse.getStatus().equals("completed")) {
					paymentinfo.setAmountRecieved(paymentTransactionResponse.getAmount());
					paymentinfo.setPaymentStatus(paymentTransactionResponse.getStatus());
					paymentinfo.setPaymentTime(paymentTransactionResponse.getCreated_at());
					String getPaymentGatewayResponse = getTranasactionJsonObject.toString();
					paymentinfo.setPaymentGatewayResponse(getPaymentGatewayResponse);
					paymentManager.updatePaymentResponse(paymentinfo, transactionId);

					// Update load request details
					loadRequest.setLoadRequestId(paymentinfo.getLoadRequestId());
					loadRequest.setStatus(LoadRequestStatus.BOOKED.getValue());
					loadRequest.setModifiedBy(paymentinfo.getUserId());
					loadRequestManager.updateLoadRequestStatus(loadRequest);

					// Send Message to load provider.
					BidsBean bidsBean = bidManager.getAwardedBidByLoadRequestId(paymentinfo.getLoadRequestId());
					long bidderUserid = bidsBean.getBidderUserId();
					users = userManager.getUserInfoByUserId(bidderUserid);

					String message = messageTemplateManager
							.getMessageBySubjectLine(MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_REQUEST_IS_BOOKED);
					message = messageUtils.afterPaymentLP_CPFinalSMSBody(message, loadRequest.getLoadRequestId(),
							users.getUserName());
					log.info("Message to Booked Load Provider [" + message + "].");
					String bookedLP = paymentTransactionResponse.getPhone();
					log.info("====bookedLP===== :" + bookedLP);
					bookedLP = bookedLP.replaceAll("[-+.^:,]", "");
					log.info("====bookedLP After===== :" + bookedLP);
					sMSSenderUtil.sendMessage(bookedLP, message);

					// Send Message to Awarded Capacity Provider
					message = messageTemplateManager.getMessageBySubjectLine(
							MessageTemplateConstants.TO_AWARDED_CAPACITY_PROVIDER_AFTER_REQUEST_IS_BOOKED);
					message = messageUtils.afterPaymentLP_CPFinalSMSBody(message, loadRequest.getLoadRequestId(),
							paymentTransactionResponse.getPhone());
					log.info("Message to Awarded Capacity Provider [" + message + "].");

					sMSSenderUtil.sendMessage(users.getUserName(), message);

					// Send Message to other capacity provider
					List<BidsBean> unAwardedBidsBean = bidManager
							.getNotAwardedBidsByLoadRequestId(paymentinfo.getLoadRequestId());
					List<Long> bidderUsersList = new ArrayList<Long>();
					for (BidsBean bidbean : unAwardedBidsBean) {
						bidderUsersList.add(bidbean.getBidderUserId());
					}
					List<Users> unAwardedUsersList = userManager.getUnAwardedCPUsersAfterLRBooked(bidderUsersList);
					List<String> unAwardedUsers = new ArrayList<String>();
					for (Users userList : unAwardedUsersList) {
						unAwardedUsers.add(userList.getUserName());
					}
					String userIdList = Arrays.toString(unAwardedUsers.toArray()).replace("[", "").replace("]", "");
					userIdList = userIdList.replaceAll(" ", "");
					message = messageTemplateManager.getMessageBySubjectLine(
							MessageTemplateConstants.TO_OTHER_CAPACITY_PROVIDERS_AFTER_REQUEST_IS_BOOKED);
					message = messageUtils.afterPaymentLP_CPFinalSMSBody(message, loadRequest.getLoadRequestId(),
							users.getUserName());
					log.info("Message to other capacity provider [" + message + "].");
					sMSSenderUtil.sendMessage(userIdList, message);
					return getTranasactionJsonObject;
				} else {
					throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "PaymentFailure"));
				}
			} else {
				throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "PaymentFailure"));
			}
			
		} catch (Exception e) {
			throw new APIExceptions(LocalUtils.getStringLocale("mlogistics_locale", "PaymentFailure"));
		}
	}
}
