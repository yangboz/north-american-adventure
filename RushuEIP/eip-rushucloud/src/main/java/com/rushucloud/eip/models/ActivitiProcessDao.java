package com.rushucloud.eip.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ActivitiProcessDao extends CrudRepository<ActivitiProcess, Long> {
	/**
	 * This method is not implemented and its working code will be
	 * auto-magically generated from its signature by Spring Data JPA.
	 *
	 * @param name
	 *            the Activiti process key name.
	 * @return the process having the passed name or null if no process is found.
	 */
	public ActivitiProcess findByName(String name);
	/**
	 * This method is not implemented and its working code will be
	 * auto-magically generated from its signature by Spring Data JPA.
	 *
	 * @param companyId
	 *            the company id.
	 * @return the process having the passed id or null if no process is found.
	 */
	public ActivitiProcess findByCompanyId(String companyId);
}