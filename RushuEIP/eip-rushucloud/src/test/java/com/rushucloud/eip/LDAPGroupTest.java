package com.rushucloud.eip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

//@see http://thysmichels.com/2014/05/25/activiti-bpm-tutorial-ldap/
public class LDAPGroupTest {
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule("activiti.cfg.xml");

	@Test
	@Ignore
	public void findGroupByMember() throws Exception {
		List<Group> groupList = activitiRule.getIdentityService()
				.createGroupQuery().groupMember("kermit").list();
		for (Group group : groupList) {
			System.out.println(group.getName());
		}
		assertNotNull(groupList);
		assertEquals(3, groupList.size());
	}
}
