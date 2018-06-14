package com.fw.config;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.security.core.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fw.utils.LocalUtils;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5261575978196967472L;

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String languageHeader = request.getHeader(Constants.LANGUAGE_HEADER_STRING);
		String message = "";
    	if(languageHeader!=null) {
    		message = LocalUtils.getStringLocale(languageHeader,"AuthenticationRequired");
    	}
    	else {
    		message = LocalUtils.getStringLocale("en-US","AuthenticationRequired");
    	}
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }

	

	
}
