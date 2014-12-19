package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class ActivitiExecutionListener implements ExecutionListener {
	private static Logger LOG = LoggerFactory
			.getLogger(ActivitiExecutionListener.class);

	@SuppressWarnings({ "unchecked" })
	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// execution.setVariable("variableSetInExecutionListener",
		// "firstValue");
		// execution.setVariable("eventReceived", execution.getEventName());
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("ProcessInstanceId", execution.getProcessInstanceId()
					.toString());
			jsonObj.put("event", execution.getEventName());
			jsonObj.put("execution", execution.toString());
			jsonObj.put("variables", execution.getVariables().toString());
			LOG.debug("Activiti execution:" + jsonObj.toString());
			// Connect to ActiveMQ to send message.
			// ActivemqSender sender = new ActivemqSender("SAMPLEQUEUE");
			// ActivemqSender sender =
			// ActivemqSender.getInstance("SAMPLEQUEUE");
			// sender.sendMessage(execution.getEventName());
			//TODO:Watch "reimbursementApproved=false", to toggle update the expense status to Reject.
			
		} catch (Error err) {
			LOG.error(err.toString());
		}

	}

}
