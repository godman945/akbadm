package com.pchome.akbadm.db.service.pfbx.bonus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.pfbx.bonus.IPfbxBonusDayReportDAO;
import com.pchome.akbadm.db.pojo.PfbxBonusDayReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfbxBonusDayReportService extends BaseService<PfbxBonusDayReport, Integer> implements IPfbxBonusDayReportService{

	public float findPfbxSaveIncome(Date reportDate) {
		float income = 0;		
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			income = map.get("saveIncome");
		}				
		return income;
	}
	
	public float findPfbxFreeIncome(Date reportDate) {
		float income = 0;
		
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			income = map.get("freeIncome");
		}
		return income;
	}
	
	public float findPfbxPostpaidIncome(Date reportDate) {
		float income = 0;
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			income = map.get("pfbxPostpaidIncome");
		}
		return income;		
	}
	
	public float findPfbxSaveExpense(Date reportDate) {
		float expense = 0;
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			expense = map.get("saveExpense");
		}
		return expense;
	}
	
	public float findPfbxFreeExpense(Date reportDate) {
		float expense = 0;
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			expense = map.get("freeExpense");
		}
		return expense;
	}
	
	public float findPfbxPostpaidExpense(Date reportDate) {
		float expense = 0;
		Map<String,Float> map = this.getIncomeAndExpense(reportDate);
		
		if(!map.isEmpty()){
			expense = map.get("pfbxPostpaidExpense");
		}
		return expense;
	}
	
	public float findTotalPfbxExpense(Date startDate, Date endDate) {
		
		float totalPfbxExpense = 0;
		List<Object> objects = ((IPfbxBonusDayReportDAO)dao).findPfbxBonusDayReport(startDate, endDate);
		
		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[6] != null){
				totalPfbxExpense = Float.valueOf(ob[6].toString());
			}
		}
		
		return totalPfbxExpense;
	}
	
	public Integer deletePfbxBonusDayReport(Date deleteDate){
		return ((IPfbxBonusDayReportDAO)dao).deletePfbxBonusDayReport(deleteDate);
	}
	
	private Map<String,Float> getIncomeAndExpense(Date reportDate){
		
		Map<String,Float> map = new HashMap<String,Float>();
		
		List<Object> objects = ((IPfbxBonusDayReportDAO)dao).findPfbxBonusDayReport(reportDate, reportDate);
		
		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[0] != null){
				map.put("saveIncome", Float.valueOf(ob[0].toString()));
				map.put("freeIncome", Float.valueOf(ob[1].toString()));
				map.put("pfbxPostpaidIncome", Float.valueOf(ob[2].toString()));
				map.put("saveExpense", Float.valueOf(ob[3].toString()));
				map.put("freeExpense", Float.valueOf(ob[4].toString()));
				map.put("pfbxPostpaidExpense", Float.valueOf(ob[5].toString()));
			}
		}
		
		log.info(" map: "+map);
		
		return map;
	}

	@Override
	public float findPfbxTotalMonthBonus(String pfbId, Date startDate, Date endDate) {
		float totalPfbxExpense = 0;
		List<Object> objects = ((IPfbxBonusDayReportDAO)dao).findPfbxIdBonusDayReport(pfbId,startDate, endDate);
		
		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[6] != null){
				totalPfbxExpense = Float.valueOf(ob[6].toString());
			}
		}
		
		return totalPfbxExpense;
	}
	
	@Override
	public float findPfbxTotalClkPrice(String pfbId, Date startDate, Date endDate) {
		float totalPfbxExpense = 0;
		List<Object> objects = ((IPfbxBonusDayReportDAO)dao).findPfbxIdBonusDayReport(pfbId,startDate, endDate);
		
		for(Object object:objects){

			Object[] ob = (Object[])object;

			if(ob[7] != null){
				totalPfbxExpense = Float.valueOf(ob[7].toString());
			}
		}
		
		return totalPfbxExpense;
	}
}
