package com.rushucloud.eip.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("upload");
    }
}
