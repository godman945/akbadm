package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdKeywordReport;

public interface IPfdKeywordReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfdKeywordReport> dataList) throws Exception;
}
