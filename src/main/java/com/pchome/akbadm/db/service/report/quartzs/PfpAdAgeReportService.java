package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfpAdAgeReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdAgeReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdAgeReportService extends BaseService<PfpAdAgeReport, Integer> implements IPfpAdAgeReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdAgeReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdAgeReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdAgeReport> dataList) throws Exception {
		((PfpAdAgeReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((PfpAdAgeReportDAO) dao).getLastDate();
	}
}
