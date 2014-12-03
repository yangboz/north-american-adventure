package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivitiExecutionListener implements ExecutionListener {
	private static Logger LOG = LoggerFactory
			.getLogger(ActivitiExecutionListener.class);
	@Override
	public void notify(DelegateExecution execution) throws Exception {
//		 execution.setVariable("variableSetInExecutionListener",
//		 "firstValue");
//		 execution.setVariable("eventReceived", execution.getEventName());
		try {
			LOG.info("ProcessInstanceId:"+execution.getProcessInstanceId()
					.toString()+",event:"+execution.getEventName()+
					",execution:"+execution.toString()+
					",variables:"+execution.getVariables().toString()
					);
			//Connect to ActiveMQ to send message.
//			ActivemqSender sender = new ActivemqSender("SAMPLEQUEUE");
//			ActivemqSender sender = ActivemqSender.getInstance("SAMPLEQUEUE");
//			sender.sendMessage(execution.getEventName());
		} catch (Error err) {
			LOG.error(err.toString());
		}

	}

}
