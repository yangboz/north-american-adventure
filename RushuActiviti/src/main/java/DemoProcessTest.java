/**
 * 
 */
import java.io.FileInputStream;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * @author yangboz
 *
 */
public class DemoProcessTest {
	//
	private static String realPath = "/Users/yangboz/Documents/Git/north-american-adventure/RushuActiviti/src/main/resources/diagrams/";
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		//
		ProcessEngine processEngine = ProcessEngineConfiguration  
                .createProcessEngineConfigurationFromResource("activiti.cfg.xml")  
                .buildProcessEngine();  
		//
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		//Deploy
		repositoryService.createDeployment().addInputStream("DemoProcess.bpmn", new FileInputStream(realPath+"DemoProcess.bpmn")).deploy();
		//
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("DemoProcess");
		//
		String procId = instance.getId();
		System.out.println("procId:"+procId);
		//
		TaskService taskService = processEngine.getTaskService();
		List<Task> tasks = taskService.createTaskQuery().taskDefinitionKey("firstTask").list();
		for(Task task : tasks){
			System.out.println("Following task is:taskId="+task.getId()+" taskName="+task.getName());
			//
			taskService.claim(task.getId(), "testUser");
		}
		//
		tasks = taskService.createTaskQuery().taskAssignee("testUser").list();
		for (Task task : tasks) {  
            System.out.println("Task for testUser: " + task.getName());  
            //
            taskService.complete(task.getId());  
        }  
		System.out.println("Number of tasks for testUser: "  
                + taskService.createTaskQuery().taskAssignee("testUser").count()); 
		// 
        tasks = taskService.createTaskQuery().taskDefinitionKey("secondTask").list();  
        for (Task task : tasks) {  
            System.out.println("Following task is : taskID -" +task.getId()+" taskName -"+ task.getName());  
            taskService.claim(task.getId(), "testUser");  
        }  
        //
        for (Task task : tasks) {  
            taskService.complete(task.getId());  
        }  
        //
        HistoryService historyService = processEngine.getHistoryService();  
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult();  
        System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
		//
	}
}
