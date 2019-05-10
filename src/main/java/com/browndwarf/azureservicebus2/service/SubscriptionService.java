package com.browndwarf.azureservicebus2.service;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SubscriptionService {

	@Autowired
	@Qualifier("jmsConnectionFactory")
	ConnectionFactory	connectionFactory;
	
	@Autowired
	ConnectionStringBuilder	connectionStringBuilder;
	
	@Autowired
	Context jmsInitialContextFactory;
	
	
    public Message receiveFromSubscription(String subscriptionName)
            throws NamingException, JMSException, InterruptedException {

    	log.info("Subscription {}", subscriptionName);

        Destination subscription = (Destination)jmsInitialContextFactory.lookup(subscriptionName);
        
        // Create Connection
        Connection connection = connectionFactory.createConnection(
        							connectionStringBuilder.getSasKeyName(), 
        							connectionStringBuilder.getSasKey()
        						);
        connection.start();
        // Create Session, no transaction, client ack
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        // Create consumer
        MessageConsumer consumer = session.createConsumer(subscription);
        
        Message	receiveMessage = consumer.receive();
        receiveMessage.acknowledge();
 
        // Set callback listener. Gets called for each received message.
        /*
        consumer.setMessageListener(message -> {
            try {
                System.out.printf("Received message %d with sq#: %s\n",
                        totalReceived.incrementAndGet(),  // increments the counter
                        message.getJMSMessageID());
                message.acknowledge();
            } catch (Exception e) {
                System.out.printf("%s", e.toString());
            }
        });
        */
        consumer.close();
        session.close();
        connection.stop();
        connection.close();
        
        return receiveMessage;
    }
	
}
