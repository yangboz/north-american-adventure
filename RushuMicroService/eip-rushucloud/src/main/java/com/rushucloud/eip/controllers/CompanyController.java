/**
 * @author yangboz
 * @see http://blog.netgloo.com/2014/10/27/using-mysql-in-spring-boot-via-spring-data-jpa-and-hibernate/
 */
package com.rushucloud.eip.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.Company;
import com.rushucloud.eip.models.CompanyDao;
import com.rushucloud.eip.models.CompanyRepository;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.models.User;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/company")
public class CompanyController {
	// @RequestMapping("company/index")
//	@RequestMapping(method = RequestMethod.GET, value = "company/index")
//	@ApiOperation(httpMethod = "GET", value = "Response a static index html view.")
//	public String home() {
//		return "index";
//	}

	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CompanyDao
	@Autowired
	private CompanyDao _companyDao;
	//
	private CompanyRepository companyRepository;

	@Autowired
	public CompanyController(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing if the reimbursement company is successfully created or not.")
	public Company create(@RequestBody @Valid Company company) {
		return this.companyRepository.save(company);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of company that is successfully get or not.")
	public JsonObject list(@AuthenticationPrincipal User user) {
//		return new JsonObject(this.companyRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC,"date"))));
		return new JsonObject(this._companyDao.findAll());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the company id is successfully get or not.")
	public JsonObject get(@PathVariable("id") long id) {
		return new JsonObject(this.companyRepository.findOne(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement company is successfully updated or not.")
	public Company update(@PathVariable("id") long id,
			@RequestBody @Valid Company company) {
		return companyRepository.save(company);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the company is successfully delete or not.")
	public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
		this.companyRepository.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
