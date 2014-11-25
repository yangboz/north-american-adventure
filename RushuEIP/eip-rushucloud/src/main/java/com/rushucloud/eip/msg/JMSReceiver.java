package com.rushucloud.eip.msg;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.FileSystemUtils;

import com.rushucloud.eip.consts.JMSConstants;

//Message driven POJO
public class JMSReceiver {
	//
	private static Logger LOG = LoggerFactory.getLogger(JMSReceiver.class);
	/**
	 * Get a copy of the application context
	 */
	@Autowired
	ConfigurableApplicationContext context;

	/**
	 * Message receiver handler
	 */
	public void receiveMessage(String message) {
		LOG.info("Received <" + message + ">");
		context.close();
		FileSystemUtils.deleteRecursively(new File(JMSConstants.LOG_FILE_NAME_ACTIVEMQ));
	}

}
