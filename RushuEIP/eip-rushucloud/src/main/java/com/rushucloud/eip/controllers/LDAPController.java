package com.rushucloud.eip.controllers;

import java.util.List;
import java.util.jar.Attributes;

import javax.naming.NamingException;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonString;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class LDAPController {
	//
	private static Logger LOG = LoggerFactory.getLogger(LDAPController.class);
	//
	@RequestMapping(method = RequestMethod.GET, value = "search")
	@ApiOperation(httpMethod = "GET", value = "LDAP search client for testing purpose.")
	public List<String> search(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "filter", required = true, defaultValue = "(objectclass=person)") String filter) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		List<String> nameList = ldapTemplate.search(partition, filter,
				new AttributesMapper() {
					public Object mapFromAttributes(Attributes attributes)
							throws NamingException {
						Object obj = (Object)attributes.get("cn");
						LOG.info("attributes.get('cn'):",obj.toString());
						return (String) ((Object)attributes.get("cn"));
					}

					@Override
					public Object mapFromAttributes(
							javax.naming.directory.Attributes arg0)
							throws NamingException {
						return null;
					}
				});
		return nameList;
	}

	//
	private LdapTemplate getLdapTemplate() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl("ldap://localhost:10389");
		contextSource.setUserDn("uid=admin,ou=system");
		contextSource.setPassword("secret");
		try {
			contextSource.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		LdapTemplate ldapTemplate = new LdapTemplate();
		ldapTemplate.setContextSource(contextSource);
		return ldapTemplate;
	}
}
