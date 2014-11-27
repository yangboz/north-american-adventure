package com.rushucloud.eip;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

//@Configuration
@ComponentScan
// @EnableWebSecurity
@EnableAutoConfiguration
// @EnableAutoConfiguration(exclude={WebSocketAutoConfiguration.class,JpaProcessEngineAutoConfiguration.class})
@ImportResource("classpath:activiti-standalone-context.xml")
public class Application {

	private static Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		// Deploying the process here,avoid duplication to @see: http://forums.activiti.org/content/duplicate-deployment-processes
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"org/activiti/test/ReimbursementRequest.bpmn20.xml")
				.enableDuplicateFiltering()
				.name("reimbursmentApproveSimple")
				.deploy();
		// Log information
		// LOG.info("Number of process definitions: " +
		// repositoryService.createProcessDefinitionQuery().desc().toString());
		LOG.info("Number of process definitions: "
				+ repositoryService.createProcessDefinitionQuery().count());
	}

}