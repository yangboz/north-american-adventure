package com.rushucloud.eip;

import static org.junit.Assert.*;

import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

//@see http://thysmichels.com/2014/05/25/activiti-bpm-tutorial-ldap/
public class LDAPUserTest {
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

	@Test
	@Ignore
	public void testCheckLogin() {
		boolean validated = activitiRule.getIdentityService().checkPassword(
				"kermit", "kermit");
		assertFalse(validated);
	}

	@Test
	@Ignore
	public void testCheckLoginFailure() {
		boolean validated = activitiRule.getIdentityService().checkPassword(
				"kermit", "kermit2");
		assertFalse(validated);
	}

	@Test
	@Ignore
	public void findUserById() throws Exception {
		User user = activitiRule.getIdentityService().createUserQuery()
				.userId("kermit").singleResult();
		assertNotNull(user);
		assertEquals("kermit", user.getId());
		assertEquals("kermit", user.getLastName());
	}
}
