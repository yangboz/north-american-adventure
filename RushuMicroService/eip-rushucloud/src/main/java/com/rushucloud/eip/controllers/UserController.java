package com.rushucloud.eip.controllers;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.OdmPerson;
import com.rushucloud.eip.models.PersonAttributesMapper;
import com.rushucloud.eip.models.SimplePerson;
import com.rushucloud.eip.models.User;
import com.rushucloud.eip.settings.LDAPSetting;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserController
{
    //
    private static Logger LOG = LogManager.getLogger(UserController.class);

    // ==============
    // PRIVATE FIELDS
    // ==============

    // ==============
    // PUBLIC METHODS
    // ==============

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(httpMethod = "POST", value = "Response a string describing if the reimbursement company is successfully created or not.")
    public JsonObject create(@RequestBody @Valid SimplePerson simplePerson,
        @RequestParam(value = "group", required = true, defaultValue = "employees") String group,
        @RequestParam(value = "company", required = true, defaultValue = "www1.rushucloud.com") String company)
    {
        // Person odmPerson = getPerson();
        // LdapTreeBuilder.getInstance().getLdapTemplate().create(odmPerson);
        // // Set the Patron attributes
        // BasicAttributes attributes = new BasicAttributes(true);// Case ignore.
        // // attributes.put("sn", simplePerson.getUsername());
        // attributes.put("uid", simplePerson.getUsername());
        // // attributes.put("wx_token", simplePerson.getWxToken());
        // BasicAttribute wxTokenAttribute = new BasicAttribute("wxToken");
        // wxTokenAttribute.add(simplePerson.getWxToken());
        // attributes.put(wxTokenAttribute);
        // //
        // attributes.put("mobile", simplePerson.getPhone());
        // attributes.put("mail", simplePerson.getEmail());
        // // Add the multiply-valued attribute
        // BasicAttribute objectClassAttribute = new BasicAttribute("objectclass");
        // objectClassAttribute.add("top");
        // // objectClassAttribute.add("person");
        // // objectClassAttribute.add("organizationalperson");
        // // objectClassAttribute.add("inetorgperson");
        // objectClassAttribute.add("extensibleObject");
        // objectClassAttribute.add("wxToken");
        // //
        // attributes.put(objectClassAttribute);
        //
        BasicAttributes personAttributes = new BasicAttributes();
        BasicAttribute personBasicAttribute = new BasicAttribute("objectclass");
        // personBasicAttribute.add("person");
        personBasicAttribute.add("top");
        // personBasicAttribute.add("extensibleObject");
        personBasicAttribute.add("person");
        personBasicAttribute.add("organizationalperson");
        personBasicAttribute.add("inetorgperson");
        // this is the object class extension.
        // @see: http://forum.spring.io/forum/spring-projects/data/ldap/90794-how-to-insert-multi-value-attribute-in-ad
        // personBasicAttribute.add("wxToken");
        // personBasicAttribute.add("code");
        //
        personAttributes.put(personBasicAttribute);
        personAttributes.put("uid", simplePerson.getUsername());
        personAttributes.put("mobile", simplePerson.getPhone());
        personAttributes.put("mail", simplePerson.getEmail());
        personAttributes.put("userPassword", simplePerson.getPassword());
        // BasicAttribute wxTokenAttribute = new BasicAttribute("wxToken");
        // wxTokenAttribute.add(simplePerson.getWxToken());
        // personAttributes.put("wxToken", wxTokenAttribute);
        // BasicAttribute codeAttribute = new BasicAttribute("code");
        // codeAttribute.add(simplePerson.getCode());
        // personAttributes.put("code", codeAttribute);
        // LdapTemplate ldapTemplate = getLdapTemplate();
        this.getLdapTemplate().bind(
            "uid=" + simplePerson.getUsername() + ",ou=" + group + ",ou=" + company + "," + ldapSetting.getBaseOn(),
            null, personAttributes);
        return new JsonObject(simplePerson);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "Response a list describing all of peron entry that is successfully get or not.")
    public JsonObject list(
        @AuthenticationPrincipal User user,
        @RequestParam(value = "baseOn", required = true, defaultValue = "ou=employees,ou=www1.rushucloud.com,dc=www") String baseOn)
    {
        // return new JsonObject(this.companyRepository.findAll(new Sort(new
        // Sort.Order(Sort.Direction.ASC,"date"))));
        // List<OdmPerson> persons = this.getLdapTemplate().findAll(OdmPerson.class);
        List<OdmPerson> persons =
            this.getLdapTemplate().search(query().base(baseOn).where("objectclass").is("person"),
                new PersonAttributesMapper());
        for (OdmPerson person : persons) {
            LOG.info("get person:" + person.toString());
        }
        return new JsonObject(persons);
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.GET)
    @ApiOperation(httpMethod = "GET", value = "Response a entry describing if the person entry uid is successfully get or not.")
    public JsonObject get(@PathVariable("uid") String uid)
    {
        return new JsonObject(this.getLdapTemplate().findOne(query().where("uid").is(uid), OdmPerson.class));
    }

    @RequestMapping(value = "/{uid}", method = RequestMethod.PUT)
    @ApiOperation(httpMethod = "PUT", value = "Response a entry describing if the person entry is successfully updated or not.")
    public OdmPerson update(@PathVariable("id") long id, @RequestBody @Valid OdmPerson person)
    {
        //
        this.getLdapTemplate().update(person);
        return this.getLdapTemplate().findOne(query().where("uid").is(person.getUid()), OdmPerson.class);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @ApiOperation(httpMethod = "DELETE", value = "Response a result describing if the peron entry is successfully delete or not.")
    public ResponseEntity<Boolean> delete(@RequestBody @Valid OdmPerson person)
    {
        this.getLdapTemplate().delete(person);
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
    }

    // @see
    // http://hoserdude.com/2014/06/19/spring-boot-configurationproperties-and-profile-management-using-yaml/
    @Autowired
    private LDAPSetting ldapSetting;

    //
    public LdapTemplate getLdapTemplate()
    {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapSetting.getUrl());
        contextSource.setUserDn(ldapSetting.getUserOn());
        contextSource.setPassword(ldapSetting.getPassword());
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
    @SuppressWarnings("unused")
    private DirContext getInitialContext() throws NamingException
    {

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        props.put(Context.PROVIDER_URL, ldapSetting.getUrl());

        if ((ldapSetting.getUserOn() != null) && (!ldapSetting.getUserOn().equals(""))) {
            props.put(Context.SECURITY_AUTHENTICATION, "simple");
            props.put(Context.SECURITY_PRINCIPAL, ldapSetting.getUserOn());
            props.put(Context.SECURITY_CREDENTIALS,
                ((ldapSetting.getPassword() == null) ? "" : ldapSetting.getPassword()));
        }

        return new InitialDirContext(props);
    }
}
