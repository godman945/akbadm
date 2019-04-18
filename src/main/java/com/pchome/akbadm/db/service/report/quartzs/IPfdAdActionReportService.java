package com.pchome.akbadm.db.service.report.quartzs;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfdAdActionReport;
import com.pchome.rmi.bonus.BonusDetailVo;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public interface IPfdAdActionReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception;

	public void deleteReportDataByReportDate(String reportDate) throws Exception;

	public void insertReportData(List<PfdAdActionReport> dataList) throws Exception;
	
	public float findPfdAdActionClickCost(String pfdCustomerInfoId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType);
	
	public float findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String payType);
	
	public List<BonusDetailVo> findPfdAdActionReportToVo(String pfdId, String startDate, String endDate, String payType);
	
	public Map<String,String> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate) throws Exception ;
	
	public int updateConvertCountData(String convertDate,String convertRangeDate) throws Exception;
}
