/**
 * @author yangboz
 * @see http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-switch-off-spring-boot-security-configuration
 */
package com.rushucloud.eip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(201)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER"); // ... etc.
    }

    // ... other stuff for application security
}
