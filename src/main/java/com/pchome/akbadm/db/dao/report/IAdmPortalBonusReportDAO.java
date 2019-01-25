package com.pchome.akbadm.db.dao.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmPortalBonusReport;
import com.pchome.akbadm.db.vo.report.AdmPortalBonusReportVO;

public interface IAdmPortalBonusReportDAO extends IBaseDAO<AdmPortalBonusReport, Integer> {

	public Map<String,Object> getPfdBonusDayReport(final String reportDate) throws Exception;
	
	public Map<String,Integer> getPfpClientCountMap(final String reportDate) throws Exception;
	
	public float findPortalPfbBonus(final String reportDate);
	
	public float findPortalOperatingIncome(final String reportDate);
	
	public int deleteReportDataByReportDate(String reportDate);
	
	public List<AdmPortalBonusReportVO> findReportData(final String firstDay, final String lastDay);
	
}
