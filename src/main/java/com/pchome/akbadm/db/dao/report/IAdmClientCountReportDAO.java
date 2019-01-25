package com.pchome.akbadm.db.dao.report;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmClientCountReport;
import com.pchome.akbadm.db.vo.report.AdmClientCountForNext30DayReportVO;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.db.vo.report.AdmCountReportVO;

public interface IAdmClientCountReportDAO extends IBaseDAO<AdmClientCountReport, Integer> {

	public List<Object> prepareReportData(final String reportDate) throws Exception;
	
	public float findLossCost(final String reportDate);
	
	public List<Object> findAdmBonusDetailReport(final String reportDate) throws Exception;
	
	public int deleteReportDataByReportDate(String reportDate);

	public List<AdmClientCountReportVO> findReportData(String firstDay, String lastDay);
	
	public List<AdmClientCountForNext30DayReportVO> findReportDataFpr30Day(final String searchDay);
	
	public List<AdmCountReportVO> findCountReportData(final String firstDay, final String lastDay);
}
