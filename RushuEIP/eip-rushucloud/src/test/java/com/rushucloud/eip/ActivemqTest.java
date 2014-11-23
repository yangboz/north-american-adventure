/**
 * @author yangboz
 * @see https://spring.io/guides/gs/messaging-jms/
 */
package com.rushucloud.eip;

import java.io.File;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.util.FileSystemUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import com.rushucloud.eip.consts.JMSConstants;

public class ActivemqTest {
	/**
	 * Get a copy of the application context
	 */
	@Autowired
	ConfigurableApplicationContext context;

	@Test
	public void testActivemq(){
		//JMS send/receive message testing
		//Clean out the ActiveMQ log data.
		FileSystemUtils.deleteRecursively(new File(JMSConstants.LOG_FILE_NAME_ACTIVEMQ));
		//Send message
		MessageCreator messageCreator = new MessageCreator(){
			public Message createMessage(Session session) throws JMSException{
				return session.createTextMessage("ActivemqTest->testActivemq->ping!");
			}
		};
		//JMSTemplate
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("ActivemqTest->testActivemq->sendingMessage!");
		jmsTemplate.send(JMSConstants.JMS_DESTINATION_ACTIVEMQ,messageCreator);
	}
}
