package com.rushucloud.eip.models;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class OdmPersonRepo {

	@Autowired
	private LdapTemplate ldapTemplate;

	public OdmPerson create(OdmPerson person) {
		ldapTemplate.create(person);
		return person;
	}

	public OdmPerson findByUid(String uid) {
		return ldapTemplate.findOne(query().where("uid").is(uid),
				OdmPerson.class);
	}

	public void update(OdmPerson person) {
		ldapTemplate.update(person);
	}

	public void delete(OdmPerson person) {
		ldapTemplate.delete(person);
	}

	public List<OdmPerson> findAll() {
		return ldapTemplate.findAll(OdmPerson.class);
	}

	public List<OdmPerson> findByLastName(String lastName) {
		return ldapTemplate.find(query().where("sn").is(lastName),
				OdmPerson.class);
	}
}