package com.rushucloud.eip.services;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("reportService")
@Transactional
public class ReportServiceImpl implements ReportService {

	@Override
	public void downloadXLS(HttpServletResponse response) {

	}

}
