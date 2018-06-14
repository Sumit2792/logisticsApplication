package com.fw.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mlogistics.properties")
public class MLogisticsPropertyReader {

	@Value("${defaultBiddingStartWaitTime}")
	private String defaultBiddingStartWaitTime;

	@Value("${defaultBiddingEndWaitTime}")
	private String defaultBiddingEndtWaitTime;

	@Value("${defaultFaberChargeInPercentage}")
	private String defaultFaberChargeInPercentage;

	@Value("${sms.smsoutbox.loginOtpSent}")
	private String loginOtpSent;

	public int getDefaultBiddingStartWaitTime() {

		if (defaultBiddingStartWaitTime != null)
			return Integer.valueOf(defaultBiddingStartWaitTime);
		else
			return 0;

	}

	public int getDefaultBiddingEndtWaitTime() {

		if (defaultBiddingEndtWaitTime != null)
			return Integer.valueOf(defaultBiddingEndtWaitTime);
		else
			return 0;
	}

	public double getDefaultFaberChargeInPercentage() {

		if (defaultFaberChargeInPercentage != null)
			return Double.valueOf(defaultFaberChargeInPercentage).doubleValue();
		else
			return 5;
	}

	public Boolean getLoginOtpSent() {

		try {
			return Boolean.valueOf(loginOtpSent);
		} catch (Exception e) {
			return true;
		}
	}

}
