package com.rushucloud.eip.models;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.rushucloud.eip.ldap.plain.domain.Person;

public class PersonAttributesMapper implements AttributesMapper<Person>
{
    @Override
    public Person mapFromAttributes(Attributes attrs) throws NamingException
    {
        Person person = new Person();
        person.setCn((String) attrs.get("cn").get());
        person.setSn((String) attrs.get("sn").get());
        person.setDescription((String) attrs.get("description").get());
        person.setUid((String) attrs.get("uid").get());
        person.setWxToken((String) attrs.get("wxToken").get());
        person.setCode((String) attrs.get("code").get());
        return person;
    }
}
