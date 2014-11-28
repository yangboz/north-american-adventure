package com.rushucloud.eip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@EnableWebMvc
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
	//@see http://www.netthreads.co.uk/2014/07/21/web-spring-4-thymeleaf-hot-cache/
	public static final String DEFAULT_PREFIX = "/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    public static final String DEFAULT_MODE = "HTML5";
    public static final String[] DEFAULT_VIEW_NAMES =
    {
        "*"
    };
 
    // Hot re-load, false=will reload changes.
    public static final boolean DEFAULT_CACHE = false;
 
    @Bean
    public ServletContextTemplateResolver templateResolver()
    {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix(DEFAULT_PREFIX);
        resolver.setSuffix(DEFAULT_SUFFIX);
        resolver.setTemplateMode(DEFAULT_MODE);
 
        return resolver;
    }
 
    @Bean
    public SpringTemplateEngine templateEngine()
    {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
 
        // Need this for hot re-load
        if (!DEFAULT_CACHE)
        {
            engine.setCacheManager(null);
        }
 
        return engine;
    }
 
    @Bean
    public ViewResolver viewResolver()
    {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setOrder(1);
        viewResolver.setCache(DEFAULT_CACHE);
        viewResolver.setViewNames(DEFAULT_VIEW_NAMES);
 
        return viewResolver;
    }
 
    @Bean
    @Description("Spring message resolver")
    public ResourceBundleMessageSource messageSource()
    {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
 
        return messageSource;
    }
 
    /**
     * Add our static resources folder mapping.
     *
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/api/**").addResourceLocations("classpath:/api/");
        registry.addResourceHandler("/webapp/**").addResourceLocations("classpath:/webapp/www/");
        registry.addResourceHandler("/admin/**").addResourceLocations("classpath:/admin/");
        registry.addResourceHandler("/angularweb/**").addResourceLocations("classpath:/angularweb/");
        registry.addResourceHandler("/uploads/**").addResourceLocations("classpath:/uploads/");
    }
}
