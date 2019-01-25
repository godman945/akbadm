package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdGroupReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdGroupReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdGroupReportService extends BaseService<PfdAdGroupReport, Integer> implements IPfdAdGroupReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdAdGroupReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdGroupReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdAdGroupReport> dataList) throws Exception {
		((PfdAdGroupReportDAO) dao).insertReportData(dataList);
	}
}
