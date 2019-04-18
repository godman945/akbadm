package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.AdmPfpdAdPvclkReport;

public interface IAdmPfpdAdPvclkReportDAO {
	
	public List<Object> prepareReportData(final String reportDate) throws Exception;
	
	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<AdmPfpdAdPvclkReport> dataList) throws Exception;
	
	public List<Object> prepareReportData2(final String reportDate) throws Exception;
	
	public List<Object> prepareReportData3(final String reportDate) throws Exception;
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception;
}
