package com.rushucloud.eip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
<<<<<<< HEAD
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@EnableWebSecurity
@ImportResource("classpath:activiti-standalone-context.xml")
=======

@Configuration
@EnableAutoConfiguration
//@ComponentScan
// @EnableAutoConfiguration(exclude = ProcessEngineAutoConfiguration.class)
//@ImportResource("classpath:/activiti-context.xml")
>>>>>>> 1664948d6c2529cc2350f47393fab954e8781c1d
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}