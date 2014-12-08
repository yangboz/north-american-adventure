package com.rushucloud.eip;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.MultipartConfigElement;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.rushucloud.eip.activemq.ActivemqReceiver;
import com.rushucloud.eip.activemq.ActivemqSender;
import com.rushucloud.eip.consts.JMSConstants;

@Configuration
@ComponentScan
// @EnableWebSecurity
@EnableAutoConfiguration
// @EnableAutoConfiguration(exclude={WebSocketAutoConfiguration.class,JpaProcessEngineAutoConfiguration.class})
@ImportResource("classpath:activiti-standalone-context.xml")
//@see: http://spring.io/guides/gs/accessing-data-rest/
@EnableJpaRepositories
@Import(RepositoryRestMvcConfiguration.class)
//
public class Application {

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(Application.class, args);
//		new SpringApplicationBuilder(Application.class).profiles("test").run(args);
		// Deploying the process here,avoid duplication to @see: http://forums.activiti.org/content/duplicate-deployment-processes
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		//TODO:Assembel the process deployment with configuration.
		//@see:
		repositoryService
				.createDeployment()
				.addClasspathResource("processes/ReimbursementRequest.bpmn20.xml")
				.addClasspathResource("processes/ReimbursementRequest.bpmn20.png")
				.enableDuplicateFiltering()
				.name("reimbursmentApproveSimple")
				.deploy();
		// Log information
		 LOG.info("Process definitions: " +
		 repositoryService.createProcessDefinitionQuery().list().toString());
		LOG.info("Number of process definitions: "
				+ repositoryService.createProcessDefinitionQuery().count());
		/*
		//Starting a process instance
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("employeeName", "employee1");
		variables.put("amountOfMoney", (long)99.8);
		variables.put("reimbursmentMotivation", "Need reimbursement for taxi.");
		//
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("reimbursementRequest",variables);
		//Verify that we started anew process instance
		LOG.info("Process instance:"+processInstance.getId());
		LOG.info("Process instances:"+runtimeService.createProcessInstanceQuery().list().toString());
		LOG.info("Number of process instances:"+runtimeService.createProcessInstanceQuery().count());
		*/
		//ActiveMQ message receiver
//		ActivemqSender sender = ActivemqSender.getInstance("SAMPLEQUEUE");
		String businessKey = repositoryService.createProcessDefinitionQuery().list().get(0).getKey();
		String processDefinitionId = repositoryService.createProcessDefinitionQuery().list().get(0).getId();
		String activemqQueueName = businessKey+"/"+processDefinitionId;
		LOG.info("ActiveMQ initializing with queue name:"+activemqQueueName);
		ActivemqSender sender = new ActivemqSender(activemqQueueName);
		sender.sendMessage("echo");
//		ActivemqReceiver receiver = new ActivemqReceiver("SAMPLEQUEUE");
//		ActivemqReceiver receiver = ActivemqReceiver.getInstance("SAMPLEQUEUE");
//		receiver.receiveMessage();
	}
	//Support file upload function
	//@see https://spring.io/guides/gs/uploading-files/
	@Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128MB");
        factory.setMaxRequestSize("128MB");
        return factory.createMultipartConfig();
    }
}