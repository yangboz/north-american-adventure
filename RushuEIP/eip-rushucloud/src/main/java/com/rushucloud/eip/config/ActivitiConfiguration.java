package com.rushucloud.eip.config;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.HistoryServiceImpl;
import org.activiti.engine.impl.IdentityServiceImpl;
import org.activiti.engine.impl.ManagementServiceImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.ldap.LDAPConfigurator;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.annotations.AbstractActivitiConfigurer;
import org.activiti.spring.annotations.EnableActiviti;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.rushucloud.eip.config.LDAPConfiguration;

//@Configuration
@EnableActiviti
@EnableTransactionManagement(proxyTargetClass = true)
@Import({ LDAPConfiguration.class })
// @PropertySource("file:conf/lc-merged.properties")
@EnableAutoConfiguration
public class ActivitiConfiguration {
	@Autowired
	LDAPConfigurator ldapConfigurator;

	@Bean
	public DataSource inMemoryDataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		// basicDataSource.setUsername("sa");
		basicDataSource.setUsername("root");
		// basicDataSource.setUrl("jdbc:h2:mem:activiti");
		basicDataSource
				.setUrl("jdbc:mysql://localhost:3306/activiti?useUnicode=true&characterEncoding=UTF-8");
		basicDataSource.setDefaultAutoCommit(false);
		// basicDataSource.setDriverClassName(org.h2.Driver.class.getName());
		basicDataSource.setDriverClassName(com.mysql.jdbc.Driver.class
				.getName());
		basicDataSource.setPassword("");
		return basicDataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
			DataSource inMemoryDataSource) {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		localContainerEntityManagerFactoryBean
				.setDataSource(inMemoryDataSource);
		localContainerEntityManagerFactoryBean
				.setPackagesToScan(new String[] { "com.rushucloud.eip.models" });
		localContainerEntityManagerFactoryBean
				.setPersistenceUnitName("company");
		localContainerEntityManagerFactoryBean
		.setPersistenceUnitName("activitiprocess");
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		jpaVendorAdapter.setGenerateDdl(false);
		jpaVendorAdapter.setShowSql(false);
		// jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		jpaVendorAdapter
				.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		localContainerEntityManagerFactoryBean
				.setJpaVendorAdapter(jpaVendorAdapter);
		return localContainerEntityManagerFactoryBean;
	}

	@Bean
	public PlatformTransactionManager jpaTransactionManager(
			EntityManagerFactory entityManagerFactoryBean) {
		return new JpaTransactionManager(entityManagerFactoryBean);
	}

	@Value("${LC_NUKE_MAIL_HOST}")
	private String mailHost;
	@Value("${LC_NUKE_MAIL_USERNAME}")
	private String mailUsername;
	@Value("${LC_NUKE_MAIL_PASSWORD}")
	private String mailServerPassword;

	@Bean
	public AbstractActivitiConfigurer abstractActivitiConfigurer(
			final EntityManagerFactory entityManagerFactoryBean,
			final PlatformTransactionManager jpaTransactionManager) {
		return new AbstractActivitiConfigurer() {
			@Override
			public void postProcessSpringProcessEngineConfiguration(
					SpringProcessEngineConfiguration engine) {
				engine.setConfigurators(new ArrayList<ProcessEngineConfigurator>(
						Arrays.asList(ldapConfigurator)));
				// engine.setDatabaseType("h2");
				engine.setDatabaseType("mysql");
				engine.setDataSource(inMemoryDataSource());
				engine.setTransactionManager(jpaTransactionManager);
				engine.setJpaEntityManagerFactory(entityManagerFactoryBean);
				engine.setJpaHandleTransaction(true);
				engine.setJobExecutorActivate(true);
				engine.setJpaCloseEntityManager(false);
				engine.setMailServerDefaultFrom("activiti@rushucloud.com");
				engine.setMailServerHost(mailHost);
				engine.setMailServerPort(587);
				engine.setMailServerUsername(mailUsername);
				engine.setMailServerPassword(mailServerPassword);
				engine.setMailServerUseSSL(true);
				engine.setMailServerUseTLS(true);
				engine.setRepositoryService(repositoryService());
				engine.setTaskService(taskService());
				engine.setManagementService(managementService());
				engine.setRuntimeService(runtimeService());
				engine.setIdentityService(identityService());
				engine.setHistoryService(historyService());
				engine.setHistoryLevel(HistoryLevel.AUDIT);
				Resource[] processResources = new Resource[1];
				ClassPathResource underwritingQueueProcess = new ClassPathResource(
						"org/activiti/test/my-process.bpmn20.xml");
				processResources[0] = underwritingQueueProcess;
				engine.setDeploymentResources(processResources);
				engine.setConfigurators(new ArrayList<ProcessEngineConfigurator>(
						Arrays.asList(ldapConfigurator)));
				engine.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
			}
		};
	}

	@Bean
	public RepositoryServiceImpl repositoryService() {
		RepositoryServiceImpl repositoryService = new RepositoryServiceImpl();
		return repositoryService;
	}

	@Bean
	public TaskServiceImpl taskService() {
		TaskServiceImpl taskService = new TaskServiceImpl();
		return taskService;
	}

	@Bean
	public ManagementServiceImpl managementService() {
		ManagementServiceImpl managementService = new ManagementServiceImpl();
		return managementService;
	}

	@Bean
	public RuntimeServiceImpl runtimeService() {
		RuntimeServiceImpl runtimeService = new RuntimeServiceImpl();
		return runtimeService;
	}

	@Bean
	public HistoryServiceImpl historyService() {
		HistoryServiceImpl historyService = new HistoryServiceImpl();
		return historyService;
	}

	@Bean
	public IdentityServiceImpl identityService() {
		IdentityServiceImpl identityService = new IdentityServiceImpl();
		return identityService;
	}
	
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();
	
	@Bean
	public ActivitiRule activityRule(
			ProcessEngineConfigurationImpl abstractActivitiConfigurer) {
//		activitiRule activityRule = new ActivitiRule();
		activitiRule.setProcessEngineConfiguration(abstractActivitiConfigurer);
		return activitiRule;
	}
}
