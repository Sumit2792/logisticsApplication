package com.fw.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fw.beans.BidsBean;
import com.fw.dao.IBidManager;
import com.fw.dao.IConfigManager;
import com.fw.dao.IMessageTemplateManager;
import com.fw.dao.ISentMessagesManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.domain.SentMessages;
import com.fw.enums.BidStatus;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.enums.LoadRequestStatus;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidActionException;
import com.fw.exceptions.UnAuthorizedActionException;

@Service
public class BidUtils {

	@Autowired
	private IConfigManager configManager;
	@Autowired
	private MLogisticsPropertyReader mLogiticsPropertyReader;
	@Autowired
	private IBidManager bidManager;
	@Autowired
	private ISentMessagesManager sentMessagesManager;
	@Autowired
	private IUserManager userManager;
	@Autowired
	private IMessageTemplateManager messageTemplateManager;

	private static int bidCount;

	Logger log = Logger.getLogger(BidUtils.class);

	public static double getOnlineCharges(double actualAmount, double faberCharge) {

		double onlineCharges = ((((actualAmount * 0.02) + (faberCharge * 0.02)) / 0.98) + 3) + 0.5;
		onlineCharges = Math.round(onlineCharges);
		return onlineCharges;
	}

	public double getFaberCharge(double actualAmount) throws NumberFormatException, APIExceptions {

		double chargePercentage = mLogiticsPropertyReader.getDefaultFaberChargeInPercentage();
		if (configManager.getConfigMap().get("defaultFaberChargeInPercentage") != null)
			chargePercentage = Double
					.valueOf((String) configManager.getConfigMap().get("defaultFaberChargeInPercentage"));
		double faberCharge = (actualAmount * chargePercentage) / 100;
		return faberCharge;
	}
	
	public double getTotalAmount(double actualAmount) {
		
		double faberCharge = 0;
		try {
			faberCharge = getFaberCharge(actualAmount);
		} catch (NumberFormatException | APIExceptions e) {
			e.printStackTrace();
		}
		double onlineCharges = getOnlineCharges(actualAmount, faberCharge);
		return (faberCharge + onlineCharges +actualAmount);
	}

	public static boolean canEditBid(BidsBean bid) throws UnAuthorizedActionException {

		BidStatus status = bid.getStatus();

		if (status == null)
			return true;

		if (status == BidStatus.AWARDED || status == BidStatus.LOAD_REQUEST_CANCELLED) {
			throw new UnAuthorizedActionException("Bid change is not allowed. Current bid status : " + status);
		}
		return true;
	}

	public static boolean isNewBidAllowed(LoadRequest loadRequest) throws APIExceptions {

		LoadRequestStatus status = loadRequest.getLoadRequestStatus();

		if (status == null)
			return true;

		if (status == LoadRequestStatus.BOOKED || status == LoadRequestStatus.DELIVERED
				|| status == LoadRequestStatus.BLOCKED_REQUEST || status == LoadRequestStatus.EXPIRED
				|| status == LoadRequestStatus.REQUEST_ON_HOLD || status == LoadRequestStatus.CANCELLED) {
			throw new InvalidActionException(
					"New bid is not allowed. Current request status : " + status);
		}
		return true;
	}

	public static boolean canAwardBid(LoadRequest loadRequest) throws APIExceptions {

		LoadRequestStatus status = loadRequest.getLoadRequestStatus();

		if (status == null)
			return true;

		if (status == LoadRequestStatus.REQUEST_ON_HOLD || status == LoadRequestStatus.BIDDING_IN_PROGRESS
				|| status == LoadRequestStatus.AWARDED || status == LoadRequestStatus.REQUESTED) {

			return true;
		} else {
			throw new InvalidActionException("Bid AWARD is not allowed . current request status : " + status);
		}
	}

	public  boolean isLowestBidAmount(double currentBidAmount, long currentBidId, long loadRequest) {

		List<BidsBean> bidList = bidManager.getBidsByLoadRequestId(loadRequest);
		bidCount = bidList.size();
		ArrayList<Double> amountList = new ArrayList<>();
		for (BidsBean bidsBean : bidList) {
			if (bidsBean.getBidId() != currentBidId ) {
				amountList.add(bidsBean.getAmount());
			}
			if(bidCount==1)
				return true;
		}
		int minIndex = minIndex(amountList);
		if (currentBidAmount < amountList.get(minIndex).doubleValue())
			return true;
		else
			return false;

	}

	public static int minIndex(ArrayList<Double> list) {

		if (list != null)
			return list.indexOf(Collections.min(list));
		else
			return -1;
	}

