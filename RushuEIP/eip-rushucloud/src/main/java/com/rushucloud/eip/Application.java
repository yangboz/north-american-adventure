package com.rushucloud.eip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableAutoConfiguration
@ComponentScan
//@EnableWebSecurity
@ImportResource("classpath:activiti-standalone-context.xml")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

}