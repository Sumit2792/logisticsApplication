package com.fw.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fw.dao.IUserFactsManager;
import com.fw.domain.UserFacts;
import com.fw.domain.Users;
import com.fw.enums.Facts;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

@Component
public class ResponseFilter extends OncePerRequestFilter {

	@Autowired 
	private AuthUserDetails authUser;
	
	@Autowired
	private IUserFactsManager userFactsManager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(response);
		wrapper.addHeader(Constants.USER_DETAIL_HEADER_STRING, getHeaderValue());
		filterChain.doFilter(request, wrapper);
	}

	private String getHeaderValue() {
		String details = "";
		if(authUser != null) {
			try {
				Users user = authUser.getAuthUserDetails();
				if(user != null) {
					details = user.getUserId() + "," + user.getUserName() + "," + user.getUserRole() + "," + getUserDisplayName(user.getUserId());
				}
			} catch (InvalidUsernameException e) { }
		}
		return details;
	}
	
	private String getUserDisplayName(long userId) {
		try {
			List<UserFacts> facts = userFactsManager.getUserFactsByUserId(userId);
			Map<Facts, String> factsMap = new HashMap<Facts, String>();
			for(UserFacts fact : facts) {
				factsMap.put(Facts.fromString(fact.getFact()), fact.getValue());
			}
			String displayName = "";
			if(factsMap.get(Facts.FIRST_NAME) != null) {
				displayName = factsMap.get(Facts.FIRST_NAME);
			}
			if(factsMap.get(Facts.MIDDLE_NAME) != null) {
				displayName = displayName + " " + factsMap.get(Facts.MIDDLE_NAME);
			}
			if(factsMap.get(Facts.LAST_NAME) != null) {
				displayName = displayName + " " + factsMap.get(Facts.LAST_NAME);
			}
			return displayName;
		} catch (APIExceptions e) {
			return "";
		}
	}
}
