package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdSizeReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdSizeReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdSizeReportService extends BaseService<PfbxAdSizeReport, Integer> implements IPfbxAdSizeReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdSizeReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdSizeReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdSizeReport> dataList) throws Exception {
		((IPfbAdSizeReportDAO) dao).insertReportData(dataList);
	}

}
