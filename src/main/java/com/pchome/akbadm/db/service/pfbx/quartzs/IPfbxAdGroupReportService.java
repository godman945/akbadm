package com.pchome.akbadm.db.service.pfbx.quartzs;


import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdGroupReport;
import com.pchome.akbadm.db.pojo.PfbxAdStyleReport;

public interface IPfbxAdGroupReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfbxAdGroupReport> dataList) throws Exception;
	
}
