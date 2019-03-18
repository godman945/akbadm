package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdAgeReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdAgeReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdAgeReportService extends BaseService<PfdAdAgeReport, Integer> implements IPfdAdAgeReportService {
	
	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdAdAgeReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdAgeReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdAdAgeReport> dataList) throws Exception {
		((PfdAdAgeReportDAO) dao).insertReportData(dataList);
	}
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfdAdAgeReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}
}
