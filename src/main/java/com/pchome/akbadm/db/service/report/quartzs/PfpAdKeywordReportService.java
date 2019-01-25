package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfpAdKeywordReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdKeywordReportService extends BaseService<PfpAdKeywordReport, Integer> implements IPfpAdKeywordReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdKeywordReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdKeywordReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdKeywordReport> dataList) throws Exception {
		((PfpAdKeywordReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((PfpAdKeywordReportDAO) dao).getLastDate();
	}
}
