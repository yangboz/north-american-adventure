/**
 * @author yangboz
 * @see http://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#howto-switch-off-spring-boot-security-configuration
 */
package com.rushucloud.eip.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Order(201)
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	//
	private static Logger LOG = LoggerFactory.getLogger(SecurityConfiguration.class);
	//
	/*
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles("USER"); // ... etc.
    }
	*/
    // ... other stuff for application security
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
			contextSource.setUrl("ldap://localhost:11389");
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
}
