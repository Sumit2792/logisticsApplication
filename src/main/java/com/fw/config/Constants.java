package com.fw.config;

public class Constants {

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = ((24 * 60 * 60 ) * 10); // 10 days
	public static final String SIGNING_KEY = "mlogistics#faberwork";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String LANGUAGE_HEADER_STRING = "mlogistics_locale";
	public static final String USER_DETAIL_HEADER_STRING = "user-detail";
	public static final String USER_FACT_NEW_NUMBER = "NEW_PHONE";
	public static final String SMS_TOKEN_NEW_LINE = "<BR>";
	public static final int LOAD_REQUEST_ALGORITHM_DAEMON_BID_COUNT = 3;
}
