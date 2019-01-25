package com.pchome.akbadm.db.dao.pfbx.report.quartz;


import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdTimeReport;

public interface IPfbAdTimeReportDAO {

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfbxAdTimeReport> dataList) throws Exception;
}
