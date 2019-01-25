package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdCustomerReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdCustomerReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdCustomerReportService extends BaseService<PfbxAdCustomerReport, Integer> implements IPfbxAdCustomerReportService {

	@Override
    public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdCustomerReportDAO) dao).prepareReportData(reportDate);
	}

	@Override
    public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdCustomerReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	@Override
    public void insertReportData(List<PfbxAdCustomerReport> dataList) throws Exception {
		((IPfbAdCustomerReportDAO) dao).insertReportData(dataList);
	}

	@Override
    public List<PfbxAdCustomerReport> selectOneByUpdateDate() throws Exception {
	    return ((IPfbAdCustomerReportDAO) dao).selectOneByUpdateDate();
	}
}
