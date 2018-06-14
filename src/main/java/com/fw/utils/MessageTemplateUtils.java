package com.fw.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fw.beans.LoadPackageDetailsBean;
import com.fw.dao.ILoadPackageManager;
import com.fw.dao.ILoadRequestDetailManager;
import com.fw.dao.IPostalAddressesManager;
import com.fw.domain.LoadRequestDetail;
import com.fw.domain.PostalAddresses;
import com.fw.exceptions.APIExceptions;

@Service
public class MessageTemplateUtils {
	
	@Autowired
	private ILoadPackageManager loadPackageManager;
	
	@Autowired
	private ILoadRequestDetailManager loadRequestDetailManager;

	@Autowired
	private IPostalAddressesManager postalAddressesManager;
	
	private final String source = "source";
	private final String destination = "destination";
	
	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}

	/**
	 * Method to replace placeholder in the SMS template for new load request.
	 * This will return the SMS after replacing all the placeholder required to prepare the message.
	 * This method should be used when you want a new load request marketing message.
	 * @param message message template to replace placeholder with values
	 * @param loadRequestId
	 * @param source city name
	 * @param destination city name
	 * @return string message
	 */
	public String getLoadRequestFinalSMSBody(String message, long loadRequestId, String source, String destination) {
		List<LoadPackageDetailsBean> packageList = loadPackageManager.getLoadPackageDetailsByRequestId(loadRequestId);
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_LOAD_REQUEST_ID, String.valueOf(loadRequestId));
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_FROM_CITY, source);
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_TO_CITY, destination);
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_WEIGHT, getWeightForSMS(packageList));
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_TYPE, getTypeForSMS(packageList));
		return message;
	}

	/**
	 * Method to get types of all packages
	 * @param packageList
	 * @return comma separated string of material types used in the packages
	 */
	private String getTypeForSMS(List<LoadPackageDetailsBean> packageList) {
		List<String> type = new ArrayList<String>();
		for(LoadPackageDetailsBean bean : packageList) {
			type.add(bean.getMaterialType().toDbString());
		}
		if(type.isEmpty()) {
			return "";
		} else {
			return Arrays.toString(type.toArray()).replace("[", "").replace("]", "").replace(", ", ",");
		}
	}

	/**
	 * Method to get all package weights with unit
	 * @param packageList
	 * @return comma separated string of weights and weight units in the packages
	 */
	private String getWeightForSMS(List<LoadPackageDetailsBean> packageList) {
		List<String> weight = new ArrayList<String>();
		for(LoadPackageDetailsBean bean : packageList) {
			weight.add(String.valueOf(bean.getWeight()*bean.getPackageCount()) + bean.getWeightUnit().name());
		}
		if(weight.isEmpty()) {
			return "";
		} else {
			return Arrays.toString(weight.toArray()).replace("[", "").replace("]", "").replace(", ", ",");
		}
	}

	/**
	 * Method to get source and destination cities for the supplied load request id
	 * @param loadRequestId
	 * @return a map having source and destination cities
	 * @throws APIExceptions if there is no source and destination city available for the supplied load request
	 */
	public Map<String, String> getCitiesByLoadRequest(long loadRequestId) throws APIExceptions {
		Map<String, String> cities = new HashMap<String, String>();
		
		LoadRequestDetail loadRequestDetail = loadRequestDetailManager.getLoadRequestDetailById(loadRequestId);

		if(loadRequestDetail != null) {
			List<Long> postalAddIds = new ArrayList<Long>();
			postalAddIds.add(loadRequestDetail.getStartLocationId());
			postalAddIds.add(loadRequestDetail.getEndLocationId());
			
			List<PostalAddresses> listOfAddresses = postalAddressesManager.getPostalAddressesByIdList(postalAddIds);
			String source = "", destination = "";
			if ((listOfAddresses != null)) {
				for (PostalAddresses postalAdress : listOfAddresses) {
					if (postalAdress.getPostalAddressId() == loadRequestDetail.getStartLocationId()) {
						source = postalAdress.getCity();
					} else if (postalAdress.getPostalAddressId() == loadRequestDetail.getEndLocationId()) {
						destination = postalAdress.getCity();
					} else {
						throw new APIExceptions("either source or destination not available for load request [" + loadRequestId + "]");
					}
				}
				if(source != null && destination != null) {
					source = source.substring(source.lastIndexOf("#") + 1);
					destination = destination.substring(destination.lastIndexOf("#") + 1);
				} else {
					throw new APIExceptions("either source or destination not available for load request [" + loadRequestId + "]");
				}
			} else {
				throw new APIExceptions("either source or destination not available for load request [" + loadRequestId + "]");
			}
			cities.put(getSource(), source);
			cities.put(getDestination(), destination);
		}
		return cities;
	}

	public String afterPaymentLP_CPFinalSMSBody(String message, long loadRequestId, String userName) {
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_LOAD_REQUEST_ID, String.valueOf(loadRequestId));
		message = message.replace(MessageTemplateConstants.MESSAGE_PLACEHOLDER_CONTACT_NUMBER, userName);
		return message;
	}
	
}
