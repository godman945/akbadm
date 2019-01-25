package com.pchome.akbadm.db.service.pfbx.quartzs;


import java.util.List;

import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.vo.pfbx.adurl.PfbAdUrlListVO;

public interface IPfbxAdUrlReportService {
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId_V20170307(PfbxCustomerInfo pfbInfo , String startDate , String endDate , String searchUrl , String domain) throws Exception;
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId2(String pfbId , String startDate , String endDate , String searchUrl , String domain) throws Exception;
	
	public List<PfbAdUrlListVO> getErrorUrlByPfbId(String pfbId , String startDate , String endDate , String searchUrl) throws Exception;
	
	public List<PfbxAdUrlReport> getListByPfbId(String pfbId) throws Exception;

	public List<Object> prepareReportData(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfbxAdUrlReport> dataList) throws Exception;
	
}
