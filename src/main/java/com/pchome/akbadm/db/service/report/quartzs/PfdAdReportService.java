package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdReportService extends BaseService<PfdAdReport, Integer> implements IPfdAdReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdAdReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdAdReport> dataList) throws Exception {
		((PfdAdReportDAO) dao).insertReportData(dataList);
	}
}
