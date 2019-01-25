package com.pchome.akbadm.db.service.pfbx.quartzs;


import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdCustomerReport;

public interface IPfbxAdCustomerReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfbxAdCustomerReport> dataList) throws Exception;

	public List<PfbxAdCustomerReport> selectOneByUpdateDate() throws Exception;

//	public float findPfdAdActionClickCost(String pfdCustomerInfoId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType);

//	public float findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String payType);
}
