package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.AdmBonusBillReport;
import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.akbadm.db.pojo.PfbxBonusDayReport;
import com.pchome.akbadm.db.pojo.PfbxBonusSet;
import com.pchome.akbadm.db.pojo.PfbxBonusSetSpecial;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.bonus.AdmBonusBillReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusBillReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusDetailReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusSetService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusDayReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusSetSpecialService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;
import com.pchome.enumerate.pfbx.bonus.EnumIncomeExpense;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class EveryDayPfbBonus {

	protected Logger log = LogManager.getRootLogger();
	
	private IAdmBonusSetService admBonusSetService;
	private IAdmRecognizeDetailService admRecognizeDetailService;
	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private IPfpAdPvclkService pfpAdPvclkService;
	private IPfbxBonusSetService pfbxBonusSetService;
	private IPfbxBonusSetSpecialService pfbxBonusSetSpecialService;
	private IPfbxBonusDayReportService pfbxBonusDayReportService;
	private AdmBonusFactory admBonusFactory;
	private IAdmBonusBillReportService admBonusBillReportService;
	private IAdmBonusDetailReportService admBonusDetailReportService;
	
	private IPfdBonusDayReportService pfdBonusDayReportService;
	private IAdmAccesslogService admAccesslogService;
	
	private IPfpRefundOrderService pfpRefundOrderService;
	
	public static String countDate;
	
	//當日每小時預估分潤金額,只是預估讓前台的報表當日每小更新時有數據
	public void bonusEstimatedProcess(String today){
		Date countDate=DateValueUtil.getInstance().stringToDate(today);
		// 系統分潤比例
		AdmBonusSet admBonusSet =  admBonusSetService.findLastAdmBonusSet(countDate);
				
		if(admBonusSet != null){
			//pchome 的營運獎金%數
			float pfpPercent = admBonusSet.getPfpPercent();
			//pfb 的獎金%數
			float pfbPercent = admBonusSet.getPfbPercent();
			if(pfpPercent > 0 && pfbPercent > 0 && ((pfpPercent + pfbPercent) == 100) ){
				//Pfb總廣告費當日版位點擊金額 
				//篩選 pfbxCustomerInfoId <> '' 的資料 ,有貼 code 的才算(這裡先拿掉了，理論上不應該有沒有被版位管理的播放)
			    //撈 pfp_ad_pvclk table
				float totalPfbClkPrice = pfpAdPvclkService.totalPfbAdPvclk(null, countDate, countDate);
				
				log.info(" totalPfbClkPrice: "+totalPfbClkPrice);
						if(totalPfbClkPrice > 0){
							//撈出所有 PFB LIST
							List<PfbxCustomerInfo> pfbxs = pfbxCustomerInfoService.findValidPfbxCustomerInfo();
							if(!pfbxs.isEmpty()){
								//可分潤總金額減掉40%(預估的)
								float totalBonusMoney=totalPfbClkPrice-(float)(totalPfbClkPrice*0.4);				
								
								for(PfbxCustomerInfo pfb:pfbxs){		
									//取出每一家的分潤 % 數,送入的日期會取出同一家PFB多筆%數的最後一筆
									log.info(" pfbId: "+pfb.getCustomerInfoId());
									PfbxBonusSet pfbxBonusSet = pfbxBonusSetService.findPfbxBonusSet(pfb.getCustomerInfoId(), countDate);

									if(pfbxBonusSet == null){
										log.info(" pfbId bonus set is null "+pfb.getCustomerInfoId());
										continue;
									}
									
									//某一家 PFB 的當日花費,由 php_pvclk 取出(點擊費用減超撥費用)
									float pfbClkPrice = pfpAdPvclkService.totalPfbAdPvclk(pfb.getCustomerInfoId(), countDate, countDate);
									
									
									float pfbBonus=pfbClkPrice*totalBonusMoney/totalPfbClkPrice;
									
									float totalBonus=pfbBonus*(pfbxBonusSet.getPfbPercent()/100);
									float totalCharge=pfbBonus*(pfbxBonusSet.getPchomeChargePercent()/100);
									
									if(pfbClkPrice <= 0 ){
										log.info(" pfbClkPrice is 0 ");
										continue;
									}
									
									
									
									
								
									PfbxBonusDayReport dayReport = new PfbxBonusDayReport();
									
									dayReport.setReportDate(countDate);
									dayReport.setPfbxCustomerInfo(pfb);
									dayReport.setPfbClkPrice(pfbClkPrice);
									dayReport.setPfbBonusPercent(pfbxBonusSet.getPfbPercent());
									dayReport.setPchomeChargePercent(pfbxBonusSet.getPchomeChargePercent());
									
									dayReport.setSaveClkPrice(0f);
									dayReport.setFreeClkPrice(0f);
									dayReport.setPostpaidClkPrice(0f);
									
									dayReport.setSaveBonus(0f);
									dayReport.setFreeBonus(0f);
									dayReport.setPostpaidBonus(0f);
									
									dayReport.setSaveCharge(0f);
									dayReport.setFreeCharge(0f);
									dayReport.setPostpaidCharge(0f);
									
									dayReport.setTotalClkPrice(pfbBonus);
									dayReport.setTotalCharge(totalCharge);
									dayReport.setTotalBonus(totalBonus);
									dayReport.setCreateDate(countDate);
									
									pfbxBonusDayReportService.saveOrUpdate(dayReport);
									
								}
							}
								
								
							
						}
						
					}
					else{
						log.info(" percent is error!! ");
						log.info(" pfpPercent: "+pfpPercent);
						log.info(" pfbPercent: "+pfbPercent);
					}
				}
				else{
					log.info(" adm_bonus_set is not data!!");
				}	
		
		
		
		
		
		
	}
	
	//前日分潤計算
	public void bonusConutProcess(Date countDate){
		
		// 系統分潤比例
		AdmBonusSet admBonusSet =  admBonusSetService.findLastAdmBonusSet(countDate);
		
		String logMsg="";
		
		if(admBonusSet != null){
			
			//pchome 的營運獎金%數
			float pfpPercent = admBonusSet.getPfpPercent();
			//pfb 的獎金%數
			float pfbPercent = admBonusSet.getPfbPercent();
			
			logMsg="營運費用%數="+pfpPercent;
			
			admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
				    null, null, null, 
				    null, null, EnumAccesslogEmailStatus.NO);
			
			if(pfpPercent > 0 && pfbPercent > 0 && ((pfpPercent + pfbPercent) == 100) ){
								
				//Pfb總廣告費當日版位點擊金額 
				//篩選 pfbxCustomerInfoId <> '' 的資料 ,有貼 code 的才算(這裡先拿掉了，理論上不應該有沒有被版位管理的播放)
				//撈 pfp_ad_pvclk table
				float totalPfbClkPrice = pfpAdPvclkService.totalPfbAdPvclk(null, countDate, countDate);
				totalPfbClkPrice = (float)Math.floor(totalPfbClkPrice);
				log.info("totalPfbClkPrice 原本: "+totalPfbClkPrice);
				log.info(" totalPfbClkPrice: "+totalPfbClkPrice);
				if(totalPfbClkPrice > 0){
					// Pfp總儲值金廣告費(刷卡，ATM進來的錢)
					float totalSaveClkPrice = admRecognizeDetailService.findPfbAdPvClkPrice(null, EnumOrderType.SAVE.getTypeTag(), countDate, countDate);
					log.info(" totalSaveClkPrice: "+totalSaveClkPrice);
					// Pfp總後付廣告費(經銷商預借的錢)要減掉預付退款費用
					float totalAdvanceRefundPrice = pfpRefundOrderService.findTotalRefundPrice(countDate, countDate, EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
					totalSaveClkPrice = totalSaveClkPrice - totalAdvanceRefundPrice;
					log.info(" totalSaveClkPrice-totalAdvanceRefundPrice: "+totalSaveClkPrice);
					
					// Pfp總禮金廣告費(送的錢)
					float totalFreeClkPrice = admRecognizeDetailService.findPfbAdPvClkPriceOrderTypeForFree(null, countDate, countDate);
					log.info(" totalFreeClkPrice: "+totalFreeClkPrice);
					// Pfp總後付廣告費(經銷商預借的錢)
					float totalPostpaidClkPrice = admRecognizeDetailService.findPfbAdPvClkPrice(null, EnumOrderType.VIRTUAL.getTypeTag(), countDate, countDate); 
					log.info(" totalPostpaidClkPrice: "+totalPostpaidClkPrice);
					// Pfp總後付廣告費(經銷商預借的錢)要減掉後付退款費用
					float totalLaterRefundPrice = pfpRefundOrderService.findTotalRefundPrice(countDate, countDate, EnumPfdAccountPayType.LATER.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
					totalPostpaidClkPrice = totalPostpaidClkPrice - totalLaterRefundPrice;
					//adm_recognize_detail 儲值扣款記錄有值才行，沒有資料一定是排程沒跑
					if(totalSaveClkPrice > 0 || totalFreeClkPrice > 0 || totalPostpaidClkPrice > 0){
						//20150908 修正公式
						//pfb 分的錢=總花費-營運費-pfd 的錢(剩下的才拿來分)
						//計算營運費用
						// pchome總儲值金營運費用
						float pchomeTotalSaveBonus = totalSaveClkPrice * (pfpPercent/100);
						log.info(" pchomeTotalSaveBonus: "+pchomeTotalSaveBonus);				
						// pchome總禮金營運費用
						float pchomeTotalFreeBonus = totalFreeClkPrice * (pfpPercent/100);
						log.info(" pchomeTotalFreeBonus: "+pchomeTotalFreeBonus);				
						// pchome總後付分營運費用
						float pchomeTotalPostpaidBonus = totalPostpaidClkPrice * (pfpPercent/100);
						log.info(" pchomeTotalPostpaidBonus: "+pchomeTotalPostpaidBonus);
						
						//計算 pfd  的費用
						
						// 更新 pfd_bonus_day_report(計算 pfd 的每日月佣金)
						//this.updatePfdBonusDayReport(countDate);
						
						
						
						// pfd總儲值金營運費用
						// 上面記 pfd table 時，順便把 save paid 佣金的金額紀錄起來了
						
						
						//20150909 經銷商規格未好先不計算，先寫 0
						//原經銷商要加的 25% 先放進營運費用
						//營運費 10% 經銷商 25% 聯播網 65%
						float pfdTotalSaveBonus = pfdBonusDayReportService.findPfdSaveMonthBonus(countDate);
						//營虧明細那裡的經銷商也先寫入 0
						//float pfdTotalSaveBonus = 0;
						log.info(" pfdTotalSaveBonus: "+pfdTotalSaveBonus);				
						
						// pfd總禮金營運費用
						// pfd 禮金不計算
						
						// pfd總後付分營運費用
						float pfdTotalPostpaidBonus = pfdBonusDayReportService.findPfdPaidMonthBonus(countDate);
						//float pfdTotalPostpaidBonus = 0;
						log.info(" pfdTotalPostpaidBonus: "+pfdTotalPostpaidBonus);
						
						
						// Pfb總儲值金分潤獎金
						float pfbTotalSaveBonus = totalSaveClkPrice - pchomeTotalSaveBonus-pfdTotalSaveBonus;
						log.info(" pfbTotalSaveBonus: "+pfbTotalSaveBonus);				
						// Pfb總禮金分潤獎金
						float pfbTotalFreeBonus = totalFreeClkPrice - pchomeTotalFreeBonus;
						log.info(" pfbTotalFreeBonus: "+pfbTotalFreeBonus);				
						// Pfb總後付分潤獎金
						float pfbTotalPostpaidBonus = totalPostpaidClkPrice - pchomeTotalPostpaidBonus - pfdTotalPostpaidBonus;
						log.info(" pfbTotalPostpaidBonus: "+pfbTotalPostpaidBonus);
						
						// 更新  pfbx_bonus_day_report
						this.updatePfbxBonusDayReport(countDate, countDate, totalPfbClkPrice, totalSaveClkPrice, totalFreeClkPrice, totalPostpaidClkPrice, 
								pfbTotalSaveBonus, pfbTotalFreeBonus, pfbTotalPostpaidBonus);
						
						
						// 更新 adm_bonus_detail_report PFD PFP PFD 支出 收入明細
						this.updateAdmBonusDetailReport(countDate);
						
						// 更新 adm_bonus_bill_report 系統每日盈虧表
						this.updateAdmBonusBillReport(pfpPercent,countDate);
					
					}else{
						
						log.info("adm_recognize_detail empty no data please check recognize quartz");
					    logMsg="pfb 分潤計算,每日花費為0,檢查recognize quartz";
						admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.ERROR, logMsg, 
							    null, null, null, 
							    null, null, EnumAccesslogEmailStatus.YES);
						
					}
				}
				
			}
			else{
				log.info(" percent is error!! ");
				log.info(" pfpPercent: "+pfpPercent);
				log.info(" pfbPercent: "+pfbPercent);
			}
		}
		else{
			log.info(" adm_bonus_set is not data!!");
		}	
	}
	
	
		
	private void updatePfbxBonusDayReport(Date startDate, Date endDate, float totalPfbClkPrice, float totalSaveClkPrice, float totalFreeClkPrice, float totalPostpaidClkPrice,
			float pfbTotalSaveBonus, float pfbTotalFreeBonus, float pfbTotalPostpaidBonus){
		String logMsg="";
		//撈出所有 PFB LIST
		List<PfbxCustomerInfo> pfbxs = pfbxCustomerInfoService.findValidPfbxCustomerInfo();
		
		if(!pfbxs.isEmpty()){
			
			Date today = new Date();
			
			for(PfbxCustomerInfo pfb:pfbxs){
				
				//取出每一家的分潤 % 數,送入的日期會取出同一家PFB多筆%數的最後一筆
				log.info(" pfbId: "+pfb.getCustomerInfoId());
				
				log.info(" startDate: "+startDate);
				
				//看看有沒有優惠%數
				
				float pcPercent=0.0f;
				float pfbPercent=0.0f;
				
				PfbxBonusSetSpecial pfbxBonusSetSpecial = pfbxBonusSetSpecialService.findPfbxBonusSet(pfb.getCustomerInfoId(), startDate);
				
				if(pfbxBonusSetSpecial!=null){
					
					pcPercent=pfbxBonusSetSpecial.getPchomeChargePercent();
					pfbPercent=pfbxBonusSetSpecial.getPfbPercent();
					
					log.info(" Special pcPercent="+pcPercent);
					log.info(" Special pfbPercent="+pfbPercent);
					
					logMsg="PFB分潤 %數(使用優惠%數)，pcPercent="+pcPercent+",pfbPercent="+pfbPercent;
					
					admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
						    null, null, pfb.getCustomerInfoId(), 
						    null, null, EnumAccesslogEmailStatus.NO);
					
				
				}else{
					PfbxBonusSet pfbxBonusSet = null;
					pfbxBonusSet=pfbxBonusSetService.findPfbxBonusSet(pfb.getCustomerInfoId(), startDate);
					
					if(pfbxBonusSet==null){
						pcPercent=100.0f;
						pfbPercent=0.0f;;
					}else{
						pcPercent=pfbxBonusSet.getPchomeChargePercent();
						pfbPercent=pfbxBonusSet.getPfbPercent();
					}
					log.info("  pcPercent="+pcPercent);
					log.info("  pfbPercent="+pfbPercent);
					
                    logMsg="PFB分潤 %數(正常)，pcPercent="+pcPercent+",pfbPercent="+pfbPercent;
					
					admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.ACCOUNT_MODIFY, logMsg, 
						    null, null, pfb.getCustomerInfoId(), 
						    null, null, EnumAccesslogEmailStatus.NO);
					
				}
				
				if(pfbPercent == 0.0f){
					log.info(" pfbId bonus set is null "+pfb.getCustomerInfoId());
					continue;
				}
				
				//某一家 PFB 的當日花費,由 php_pvclk 取出(點擊費用減超撥費用)
				float pfbClkPrice = pfpAdPvclkService.totalPfbAdPvclk(pfb.getCustomerInfoId(), startDate, endDate);
				
				if(pfbClkPrice <= 0 ){
					log.info(" pfbClkPrice is 0 ");
					continue;
				}
				
				// Pfb 播出廣告占總廣告比
				//float pfbClkPercent = pfbClkPrice / totalPfbClkPrice;	
				
				float saveClkPrice = (pfbClkPrice * pfbTotalSaveBonus) / totalPfbClkPrice;
				log.info(" saveClkPrice: "+saveClkPrice);
				
				float freeClkPrice = (pfbClkPrice * pfbTotalFreeBonus) / totalPfbClkPrice;
				log.info(" freeClkPrice: "+freeClkPrice);
				
				float postpaidClkPrice = (pfbClkPrice * pfbTotalPostpaidBonus) / totalPfbClkPrice;;
				log.info(" postpaidClkPrice: "+postpaidClkPrice);
				
				float saveBonus = saveClkPrice * (pfbPercent/100);
				log.info(" saveBonus: "+saveBonus);
				
				float freeBonus = freeClkPrice * (pfbPercent/100);
				log.info(" freeBonus: "+freeBonus);
				
				float postpaidBonus = postpaidClkPrice * (pfbPercent/100);
				log.info(" postpaidBonus: "+postpaidBonus);
				
				float saveCharge = saveClkPrice * (pcPercent/100);
				log.info(" saveCharge: "+saveCharge);
				
				float freeCharge = freeClkPrice * (pcPercent/100);
				log.info(" freeCharge: "+freeCharge);
				
				float postpaidCharge = postpaidClkPrice * (pcPercent/100);
				log.info(" postpaidCharge: "+postpaidCharge);
				
				float totalClkPrice = saveClkPrice + freeClkPrice + postpaidClkPrice;
				log.info(" totalClkPrice: "+totalClkPrice);
				
				float totalBonus = saveBonus + freeBonus + postpaidBonus;	
				log.info(" totalBonus: "+totalBonus);
				
				float totalCharge = saveCharge + freeCharge + postpaidCharge;
				log.info(" totalCharge: "+totalCharge);
				
				PfbxBonusDayReport dayReport = new PfbxBonusDayReport();
				
				dayReport.setReportDate(startDate);
				dayReport.setPfbxCustomerInfo(pfb);
				dayReport.setPfbClkPrice(pfbClkPrice);
				dayReport.setPfbBonusPercent(pfbPercent);
				dayReport.setPchomeChargePercent(pcPercent);
				
				dayReport.setSaveClkPrice(saveClkPrice);
				dayReport.setFreeClkPrice(freeClkPrice);
				dayReport.setPostpaidClkPrice(postpaidClkPrice);
				
				dayReport.setSaveBonus(saveBonus);
				dayReport.setFreeBonus(freeBonus);
				dayReport.setPostpaidBonus(postpaidBonus);
				
				dayReport.setSaveCharge(saveCharge);
				dayReport.setFreeCharge(freeCharge);
				dayReport.setPostpaidCharge(postpaidCharge);
				
				dayReport.setTotalClkPrice(totalClkPrice);
				dayReport.setTotalCharge(totalCharge);
				dayReport.setTotalBonus(totalBonus);
				dayReport.setCreateDate(today);
				
				pfbxBonusDayReportService.saveOrUpdate(dayReport);
				
					
				
			}
			
		}else{
			log.info(" pfbxs is empty!!");
		}
		
	}
	
	

	
	private void updateAdmBonusDetailReport(Date reportDate){
		
		for(EnumAdmBonusDetailItem item:EnumAdmBonusDetailItem.values()){
			AAdmBonus admBonus = admBonusFactory.getAdmBonus(item);
			log.info(" admBonusName: "+item.getClass());
			admBonus.updateAdmBonusDetailReport(reportDate);
		}
	}
	
	private void updateAdmBonusBillReport(float pfpPercent,Date reportDate) {
		
		Date today = new Date();
		
		// 補程式碼
		
		float income = admBonusDetailReportService.findIncomeOrExpense(reportDate, EnumIncomeExpense.INCOME);
		float expense = admBonusDetailReportService.findIncomeOrExpense(reportDate, EnumIncomeExpense.EXPENSE);;
		float sysClkPrice = admBonusDetailReportService.findTotalClkPrice(reportDate);
		sysClkPrice = (float)Math.floor(sysClkPrice);
		float total = income - expense;
		
		AdmBonusBillReport admBonusBillReport = new AdmBonusBillReport();
		
		admBonusBillReport.setReportDate(reportDate);
		admBonusBillReport.setPchomePercent(pfpPercent);
		admBonusBillReport.setIncome(income);
		admBonusBillReport.setExpense(expense);
		admBonusBillReport.setSysClkPrice(sysClkPrice);
		admBonusBillReport.setTotal(total);
		admBonusBillReport.setCreateDate(today);
		
		admBonusBillReportService.saveOrUpdate(admBonusBillReport);
	}
	
	public void setAdmBonusSetService(IAdmBonusSetService admBonusSetService) {
		this.admBonusSetService = admBonusSetService;
	}

	public void setAdmRecognizeDetailService(
			IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setPfbxCustomerInfoService(
			IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}

	public void setPfbxBonusSetService(IPfbxBonusSetService pfbxBonusSetService) {
		this.pfbxBonusSetService = pfbxBonusSetService;
	}

	public void setPfbxBonusDayReportService(
			IPfbxBonusDayReportService pfbxBonusDayReportService) {
		this.pfbxBonusDayReportService = pfbxBonusDayReportService;
	}

	public void setAdmBonusFactory(AdmBonusFactory admBonusFactory) {
		this.admBonusFactory = admBonusFactory;
	}

	public void setAdmBonusBillReportService(
			AdmBonusBillReportService admBonusBillReportService) {
		this.admBonusBillReportService = admBonusBillReportService;
	}

	public void setAdmBonusDetailReportService(
			IAdmBonusDetailReportService admBonusDetailReportService) {
		this.admBonusDetailReportService = admBonusDetailReportService;
	}



	public void setPfdBonusDayReportService(
			IPfdBonusDayReportService pfdBonusDayReportService) {
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
	}

	public void setPfbxBonusSetSpecialService(IPfbxBonusSetSpecialService pfbxBonusSetSpecialService) {
		this.pfbxBonusSetSpecialService = pfbxBonusSetSpecialService;
	}

	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}	
	

	
}
