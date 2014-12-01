package com.rushucloud.eip.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.models.ItemDao;
import com.rushucloud.eip.models.ItemRepository;
import com.rushucloud.eip.models.Item.ItemType;
import com.rushucloud.eip.models.Vendor;
import com.rushucloud.eip.models.VendorDao;
import com.rushucloud.eip.models.VendorRepository;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/vendors")
// @see: http://java.dzone.com/articles/spring-rest-controller
public class VendorsController {
	private Logger LOG = LoggerFactory.getLogger(VendorsController.class);
	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CompanyDao
	@Autowired
	private VendorDao _vendorDao;
	//
	private VendorRepository vendorRepository;

	@Autowired
	public VendorsController(VendorRepository vendorRepository) {
		this.vendorRepository = vendorRepository;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing if the vendor is successfully created or not.")
	public Vendor create(@RequestBody @Valid Vendor vendor) {
		return this.vendorRepository.save(vendor);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of vendors that is successfully get or not.")
	public JsonObject list() {
		return new JsonObject(this.vendorRepository.findAll());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the vendor id is successfully get or not.")
	public Vendor get(@PathVariable("id") long id) {
		return this.vendorRepository.findOne(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement item is successfully updated or not.")
	public Vendor update(@PathVariable("id") long id,
			@RequestBody @Valid Vendor vendor) {
		return vendorRepository.save(vendor);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the item is successfully delete or not.")
	public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
		this.vendorRepository.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}