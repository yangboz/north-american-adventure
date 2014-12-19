package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rushucloud.eip.activemq.ActivemqSender;

@SuppressWarnings("serial")
public class ActivitiTaskListener implements TaskListener {
	//
	private static Logger LOG = LoggerFactory
			.getLogger(ActivitiTaskListener.class);

	@SuppressWarnings({ "unchecked"})
	@Override
	public void notify(DelegateTask delegateTask) {
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("taskId", delegateTask.getId());
			jsonObj.put("assignee", delegateTask.getAssignee());
			jsonObj.put("category", delegateTask.getCategory());
			jsonObj.put("candidates", delegateTask.getCandidates().toString());
			//LOG info.
			LOG.debug("Activiti task:" + jsonObj.toString());
			// Connect to ActiveMQ to send message.
			ActivemqSender sender = new ActivemqSender();
			// Unique the queue name
			ActivemqSender.queueName = ActivemqSender.channelName + "/"
					+ delegateTask.getAssignee();
			// Then send corresponding message.
			sender.sendMessage(jsonObj.toJSONString());
		} catch (Error err) {
			LOG.error(err.toString());
		}
	}

}
