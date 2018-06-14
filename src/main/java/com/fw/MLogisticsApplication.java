package com.fw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


import org.apache.log4j.Logger;

@SpringBootApplication
@EnableScheduling
public class MLogisticsApplication {
	private final static Logger logger = Logger.getLogger(MLogisticsApplication.class);

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(MLogisticsApplication.class, args);	
		logger.info("Server Started ...."+applicationContext.getApplicationName());
	}
}
