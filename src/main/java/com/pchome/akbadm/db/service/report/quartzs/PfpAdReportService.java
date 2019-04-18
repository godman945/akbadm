package com.pchome.akbadm.db.service.report.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.report.quartzs.IPfpAdReportDAO;
import com.pchome.akbadm.db.dao.report.quartzs.PfpAdGroupReportDAO;
import com.pchome.akbadm.db.pojo.PfpAdReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.soft.util.DateValueUtil;

public class PfpAdReportService extends BaseService<PfpAdReport, Integer> implements IPfpAdReportService {

	public List<Object> prepareReportData(final String reportDate) throws Exception {
		return ((IPfpAdReportDAO) dao).prepareReportData(reportDate);
	}
 
	public Integer deleteReportDataByReportDate(String reportDate) throws Exception {
		return ((IPfpAdReportDAO) dao).deleteReportDataByReportDate(DateValueUtil.getInstance().stringToDate(reportDate));
	}

	public void insertReportData(List<PfpAdReport> dataList) throws Exception {
		((IPfpAdReportDAO) dao).insertReportData(dataList);
	}

	public List<Object> getLastDate() throws Exception {
		return ((IPfpAdReportDAO) dao).getLastDate();
	}

	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception {
		return ((IPfpAdReportDAO) dao).updateConvertCountData(convertDate,convertRangeDate);
	}	
}
