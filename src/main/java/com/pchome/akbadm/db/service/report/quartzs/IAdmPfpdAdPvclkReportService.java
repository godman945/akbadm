package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmPfpdAdPvclkReport;

public interface IAdmPfpdAdPvclkReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception;
	
	public List<Object> prepareReportData2(String reportDate) throws Exception;
	
	public List<Object> prepareReportData3(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<AdmPfpdAdPvclkReport> dataList) throws Exception;
	
	public int updateConvertCountData(String convertDate, String convertRangeDate) throws Exception;
	
}
