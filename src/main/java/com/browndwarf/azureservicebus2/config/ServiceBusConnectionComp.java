package com.browndwarf.azureservicebus2.config;



import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.qpid.jms.jndi.JmsInitialContextFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;

@Component
public class ServiceBusConnectionComp {
	
	private String connectionString = "Endpoint=sb://solumservicebus.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=2LSzZl/x7G2A2sTQ6ALvX2BgIF+fnk2Jegws8uIq3vM=";

	private String topicName = "azuretesttopic";
	
	private String subscriptionName = "Subscription1";

	
	@Bean
	public ConnectionStringBuilder connectionStringBuilder() {
		return new ConnectionStringBuilder(connectionString);
	}
	
	@Bean
	public Context jmsInitialContextFactory() throws NamingException {
		JmsInitialContextFactory	jmsContaxtFactory = new JmsInitialContextFactory();
		
        ConnectionStringBuilder csb = connectionStringBuilder();

        // set up the JNDI context 
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("connectionfactory.SBCF", "amqps://" + csb.getEndpoint().getHost() + "?amqp.idleTimeout=120000&amqp.traceFrames=true");
        hashtable.put("topic.TOPIC", topicName);
        
        String subscriptionString = new StringBuilder(topicName).append("//Subscriptions//").append(subscriptionName).toString();
        hashtable.put("queue.SUBSCRIPTION1", subscriptionString);
        
        return jmsContaxtFactory.getInitialContext(hashtable);
	}
	
	@Bean
	@Qualifier("jmsConnectionFactory")
	public ConnectionFactory connectionFactory() throws NamingException {
		return (ConnectionFactory)jmsInitialContextFactory().lookup("SBCF");
	}
	
	@Bean
	public Destination destination() throws NamingException {
		return (Destination)jmsInitialContextFactory().lookup("TOPIC");
	}
}
