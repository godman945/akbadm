package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;



import com.pchome.akbadm.db.dao.report.quartzs.PfpAdWebsiteReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdWebsiteReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdWebsiteReportService extends BaseService<PfpAdWebsiteReport, Integer> implements IPfpAdWebsiteReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdWebsiteReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdWebsiteReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdWebsiteReport> dataList) throws Exception {
		((PfpAdWebsiteReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((PfpAdWebsiteReportDAO) dao).getLastDate();
	}
}
