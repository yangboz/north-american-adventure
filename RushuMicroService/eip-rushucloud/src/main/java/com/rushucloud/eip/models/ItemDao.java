package com.rushucloud.eip.models;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface ItemDao extends CrudRepository<Item, Long> {
	/**
	 * This method is not implemented and its working code will be
	 * auto-magically generated from its signature by Spring Data JPA.
	 *
	 * @param id
	 *            the item id.
	 * @return the item having the passed email or null if no item is found.
	 */
	public Item findById(long id);
}
