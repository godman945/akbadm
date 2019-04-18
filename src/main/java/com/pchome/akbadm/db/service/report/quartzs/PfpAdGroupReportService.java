package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfpAdGroupReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdGroupReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdGroupReportService extends BaseService<PfpAdGroupReport, Integer> implements IPfpAdGroupReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdGroupReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdGroupReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdGroupReport> dataList) throws Exception {
		((PfpAdGroupReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((PfpAdGroupReportDAO) dao).getLastDate();
	}

	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfpAdGroupReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}
}
