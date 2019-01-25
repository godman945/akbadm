package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdTimeReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdTimeReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfdAdTimeReportService extends BaseService<PfdAdTimeReport, Integer> implements IPfdAdTimeReportService {

	@Override
	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((PfdAdTimeReportDAO) dao).prepareReportData(reportDate);
	}

	@Override
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdTimeReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	@Override
	public void insertReportData(List<PfdAdTimeReport> dataList) throws Exception {
		((PfdAdTimeReportDAO) dao).insertReportData(dataList);
	}

}
