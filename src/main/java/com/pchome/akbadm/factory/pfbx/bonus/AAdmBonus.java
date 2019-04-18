package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.AdmBonusDetailReport;
import com.pchome.akbadm.db.pojo.AdmBonusSet;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusDetailReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusSetService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.enumerate.pfbx.bonus.EnumAdmBonusDetailItem;

public abstract class AAdmBonus {

	protected Logger log = LogManager.getRootLogger();
	
	protected IAdmBonusSetService admBonusSetService;
	protected IAdmRecognizeDetailService admRecognizeDetailService;
	protected IPfbxBonusDayReportService pfbxBonusDayReportService;
	protected IPfdBonusDayReportService pfdBonusDayReportService;
	protected IAdmBonusDetailReportService admBonusDetailReportService;
	protected IPfpRefundOrderService pfpRefundOrderService;
	
	public void setAdmBonusDetailReportService(IAdmBonusDetailReportService admBonusDetailReportService) {
		this.admBonusDetailReportService = admBonusDetailReportService;
	}

	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}

	private AdmBonusSet getAdmBonusSet(Date startDate){
		return admBonusSetService.findLastAdmBonusSet(startDate);
	}
	
	protected abstract EnumAdmBonusDetailItem getBonusItem(); 
	
	protected abstract float findSaveBonus(Date transDate, AdmBonusSet admBonusSet);
	
	protected abstract float findFreeBonus(Date transDate, AdmBonusSet admBonusSet);
	
	protected abstract float findPostpaidBonus(Date transDate, AdmBonusSet admBonusSet);
	
	public void updateAdmBonusDetailReport(Date reportDate){
		
		Date today = new Date();
		
		EnumAdmBonusDetailItem admBonusDetailItem = this.getBonusItem();
		log.info(" admBonusDetailItem: "+admBonusDetailItem.getItemName());	
		AdmBonusSet admBonusSet = this.getAdmBonusSet(reportDate);
		float saveBonus = this.findSaveBonus(reportDate, admBonusSet);
		log.info(" saveBonus: "+saveBonus);
		float freeBonus = this.findFreeBonus(reportDate, admBonusSet);
		log.info(" freeBonus: "+freeBonus);
		float postpaidBonus = this.findPostpaidBonus(reportDate, admBonusSet);
		log.info(" postpaidBonus: "+postpaidBonus);
		float totalBonus = saveBonus + freeBonus + postpaidBonus;
		log.info(" totalBonus: "+totalBonus);
		
		// 更新 adm_bonus_detail_report
		AdmBonusDetailReport admBonusDetailReport = new AdmBonusDetailReport();
		
		admBonusDetailReport.setReportDate(reportDate);
		admBonusDetailReport.setDetailItem(admBonusDetailItem.getItemId());
		admBonusDetailReport.setItemName(admBonusDetailItem.getItemName());
		admBonusDetailReport.setIncomeExpense(admBonusDetailItem.getIncomeExpense().getTag());
		admBonusDetailReport.setSave(saveBonus);
		admBonusDetailReport.setFree(freeBonus);
		admBonusDetailReport.setPostpaid(postpaidBonus);
		admBonusDetailReport.setTotal(totalBonus);
		admBonusDetailReport.setCreateDate(today);
		
		admBonusDetailReportService.saveOrUpdate(admBonusDetailReport);
		
		
		
	}
	
	public void setAdmBonusSetService(IAdmBonusSetService admBonusSetService) {
		this.admBonusSetService = admBonusSetService;
	}

	public void setAdmRecognizeDetailService(
			IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setPfbxBonusDayReportService(
			IPfbxBonusDayReportService pfbxBonusDayReportService) {
		this.pfbxBonusDayReportService = pfbxBonusDayReportService;
	}

	public void setPfdBonusDayReportService(
			IPfdBonusDayReportService pfdBonusDayReportService) {
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}
	
}
