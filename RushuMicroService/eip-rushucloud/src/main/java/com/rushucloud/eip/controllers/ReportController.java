package com.rushucloud.eip.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
 * @see http://stackoverflow.com/questions/11677572/dealing-with-xerces-hell-in-java-maven
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
        PDF("PDF", "application/pdf"),
        HTML("HTML", "application/vnd.ms-excel"),
        XLS("XLS", "application/vnd.ms-excel"),
        XML("XML", "text/xml"),
        RTF("RTF", "application/rtf"),
        CSV("CSV", "text/plain"),
        TXT("TXT", "text/plain");
        //
        private String value;

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        private String contentType;

        public String getContentType()
        {
            return contentType;
        }

        public void setContentType(String contentType)
        {
            this.contentType = contentType;
        }

        private DocType(String value, String contentType)
        {
            this.setValue(value);
            this.setContentType(contentType);
        }
    }

    /**
     * @throws JRException
     * @see: http://jasperreports.sourceforge.net/sample.reference/fonts/index.html Downloads the report as an PDF
     *       format.
     *       <p>
     *       Make sure this method doesn't return any model. Otherwise, you'll get an
     *       "IllegalStateException: getOutputStream() has already been called for this response"
     */
    @RequestMapping(value = "/doc", method = RequestMethod.GET, params = {"type"})
    public JsonObject getDocument(@RequestParam(value = "type") String type) throws JRException
    {
        String docUrl = "";
        LOG.info("Required document type is: " + type);
        // FastReport fastReport = new FastReport();
        // Create a JRDataSource,the Collection used here contains dummy hard-coded objects...
        List<Expense> expensesAll = Lists.newArrayList(this._expenseDao.findAll());
        // Log.info("expensesAll for JRBeanCollectionDataSource"+expensesAll);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(expensesAll);
        // LOG.debug("fastReport data(before PDF generate):" + expensesAll.toString());
        //
        long start = System.currentTimeMillis();
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
            // SAXParser parser = new SAXParser();
            // LOG.info("DocType.PDF.getValue() == type.toUpperCase(): "
            // + DocType.PDF.getValue().toString().equals(type.toUpperCase()));
            /**
             * 1- export to PDF
             */
            if (DocType.PDF.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.PDF.getValue().toLowerCase();
                try {
                    JasperExportManager.exportReportToPdfFile(printFileName, docUrl);
                    // } catch (NullPointerException ex) {
                    // LOG.error("NPE encountered in body: " + ex.getStackTrace().toString());
                    // } catch (Throwable ex) {
                    // LOG.error("Regular Throwable: " + ex.getMessage());
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToPdfFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }

            /**
             * 2- export to HTML
             */
            if (DocType.HTML.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.HTML.getValue().toLowerCase();
                try {
                    JasperExportManager.exportReportToHtmlFile(printFileName, docUrl);
                    // } catch (NullPointerException ex) {
                    // LOG.error("NPE encountered in body: " + ex.getStackTrace().toString());
                    // } catch (Throwable ex) {
                    // LOG.error("Regular Throwable: " + ex.getMessage());
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToHTMLFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
            /**
             * 3- export to Excel sheet
             */
            if (DocType.XLS.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.XLS.getValue().toLowerCase();
                try {
                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, docUrl);
                    exporter.exportReport();
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToXLSFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
            /**
             * 4- export to XML
             */
            if (DocType.XML.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.XML.getValue().toLowerCase();
                try {
                    JasperExportManager.exportReportToXmlFile(printFileName, docUrl, true);
                    // } catch (NullPointerException ex) {
                    // LOG.error("NPE encountered in body: " + ex.getStackTrace().toString());
                    // } catch (Throwable ex) {
                    // LOG.error("Regular Throwable: " + ex.getMessage());
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToXMLFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
            /**
             * 5- export to TXT
             */
            if (DocType.TXT.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.CSV.getValue().toLowerCase();
                try {
                    JRTextExporter exporter = new JRTextExporter();
                    exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, docUrl);
                    exporter.exportReport();
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToXLSFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
            /**
             * 6- export to RTF
             */
            if (DocType.RTF.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.RTF.getValue().toLowerCase();
                try {
                    JRRtfExporter exporter = new JRRtfExporter();
                    exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, docUrl);
                    exporter.exportReport();
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToXLSFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
            /**
             * 7- export to CSV
             */
            if (DocType.CSV.getValue().toString().equals(type.toUpperCase())) {
                docUrl = JASPER_REPORT_BASE + "." + DocType.CSV.getValue().toLowerCase();
                try {
                    JRCsvExporter exporter = new JRCsvExporter();
                    exporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, printFileName);
                    exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, docUrl);
                    exporter.exportReport();
                } catch (Exception e) {
                    LOG.error("JasperExportManager.exportReportToXLSFile: " + e.getClass().toString()
                        + e.getStackTrace().toString());
                    e.printStackTrace();
                }
            }
        }
        LOG.info("Report document created time : " + (System.currentTimeMillis() - start));
        // e.g. http://localhost:8082/api/reports/com.rushucloud.eip.reports.FastReport.pdf
        LOG.info("Report document url:" + docUrl);
        return new JsonObject(docUrl);
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
