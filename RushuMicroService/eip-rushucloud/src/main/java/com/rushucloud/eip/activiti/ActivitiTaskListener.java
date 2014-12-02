package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.pvm.delegate.ExecutionListenerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivitiTaskListener implements ExecutionListener,TaskListener {

	private static Logger LOG = LoggerFactory.getLogger(ActivitiTaskListener.class);
	
	@Override
	public void notify(DelegateExecution execution) throws Exception {
//		execution.setVariable("variableSetInExecutionListener", "firstValue");
//	    execution.setVariable("eventReceived", execution.getEventName());
	    try {
			LOG.info("ProcessInstanceId:",execution.getProcessInstanceId().toString(),"event:",execution.getEventName(),"execution:",execution.toString());
		}catch(Error err)
		{
			LOG.error(err.toString());
		}
	}

	@Override
	public void notify(DelegateTask task) {
		try {
			LOG.info("task:",task.toString());
		}catch(Error err)
		{
			LOG.error(err.toString());
		}
	}

}
