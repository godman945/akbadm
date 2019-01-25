package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.IAdmPfpdAdPvclkReportDAO;
import com.pchome.akbadm.db.pojo.AdmPfpdAdPvclkReport;
import com.pchome.akbadm.db.service.BaseService;

public class AdmPfpdAdPvclkReportService extends BaseService<AdmPfpdAdPvclkReport, Integer> implements IAdmPfpdAdPvclkReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		log.info(">>>>>> quartz AdmPfpdAdPvclkReport1 call");
		return ((IAdmPfpdAdPvclkReportDAO) dao).prepareReportData(reportDate);
	}
 
	public List<Object> prepareReportData2(final String reportDate) throws Exception {
		log.info(">>>>>> quartz AdmPfpdAdPvclkReport2 call");
		return ((IAdmPfpdAdPvclkReportDAO) dao).prepareReportData2(reportDate);
	}
	
	public List<Object> prepareReportData3(final String reportDate) throws Exception {
		log.info(">>>>>> quartz AdmPfpdAdPvclkReport3 call");
		return ((IAdmPfpdAdPvclkReportDAO) dao).prepareReportData3(reportDate);
	}
	
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IAdmPfpdAdPvclkReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<AdmPfpdAdPvclkReport> dataList) throws Exception {
		((IAdmPfpdAdPvclkReportDAO) dao).insertReportData(dataList);
	}
	
}
