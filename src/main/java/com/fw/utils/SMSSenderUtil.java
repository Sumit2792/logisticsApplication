package com.fw.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fw.config.Constants;
import com.fw.exceptions.SMSServiceException;

import org.apache.log4j.Logger;

@Configuration
@PropertySource("classpath:mlogistics.properties")
public class SMSSenderUtil {

	public final static Logger log = Logger.getLogger(SMSSenderUtil.class);

	@Value("${sms.smsoutbox.sendSMSURL}")
	private String smsServiceBaseUrl;

	@Value("${sms.smsoutbox.apiKey}")
	private String smsServiceAPIKey;

	@Value("${sms.smsoutbox.campaign}")
	private String smsServiceCampaign;

	@Value("${sms.smsoutbox.routeId}")
	private String smsServiceRouteId;

	@Value("${sms.smsoutbox.type}")
	private String smsServiceType;

	@Value("${sms.smsoutbox.senderId}")
	private String smsServiceSenderId;

	@Value("${sms.smsoutbox.delieveryURL}")
	private String smsServiceDeliveryURL;

	@Value("${sms.smsoutbox.getDLR}")
	private String smsServiceDLR;

	public String sendOTP(String mobileNumber, String message)
			throws SMSServiceException, UnsupportedEncodingException {
		String otp = createRandomRegistryId();
		message = message.replace("#####", otp);
		String shootId = sendMessage(mobileNumber, message);
		if (shootId != null && shootId.contains("SMS-SHOOT-ID/")) {
			shootId = shootId.substring(shootId.lastIndexOf("/") + 1);
			getDeliveryStatus(shootId);
			return otp;
		} else if (shootId != null && shootId.equalsIgnoreCase("Err")) {
			return "Err";
		}
		return null;
	}

	private String createRandomRegistryId() {
		Random r = new Random();
		int numbers = 100000 + (int) (r.nextFloat() * 899900);
		String val = String.valueOf(numbers);
		return val;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// smsoutbox.com related changes

	public String sendMessage(String phoneNumbers, String messageTemplate) throws SMSServiceException {
		String shootID = null;
		try {
			
			messageTemplate = messageTemplate.replace(Constants.SMS_TOKEN_NEW_LINE, "\r\n");

			messageTemplate = URLEncoder.encode(messageTemplate, "UTF-8");

			String sendSMSURL = smsServiceBaseUrl + "key=" + smsServiceAPIKey + "&campaign=" + smsServiceCampaign
					+ "&routeid=" + smsServiceRouteId + "&type=" + smsServiceType + "&contacts=" + phoneNumbers
					+ "&senderid=" + smsServiceSenderId + "&msg=" + messageTemplate;
			// Send data

			HttpURLConnection conn = (HttpURLConnection) new URL(sendSMSURL).openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			String responseMessage = stringBuffer.toString();
			if (responseMessage.contains("SMS-SHOOT-ID/")) {
				shootID = responseMessage;
				log.info("Successfuly submitted sms requests on SmsOutbox.com with response [" + responseMessage + "]");
			} else if (responseMessage.contains("ERR")) {
				log.error("Error in calling SmsOutbox.com for sending SMS. Error:[" + responseMessage + "]");
				shootID = "Err";
			} else {
				log.error("Error in calling SmsOutbox.com for sending SMS. Error:[" + responseMessage + "]");
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			throw new SMSServiceException(
					"Failed to send OTP due to internal server error. We regret inconvenience caused , our team working on this.");

		}
		return shootID;
	}

	public String getDeliveryStatus(String shootIDValue) throws SMSServiceException {

		String delieveryURL = smsServiceDeliveryURL + "/" + smsServiceAPIKey + "/" + smsServiceDLR + "/" + shootIDValue;
		String response;
		// GetDelieveryStatus
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(delieveryURL).openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			response = stringBuffer.toString();
			log.info("Generated Delievery Report = " + response);
		} catch (Exception e) {
			String errorMessage = "Error in calling SmsOutbox.com for sending SMS. Error:[" + e.getMessage() + "]";
			log.error(errorMessage,e);
			throw new SMSServiceException("Failed to verify delivery status of the message");
		}
		return response;

	}

}