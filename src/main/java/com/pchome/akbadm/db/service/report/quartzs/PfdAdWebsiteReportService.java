package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdWebsiteReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdWebsiteReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdWebsiteReportService extends BaseService<PfdAdWebsiteReport, Integer> implements IPfdAdWebsiteReportService {

	@Override
	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdAdWebsiteReportDAO) dao).prepareReportData(reportDate);
	}

	@Override
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdWebsiteReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	@Override
	public void insertReportData(List<PfdAdWebsiteReport> dataList) throws Exception {
		((PfdAdWebsiteReportDAO) dao).insertReportData(dataList);
	}
	
	@Override
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfdAdWebsiteReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}

}
