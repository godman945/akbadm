package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfpAdTimeReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdTimeReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdTimeReportService extends BaseService<PfpAdTimeReport, Integer> implements IPfpAdTimeReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfpAdTimeReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfpAdTimeReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfpAdTimeReport> dataList) throws Exception {
		((PfpAdTimeReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((PfpAdTimeReportDAO) dao).getLastDate();
	}

	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfpAdTimeReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}
}
