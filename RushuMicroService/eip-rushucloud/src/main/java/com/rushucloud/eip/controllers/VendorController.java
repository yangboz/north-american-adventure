package com.rushucloud.eip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.models.Vendor;
import com.rushucloud.eip.models.VendorDao;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class VendorController {
	private Logger LOG = LoggerFactory.getLogger(VendorController.class);
	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CompanyDao
	@Autowired
	private VendorDao _vendorDao;

	// ==============
	// PUBLIC METHODS
	// ==============

	/**
	 * Create a new vendor and save it in the database.
	 *
	 * @param name
	 *            vendor name
	 * @return a string describing if the vendor is successfully created or not.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "vendor/add")
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement item is successfully created or not.")
	public JsonString add(
			@RequestParam(value = "name", required = true, defaultValue = "Vendor_00") String name) {
		try {
			Vendor vendor = new Vendor(name);
			_vendorDao.save(vendor);
		} catch (Exception ex) {
			return new JsonString("Error creating the vendor: " + ex.toString());
		}
		return new JsonString("vendor succesfully created!");
	}

	/**
	 * Return the id for the vendor info having the passed id.
	 *
	 * @param id
	 *            the id to search in the database.
	 * @return the vendor id or a message error if the vendor is not found.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "vendor/get-by-id")
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the vendor id is successfully get or not.")
	public JsonString getById(
			@RequestParam(value = "id", required = true, defaultValue = "1") long id) {
		String vendorId;
		try {
			Vendor vendor = _vendorDao.findById(id);
			vendorId = String.valueOf(vendor.getId());
		} catch (Exception ex) {
			return new JsonString("vendor not found");
		}
		return new JsonString("The vendor id is: " + vendorId);
	}

}
