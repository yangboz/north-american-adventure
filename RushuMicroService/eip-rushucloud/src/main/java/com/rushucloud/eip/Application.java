package com.rushucloud.eip;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.rushucloud.eip.activemq.ActivemqSender;
import com.rushucloud.eip.config.JmsConfiguration;
import com.rushucloud.eip.config.PropertiesInitializer;
import com.rushucloud.eip.consts.JMSConstants;
import com.rushucloud.eip.models.Company;

@Configuration
//
@PropertySources({
		@PropertySource(value = "classpath:application-${spring.profiles.active}.properties"),
		@PropertySource(value = "classpath:log4j-${spring.profiles.active}.properties") })
//
@ComponentScan("com.rushucloud.eip")
// @EnableWebSecurity
@EnableAutoConfiguration
// @EnableAutoConfiguration(exclude={WebSocketAutoConfiguration.class,JpaProcessEngineAutoConfiguration.class})
//
@ImportResource("classpath:activiti-standalone-context-${spring.profiles.active}.xml")
// @see: http://spring.io/guides/gs/accessing-data-rest/
@EnableJpaRepositories
//
@Import(RepositoryRestMvcConfiguration.class)
//
public class Application extends SpringBootServletInitializer {
	//
	private static Logger LOG = LogManager.getLogger(Application.class);
	//
	private static Class<Application> applicationClass = Application.class;

	//
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
	//
	public static void main(String[] args) throws InterruptedException {
		//
		SpringApplication.run(applicationClass, args);
		// new
		// SpringApplicationBuilder(Application.class).profiles("test").run(args);
		// Deploying the process here,avoid duplication to @see:
		// http://forums.activiti.org/content/duplicate-deployment-processes
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		// TODO:Assembel the process deployment with configuration.
		// @see:
		repositoryService
				.createDeployment()
				.addClasspathResource(
						"processes/ReimbursementRequest.bpmn20.xml")
				.addClasspathResource(
						"processes/ReimbursementRequest.bpmn20.png")
				.enableDuplicateFiltering().name("reimbursmentApproveSimple")
				.deploy();
		// Log information
		LOG.info("Process definitions: "
				+ repositoryService.createProcessDefinitionQuery().list()
						.toString());
		LOG.info("Number of process definitions: "
				+ repositoryService.createProcessDefinitionQuery().count());
		/*
		 * //Starting a process instance Map<String,Object> variables = new
		 * HashMap<String,Object>(); variables.put("employeeName", "employee1");
		 * variables.put("amountOfMoney", (long)99.8);
		 * variables.put("reimbursmentMotivation",
		 * "Need reimbursement for taxi."); // RuntimeService runtimeService =
		 * processEngine.getRuntimeService(); ProcessInstance processInstance =
		 * runtimeService
		 * .startProcessInstanceByKey("reimbursementRequest",variables);
		 * //Verify that we started anew process instance
		 * LOG.info("Process instance:"+processInstance.getId());
		 * LOG.info("Process instances:"
		 * +runtimeService.createProcessInstanceQuery().list().toString());
		 * LOG.info
		 * ("Number of process instances:"+runtimeService.createProcessInstanceQuery
		 * ().count());
		 */
		// ActiveMQ message receiver
		// ActivemqSender sender = ActivemqSender.getInstance("SAMPLEQUEUE");
		String businessKey = repositoryService.createProcessDefinitionQuery()
				.list().get(0).getKey();
		String processDefinitionId = repositoryService
				.createProcessDefinitionQuery().list().get(0).getId();
		String activemqChannelName = businessKey + "/" + processDefinitionId;
		// Save the queueName.
		ActivemqSender.channelName = activemqChannelName;
		LOG.info("ActiveMQ initializing with channel name:"
				+ ActivemqSender.channelName + ",brokerUrl:"
				+ JMSConstants.URL_BROKER_ACTIVEMQ);
		// ActivemqSender sender = new ActivemqSender(activemqQueueName);
		// sender.sendMessage("echo");//For testing
		// ActivemqReceiver receiver = new ActivemqReceiver("SAMPLEQUEUE");
		// ActivemqReceiver receiver =
		// ActivemqReceiver.getInstance("SAMPLEQUEUE");
		// receiver.receiveMessage();
		// Generate basic data base.
		// // EntityManager entityManager =
		// Persistence.createEntityManagerFactory("activiti_test").createEntityManager();
		// entityManager.getTransaction().begin();
		// Company company = new Company();
		// company.setBusinessKey(businessKey);
		// company.setDate(new Date());
		// company.setDomain("example.com");
		// company.setEmail("sample@example.com");
		// company.setName("EXAMPLE.COM");
		// entityManager.persist(company);
		// entityManager.getTransaction().commit();
	}

	// @see:
	// http://stackoverflow.com/questions/26425067/resolvedspring-boot-access-to-entitymanager
	@Bean
	public PersistenceAnnotationBeanPostProcessor persistenceBeanPostProcessor() {
		return new PersistenceAnnotationBeanPostProcessor();
	}
	// @see: http://stackoverflow.com/questions/23446928/spring-boot-uploading-files-path
	@Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}