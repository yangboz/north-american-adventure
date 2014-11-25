package com.rushucloud.eip;

import static org.junit.Assert.*;

import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rushucloud.eip.config.ActivitiConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ActivitiConfiguration.class)
public class SpringLDAPUserTest {
	@Autowired
	@Rule
	public ActivitiRule activitiSpringRule;

	@Test
	public void testCheckLogin() {
		boolean validated = activitiSpringRule.getIdentityService()
				.checkPassword("kermit", "kermit");
		assertTrue(validated);
	}

	@Test
	public void testCheckLoginFailure() {
		boolean validated = activitiSpringRule.getIdentityService()
				.checkPassword("kermit", "kermit2");
		assertFalse(validated);
	}

	@Test
	public void findUserById() throws Exception {
		User user = activitiSpringRule.getIdentityService().createUserQuery()
				.userId("kermit").singleResult();
		assertNotNull(user);
		assertEquals("kermit", user.getId());
		assertEquals("kermit", user.getLastName());
	}
}
