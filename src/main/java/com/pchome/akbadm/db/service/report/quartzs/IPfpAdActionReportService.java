package com.pchome.akbadm.db.service.report.quartzs;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdActionReport;

public interface IPfpAdActionReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdActionReport> dataList) throws Exception;
	
	public float findAdClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
	
	public float findAdInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
	
	public float findAdClkAndInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType);
}
