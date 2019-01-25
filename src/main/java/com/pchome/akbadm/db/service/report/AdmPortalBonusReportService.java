package com.pchome.akbadm.db.service.report;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.IAdmPortalBonusReportDAO;
import com.pchome.akbadm.db.pojo.AdmPortalBonusReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.AdmPortalBonusReportVO;

public class AdmPortalBonusReportService extends BaseService<AdmPortalBonusReport, Integer> implements IAdmPortalBonusReportService {

	@Override
	public Map<String, Object> getPfdBonusDayReport(String reportDate) throws Exception {
		return ((IAdmPortalBonusReportDAO) dao).getPfdBonusDayReport(reportDate);
	}

	@Override
	public Map<String, Integer> getPfpClientCountMap(String reportDate) throws Exception {
		return ((IAdmPortalBonusReportDAO) dao).getPfpClientCountMap(reportDate);
	}

	@Override
	public float findPortalPfbBonus(String reportDate) {
		return ((IAdmPortalBonusReportDAO) dao).findPortalPfbBonus(reportDate);
	}

	@Override
	public float findPortalOperatingIncome(String reportDate) {
		return ((IAdmPortalBonusReportDAO) dao).findPortalOperatingIncome(reportDate);
	}

	@Override
	public int deleteReportDataByReportDate(String reportDate) {
		return ((IAdmPortalBonusReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	@Override
	public List<AdmPortalBonusReportVO> findReportData(String firstDay,String lastDay) {
		// TODO Auto-generated method stub
		return ((IAdmPortalBonusReportDAO) dao).findReportData(firstDay, lastDay);
	}

}
