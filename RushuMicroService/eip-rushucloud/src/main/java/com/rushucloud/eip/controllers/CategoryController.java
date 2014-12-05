package com.rushucloud.eip.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.Category;
import com.rushucloud.eip.models.CategoryDao;
import com.rushucloud.eip.models.CategoryRepository;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/category")
public class CategoryController {
	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CategoryDao
	@Autowired
	private CategoryDao _categoryDao;
	//
	private CategoryRepository categoryRepository;

	@Autowired
	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing if the reimbursement category is successfully created or not.")
	public Category create(@RequestBody @Valid Category category) {
		return this.categoryRepository.save(category);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of category that is successfully get or not.")
	public JsonObject list() {
		return new JsonObject(this.categoryRepository.findAll(new Sort(
				new Sort.Order(Sort.Direction.ASC, "date"))));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the category id is successfully get or not.")
	public Category get(@PathVariable("id") long id) {
		return this.categoryRepository.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement category is successfully updated or not.")
	public Category update(@PathVariable("id") long id,
			@RequestBody @Valid Category category) {
		return categoryRepository.save(category);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the category is successfully delete or not.")
	public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
		this.categoryRepository.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
