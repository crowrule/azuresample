package com.browndwarf.azureservicebus2.controller;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DemoController {

	@Autowired
	@Qualifier("jmsConnectionFactory")
	ConnectionFactory	connectionFactory;
	
	@Autowired
	ConnectionStringBuilder	connectionStringBuilder;
	
	@Autowired
	Destination	destination;
	
	
	@RequestMapping(value="/")
	public String demoAPI() throws JMSException {
        // Create Connection
        Connection connection = connectionFactory.createConnection(connectionStringBuilder.getSasKeyName(), connectionStringBuilder.getSasKey());
        connection.start();
        // Create Session, no transaction, client ack
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        // Create producer
        MessageProducer producer = session.createProducer(destination);

        // Send messaGES
        for (int i = 0; i < 10; i++) {
            BytesMessage message = session.createBytesMessage();
            message.writeBytes(String.valueOf(i).getBytes());
            producer.send(message);
            System.out.printf("Sent message %d.\n", i + 1);
        }

        producer.close();
        session.close();
        connection.stop();
        connection.close();
        
		return "Completed";		
	}
	
	
}
