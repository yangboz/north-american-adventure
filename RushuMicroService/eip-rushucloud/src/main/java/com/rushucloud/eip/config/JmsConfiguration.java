package com.rushucloud.eip.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.rushucloud.eip.activemq.ActivemqSender;
import com.rushucloud.eip.consts.JMSConstants;

@Configuration
@PropertySource("classpath:jms-${spring.profiles.active}.properties")
public class JmsConfiguration {

	@Value("${spring.jms.brokerUrl}")
	private String brokerUrl;

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	private static Logger LOG = LogManager.getLogger(JmsConfiguration.class);

	@Bean
	public ActivemqSender activemqSender() {
		//
		ActivemqSender activemqSender = new ActivemqSender();
		//
		JMSConstants.URL_BROKER_ACTIVEMQ = brokerUrl;
		//
		LOG.info("JMSConstants.URL_BROKER_ACTIVEMQ:" + brokerUrl);
		return activemqSender;
	}
	
}