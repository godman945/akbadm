package com.pchome.akbadm.db.service.pfbx.quartzs;

import java.util.List;

import com.pchome.akbadm.db.dao.pfbx.report.quartz.IPfbAdDeviceReportDAO;
import com.pchome.akbadm.db.pojo.PfbxAdDeviceReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxAdDeviceReportService extends BaseService<PfbxAdDeviceReport, Integer> implements IPfbxAdDeviceReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((IPfbAdDeviceReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((IPfbAdDeviceReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfbxAdDeviceReport> dataList) throws Exception {
		((IPfbAdDeviceReportDAO) dao).insertReportData(dataList);
	}
	
	/*public float findPfdAdActionClickCost(String pfdCustomerInfoId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType) {
		
		float adClickCost = 0;
		
		List<Object> list = ((IPfdAdActionReportDAO) dao).findPfdAdActionClickCost(
																pfdCustomerInfoId, 
																DateValueUtil.getInstance().stringToDate(startDate), 
																DateValueUtil.getInstance().stringToDate(endDate),
																enumPfdAccountPayType.getPayType());
		for(Object object:list){	
			
			if(object != null){
				
				adClickCost = Float.parseFloat(object.toString());
			}				
		}
		
		return adClickCost;
	}*/
	
	/*public float findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String payType) {
		
		float adClickCost = 0;
		
		List<Object> list = ((IPfdAdActionReportDAO) dao).findPfpAdActionClickCost(
																pfdCustomerInfoId, 
																pfpCustomerInfoId,
																DateValueUtil.getInstance().stringToDate(startDate), 
																DateValueUtil.getInstance().stringToDate(endDate),
																payType);
		for(Object object:list){	
			
			if(object != null){
				
				adClickCost = Float.parseFloat(object.toString());
			}				
		}
		
		return adClickCost;
	}*/

}
