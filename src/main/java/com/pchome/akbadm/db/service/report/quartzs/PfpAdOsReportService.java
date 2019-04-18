package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfpAdOsReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdOsReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdOsReportService extends BaseService<PfpAdOsReport, Integer> implements IPfpAdOsReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdOsReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdOsReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdOsReport> dataList) throws Exception {
		((PfpAdOsReportDAO) dao).insertReportData(dataList);
	}

	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfpAdOsReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}
}
