package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdUnitReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdUnitReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdUnitReportService extends BaseService<PfbxAdUnitReport, Integer> implements IPfbxAdUnitReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdUnitReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdUnitReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdUnitReport> dataList) throws Exception {
		((IPfbAdUnitReportDAO) dao).insertReportData(dataList);
	}

}
