package com.fw.utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fw.beans.BidsBean;
import com.fw.beans.FromToAddressBean;
import com.fw.beans.FromToAddressComponent;
import com.fw.beans.LocationBean;
import com.fw.dao.IBidManager;
import com.fw.dao.IMessageTemplateManager;
import com.fw.dao.ISentMessagesManager;
import com.fw.dao.IUserManager;
import com.fw.domain.LoadRequest;
import com.fw.domain.PostalAddresses;
import com.fw.domain.SentMessages;
import com.fw.enums.AddressComponentTypes;
import com.fw.enums.ContactStatus;
import com.fw.enums.ContactType;
import com.fw.enums.LoadRequestStatus;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.BadRequestException;
import com.fw.exceptions.InvalidActionException;
import com.fw.exceptions.SMSServiceException;

@Service
public class LoadRequestUtils {

	@Autowired
	private ISentMessagesManager sentMessagesManager;
	@Autowired
	private IMessageTemplateManager messageTemplateManager;
	@Autowired
	private IBidManager bidManager;
	@Autowired
	private IUserManager userManager;

	static Logger log = Logger.getLogger(LoadRequestUtils.class);

	public static boolean isRequestExpired(Date startShippingDate, LoadRequestStatus currentStatus) {
		if (currentStatus == LoadRequestStatus.REQUESTED && startShippingDate != null) {
			LocalDateTime startShippingTime = new java.sql.Timestamp(startShippingDate.getTime()).toLocalDateTime();

			LocalDateTime ldtNow = DateTimeUtils.getInstantDateTimeWithZoneId("UTC");

			//log.info("-------start ShippingT ime : " + startShippingTime);
			//log.info("-------Now[UTC] : " + ldtNow);

			if (!ldtNow.isBefore(startShippingTime)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean canEditLoadRequest(LoadRequest loadRequest) throws InvalidActionException {

		LoadRequestStatus status = loadRequest.getLoadRequestStatus();

		if (status == null)
			return true;

		if (status == LoadRequestStatus.CANCELLED || status == LoadRequestStatus.BOOKED
				|| status == LoadRequestStatus.DELIVERED || status == LoadRequestStatus.BLOCKED_REQUEST
				|| status == LoadRequestStatus.EXPIRED) {
			throw new InvalidActionException("Invalid Action : Load request change is not allowed.");
		}
		return true;
	}

	public static String getCityStateCountry(String city, String state, String country) {
		String mergedValue = "";
		String pattern = "##";
		city = city.lastIndexOf(pattern) < 0 ? city
				: city.substring(city.lastIndexOf(pattern) + pattern.length(), city.length());
		state = state.lastIndexOf(pattern) < 0 ? state
				: state.substring(state.lastIndexOf(pattern) + pattern.length(), state.length());
		country = country.lastIndexOf(pattern) < 0 ? country
				: country.substring(country.lastIndexOf(pattern) + pattern.length(), country.length());
		mergedValue = city + ", " + state + ", " + country;
		// mergedValue = mergedValue.replaceAll("##", "");

		return mergedValue;
	}

	public static String getAreaCityStateCountry(String area, String city, String state, String country) {
		String mergedValue = "";
		String pattern = "##";
		city = city.lastIndexOf(pattern) < 0 ? city
				: city.substring(city.lastIndexOf(pattern) + pattern.length(), city.length());
		state = state.lastIndexOf(pattern) < 0 ? state
				: state.substring(state.lastIndexOf(pattern) + pattern.length(), state.length());
		country = country.lastIndexOf(pattern) < 0 ? country
				: country.substring(country.lastIndexOf(pattern) + pattern.length(), country.length());
		mergedValue = area + ", " + city + ", " + state + ", " + country;
		// mergedValue = mergedValue.replaceAll("##", "");

		return mergedValue;
	}

	public void sendMessage(Map<Long, String> userNameAndIds, String smsToBeSend) throws SMSServiceException {

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

			sentMessagesManager.persistBatchSentMessages(msgList, true);
		} else {
			log.error("***************sendMessage : User list is empty.*******************");
		}
	}

	public void sendMessageToLoader(LoadRequest loadRequest, String subjectLine) {

		try {
			if (loadRequest == null || subjectLine == null)
				return;

			String messageToBeSend = messageTemplateManager.getMessageBySubjectLine(subjectLine);
			if (messageToBeSend != null && messageToBeSend.trim().equals("NOTFOUND")) {
				log.fatal("Message format is not available");
				return;
			}
			switch (subjectLine) {
			case MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_LOAD_REQUEST_CANCLLED:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;
			case MessageTemplateConstants.TO_LP_and_CPs_AFTER_REQUEST_BLOCK:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;	
			case MessageTemplateConstants.TO_LOAD_PROVIDER_WHEN_LOAD_REQUEST_GOES_ON_HOLD:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;		
			case MessageTemplateConstants.TO_LOAD_PROVIDER_AFTER_REQUEST_IS_RESUME:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;		
			default:
				return ;
			}
			//if(messageToBeSend !=null) messageToBeSend = messageToBeSend.replaceAll(Constants.SMS_TOKEN_NEW_LINE, "\\r\\n");
			Set<Long> userIds = new HashSet<Long>();
			userIds.add(loadRequest.getUserId());
			Map<Long, String> userNameAndIds = userManager.getUsersByIds(userIds);
			if (messageToBeSend != null)
				sendMessage(userNameAndIds, messageToBeSend);
		} catch (Exception e) {
			log.fatal("Error while sending message.  error : " + e.getMessage());
		}
	}

	public void sendMessageToAllBidders(LoadRequest loadRequest, String subjectLine, boolean excludeAwardedBid) {

		try {
			if (loadRequest == null || subjectLine == null)
				return;

			String messageToBeSend = messageTemplateManager.getMessageBySubjectLine(subjectLine);
			if (messageToBeSend != null && messageToBeSend.trim().equals("NOTFOUND")) {
				log.fatal("Message format is not available");
				return;
			}
			
			switch (subjectLine) {
			case MessageTemplateConstants.TO_CAPACITY_PROVIDER_AFTER_REQUEST_IS_EDIT:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;
			case MessageTemplateConstants.TO_ALL_CAPACITY_PROVIDERS_AFTER_LOAD_REQUEST_CANCLLED:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;	
			case MessageTemplateConstants.TO_LP_and_CPs_AFTER_REQUEST_BLOCK:
				messageToBeSend = messageToBeSend.replace("#LoadRequestId#", loadRequest.getLoadRequestId()+"");
				break;		
			default:
				return ;
			}
			//if(messageToBeSend !=null) messageToBeSend = messageToBeSend.replaceAll(Constants.SMS_TOKEN_NEW_LINE, "\\r\\n");

			Map<Long, String> userNameAndIds = getBiddersPhoneNumberAndUserId(loadRequest.getLoadRequestId(),
					excludeAwardedBid);
			if (messageToBeSend != null)
				sendMessage(userNameAndIds, messageToBeSend);
		} catch (Exception e) {
			log.fatal("Error while sending message.  error : " + e.getMessage());
		}
	}

	public void sendMessageToAwardedBidder(LoadRequest loadRequest, String subjectLine) {

		try {
			if (loadRequest == null || subjectLine == null)
				return;

			String messageToBeSend = messageTemplateManager.getMessageBySubjectLine(subjectLine);
			if (messageToBeSend != null && messageToBeSend.trim().equals("NOTFOUND")) {
				log.fatal("Message format is not available");
				return;
			}
			
			//if(messageToBeSend !=null) messageToBeSend = messageToBeSend.replaceAll(Constants.SMS_TOKEN_NEW_LINE, "\\r\\n");

			Set<Long> userIds = new HashSet<Long>();
			userIds.add(loadRequest.getUserId());
			Map<Long, String> userNameAndIds = getAwardedBiddersPhoneNumberAndUserId(loadRequest.getLoadRequestId());
			if (messageToBeSend != null)
				sendMessage(userNameAndIds, messageToBeSend);

		} catch (Exception e) {
			log.fatal("Error while sending message.  error : " + e.getMessage());
		}
	}


	private Map<Long, String> getBiddersPhoneNumberAndUserId(long loadRequestId, boolean excludeAwardedBid) {

		List<Long> userIds = bidManager.getAllBiddersIdsByLoadRequestId(loadRequestId);
		Set<Long> userIdSet = new HashSet<Long>();
		BidsBean bid = null;
		if (excludeAwardedBid) {
			try {
				bid = bidManager.getAwardedBidByLoadRequestId(loadRequestId);
			} catch (APIExceptions e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (Long userId : userIds) {
			userIdSet.add(userId);
		}
		if (bid != null && excludeAwardedBid)
			userIdSet.remove(new Long(bid.getBidderUserId()));

		Map<Long, String> userNameAndIds = userManager.getUsersByIds(userIdSet);
		return userNameAndIds;
	}

	private Map<Long, String> getAwardedBiddersPhoneNumberAndUserId(long loadRequestId) throws APIExceptions {

		BidsBean bid = bidManager.getAwardedBidByLoadRequestId(loadRequestId);

		if (bid == null)
			return new HashMap<Long, String>();
		Set<Long> userIdSet = new HashSet<Long>();
		userIdSet.add(bid.getBidderUserId());
		Map<Long, String> userNameAndIds = userManager.getUsersByIds(userIdSet);
		return userNameAndIds;
	}

	public static PostalAddresses getPostalAddressFromToAddressBean(FromToAddressBean fromToAddressBean,
			String addressType) throws APIExceptions {

		validateAddress(fromToAddressBean, addressType);

		String city, state, country, subLocality, locality = null;

		FromToAddressComponent[] fromAddressComponent = fromToAddressBean.getLocationJSON().getAddress_components();
		subLocality = getAddressComponentByType(fromAddressComponent, AddressComponentTypes.sublocality.toDbString());
		locality = getAddressComponentByType(fromAddressComponent, AddressComponentTypes.locality.toDbString());
		city = getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_2.toDbString());
		state = getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_1.toDbString());
		country = getAddressComponentByType(fromAddressComponent, AddressComponentTypes.country.toDbString());
		
		LocationBean location = null;

		PostalAddresses postalAddresses = new PostalAddresses();

		postalAddresses.setAddressLine1(fromToAddressBean.getAddressLine1());
		if (fromToAddressBean.getLocationJSON() != null) {
			if (fromToAddressBean.getLocationJSON().getGeometry() != null)
				location = fromToAddressBean.getLocationJSON().getGeometry().getLocation();
			postalAddresses.setLocationJSON(fromToAddressBean.getLocationJSON().toString());
			// postalAddresses.setArea(fromToAddressBean.getLocationJSON().getFormatted_address());
		}
		if (!locality.equalsIgnoreCase("locality")) {
			if (!locality.equalsIgnoreCase(city)) {
				postalAddresses.setArea(locality);
			} 
			if (locality.equalsIgnoreCase(city) && !subLocality.equalsIgnoreCase("sublocality")) {
				postalAddresses.setArea(subLocality);
			}
			if (!locality.equalsIgnoreCase(city) && !subLocality.equalsIgnoreCase("sublocality")) {
				postalAddresses.setArea(subLocality);
			}
			if (!locality.equalsIgnoreCase(city) && subLocality.equalsIgnoreCase("sublocality")) {
				postalAddresses.setArea(locality);
			}
			if (!locality.equalsIgnoreCase(city) && !subLocality.equalsIgnoreCase("sublocality")) {
				postalAddresses.setArea(subLocality);
			}
		}

		postalAddresses.setCity(city);
		postalAddresses.setState(state);
		postalAddresses.setCountry(country);
		if (location != null) {
			postalAddresses.setLongitude(Double.valueOf(location.getLng()));
			postalAddresses.setLatitude(Double.valueOf(location.getLat()));
		}

		return postalAddresses;
	}

	public static String getAddressComponentByType(FromToAddressComponent[] fromToAddressComponent,
			String addressType) {

		for (FromToAddressComponent fromToAddressComponent2 : fromToAddressComponent) {

			ArrayList<String> arrTypes = fromToAddressComponent2.getTypes();

			for (String type : arrTypes) {

				if (type.equals(addressType)) {
					return fromToAddressComponent2.getLong_name();
				}
			}
		}

		return addressType;

	}

	private static void validateAddress(FromToAddressBean fromToAddressBean, String addressType)
			throws BadRequestException {

		if (fromToAddressBean == null)
			throw new BadRequestException("Invalid Request.Address is required.");

		if (fromToAddressBean.getLocationJSON() == null)
			throw new BadRequestException(addressType + " : Invalid Address.");

		String city, state, country = null;
		FromToAddressComponent[] fromAddressComponent = fromToAddressBean.getLocationJSON().getAddress_components();
		city = getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_2.toDbString());
		state = getAddressComponentByType(fromAddressComponent,
				AddressComponentTypes.administrative_area_level_1.toDbString());
		country = getAddressComponentByType(fromAddressComponent, AddressComponentTypes.country.toDbString());

		if (city == null) {
			throw new BadRequestException("[" + addressType + "]  Invalid Address. City is not found.");
		}
		if (state == null) {
			throw new BadRequestException("[" + addressType + "]  Invalid Address. State is not found.");
		}
		if (country == null) {
			throw new BadRequestException("[" + addressType + "]  Invalid Address. Country is not found");
		}
		/*
		 * if (fromAddressComponent.getPin() != null) { String pin =
		 * fromAddressComponent.getPin().trim(); if ((pin.trim().length() != 6 &&
		 * fromAddressComponent.getCountry().toUpperCase().contains("INDIA")) ||
		 * (!NumberUtils.isDigits(pin) &&
		 * fromAddressComponent.getCountry().toUpperCase().contains("INDIA"))) throw new
		 * BadRequestException("Invalid pin code."); }
		 */
	}
}
