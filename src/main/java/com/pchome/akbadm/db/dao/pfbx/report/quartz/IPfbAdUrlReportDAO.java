package com.pchome.akbadm.db.dao.pfbx.report.quartz;


import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;


public interface IPfbAdUrlReportDAO {
	
	public List<String> getErrorUrlByPfbId2(String pfbId , String startDate , String endDate , String searchUrl , String domain) throws Exception;
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId(String pfbId , String startDate , String endDate , String searchUrl) throws Exception;
	
	public List<String> getListByPfbId2(String pfbcId , String domain) throws Exception;
	
	public List<PfbxAdUrlReport> getListByPfbId(String pfbId) throws Exception;

	public List<Object> prepareReportData(final String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfbxAdUrlReport> dataList) throws Exception;
}
