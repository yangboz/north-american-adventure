/**
 * @author yangboz
 * @see http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-switch-off-spring-boot-security-configuration
 */
package com.rushucloud.eip.config;

import javax.ws.rs.HttpMethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@Order(201)
//@EnableWebMvcSecurity
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//
	private static Logger LOG = LogManager.getLogger(SecurityConfiguration.class);
	//
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER"); // ... etc.
            //security, because we need to log in to chat testing, right?
            auth.inMemoryAuthentication().withUser("ian").password("ian").roles("USER");
            auth.inMemoryAuthentication().withUser("dan").password("dan").roles("USER");
            auth.inMemoryAuthentication().withUser("chris").password("chris").roles("USER");
    }
    // ... other stuff for application security
    //@see:  http://smlsun.com/blog/2014/01/19/Spring-Security-Basic-Authentication-with-Ajax-request/
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**");
        web.ignoring()
        .antMatchers(HttpMethod.GET, "/**");
    }
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest()
				.fullyAuthenticated().and().formLogin();
	}
	//
	@Configuration
	protected static class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter{
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception{
			LdapContextSource contextSource = new LdapContextSource();
			contextSource.setUrl("ldap://localhost:10389");
			contextSource.setUserDn("uid=admin,ou=system");
			contextSource.setPassword("secret");
			//
//			LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> authentication =
//					auth.ldapAuthentication().userDnPatterns("uid={0},ou=employees")
//			.groupSearchBase("dc=rushucloud,dc=com").contextSource(contextSource);
			LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder>.ContextSourceBuilder authentication =
			auth.ldapAuthentication().userDnPatterns("uid={0},ou=people")
			.groupSearchBase("ou=groups").contextSource()
			.ldif("classpath:test-server.ldif");
			//
			LOG.info("LDAP authentication:",authentication.toString());
		}
	}
	*/
}
