package com.pchome.akbadm.db.dao.report.quartzs;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdReport;

public interface IPfpAdReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public Integer deleteReportDataByReportDate(Date reportDate) throws Exception;

	public void insertReportData(List<PfpAdReport> dataList) throws Exception;

	public List<Object> getLastDate() throws Exception;
}
