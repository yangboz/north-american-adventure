package com.rushucloud.eip;

import static org.junit.Assert.*;

import java.util.List;

import org.activiti.engine.identity.Group;
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
public class SpringLDAPGroupTest {
	@Autowired
	@Rule
	public ActivitiRule activitiSpringRule;

	@Test
	public void findGroupByMember() throws Exception {
		List<Group> groupList = activitiSpringRule.getIdentityService()
				.createGroupQuery().groupMember("kermit").list();
		for (Group group : groupList) {
			System.out.println(group.getName());
		}
		assertNotNull(groupList);
		assertEquals(3, groupList.size());
	}
}
