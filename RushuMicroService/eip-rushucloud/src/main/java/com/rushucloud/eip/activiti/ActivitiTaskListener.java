package com.rushucloud.eip.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivitiTaskListener implements TaskListener {

	private static Logger LOG = LoggerFactory.getLogger(ActivitiTaskListener.class);
	
	@Override
	public void notify(DelegateTask task) {
		try {
			// Pass the values we get back to the work-flow 
			DelegateExecution execution = task.getExecution();		
			execution.setVariable("apcwf_someId", task.getVariable("apcwf_someId"));
			execution.setVariable("apcwf_someText", task.getVariable("apcwf_sometext"));
			LOG.info("task:",task.toString(),"execution:",execution.toString());
		}catch(Error err)
		{
			LOG.error(err.toString());
		}
	}

}
