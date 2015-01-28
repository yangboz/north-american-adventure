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

import java.util.Map;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.DJValueFormatter;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;

public class FastReport extends BaseDjReport
{

    @Override
    public DynamicReport buildReport(String title, String subtitle, Boolean printBackgroundOnOddRows,
        Boolean useFullPageWidth) throws Exception
    {
        /**
         * Creates the DynamicReportBuilder and sets the basic options for the report
         */
        FastReportBuilder drb = new FastReportBuilder();
        drb.addColumn("Id", "id", Long.class.getName(), 60)
            .addColumn("Amount", "amount", Double.class.getName(), 60)
            .addColumn("条目名", "name", String.class.getName(), 100)
            // .addColumn("Name", "name", String.class.getName(), 100)
            .addColumn("Owner", "owner", String.class.getName(), 100)
            .addColumn("Status", "status", Enum.class.getName(), 100)
            .addGroups(2)
            // .setTitle("November \"2006\" sales report")
            // .setSubtitle("This report was generated at " + new Date())
            // .setPrintBackgroundOnOddRows(true)
            // .setUseFullPageWidth(true);
            .setTitle(title).setSubtitle(subtitle).setPrintBackgroundOnOddRows(printBackgroundOnOddRows)
            .setUseFullPageWidth(useFullPageWidth);

        drb.addGlobalFooterVariable(drb.getColumn(1), DJCalculation.SUM, null, new DJValueFormatter()
        {

            @Override
            public String getClassName()
            {
                return String.class.getName();
            }

            @Override
            public Object evaluate(Object value, Map fields, Map variables, Map parameters)
            {
                return (value == null ? "0" : value.toString()) + " Total";
            }
        });

        DynamicReport dr = drb.build();

        return dr;
    }

    public DynamicReport buildReport() throws Exception
    {
        return null;
    }

    // public static void main(String[] args) throws Exception {
    // FastReport test = new FastReport();
    // test.testReport();
    // test.exportToJRXML();
    // JasperViewer.viewReport(test.jp); //finally display the report report
    // JasperDesignViewer.viewReportDesign(test.jr);
    // }

}
