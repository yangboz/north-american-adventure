package com.rushucloud.eip.models;

import org.apache.directory.shared.ldap.aci.UserClass.Name;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

@Entry(objectClasses = { "person", "top" }, base = "ou=employees")
public class Person {
	@Id
	private Name dn;
	
	@Attribute(name = "uid")
	@DnAttribute(value = "uid", index = 1)
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Attribute(name = "cn")
	@DnAttribute(value = "cn", index = 2)
	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	// No @Attribute annotation means this will be bound to the LDAP attribute
	// with the same value
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@DnAttribute(value = "ou", index = 0)
	@Transient
	private String company;

	@Transient
	private String someUnmappedField;
	// ...more attributes below
	@Transient
	private String lastName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
