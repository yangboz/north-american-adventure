package com.rushucloud.eip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
// @EnableAutoConfiguration(exclude = ProcessEngineAutoConfiguration.class)
//@ImportResource("classpath:/activiti-context.xml")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}