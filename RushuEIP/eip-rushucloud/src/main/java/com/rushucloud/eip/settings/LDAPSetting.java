package com.rushucloud.eip.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
//@see https://docs.oracle.com/javase/tutorial/jndi/ldap/jndi.html
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="ldap")
public class LDAPSetting {
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String userOn;
	public String getUserOn() {
		return userOn;
	}
	public void setUserOn(String userOn) {
		this.userOn = userOn;
	}
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
