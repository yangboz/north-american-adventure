package com.rushucloud.eip.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;

public interface ReportService {
	public void downloadXLS(HttpServletResponse response)  throws ColumnBuilderException, ClassNotFoundException, JRException ;
	public List<Object[]> getExpensesGroupByStatus(String owner);
}
