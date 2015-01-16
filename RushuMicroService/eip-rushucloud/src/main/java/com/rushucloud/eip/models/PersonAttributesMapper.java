package com.rushucloud.eip.models;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class PersonAttributesMapper implements AttributesMapper<Person> {
    public Person mapFromAttributes(Attributes attrs) throws NamingException {
       Person person = new Person();
       person.setFullName((String)attrs.get("cn").get());
       person.setLastName((String)attrs.get("sn").get());
       person.setDescription((String)attrs.get("description").get());
       person.setUid((String)attrs.get("uid").get());
       return person;
    }
 }
