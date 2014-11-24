package com.rushucloud.eip.config;

import org.activiti.ldap.LDAPConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//@see http://thysmichels.com/2014/05/25/activiti-bpm-tutorial-ldap/#comment-10436
//TODO: LDAP user verification.
@Configuration
public class LDAPConfiguration {

	@Bean
	public LDAPConfigurator LDAPConfig() {
		LDAPConfigurator ldapConfig = new LDAPConfigurator();
		//
		ldapConfig.setServer("ldap://localhost");
//		ldapConfig.setServer("localhost");
		ldapConfig.setPort(10389);
		ldapConfig.setUser("uid=admin,ou=system");
		ldapConfig.setPassword("secret");

		ldapConfig.setBaseDn("ou=employees,dc=inflinx,dc=com");
		// ldapConfig.setBaseDn("ou=users");
		ldapConfig
				.setQueryUserByUserId("(&(objectClass=inetOrgPerson)(uid={0}))");
		ldapConfig
				.setQueryUserByFullNameLike("(&(objectClass=inetOrgPerson)(|({0}=*{1}*)({2}=*{3}*)))");
		ldapConfig
				.setQueryGroupsForUser("(&(objectClass=groupOfUniqueNames)(uniqueMember={0}))");

		ldapConfig.setUserIdAttribute("uid");
		ldapConfig.setUserFirstNameAttribute("cn");
		ldapConfig.setUserLastNameAttribute("sn");

		ldapConfig.setGroupIdAttribute("cn");
		ldapConfig.setGroupNameAttribute("cn");

		return ldapConfig;
	}
}
