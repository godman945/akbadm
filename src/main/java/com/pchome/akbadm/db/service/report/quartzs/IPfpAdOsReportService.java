package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdOsReport;

public interface IPfpAdOsReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdOsReport> dataList) throws Exception;
	
	public int updateConvertCountData(String convertDate, String convertRangeDate) throws Exception;
}
