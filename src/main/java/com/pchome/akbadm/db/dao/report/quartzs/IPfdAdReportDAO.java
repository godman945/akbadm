package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdAdReport;

public interface IPfdAdReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfdAdReport> dataList) throws Exception;
	
	public int updateConvertCountData(String convertDate,String convertRangeDate)throws Exception;
}
