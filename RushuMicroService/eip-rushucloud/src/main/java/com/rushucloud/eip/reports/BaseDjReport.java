/*
 * DynamicJasper: A library for creating reports dynamically by specifying
 * columns, groups, styles, etc. at runtime. It also saves a lot of development
 * time in many cases! (http://sourceforge.net/projects/dynamicjasper)
 *
 * Copyright (C) 2008  FDV Solutions (http://www.fdvsolutions.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 *
 * License as published by the Free Software Foundation; either
 *
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *
 */
package com.rushucloud.eip.reports;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.util.SortUtils;

public abstract class BaseDjReport
{

    public Map getParams()
    {
        return params;
    }

    protected static final Logger LOG = LogManager.getLogger(BaseDjReport.class);

    protected JasperPrint jp;

    protected JasperReport jr;

    protected Map params = new HashMap();

    protected DynamicReport dr;

    public abstract DynamicReport buildReport(String title, String subtitle, Boolean printBackgroundOnOddRows,
        Boolean useFullPageWidth) throws Exception;

    public String testReport(String title, String subtitle, Boolean printBackgroundOnOddRows, Boolean useFullPageWidth,
        JRBeanCollectionDataSource ds)
    {
        try {
            dr = buildReport(title, subtitle, printBackgroundOnOddRows, useFullPageWidth);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error(e.toString());
        }

        /**
         * Get a JRDataSource implementation
         */
        // JRDataSource ds = getDataSource();

        /**
         * Creates the JasperReport object, we pass as a Parameter the DynamicReport, a new ClassicLayoutManager
         * instance (this one does the magic) and the JRDataSource
         */
        try {
            jr = DynamicJasperHelper.generateJasperReport(dr, getLayoutManager(), params);
        } catch (JRException e) {
            e.printStackTrace();
            LOG.error(e.toString());
        }

        /**
         * Creates the JasperPrint object, we pass as a Parameter the JasperReport object, and the JRDataSource
         */
        LOG.debug("Filling...");
        if (ds != null)
            try {
                jp = JasperFillManager.fillReport(jr, params, ds);
            } catch (JRException e) {
                e.printStackTrace();
                LOG.error(e.toString());
            }
        else
            try {
                jp = JasperFillManager.fillReport(jr, params);
            } catch (JRException e) {
                e.printStackTrace();
                LOG.error(e.toString());
            }

        LOG.debug("Filling done.");
        // exportReport();

        // LOG.debug("test finished");
        return exportReport();
    }

    protected LayoutManager getLayoutManager()
    {
        return new ClassicLayoutManager();
    }

    public String getClassPath()
    {
        String classPath = this.getClass().getResource("/").getPath();
        return classPath;
    }

    protected String exportReport()
    {
        LOG.debug("Exporting....");
        // String exportUrl = System.getProperty("user.dir") + "/target/reports/" + this.getClass().getName() + ".pdf";
        String timestampUrl = getReportTimestampUri(".pdf");
        String exportFullUrl = getReportFullUri(timestampUrl);
        try {
            ReportExporter.exportReport(jp, exportFullUrl);
        } catch (FileNotFoundException | JRException e) {
            // e.printStackTrace();
            LOG.error(e.toString());
        }
        // exportToJRXML();
        // exportToHTML();
        return timestampUrl;
    }

    private String getReportFullUri(String timestampUrl)
    {
        return getClassPath() + "/reports/" + timestampUrl;
    }

    private String getReportTimestampUri(String fileExt)
    {
        String timestampFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date()) + fileExt;
        return timestampFileName;
    }

    protected void exportToJRXML() throws Exception
    {
        if (this.jr != null) {
            DynamicJasperHelper.generateJRXML(this.jr, "UTF-8", System.getProperty("user.dir") + "/target/reports/"
                + this.getClass().getName() + ".jrxml");

        } else {
            DynamicJasperHelper.generateJRXML(this.dr, this.getLayoutManager(), this.params, "UTF-8",
                System.getProperty("user.dir") + "/target/reports/" + this.getClass().getName() + ".jrxml");
        }
    }

    protected void exportToHTML() throws Exception
    {
        ReportExporter.exportReportHtml(this.jp, System.getProperty("user.dir") + "/target/reports/"
            + this.getClass().getName() + ".html");
    }

    /**
     * @return JRDataSource
     */
    protected JRDataSource getDataSource()
    {
        Collection dummyCollection = RepositoryProducts.getDummyCollection();
        dummyCollection = SortUtils.sortCollection(dummyCollection, dr.getColumns());

        JRDataSource ds = new JRBeanCollectionDataSource(dummyCollection); // Create
                                                                           // a
                                                                           // JRDataSource,
                                                                           // the
                                                                           // Collection
                                                                           // used
                                                                           // here
                                                                           // contains
                                                                           // dummy
                                                                           // hardcoded
                                                                           // objects...
        return ds;
    }

    public Collection getDummyCollectionSorted(List columnlist)
    {
        Collection dummyCollection = RepositoryProducts.getDummyCollection();
        return SortUtils.sortCollection(dummyCollection, columnlist);

    }

    public DynamicReport getDynamicReport()
    {
        return dr;
    }

    /**
     * Uses a non blocking HSQL DB. Also uses HSQL default test data
     * 
     * @return a Connection
     * @throws Exception
     */
    public static Connection createSQLConnection() throws Exception
    {
        Connection con = null;
        Class.forName("org.hsqldb.jdbcDriver");
        con = DriverManager.getConnection("jdbc:hsqldb:file:target/test-classes/hsql/test_dj_db", "sa", "");

        return con;
    }

    public int getYear()
    {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
}
