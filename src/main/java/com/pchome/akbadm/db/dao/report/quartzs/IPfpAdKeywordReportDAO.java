package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdKeywordReport;

public interface IPfpAdKeywordReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfpAdKeywordReport> dataList) throws Exception;

	public List<Object> getLastDate() throws Exception;
}
