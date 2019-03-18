package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdActionReport;

public interface IPfpAdActionReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdActionReport> dataList) throws Exception;
	
	public float findAdClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
	
	public float findAdInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
	
	public float findAdClkAndInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
	
	public List<Object> findPfpAdActionReport(Date startDate, Date endDate);
	
	public int updateConvertCountData(String convertDate,String convertRangeDate);
}
