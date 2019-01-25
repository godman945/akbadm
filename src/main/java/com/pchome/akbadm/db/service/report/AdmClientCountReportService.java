package com.pchome.akbadm.db.service.report;

import java.util.List;

import com.pchome.akbadm.db.dao.report.IAdmClientCountReportDAO;
import com.pchome.akbadm.db.pojo.AdmClientCountReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.report.AdmClientCountForNext30DayReportVO;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.db.vo.report.AdmCountReportVO;

public class AdmClientCountReportService extends BaseService<AdmClientCountReport, Integer> implements IAdmClientCountReportService {

	@Override
	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IAdmClientCountReportDAO) dao).prepareReportData(reportDate);
	}

	@Override
	public float findLossCost(String reportDate) {
		return ((IAdmClientCountReportDAO) dao).findLossCost(reportDate);
	}

	@Override
	public List<Object> findAdmBonusDetailReport(String reportDate) throws Exception {
		return ((IAdmClientCountReportDAO) dao).findAdmBonusDetailReport(reportDate);
	}

	@Override
	public int deleteReportDataByReportDate(String reportDate){
		return ((IAdmClientCountReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}
	
	@Override
	public List<AdmClientCountReportVO> findReportData(String firstDay, String lastDay){
		return ((IAdmClientCountReportDAO) dao).findReportData(firstDay, lastDay);
	}
	
	@Override
	public List<AdmClientCountForNext30DayReportVO> findReportDataFpr30Day(final String searchDay){
		return ((IAdmClientCountReportDAO) dao).findReportDataFpr30Day(searchDay);
	}
	
	@Override
	public List<AdmCountReportVO> findCountReportData(String firstDay, String lastDay){
		return ((IAdmClientCountReportDAO) dao).findCountReportData(firstDay, lastDay);
	}
	
}
