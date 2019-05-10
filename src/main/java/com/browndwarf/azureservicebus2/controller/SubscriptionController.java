package com.browndwarf.azureservicebus2.controller;

import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.browndwarf.azureservicebus2.service.SubscriptionService;

@Controller 
@RequestMapping("/subscribe")
public class SubscriptionController {

	@Autowired
	SubscriptionService	subscriptionService;
	
	@RequestMapping(method=RequestMethod.GET) 
	public String subscribe(Model model) throws NamingException, JMSException, InterruptedException 
	{ 
		Message receiveMessage = subscriptionService.receiveFromSubscription("SUBSCRIPTION1");
		Map<String, Object>	param = new HashMap<String, Object>();
		
		param.put("id", receiveMessage.getJMSMessageID());
		param.put("data", "Test Data");
		
		model.addAttribute("myData", param); 
		
		return "subscribe"; 
	}

}
