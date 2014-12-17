package com.rushucloud.eip.services;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rushucloud.eip.models.Expense;
import com.rushucloud.eip.report.Exporter;
import com.rushucloud.eip.report.Layouter;
import com.rushucloud.eip.report.Writer;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

//@Component("reportService")
//@Component
//@Transactional
@Service
@Transactional
@Repository
public class ReportServiceImpl implements ReportService {

	private static Logger logger = Logger.getLogger(ReportServiceImpl.class);
	// @see: //@see:
	// http://stackoverflow.com/questions/26425067/resolvedspring-boot-access-to-entitymanager
	// @Resource(name="sessionFactory")
	@PersistenceContext
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Object[]> getExpensesGroupByStatus(String owner) {
		String queryStr = "SELECT status,COUNT(status) FROM "
				+ Expense.class.getName() + " WHERE owner='" + owner
				+ "' GROUP BY status";
		List<Object[]> results = this.entityManager.createQuery(queryStr)
				.getResultList();
		return results;
	}

	/**
	 * Processes the download for Excel format. It does the following steps:
	 * 
	 * <pre>
	 * 1. Build the report layout
	 * 2. Retrieve the datasource
	 * 3. Compile the report layout
	 * 4. Generate the JasperPrint object
	 * 5. Export to a particular format, ie. XLS
	 * 6. Set the HttpServletResponse properties
	 * 7. Write to the output stream
	 * </pre>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void downloadXLS(HttpServletResponse response)
			throws ColumnBuilderException, ClassNotFoundException, JRException {
		logger.debug("Downloading Excel report");

		// 1. Build the report layout
		DynamicReport dr = Layouter.buildParentReportLayout();

		// 2. Add the datasource to a HashMap parameter
		HashMap<String, Object> params = new HashMap<String, Object>();
		// Here we're adding a custom datasource named "dynamicReportDs"
		// It's the name we put in the Layouter
		params.put("dynamicReportDs", getDatasource());

		// 3. Compile the report layout
		// This will regardless if you activate the
		// .setWhenNoDataAllSectionNoDetail() property or not
		// in the parentReportBuilder under the Layouter class. However you're
		// FORCED
		// to provide a dummy datasource for the parent report
		JasperReport jr = DynamicJasperHelper.generateJasperReport(dr,
				new ClassicLayoutManager(), params);

		// 4. Generate the JasperPrint object which also fills the report with
		// data
		// This will regardless if you activate the
		// .setWhenNoDataAllSectionNoDetail() property or not
		// in the parentReportBuilder under the Layouter class. However you're
		// FORCED
		// to provide a dummy datasource for the parent report
		JasperPrint jp = JasperFillManager.fillReport(jr, params,
				getJRDummyDatasource());

		// We can also combine compilation (3) and generation (4) in a single
		// line
		// This will only work if you add the .setWhenNoDataAllSectionNoDetail()
		// property
		// in the parentReportBuilder under the Layouter class
		// JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dr, new
		// ClassicLayoutManager(), params);

		// Create the output byte stream where the data will be written
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// 5. Export to XLS format
		Exporter.exportToXLS(jp, baos);

		// 6. Set the response properties
		String fileName = "SalesReport.xls";
		response.setHeader("Content-Disposition", "inline; filename="
				+ fileName);
		// Make sure to set the correct content type
		response.setContentType("application/vnd.ms-excel");
		// Assign the byte stream's size
		response.setContentLength(baos.size());

		// 7. Write to response stream
		Writer.write(response, baos);
	}

	/**
	 * Retrieves a Java List datasource.
	 * <p>
	 * The data is retrieved from a Hibernate HQL query.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getDatasource() {
		// Create query for retrieving products
		String queryStr = "SELECT amount,name,date FROM " + Expense.class.getName();
		List<Object[]> results = this.entityManager.createQuery(queryStr)
				.getResultList();
		// Return the data source
		logger.debug("Retrieving datasource:"+results.toString());
		return results;
	}

	/**
	 * Retrieves a dummy JRDataSource.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JRDataSource getJRDummyDatasource() {
		logger.debug("Retrieving JRdatasource");

		// Return the datasource
		return null;
	}

}
