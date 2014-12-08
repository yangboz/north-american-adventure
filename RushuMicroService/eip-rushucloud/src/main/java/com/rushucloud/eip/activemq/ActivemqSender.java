package com.rushucloud.eip.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rushucloud.eip.consts.JMSConstants;

//@see: http://www.coderpanda.com/jms-example-using-apache-activemq/
public class ActivemqSender {
	private ConnectionFactory factory = null;
	private Connection connection = null;
	private Session session = null;
	private Destination destination = null;
	private MessageProducer producer = null;
	private static Logger LOG = LoggerFactory.getLogger(ActivemqSender.class);
	//Queue name storage
	static public String queueName = null;
	

	public ActivemqSender(String queueName) {
		ActivemqSender.queueName = queueName;
		//
		try {
			factory = new ActiveMQConnectionFactory(
					JMSConstants.URL_BROKER_ACTIVEMQ);
			connection = factory.createConnection();
			connection.start();
			LOG.info("Start activemq success.");
		} catch (JMSException e) {
			// e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

	// private static ActivemqSender instance = null;
	//
	// protected ActivemqSender(String queueName) {
	// // Exists only to defeat instantiation.
	// this.queueName = queueName;
	// }
	//
	// public static ActivemqSender getInstance(String queueName) {
	// if (instance == null) {
	// instance = new ActivemqSender(queueName);
	// }
	// return instance;
	// }

	public void sendMessage(String value) {
		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(ActivemqSender.queueName);
			producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage();
			// message.setText("Hello ...This is a sample message..sending from FirstClient");
			message.setText(value);
			producer.send(message);
			// System.out.println("Sent: " + message.getText());
			LOG.info("Sent activemq message: " + message.getText());
		} catch (JMSException e) {
			// e.printStackTrace();
			LOG.error(e.getMessage());
		}
	}

}
