package com.fw.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fw.exceptions.APIExceptions;

@Controller
public class WebSocketController {
	
	@MessageMapping("/public/send/message")
    @SendTo("/public/updateDashboard")
    public String onReceivedMesage(String message) throws APIExceptions, JsonProcessingException
    {
       return message;
		
    }
}
