package org.activiti.designer.test;

import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import javax.annotation.Resource;
 
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/Users/yangboz/Documents/Git/north-american-adventure/RushuActiviti/src/main/resources/spring-config/beans.xml"})
public class ProcessTestHumanServiceTaskExample {
	private String filename = "/Users/yangboz/Documents/Git/north-american-adventure/RushuActiviti/src/main/resources/diagrams/HumanAndServiceTaskExample.bpmn";
	/**
	 * Inject repository service
	 */
	@Resource
	private RepositoryService repositoryService;
	/**
	 * Inject runtime service
	 */	
	@Resource
	private RuntimeService runtimeService;
 
	/**
	 * Inject task service
	 */	
	@Resource
	private TaskService taskService;
 
	@Test
	public void startProcess() throws Exception {
 
		/*
		 * Deploy the process
		 */
		repositoryService.createDeployment().enableDuplicateFiltering().addInputStream("HumanAndServiceTaskExample.bpmn20.xml",
				new FileInputStream(filename)).deploy();
 
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("processStartedBy", "test@Rushucloud.com");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HumanAndServiceTaskExample", variableMap);
 
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
 
	@Test
	public void claimAndCompleteHumanTask() throws Exception {
		List<Task> tasks= taskService.createTaskQuery().processDefinitionKey("HumanAndServiceTaskExample").taskDefinitionKey("HumanTaskExample").list();
		for(Task task:tasks){
			taskService.claim(task.getId(), "test");
			Map<String, Object> variableMap = new HashMap<String, Object>();
			variableMap.put("HumanTaskCompletedBy", "test");
			taskService.complete(task.getId(),variableMap);
		}
	}
}
