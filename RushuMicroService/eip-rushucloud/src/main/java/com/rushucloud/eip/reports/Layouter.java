package com.rushucloud.eip.reports;

import java.util.Date;

import org.apache.log4j.Logger;

import ar.com.fdvs.dj.core.DJConstants;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DJBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;

/**
 * Everything under the package org.krams.tutorial.dynamicjasper are settings
 * imposed by DynamicJasper (not Jasper)
 * <p>
 * Builds the report layout, the template, the design, the pattern or whatever
 * synonym you may want to call it.
 * 
 * @author Krams at {@link http://krams915@blogspot.com}
 */
public class Layouter {

	private static Logger logger = Logger.getLogger("Layouter");

	/**
	 * Builds the report layout. This doesn't have any data yet. This is your
	 * template.
	 * 
	 * @return the layout
	 */
	public static DynamicReport buildChildReportLayout()
			throws ColumnBuilderException, ClassNotFoundException {

		// Create an instance of FastReportBuilder
		FastReportBuilder drb = new FastReportBuilder();

		// Create columns
		// The column fields must match the name and type of the properties in
		// your datasource
		drb.addColumn("Id", "id", Long.class.getName(), 50)
				.addColumn("Amount", "amount", Double.class.getName(), 50)
				.addColumn("Name", "name", String.class.getName(), 50)
				// Disables pagination
				.setIgnorePagination(true)

				// Experiment with this numbers to see the effect
				.setMargins(0, 0, 0, 0)

				// Set the title shown inside the Excel file
				.setTitle("Expense Report")

				// Set the subtitle shown inside the Excel file
				.setSubtitle("This report was generated at " + new Date())

				// Set to true if you want to constrain your report within the
				// page boundaries
				// For longer reports, set it to false
				.setUseFullPageWidth(true);

		// Set the name of the file
		// drb.setReportName("Sales Report");

		// Build the report layout. It doesn't have any data yet!
		DynamicReport dr = drb.build();

		// Return the layout
		return dr;
	}

	/**
	 * Builds the parent report layout. This doesn't have any data yet. This is
	 * your template.
	 * 
	 * @return the layout
	 */
	public static DynamicReport buildParentReportLayout()
			throws ColumnBuilderException, ClassNotFoundException {

		// Create an instance of FastReportBuilder
		FastReportBuilder parentReportBuilder = new FastReportBuilder();

		// Disables pagination
		parentReportBuilder.setIgnorePagination(true)

		// Experiment with this numbers to see the effect
				.setMargins(0, 0, 0, 0)

				// This is a critical property!!!
				// If the parentReportBuilder doesn't have an associated
				// datasource, you must set this property!
				.setWhenNoDataAllSectionNoDetail()

				// Set to true if you want to constrain your report within the
				// page boundaries
				// For longer reports, set it to false
				.setUseFullPageWidth(true);

		// Set the name of the file
		parentReportBuilder.setReportName("Concat Report");

		// Create a child report
		try {
			// Add the dynamicreport to the parent report layout
			parentReportBuilder.addConcatenatedReport(buildChildReportLayout(),
					new ClassicLayoutManager(), "dynamicReportDs",
					DJConstants.DATA_SOURCE_ORIGIN_PARAMETER,
					DJConstants.DATA_SOURCE_TYPE_COLLECTION);

		} catch (DJBuilderException e) {
			logger.error("Unable to concat child report");
			throw new RuntimeException("Unable to concat child report");
		}

		// Build the parent report layout. It doesn't have any data yet!
		DynamicReport dr = parentReportBuilder.build();

		// Return the layout
		return dr;
	}

}
