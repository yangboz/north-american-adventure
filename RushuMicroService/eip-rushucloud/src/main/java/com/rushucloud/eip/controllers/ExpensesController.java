package com.rushucloud.eip.controllers;

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
import com.rushucloud.eip.models.Expense;
import com.rushucloud.eip.models.ExpenseDao;
import com.rushucloud.eip.models.ExpenseRepository;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {
	//
	private static Logger LOG = LoggerFactory.getLogger(ExpensesController.class);
	// ==============
	// PRIVATE FIELDS
	// ==============
//	@PersistenceContext
//	protected EntityManager entityManager;

	// Autowire an object of type ExpenseDao
	@Autowired
	private ExpenseDao _expenseDao;
	//
	private ExpenseRepository expenseRepository;

	@Autowired
	public ExpensesController(ExpenseRepository expenseRepository) {
		this.expenseRepository = expenseRepository;
	}

	// ==============
	// PUBLIC METHODS
	// ==============

	@RequestMapping(method = RequestMethod.POST)
	@ApiOperation(httpMethod = "POST", value = "Response a string describing if the reimbursement expense is successfully created or not.")
	public Expense create(@RequestBody @Valid Expense expense) {
//		return this.expenseRepository.save(expense);
		return this._expenseDao.save(expense);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of expense that is successfully get or not.")
	public JsonObject list() {
//		return new JsonObject(this.expenseRepository.findAll());
		return new JsonObject(this._expenseDao.findAll());
	}

	@RequestMapping(value="/#/{owner}",method = RequestMethod.GET)
//	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of expense that is successfully get or not.")
//	public Iterable<Expense> expensesByOwner(@PathVariable("owner") String owner) {
	public JsonObject expensesByOwner(@RequestParam(value = "owner", required = true, defaultValue = "employee1") String owner) {
		//
		Iterable<Expense> result = this._expenseDao.findExpensesByOwner(owner);
//		LOG.debug("expensesByOwner()result:"+result.toString());
		return new JsonObject(result);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(httpMethod = "GET", value = "Response a string describing if the expense id is successfully get or not.")
	public Expense get(@PathVariable("id") long id) {
//		return this.expenseRepository.findOne(id);
		return this._expenseDao.findById(id);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ApiOperation(httpMethod = "PUT", value = "Response a string describing if the reimbursement expense is successfully updated or not.")
	public Expense update(@PathVariable("id") long id,
			@RequestBody @Valid Expense expense) {
//		return this.expenseRepository.save(expense);
		return this._expenseDao.save(expense);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ApiOperation(httpMethod = "DELETE", value = "Response a string describing if the expense is successfully delete or not.")
	public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
		this.expenseRepository.delete(id);
//		this._expenseDao.delete(id);
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
