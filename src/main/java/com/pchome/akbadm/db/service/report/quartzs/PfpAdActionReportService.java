package com.pchome.akbadm.db.service.report.quartzs;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.IPfpAdActionReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdActionReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdActionReportService extends BaseService<PfpAdActionReport, Integer> implements IPfpAdActionReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((IPfpAdActionReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfpAdActionReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdActionReport> dataList) throws Exception {
		((IPfpAdActionReportDAO) dao).insertReportData(dataList);
	}
	
	public float findAdClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {
		return ((IPfpAdActionReportDAO) dao).findAdClkPrice(customerInfoId,	startDate, endDate, payType);
	}
	
	public float findAdInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {
		return ((IPfpAdActionReportDAO) dao).findAdInvalidClkPrice(customerInfoId,	startDate, endDate, payType);
	}
	
	public float findAdClkAndInvalidClkPrice(String customerInfoId, Date startDate, Date endDate, String payType) {
		return ((IPfpAdActionReportDAO) dao).findAdClkAndInvalidClkPrice(customerInfoId, startDate, endDate, payType);
	}
}
