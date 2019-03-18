package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdReport;

public interface IPfpAdReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public Integer deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdReport> dataList) throws Exception;

	public List<Object> getLastDate() throws Exception;
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception;
}
