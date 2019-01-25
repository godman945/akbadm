package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfdAdTemplateReport;
import com.pchome.akbadm.db.vo.AdTemplateReportVO;

public interface IPfdAdTemplateReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfdAdTemplateReport> dataList) throws Exception;

	public List<AdTemplateReportVO> findPfdAdTemplateReport(String startDate, String endDate,
			String pfdCustomerInfoId) throws Exception;
}
