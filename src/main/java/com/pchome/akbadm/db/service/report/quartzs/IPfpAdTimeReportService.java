package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdTimeReport;

public interface IPfpAdTimeReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdTimeReport> dataList) throws Exception;

	public List<Object> getLastDate() throws Exception;
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception;
}
