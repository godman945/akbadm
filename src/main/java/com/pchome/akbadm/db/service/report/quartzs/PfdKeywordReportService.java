package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdKeywordReportDAO;
import com.pchome.akbadm.db.pojo.PfdKeywordReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdKeywordReportService extends BaseService<PfdKeywordReport, Integer> implements IPfdKeywordReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdKeywordReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdKeywordReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdKeywordReport> dataList) throws Exception {
		((PfdKeywordReportDAO) dao).insertReportData(dataList);
	}
}
