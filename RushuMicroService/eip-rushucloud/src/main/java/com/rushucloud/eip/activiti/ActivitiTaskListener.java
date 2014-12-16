package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rushucloud.eip.activemq.ActivemqSender;

public class ActivitiTaskListener implements TaskListener {

	private static Logger LOG = LoggerFactory
			.getLogger(ActivitiTaskListener.class);

	@Override
	public void notify(DelegateTask delegateTask) {
		try {
			LOG.info("task:" + delegateTask.toString() + ",assignee:"
					+ delegateTask.getAssignee() + ",candidates:"
					+ delegateTask.getCandidates() + ",category:"
					+ delegateTask.getCategory());
			// Connect to ActiveMQ to send message.
			 ActivemqSender sender = new ActivemqSender();
//			ActivemqSender sender = ActivemqSender.getInstance("SAMPLEQUEUE");
			 // Unique the queue name
			 ActivemqSender.queueName += "/"+delegateTask.getAssignee();
			 // Then send corresponding message. 
			sender.sendMessage(delegateTask.toString());
		} catch (Error err) {
			LOG.error(err.toString());
		}
	}

}
