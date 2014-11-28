package com.rushucloud.eip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.models.Item.ItemType;
import com.rushucloud.eip.models.ItemDao;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
public class ItemController {

		// ==============
		// PRIVATE FIELDS
		// ==============

		// Autowire an object of type CompanyDao
		@Autowired
		private ItemDao _itemDao;

		// ==============
		// PUBLIC METHODS
		// ==============

		/**
		 * Create a new reimbursement item and save it in the database.
		 *
		 * @param email
		 *            item email
		 * @param name
		 *            item name
		 * @return a string describing if the item is successfully created or
		 *         not.
		 */
		@RequestMapping(method = RequestMethod.PUT, value = "item/add")
		@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement item is successfully created or not.")
		public JsonString add(
				@RequestParam(value = "name", required = true, defaultValue = "Reim_item_00") String name,
				@RequestParam(value = "type", required = true, defaultValue = "CostComsumed") ItemType type,
				@RequestParam(value = "amount", required = true, defaultValue = "1.88") double amount) {
			try {
				Item item = new Item(amount, name, type);
				_itemDao.save(item);
			} catch (Exception ex) {
				return new JsonString("Error creating the item: "
						+ ex.toString());
			}
			return new JsonString("item succesfully created!");
		}

		/**
		 * Delete the item having the passed id.
		 *
		 * @param id
		 *            the id for the item to delete
		 * @return a string describing if the item is successfully deleted or
		 *         not.
		 */
		@RequestMapping(method = RequestMethod.DELETE, value = "item/delete")
		@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the item is successfully delete or not.")
		public JsonString delete(long id) {
			try {
				Item item = new Item(id);
				_itemDao.delete(item);
			} catch (Exception ex) {
				return new JsonString("Error deleting the item:" + ex.toString());
			}
			return new JsonString("item succesfully deleted!");
		}

		/**
		 * Return the id for the item having the passed email.
		 *
		 * @param id
		 *            the id to search in the database.
		 * @return the item id or a message error if the item is not found.
		 */
		@RequestMapping(method = RequestMethod.GET, value = "item/get-by-id")
		@ApiOperation(httpMethod = "GET", value = "Response a string describing if the item id is successfully get or not.")
		public JsonString getById(
				@RequestParam(value = "id", required = true, defaultValue = "1") long id) {
			String itemId;
			try {
				Item item = _itemDao.findById(id);
				itemId = String.valueOf(item.getId());
			} catch (Exception ex) {
				return new JsonString("item not found");
			}
			return new JsonString("The item id is: " + itemId);
		}

		/**
		 * Update the email and the name for the item in the database having the
		 * passed id.
		 *
		 * @param id
		 *            the id for the item to update.
		 * @param amount
		 *            the new amount.
		 * @param name
		 *            the new name.
		 * @param type
		 * 			  the item type.
		 * @return a string describing if the item is successfully updated or
		 *         not.
		 */
		@RequestMapping(method = RequestMethod.POST, value = "item/update")
		@ApiOperation(httpMethod = "POST", value = "Response a string describing if the item is successfully updated or not")
		public JsonString updateitem(long id,double amount, String name, ItemType type) {
			try {
				Item item = _itemDao.findOne(id);
				item.setAmount(amount);
				item.setType(type);
				item.setName(name);
				_itemDao.save(item);
			} catch (Exception ex) {
				return new JsonString("Error updating the item: "
						+ ex.toString());
			}
			return new JsonString("item succesfully updated!");
		}
}
