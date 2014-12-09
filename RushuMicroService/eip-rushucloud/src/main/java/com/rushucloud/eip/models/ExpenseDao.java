package com.rushucloud.eip.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ExpenseDao extends CrudRepository<Expense, Long> {
	/**
	 * This method is not implemented and its working code will be
	 * auto-magically generated from its signature by Spring Data JPA.
	 *
	 * @param id
	 *            the expense id.
	 * @return the expense having the passed id or null if no expense is found.
	 */
	public Expense findById(long id);
}
