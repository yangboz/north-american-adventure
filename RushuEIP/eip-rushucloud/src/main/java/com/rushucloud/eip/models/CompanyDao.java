/**
 * @author yangboz
 * @see http://blog.netgloo.com/2014/10/27/using-mysql-in-spring-boot-via-spring-data-jpa-and-hibernate/
 */
package com.rushucloud.eip.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface CompanyDao extends CrudRepository<Company, Long> {
	/**
	 * This method is not implemented and its working code will be
	 * auto-magically generated from its signature by Spring Data JPA.
	 *
	 * @param email
	 *            the user email.
	 * @return the user having the passed email or null if no user is found.
	 */
	public Company findByEmail(String email);
}
