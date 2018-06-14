/**
 * 
 */
package com.fw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author ramsingh
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.socket.config.annotation.
	 * WebSocketMessageBrokerConfigurer#registerStompEndpoints(org.springframework.
	 * web.socket.config.annotation.StompEndpointRegistry)
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("mLogistics-WebSocket").setAllowedOrigins("*");
		registry.addEndpoint("mLogistics-WebSocket").setAllowedOrigins("*").withSockJS().setSessionCookieNeeded(true)
				.setHttpMessageCacheSize(1000);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableSimpleBroker("/public/updateDashboard");
		registry.setApplicationDestinationPrefixes("/mLogistics");

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		super.configureClientInboundChannel(registration);
	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration registration) {
		registration.taskExecutor().corePoolSize(50).maxPoolSize(64);
	}

}
