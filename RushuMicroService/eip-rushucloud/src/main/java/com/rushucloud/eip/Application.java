package com.rushucloud.eip;

import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.WebSocketAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

//@Configuration
@ComponentScan
// @EnableWebSecurity
@EnableAutoConfiguration
//@EnableAutoConfiguration(exclude={WebSocketAutoConfiguration.class,JpaProcessEngineAutoConfiguration.class})
@ImportResource("classpath:activiti-standalone-context.xml")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}