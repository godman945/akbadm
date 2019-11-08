package com.pchome.akbadm.factory.pfbx.bonus;


import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.bonus.EnumPfbxBonusTrans;



public class EveryMonthPfbBonus {

	protected Logger log = LogManager.getRootLogger();
	
	
	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private BonusTransDetailProcess bonusTransDetailProcess;
	
	
	
	public void bonusConutProcess(String monthValue){
		
		
		
		//每個月 26 號要處理的事
		log.info("PFB Month Bonus quartz Process....");
		
		//開始處理每一家 PFB 的分潤請款
		List<PfbxCustomerInfo> pfbxs = pfbxCustomerInfoService.findValidPfbxCustomerInfo();
		
		for(PfbxCustomerInfo pfb:pfbxs){						
			if(!pfb.getCustomerInfoId().equals("PFBC20180110003")) {
				continue;
			}
			
			log.info(">>>>>>>>>>>DEBUG PFBC20180110003 START");
			
			
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
