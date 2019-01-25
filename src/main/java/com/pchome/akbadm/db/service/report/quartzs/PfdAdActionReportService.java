package com.pchome.akbadm.db.service.report.quartzs;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.report.quartzs.IPfdAdActionReportDAO;
import com.pchome.akbadm.db.dao.report.quartzs.PfdAdActionReportDAO;
import com.pchome.akbadm.db.pojo.PfdAdActionReport;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.rmi.bonus.BonusDetailVo;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class PfdAdActionReportService extends BaseService<PfdAdActionReport, Integer> implements IPfdAdActionReportService {

	public List<Object> prepareReportData(String reportDate) throws Exception {
		return ((PfdAdActionReportDAO) dao).prepareReportData(reportDate);
	}
 
	public void deleteReportDataByReportDate(String reportDate) throws Exception {
		((PfdAdActionReportDAO) dao).deleteReportDataByReportDate(reportDate);
	}

	public void insertReportData(List<PfdAdActionReport> dataList) throws Exception {
		((PfdAdActionReportDAO) dao).insertReportData(dataList);
	}
	
	public float findPfdAdActionClickCost(String pfdCustomerInfoId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType) {
		
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
	}
	
	public float findPfpAdActionClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String payType) {
		
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
	}
	
	public List<BonusDetailVo> findPfdAdActionReportToVo(String pfdId, String startDate, String endDate, String payType) {
		
		List<BonusDetailVo> vos = null;
		
		List<Object> objects = ((IPfdAdActionReportDAO) dao).findPfdAdActionReportToVo(
														pfdId, 
														DateValueUtil.getInstance().stringToDate(startDate), 
														DateValueUtil.getInstance().stringToDate(endDate), 
														payType);
		
		for(Object object:objects){	
			
			Object[] ob = (Object[])object;
			
			if(ob[0] != null){
				
				if(vos == null){
					vos = new ArrayList<BonusDetailVo>();
				}
				
				BonusDetailVo vo = new BonusDetailVo();
				
				String pfpId = ob[0].toString();
				String pfpName = ob[1].toString();
				String getpayType=ob[2].toString();
				float adClkPrice = Float.valueOf(ob[3].toString());
				
				//log.info("\n ob0: "+pfpId+" \n ob1: "+pfpName+" \n ob2: "+adClkPrice);
				
				vo.setPfpId(pfpId);
				vo.setPfpName(pfpName);
				vo.setPayType(getpayType);
				vo.setAdClkPrice(adClkPrice);
				
				vos.add(vo);
			}				
		}

		
		
		
		
		return vos;
	}
	
	public Map<String,String> findPfpAdClickByPfd(String pfdCustomerInfoId,Date startDate, Date endDate) throws Exception {
		List<PfdAdActionReport> adClkPriceList = ((IPfdAdActionReportDAO) dao).findPfpAdClickByPfd(pfdCustomerInfoId,startDate, endDate);
		Map<String,String> oneWeekAdCost = new LinkedHashMap<String,String>();
		
		for (Object object : adClkPriceList) {
			Object[] ob = (Object[]) object;
			String pfpCustomerInfoId = ob[0].toString();
			String adClkPrice = ob[1].toString();
			oneWeekAdCost.put(pfpCustomerInfoId, adClkPrice);
		}
		
		return oneWeekAdCost;
	}

}
