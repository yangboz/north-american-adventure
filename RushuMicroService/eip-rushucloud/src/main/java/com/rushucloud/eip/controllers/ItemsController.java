package com.rushucloud.eip.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.Expense;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.models.ItemDao;
import com.rushucloud.eip.models.ItemRepository;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/items")
// @see: http://java.dzone.com/articles/spring-rest-controller
public class ItemsController {
	private Logger LOG = LoggerFactory.getLogger(ItemsController.class);
	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CompanyDao
	@Autowired
	private ItemDao _itemDao;
	//
	private ItemRepository itemRepository;

	@Autowired
	public ItemsController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing if the reimbursement item is successfully created or not.")
	public Item create(@RequestBody @Valid Item item) {
		return this.itemRepository.save(item);
	}

//	@RequestMapping(method = RequestMethod.GET)
//	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of item that is successfully get or not.")
//	public JsonObject list() {
//		return new JsonObject(this.itemRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC,"date"))));
//	}
	
	@RequestMapping(method = RequestMethod.GET,params = {"owner"})
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of item that is successfully get or not.")
	public JsonObject list(@RequestParam(value = "owner") String owner) {
//		return new JsonObject(this.expenseRepository.findAll());
		if(owner!=null)
		{
			//
			Iterable<Item> result = this._itemDao.findItemsByOwner(owner);
//			LOG.debug("itemsByOwner()result:"+result.toString());
			return new JsonObject(result);
		}else{
			return new JsonObject(this._itemDao.findAll());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the item id is successfully get or not.")
	public Item get(@PathVariable("id") long id) {
		return this.itemRepository.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement item is successfully updated or not.")
	public Item update(@PathVariable("id") long id,
			@RequestBody @Valid Item item) {
		return itemRepository.save(item);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the item is successfully delete or not.")
	public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
		this.itemRepository.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
