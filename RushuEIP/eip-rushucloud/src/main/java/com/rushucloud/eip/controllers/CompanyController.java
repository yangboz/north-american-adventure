/**
 * @author yangboz
 * @see http://blog.netgloo.com/2014/10/27/using-mysql-in-spring-boot-via-spring-data-jpa-and-hibernate/
 */
package com.rushucloud.eip.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonString;
import com.rushucloud.eip.models.Company;
import com.rushucloud.eip.models.CompanyDao;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
public class CompanyController {
	@RequestMapping("/home")
	public String home(){
		return "index";
	}
	// ==============
	// PRIVATE FIELDS
	// ==============

	// Autowire an object of type CompanyDao
	@Autowired
	private CompanyDao _companyDao;

	// ==============
	// PUBLIC METHODS
	// ==============

	/**
	 * Create a new company and save it in the database.
	 *
	 * @param email
	 *            company email
	 * @param name
	 *            use name
	 * @return a string describing if the company is successfully created or
	 *         not.
	 */
	// @RequestMapping("/create")
	// @ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "create")
	@ApiOperation(httpMethod = "GET", value = "a string describing if the company is successfully created or not.")
	public JsonString create(
			@RequestParam(value="email", required=true, defaultValue="test@test.com") String email,
			@RequestParam(value="name", required=true, defaultValue="tester") String name,
			@RequestParam(value="domain", required=true, defaultValue="example.com") String domain
			) {
		try {
			Company company = new Company(email, name,domain);
			_companyDao.save(company);
		} catch (Exception ex) {
			return new JsonString("Error creating the company: " + ex.toString());
		}
		return new JsonString("company succesfully created!");
	}

	/**
	 * Delete the company having the passed id.
	 *
	 * @param email
	 *            the email for the company to delete
	 * @return a string describing if the company is successfully deleted or
	 *         not.
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(long id) {
		try {
			Company company = new Company(id);
			_companyDao.delete(company);
		} catch (Exception ex) {
			return "Error deleting the company:" + ex.toString();
		}
		return "company succesfully deleted!";
	}

	/**
	 * Return the id for the company having the passed email.
	 *
	 * @param email
	 *            the email to search in the database.
	 * @return the company id or a message error if the company is not found.
	 */
	@RequestMapping("/get-by-email")
	@ResponseBody
	public String getByEmail(String email) {
		String companyId;
		try {
			Company company = _companyDao.findByEmail(email);
			companyId = String.valueOf(company.getId());
		} catch (Exception ex) {
			return "company not found";
		}
		return "The company id is: " + companyId;
	}

	/**
	 * Update the email and the name for the company in the database having the
	 * passed id.
	 *
	 * @param id
	 *            the id for the company to update.
	 * @param email
	 *            the new email.
	 * @param name
	 *            the new name.
	 * @return a string describing if the company is successfully updated or
	 *         not.
	 */
	@RequestMapping("/update")
	@ResponseBody
	public String updatecompany(long id, String email, String name) {
		try {
			Company company = _companyDao.findOne(id);
			company.setEmail(email);
			company.setName(name);
			_companyDao.save(company);
		} catch (Exception ex) {
			return "Error updating the company: " + ex.toString();
		}
		return "company succesfully updated!";
	}
}
