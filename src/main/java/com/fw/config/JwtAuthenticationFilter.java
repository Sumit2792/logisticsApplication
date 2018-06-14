package com.fw.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fw.utils.LocalUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(Constants.HEADER_STRING);
        String languageHeader = req.getHeader(Constants.LANGUAGE_HEADER_STRING);
        String username = null;
        String authToken = null;
        String userrole = null;
        try {
        	if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) 
        	{
        		authToken = header.replace(Constants.TOKEN_PREFIX,"");
        		username = jwtTokenUtil.getUsernameFromToken(authToken);
        		userrole = jwtTokenUtil.getUserroleFromToken(authToken);	
        	} 
        	
        	if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        		//UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        		UserDetails userDetails = new CustomUser(username,"",Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")), languageHeader, userrole);	
        		if (jwtTokenUtil.validateToken(authToken, userDetails)) {
        			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        			SecurityContextHolder.getContext().setAuthentication(authentication);
        		}
        	}
        	chain.doFilter(req, res);
        } catch (SignatureException e) {
        	sendError(languageHeader, "InvalidToken", req, res, chain);
        } catch (IllegalArgumentException e) {
        	sendError(languageHeader, "InvalidToken", req, res, chain);
        } catch (ExpiredJwtException e) {
        	sendError(languageHeader, "TokenExpired", req, res, chain);
        } 
    }

	private void sendError(String languageHeader, String errMessage, HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
    	if(!req.getRequestURI().contains("/public/")) {
    		String message = "";
    		if(languageHeader!=null) {
    			message = LocalUtils.getStringLocale(languageHeader,errMessage);
    		}
    		else {
    			message = LocalUtils.getStringLocale("en-US",errMessage);
    		}
    		res.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    	} else {
    		chain.doFilter(req, res);
    	}
	}

}
