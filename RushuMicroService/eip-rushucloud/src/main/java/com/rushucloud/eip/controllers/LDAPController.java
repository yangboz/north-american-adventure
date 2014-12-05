package com.rushucloud.eip.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aj.org.objectweb.asm.Attribute;

import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.settings.LDAPSetting;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//@see: http://www.javaworld.com/article/2076073/java-web-development/ldap-and-jndi--together-forever.html
@RestController
public class LDAPController {
	//
	private static Logger LOG = LoggerFactory.getLogger(LDAPController.class);

	//
	@RequestMapping(method = RequestMethod.GET, value = "ldap/search")
	@ApiOperation(httpMethod = "GET", value = "LDAP search client for testing purpose.")
	public List<String> search(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "filter", required = true, defaultValue = "(objectclass=person)") String filter) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		List<String> nameList = ldapTemplate.search(partition, filter,
				new AttributesMapper() {
					public Object mapFromAttributes(Attributes attributes)
							throws NamingException {
						HashMap obj = (HashMap) attributes.get("cn");
						LOG.info("attributes.get('cn'):", obj.toString());
						// return (Object)attributes.get("cn");
						return obj.toString();
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
	@RequestMapping(method = RequestMethod.PUT, value = "ldap/add")
	@ApiOperation(httpMethod = "PUT", value = "LDAP search client for adding purpose.")
	public void add(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "partons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "parton9999") String uid,
			@RequestParam(value = "sn", required = true, defaultValue = "Patron9999") String sn,
			@RequestParam(value = "cn", required = true, defaultValue = "New Patron9999") String cn
//			@RequestParam(value = "basicAttributes", required = false, defaultValue = "") BasicAttributes basicAttributes
			) {
		// Set the Patron attributes
		BasicAttributes attributes = new BasicAttributes();
		attributes.put("sn", sn);
		attributes.put("cn", cn);
		// Add the multi-valued attribute
		BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
		objectClassAttribute.add("top");
		objectClassAttribute.add("person");
		objectClassAttribute.add("organizationalperson");
		objectClassAttribute.add("inetorgperson");
		attributes.put(objectClassAttribute);
		LdapTemplate ldapTemplate = getLdapTemplate();
		ldapTemplate.bind("uid="+uid+",ou="+ou+","+partition, null,
				attributes);
	}
	
	//
	@RequestMapping(method = RequestMethod.POST, value = "ldap/update")
	@ApiOperation(httpMethod = "POST", value = "LDAP search client for updating purpose.")
	public void update(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "partons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "parton9999") String uid
//			@RequestParam(value = "basicAttributes", required = false, defaultValue = "") BasicAttributes basicAttributes
			) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		BasicAttribute attribute = new BasicAttribute("telephoneNumber", "801 100 1000"); 
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attribute);
		ldapTemplate.modifyAttributes("uid="+uid+",ou="+ou+","+partition, new ModificationItem[] {item});
	}
	
	//
	@RequestMapping(method = RequestMethod.DELETE, value = "ldap/delete")
	@ApiOperation(httpMethod = "DELETE", value = "LDAP search client for deleting purpose.")
	public void delete(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "partons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "parton9999") String uid
			) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		ldapTemplate.unbind("uid="+uid+",ou="+ou+","+partition);
	}
	//@see http://hoserdude.com/2014/06/19/spring-boot-configurationproperties-and-profile-management-using-yaml/
	@Autowired
	private LDAPSetting ldapConfig;
	//
	private LdapTemplate getLdapTemplate() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(ldapConfig.getUrl());
		contextSource.setUserDn(ldapConfig.getUserOn());
		contextSource.setPassword(ldapConfig.getPassword());
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
