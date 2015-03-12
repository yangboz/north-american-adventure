package com.rushucloud.eip.controllers;

import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.parsers.SAXParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.rushucloud.eip.dto.JsonObject;
import com.rushucloud.eip.models.DataBeanList;
import com.rushucloud.eip.models.Expense;
import com.rushucloud.eip.models.ExpenseDao;
import com.rushucloud.eip.settings.FolderSetting;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Handles Dynamic/Jasper reports download requests
 * 
 * @author Krams at {@link http://krams915@blogspot.com}
 */
@RestController
@RequestMapping("/report")
public class ReportController
{

    private static Logger LOG = LogManager.getLogger(ReportController.class);

    // @Resource(name="downloadService")
    // private DownloadService downloadService;
    // Auto-wire an object of type ExpenseDao
    @Autowired
    private ExpenseDao _expenseDao;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private FolderSetting folderSetting;

    /**
     * Downloads the report as an Excel format.
     * <p>
     * Make sure this method doesn't return any model. Otherwise, you'll get an
     * "IllegalStateException: getOutputStream() has already been called for this response"
     * 
     * @throws IOException
     * @throws FontFormatException
     */
    @RequestMapping(value = "/xls", method = RequestMethod.GET)
    // TODO: @see:
    // http://krams915.blogspot.in/2011/02/spring-3-dynamicjasper-hibernate_4736.html,
    // make it right.
    public @ResponseBody void getXLS(HttpServletResponse response, Model model) throws JRException
    {
        LOG.debug("Received request to download report as an XLS");

        // Delegate to downloadService. Make sure to pass an instance of
        // HttpServletResponse
        // reportService.downloadXLS(response);
    }

    //
    // @PersistenceContext
    // EntityManager entityManager;
    //
    @RequestMapping(method = RequestMethod.GET, params = {"owner"})
    @ApiOperation(httpMethod = "GET", value = "Response a list describing all of reports that is successfully get or not.")
    public JsonObject list(@RequestParam(value = "owner") String owner)
    {
        // return new JsonObject(this.expenseRepository.findAll());
        if (owner != null) {
            // Query query =
            // entityManager.createQuery("SELECT status,COUNT(status) FROM expenses WHERE owner='"+owner+"' GROUP BY status");
            // List<Object[]> results = query.getResultList();
            // return new JsonObject(results);
            // return new JsonObject(reportService.getExpensesGroupByStatus(owner));
            //
            Iterable<Expense> result = this._expenseDao.findExpensesByOwner(owner);
            // // LOG.debug("itemsByOwner()result:"+result.toString());
            return new JsonObject(result);
        } else {
            return new JsonObject(this._expenseDao.findAll());
        }
    }

    public static enum DocType
    {
        PDF,
        HTML,
        XLS,
        XML,
        RTF,
        CSV,
        TXT
    }

    public String getContentType(DocType docType)
    {
        String contentType = "text/html";
        switch (docType) {
            case PDF:
                contentType = "application/pdf";
                break;
            case XLS:
                contentType = "application/vnd.ms-excel";
                break;
            case XML:
                contentType = "text/xml";
                break;
            case RTF:
                contentType = "application/rtf";
                break;
            case CSV:
                contentType = "text/plain";
                break;
            default:
                break;
        }
        return contentType;
    }

    /**
     * @throws JRException
     * @see: http://jasperreports.sourceforge.net/sample.reference/fonts/index.html Downloads the report as an PDF
     *       format.
     *       <p>
     *       Make sure this method doesn't return any model. Otherwise, you'll get an
     *       "IllegalStateException: getOutputStream() has already been called for this response"
     */
    @RequestMapping(value = "/pdf", method = RequestMethod.GET, params = {"title", "subtitle", "background", "fullpage"})
    public JsonObject getPDF(@RequestParam(value = "title") String title,
        @RequestParam(value = "subtitle") String subtitle,
        @RequestParam(value = "background", defaultValue = "true") Boolean printBackgroundOnOddRows,
        @RequestParam(value = "fullpage", defaultValue = "true") Boolean useFullPageWidth) throws JRException
    {
        String pdfUrl = "";
        // FastReport fastReport = new FastReport();
        // Create a JRDataSource,the Collection used here contains dummy hard-coded objects...
        List<Expense> expensesAll = Lists.newArrayList(this._expenseDao.findAll());
        // Log.info("expensesAll for JRBeanCollectionDataSource"+expensesAll);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(expensesAll);
        LOG.debug("fastReport data(before PDF generate):" + expensesAll.toString());
        //
        long start = System.currentTimeMillis();
        // try {
        // String tempPdfUrl =
        // JasperExportManager.exportReportToPdfFile(this.getClass().getResource("/").getPath()
        // + "/reports/A4_blank.jrprint");
        // LOG.info("tempPdfUrl:" + tempPdfUrl);
        // } catch (JRException e1) {
        // // e1.printStackTrace();
        // LOG.error(e1.toString());
        // }
        //
        String jasperDestFile = JasperCompileManager.compileReportToFile(JRXML_SOURCE_FILE);
        //
        String printFileName = null;
        DataBeanList DataBeanList = new DataBeanList();
        ArrayList dataList = DataBeanList.getDataBeanList();
        JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList, true);
        //
        Map parameters = new HashMap();
        LOG.info("JASPER_DEST_FILE:" + JASPER_DEST_FILE);
        try {
            printFileName = JasperFillManager.fillReportToFile(JASPER_DEST_FILE, parameters, beanColDataSource);
        } catch (Exception e) {
            LOG.error("JasperFillManager.fillReportToFile: " + e.toString());
            e.printStackTrace();
        }
        if (printFileName != null) {
            //
            SAXParser parser = new SAXParser();
            /**
             * 1- export to PDF
             */
            try {
                JasperExportManager.exportReportToPdfFile(printFileName, JASPER_REPORT_BASE + ".pdf");
                // } catch (NullPointerException ex) {
                // LOG.error("NPE encountered in body: " + ex.getStackTrace().toString());
                // } catch (Throwable ex) {
                // LOG.error("Regular Throwable: " + ex.getMessage());
            } catch (Exception e) {
                LOG.error("JasperExportManager.exportReportToPdfFile: " + e.getClass().toString()
                    + e.getStackTrace().toString());
                e.printStackTrace();
            }
            pdfUrl = JASPER_REPORT_BASE + ".pdf";
            /**
             * 2- export to HTML
             */
            // JasperExportManager.exportReportToHtmlFile(printFileName, JASPER_REPORT_BASE + ".html");

            /**
             * 3- export to Excel sheet
             */
            // JRXlsExporter exporter = new JRXlsExporter();

            // exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
            // exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, JASPER_REPORT_BASE + ".xls");

            // exporter.exportReport();
        }
        LOG.info("PDF creation time : " + (System.currentTimeMillis() - start));
        // e.g. http://localhost:8082/api/reports/com.rushucloud.eip.reports.FastReport.pdf
        LOG.info("fastReport data(after PDF generate):" + pdfUrl);
        return new JsonObject(pdfUrl);
    }

    public String getClassPath()
    {
        String classPath = this.getClass().getResource("/").getPath();
        return classPath;
    }

    private final String JASPER_REPORT_BASE = getClassPath() + "reports/jasper_report_template";

    private final String JRXML_SOURCE_FILE = JASPER_REPORT_BASE + ".jrxml";

    private final String JASPER_DEST_FILE = JASPER_REPORT_BASE + ".jasper";

    // @ExceptionHandler(IllegalArgumentException.class)
    // public void handleBadRequests(HttpServletResponse response) throws IOException
    // {
    // response.sendError(HttpStatus.BAD_REQUEST.value(), response.toString());
    // }
}
