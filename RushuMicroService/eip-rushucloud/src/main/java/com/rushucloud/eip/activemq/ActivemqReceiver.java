package com.rushucloud.eip.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rushucloud.eip.consts.JMSConstants;

//@see: http://www.coderpanda.com/jms-example-using-apache-activemq/
public class ActivemqReceiver {
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;

	private static Logger LOG = LoggerFactory.getLogger(ActivemqReceiver.class);

	 public ActivemqReceiver() {
	 }

//	private static ActivemqReceiver instance = null;
//
//	protected ActivemqReceiver(String queueName) {
//		// Exists only to defeat instantiation.
//		this.queueName = queueName;
//	}
//
//	public static ActivemqReceiver getInstance(String queueName) {
//		if (instance == null) {
//			instance = new ActivemqReceiver(queueName);
//		}
//		return instance;
//	}

	public void receiveMessage() {
		try {
			factory = new ActiveMQConnectionFactory(
					JMSConstants.URL_BROKER_ACTIVEMQ);
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(ActivemqSender.queueName);
			consumer = session.createConsumer(destination);
			Message message = consumer.receive();
			if (message instanceof TextMessage) {
				TextMessage text = (TextMessage) message;
				// System.out.println("Message is : " + text.getText());
				LOG.info("Received activemq message is : " + text.getText()+",queueName:"+ActivemqSender.queueName);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
