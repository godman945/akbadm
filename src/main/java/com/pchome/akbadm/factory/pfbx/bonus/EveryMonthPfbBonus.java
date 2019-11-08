package com.pchome.akbadm.factory.pfbx.bonus;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusTrans;



public class EveryMonthPfbBonus {

	protected Logger log = LogManager.getRootLogger();
	
	
	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private BonusTransDetailProcess bonusTransDetailProcess;
	
	
	
	public void bonusConutProcess(String monthValue){
		
		
		
		//每個月 26 號要處理的事
		log.info("PFB Month Bonus quartz Process....");
		
		//開始處理每一家 PFB 的分潤請款
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		//撈取fbx_ad_time_report中年度月份[monthValue]所有pfb user
		List<PfbxCustomerInfo> pfbxs = pfbxCustomerInfoService.getPfbxCustomerInfoListByReport(String.valueOf(cal.get(Calendar.YEAR))+"-"+monthValue);
		for(PfbxCustomerInfo pfb:pfbxs){						
			
			//每月 1 更新
			//更新 pfbx_bonus_trans_detail 分潤紀錄表	
			
			//寫入分潤金額(上月) 會更新 pfbx_bonus_bill bill_remain 
			bonusTransDetailProcess.insertItemTransDetail(pfb, EnumPfbxBonusTrans.ADD_EXPENSE,monthValue,null);
			
			//寫入無效流量(上月) 會更新 pfbx_bonus_bill bill_remain 
			bonusTransDetailProcess.insertItemTransDetail(pfb, EnumPfbxBonusTrans.INVALID_TRAFFIC,monthValue,null);
			
			//寫入開始餘額(本月)
			bonusTransDetailProcess.insertItemTransDetail(pfb, EnumPfbxBonusTrans.BONUS_START,monthValue,null);

			//檢查請款門檻-新增請款單(本月)
			//PM 說不要自動建，改讓 user 自己申請，先註解掉 20150624 by nico
			//bonusTransDetailProcess.insertItemTransDetail(pfb, EnumPfbxBonusTrans.APPLY_PROCESS,monthValue);

			//檢查請款門檻
			bonusTransDetailProcess.buildBonusBoard(pfb);
			
		}
		
	}
	

	





public void setPfbxCustomerInfoService(
		IPfbxCustomerInfoService pfbxCustomerInfoService) {
	this.pfbxCustomerInfoService = pfbxCustomerInfoService;
}



public void setBonusTransDetailProcess(
		BonusTransDetailProcess bonusTransDetailProcess) {
	this.bonusTransDetailProcess = bonusTransDetailProcess;
}

	
	
	
}
