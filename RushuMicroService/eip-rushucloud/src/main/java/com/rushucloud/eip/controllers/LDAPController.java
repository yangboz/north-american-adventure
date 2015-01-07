package com.rushucloud.eip.controllers;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.validation.Valid;

import org.apache.directory.server.core.DefaultDirectoryService;
import org.apache.directory.server.core.DirectoryService;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.settings.LDAPSetting;
import com.rushucloud.eip.thirdParty.WeixinUser;
import com.wordnik.swagger.annotations.ApiOperation;
//@see: http://www.javaworld.com/article/2076073/java-web-development/ldap-and-jndi--together-forever.html
//@see: https://docs.oracle.com/javase/tutorial/jndi/ldap/operations.html
//@see: http://docs.spring.io/spring-ldap/docs/current/reference/
//@see: LDAP Manager Java code example: http://www.javafaq.nu/java-example-code-409.html

@RestController
public class LDAPController {
	//
	private static Logger LOG = LogManager.getLogger(LDAPController.class);
	//
	@RequestMapping(method = RequestMethod.GET, value = "ldap/search")
	@ApiOperation(httpMethod = "GET", value = "LDAP search client for testing purpose.")
	public JsonObject search(
			@RequestParam(value = "partition", required = true, defaultValue = "ou=employees,ou=www1.rushucloud.com,dc=www") String partition,
			@RequestParam(value = "filter", required = true, defaultValue = "(objectclass=person)") String filter) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		// AndFilter andFilter = new AndFilter();
		// andFilter.and(new EqualsFilter("objectclass", "Person"));
		// andFilter.and(new EqualsFilter("ou", "employees"));
		return new JsonObject(ldapTemplate.search(
		// query().where("objectclass").is("person"),
		// partition, andFilter.encode(),
				partition, filter, new AttributesMapper<String>() {
					@Override
					public String mapFromAttributes(
							javax.naming.directory.Attributes attrs)
							throws NamingException {
						LOG.debug("javax.naming.directory.Attributes:"+ attrs.toString());
						return attrs.get("uid").get().toString();
					}
				}));
	}

	// Notice:
	// http://stackoverflow.com/questions/4935612/how-to-create-a-partition-in-the-root-in-apacheds-server-programmatically-using
	// Notice:
	// http://stackoverflow.com/questions/8512393/custom-partition-in-apacheds
	// So currently using o=xxx.com,parent as dc=www at child level for DIT
	// structure.
	// In the future, using partition as dc=xxx.com at root level for DIT
	// structure.
	@RequestMapping(method = RequestMethod.POST, value = "ldap/dc")
	@ApiOperation(httpMethod = "POST", value = "LDAP orgnization(as domain component) adding function.")
	public Boolean addOrgnizationUnit(
			@RequestParam(value = "partitionId(o/ou)", required = true, defaultValue = "xxx.com") String partitionId,
			@RequestParam(value = "partitionDn(domain)", required = true, defaultValue = "www") String partitionDn)
			throws Exception {
		LdapTemplate ldapTemplate = getLdapTemplate();
		//
		// Set the Patron attributes
		BasicAttributes attributes = new BasicAttributes();
		attributes.put("ou", partitionId);
		// Add the multiply-valued attribute
		BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
		objectClassAttribute.add("top");
		objectClassAttribute.add("organizationalUnit");
//		objectClassAttribute.add("domain");
		attributes.put(objectClassAttribute);
		ldapTemplate.bind("ou=" + partitionId  + ",dc=" + partitionDn, null,
				attributes);
		//
		/*
		BasicAttributes attrs = new BasicAttributes(true);
		BasicAttribute objClass = new BasicAttribute("objectclass");
		objClass.add("top");
		// objClass.add("domain");//domain,organizational,organizationalUnit
		objClass.add("organizationalUnit");
		attrs.put(objClass);
		// Create the context
		// DirContext dirCtx = this.getInitialContext();
		DirContext dirCtx = ldapTemplate.getContextSource()
				.getReadWriteContext();
		// dirCtx.bind(partitionDn, null, attrs);
		// Create the context
//		Context result = dirCtx.createSubcontext("ou=" + partitionDn, attrs);
		// Check that it was created by listing its parent
		NamingEnumeration list = dirCtx.list("");

		// Go through each item in list
		while (list.hasMore()) {
			NameClassPair nc = (NameClassPair) list.next();
			// System.out.println(nc);
			LOG.info("NameClassPair:" + nc.toString());
		}
		result.close();
		dirCtx.close();
		*/
		/*
		 * DirectoryService directoryService = this.getDirectoryService();
		 * 
		 * // Create a new partition JdbmPartition helloPartition = new
		 * JdbmPartition(); helloPartition.setId("hello");
		 * helloPartition.setSuffix("ou=hello");
		 * helloPartition.init(directoryService);
		 * 
		 * directoryService.addPartition(helloPartition);
		 * directoryService.startup();
		 * 
		 * // ClonedServerEntry entry =
		 * directoryService.getAdminSession().lookup(new LdapDN("ou=hello")); //
		 * Assert.assertNotNull(entry);
		 * 
		 * directoryService.shutdown();
		 */
		//
		return true;
	}

	//
	@RequestMapping(method = RequestMethod.PUT, value = "ldap/add")
	@ApiOperation(httpMethod = "PUT", value = "LDAP client for adding attribute/entry purpose.")
	public void add(
			@RequestParam(value = "partition", required = true, defaultValue = "ou=www1.rushucloud.com,dc=www") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "patrons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "patron9999") String uid,
			@RequestParam(value = "sn", required = true, defaultValue = "Patron9999") String sn,
			@RequestParam(value = "cn", required = true, defaultValue = "New Patron9999") String cn
	// @RequestParam(value = "basicAttributes", required = false, defaultValue =
	// "") BasicAttributes basicAttributes
	) {
		// Set the Patron attributes
		BasicAttributes attributes = new BasicAttributes();
		attributes.put("sn", sn);
		attributes.put("cn", cn);
		// Add the multiply-valued attribute
		BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
		objectClassAttribute.add("top");
		objectClassAttribute.add("person");
		objectClassAttribute.add("organizationalperson");
		objectClassAttribute.add("inetorgperson");
		attributes.put(objectClassAttribute);
		LdapTemplate ldapTemplate = getLdapTemplate();
		ldapTemplate.bind("uid=" + uid + ",ou=" + ou + "," + partition, null,
				attributes);
	}

	//
	@RequestMapping(method = RequestMethod.POST, value = "ldap/import")
	@ApiOperation(httpMethod = "POST", value = "LDAP gather user and groups for importing purpose.")
	public void gather(
			@RequestBody @Valid List<WeixinUser> users,
			@RequestParam(value = "partitionId(o/ou)", required = true, defaultValue = "xxx.com") String partitionId,
			@RequestParam(value = "partitionDn(domain)", required = true, defaultValue = "www") String partitionDn) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		for (WeixinUser user : users) {
//			BasicAttribute attribute = new BasicAttribute("mobile",
//					user.getMobile());
//			//
//			ModificationItem item = new ModificationItem(
//					DirContext.ADD_ATTRIBUTE, attribute);
//			ldapTemplate.modifyAttributes("uid=" + user.getWeixinId() + ",ou="
//					+ user.getDepartment() + "," + partition,
//					new ModificationItem[] { item });
			
			// Set the Person attributes
			BasicAttributes attributes = new BasicAttributes();
			attributes.put("sn", user.getName());
			attributes.put("cn", user.getWeixinId());
			attributes.put("mobile", user.getMobile());
//			attributes.put("mail", user.getEmail());
			// Add the multiply-valued attribute
			BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
			objectClassAttribute.add("top");
			objectClassAttribute.add("person");
			objectClassAttribute.add("organizationalperson");
			objectClassAttribute.add("inetorgperson");
//			objectClassAttribute.add("mail");
			attributes.put(objectClassAttribute);
			//
			ldapTemplate.bind("uid=" + user.getWeixinId() + ",ou=" + partitionId + ",dc=" + partitionDn, null,
					attributes);
		}
	}

	//
	@RequestMapping(method = RequestMethod.PUT, value = "ldap/update")
	@ApiOperation(httpMethod = "PUT", value = "LDAP search client for updating purpose.")
	public void update(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "partons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "parton9999") String uid
	// @RequestParam(value = "basicAttributes", required = false, defaultValue =
	// "") BasicAttributes basicAttributes
	) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		BasicAttribute attribute = new BasicAttribute("telephoneNumber",
				"801 100 1000");
		ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE,
				attribute);
		ldapTemplate.modifyAttributes("uid=" + uid + ",ou=" + ou + ","
				+ partition, new ModificationItem[] { item });
	}

	//
	@RequestMapping(method = RequestMethod.DELETE, value = "ldap/delete")
	@ApiOperation(httpMethod = "DELETE", value = "LDAP search client for deleting purpose.")
	public void delete(
			@RequestParam(value = "partition", required = true, defaultValue = "dc=inflinx,dc=com") String partition,
			@RequestParam(value = "ou", required = true, defaultValue = "partons") String ou,
			@RequestParam(value = "uid", required = true, defaultValue = "parton9999") String uid) {
		LdapTemplate ldapTemplate = getLdapTemplate();
		ldapTemplate.unbind("uid=" + uid + ",ou=" + ou + "," + partition);
	}

	// @see
	// http://hoserdude.com/2014/06/19/spring-boot-configurationproperties-and-profile-management-using-yaml/
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

	// @see: http://www.javafaq.nu/java-example-code-409.html
	private DirContext getInitialContext() throws NamingException {

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL, ldapConfig.getUrl());

		if ((ldapConfig.getUserOn() != null)
				&& (!ldapConfig.getUserOn().equals(""))) {
			props.put(Context.SECURITY_AUTHENTICATION, "simple");
			props.put(Context.SECURITY_PRINCIPAL, ldapConfig.getUserOn());
			props.put(
					Context.SECURITY_CREDENTIALS,
					((ldapConfig.getPassword() == null) ? "" : ldapConfig
							.getPassword()));
		}

		return new InitialDirContext(props);
	}

	//
	private DirectoryService getDirectoryService() throws Exception {
		DirectoryService directoryService;

		directoryService = new DefaultDirectoryService();
		directoryService.setShutdownHookEnabled(true);

		File workingDir = new File("work");
		directoryService.setWorkingDirectory(workingDir);

		return directoryService;

		// Create a new partition
		// JdbmPartition helloPartition = new JdbmPartition();
		// helloPartition.setId("hello");
		// helloPartition.setSuffix("ou=hello");
		// helloPartition.init(directoryService);
		//
		// directoryService.addPartition(helloPartition);
		// directoryService.startup();
		//
		// ClonedServerEntry entry =
		// directoryService.getAdminSession().lookup(new LdapDN("ou=hello"));
		// Assert.assertNotNull(entry);
		//
		// directoryService.shutdown();
	}
}
