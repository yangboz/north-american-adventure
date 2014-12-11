package com.rushucloud.eip.services;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

public interface ReportService {
	public void downloadXLS(HttpServletResponse response)  throws ColumnBuilderException, ClassNotFoundException, JRException ;
}
