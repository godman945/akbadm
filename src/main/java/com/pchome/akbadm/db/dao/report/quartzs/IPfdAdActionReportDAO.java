package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdAdActionReport;

public interface IPfdAdActionReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfdAdActionReport> dataList) throws Exception;
	
	public List<Object> findPfdAdActionClickCost(String pfdCustomerInfoId, Date startDate, Date endDate, String payType);
	
	public List<Object> findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, Date startDate, Date endDate, String payType);
	
	public List<Object> findPfdAdActionReportToVo(final String pfdId, final Date startDate, final Date endDate, final String payType);
	
	public List<PfdAdActionReport> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate);
}
