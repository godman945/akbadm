package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdStyleReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdStyleReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdStyleReportService extends BaseService<PfbxAdStyleReport, Integer> implements IPfbxAdStyleReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdStyleReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdStyleReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdStyleReport> dataList) throws Exception {
		((IPfbAdStyleReportDAO) dao).insertReportData(dataList);
	}

}
