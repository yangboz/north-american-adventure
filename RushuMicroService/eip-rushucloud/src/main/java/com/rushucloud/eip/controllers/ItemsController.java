package com.rushucloud.eip.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.models.ItemRepository;

@RestController
@RequestMapping("/items")
public class ItemsController {
	private ItemRepository itemRepository;

	@Autowired
	public ItemsController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Item create(@RequestBody @Valid Item item) {
		return this.itemRepository.save(item);
	}

	@RequestMapping(method = RequestMethod.GET)
	public JsonObject list() {
		return new JsonObject(this.itemRepository.findAll());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Item get(@PathVariable("id") long id) {
		return this.itemRepository.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Item update(@PathVariable("id") long id,
			@RequestBody @Valid Item item) {
		return itemRepository.save(item);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
		this.itemRepository.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