	public void sendMessage(Map<Long, String> userNameAndIds, String smsToBeSend) {

		if (!userNameAndIds.isEmpty()) {

			String uuid = String.valueOf(UUID.randomUUID());

			List<SentMessages> msgList = new ArrayList<SentMessages>();
			Set<Entry<Long, String>> userItr = userNameAndIds.entrySet();
			for (Entry<Long, String> user : userItr) {

				SentMessages msg = new SentMessages();
				msg.setUserId(user.getKey());
				msg.setContact(user.getValue());
				msg.setMessageBody(smsToBeSend);
				msg.setStatus(ContactStatus.PENDING);
				msg.setType(ContactType.SMS);
				msg.setBatchId(uuid);
				msgList.add(msg);
			}
			int[] rows = sentMessagesManager.persistBatchSentMessages(msgList, true);
			if (rows.length < 1)
				log.error("Failed to save message in sent message queue.");

		} else {
			log.error("***************sendMessage : User list is empty.*******************");
		}
	}

	public void sendMessageToLoaderOnLowestBid(LoadRequest loadRequest, double amount, long bidId,
			String subjectLine) {

		try {
			if (loadRequest == null || subjectLine == null)
				return;

			String messageToBeSend = messageTemplateManager.getMessageBySubjectLine(subjectLine);
			if (messageToBeSend != null && messageToBeSend.trim().equals("NOTFOUND")) {
				log.fatal("Message format is not available");
				return;
			}
			// do message formatting here , replacement
			if (isLowestBidAmount(amount, bidId, loadRequest.getLoadRequestId())) {
				
				messageToBeSend=  messageToBeSend.replace("#LoadRequestId#", ""+loadRequest.getLoadRequestId());
				messageToBeSend = messageToBeSend.replace("#Lowest#", ""+amount);
				messageToBeSend = messageToBeSend.replace("#BidCount#", ""+bidCount);
				
				Set<Long> userIds = new HashSet<Long>();
				userIds.add(loadRequest.getUserId());
				Map<Long, String> userNameAndIds = userManager.getUsersByIds(userIds);
				if (messageToBeSend != null)
					sendMessage(userNameAndIds, messageToBeSend);
			}
		
		} catch (Exception e) {
			log.fatal("Error while sending message.  error : " + e.getMessage());
		}
	}

	public void sendAwardedMessageToLoaderAndBidder(LoadRequest loadRequest, BidsBean currentBid, String subjectLine) {

		try {
			if (loadRequest == null || subjectLine == null)
				return;

			String messageToBeSend = messageTemplateManager.getMessageBySubjectLine(subjectLine);
			if (messageToBeSend != null && messageToBeSend.trim().equals("NOTFOUND")) {
				log.fatal("Message format is not available");
				return;
			}
			messageToBeSend =  messageToBeSend.replace("#LoadRequestId#", ""+loadRequest.getLoadRequestId());
			messageToBeSend = messageToBeSend.replace("#Amount#", ""+getTotalAmount(currentBid.getAmount()) );
			//if(messageToBeSend !=null) messageToBeSend = messageToBeSend.replaceAll(Constants.SMS_TOKEN_NEW_LINE, "\\r\\n");
			
			Set<Long> userIds = new HashSet<Long>();
			userIds.add(loadRequest.getUserId());

			Map<Long, String> userNameAndIds = userManager.getUsersByIds(userIds);
			if (messageToBeSend != null)
				sendMessage(userNameAndIds, messageToBeSend);
			userIds.clear();
			BidsBean awardedBid = getAwardedBid(loadRequest.getLoadRequestId());
			if (awardedBid != null) {
				messageToBeSend = messageTemplateManager.getMessageBySubjectLine(MessageTemplateConstants.TO_CAPACITY_PROVIDER_AFTER_REQUEST_IS_AWARDED);
				messageToBeSend =  messageToBeSend.replace("#LoadRequestId#", ""+loadRequest.getLoadRequestId());
				userIds.add(awardedBid.getBidderUserId());
				userNameAndIds = userManager.getUsersByIds(userIds);
				// format messageToBeSend here for
				sendMessage(userNameAndIds, messageToBeSend);
			}
		} catch (Exception e) {
			log.fatal("Error while sending message.  error : " + e.getMessage());
		}
	}
	
	private BidsBean getAwardedBid(long loadRequestId) {
		try {
			BidsBean bid = bidManager.getAwardedBidByLoadRequestId(loadRequestId);
			return bid;
		} catch (APIExceptions e) {
			return null;
		}
	}

}
