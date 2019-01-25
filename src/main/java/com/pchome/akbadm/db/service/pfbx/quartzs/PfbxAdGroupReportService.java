package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdGroupReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdGroupReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdGroupReportService extends BaseService<PfbxAdGroupReport, Integer> implements IPfbxAdGroupReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdGroupReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdGroupReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdGroupReport> dataList) throws Exception {
		((IPfbAdGroupReportDAO) dao).insertReportData(dataList);
	}

}
