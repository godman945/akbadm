package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.PfdAdTemplateReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdTemplateReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;

public class PfdAdTemplateReportService extends BaseService<PfdAdTemplateReport, Integer> implements IPfdAdTemplateReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((PfdAdTemplateReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdTemplateReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdAdTemplateReport> dataList) throws Exception {
		((PfdAdTemplateReportDAO) dao).insertReportData(dataList);
	}

	public List<AdTemplateReportVO> findPfdAdTemplateReport(String startDate, String endDate,
			String pfdCustomerInfoId) throws Exception {
		return ((PfdAdTemplateReportDAO) dao).findPfdAdTemplateReport(startDate, endDate, pfdCustomerInfoId);
	}
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((PfdAdTemplateReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}
}
