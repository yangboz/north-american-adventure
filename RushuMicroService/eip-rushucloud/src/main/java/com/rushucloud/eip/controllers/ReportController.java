package com.rushucloud.eip.controllers;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.Expense;
import com.rushucloud.eip.models.ExpenseDao;
import com.rushucloud.eip.models.Item;
import com.rushucloud.eip.services.ReportService;
import com.wordnik.swagger.annotations.ApiOperation;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

/**
 * Handles download requests
 * 
 * @author Krams at {@link http://krams915@blogspot.com}
 */
@RestController
@RequestMapping("/report")
public class ReportController {

	private static Logger logger = Logger.getLogger(ReportController.class);
	
//	@Resource(name="downloadService")
//	private DownloadService downloadService;
	// Auto-wire an object of type ExpenseDao
	@Autowired
	private ExpenseDao _expenseDao;
	
	@Autowired
	private ReportService reportService;

	/**
	 * Downloads the report as an Excel format. 
	 * <p>
	 * Make sure this method doesn't return any model. Otherwise, you'll get 
	 * an "IllegalStateException: getOutputStream() has already been called for this response"
	 */
    @RequestMapping(value = "/xls", method = RequestMethod.GET)
    //TODO: @see: http://krams915.blogspot.in/2011/02/spring-3-dynamicjasper-hibernate_4736.html, make it right.
    public @ResponseBody void getXLS(HttpServletResponse response, Model model) throws ColumnBuilderException, ClassNotFoundException, JRException {
    	logger.debug("Received request to download report as an XLS");
    	
    	// Delegate to downloadService. Make sure to pass an instance of HttpServletResponse 
    	reportService.downloadXLS(response);
	}
    //
    @PersistenceContext
    EntityManager entityManager;
    //
	@RequestMapping(method = RequestMethod.GET,params = {"owner"})
	@ApiOperation(httpMethod = "GET", value = "Response a list describing all of reports that is successfully get or not.")
	public JsonObject list(@RequestParam(value = "owner") String owner) {
//		return new JsonObject(this.expenseRepository.findAll());
		if(owner!=null)
		{
			//
			Iterable<Expense> result = this._expenseDao.findExpensesByOwner(owner);
//			LOG.debug("itemsByOwner()result:"+result.toString());
			return new JsonObject(result);
		}else{
			return new JsonObject(this._expenseDao.findAll());
		}
	}
}
