package com.pchome.akbadm.factory.pfbx.bonus;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusBillReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IAdmBonusDetailReportService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.soft.util.DateValueUtil;

public class DeletePfbBonus {

	protected Logger log = LogManager.getRootLogger();
	
	private IAdmBonusBillReportService admBonusBillReportService;
	private IAdmBonusDetailReportService admBonusDetailReportService;
	private IPfbxBonusDayReportService pfbxBonusDayReportService;
	//private IPfdBonusDayReportService pfdBonusDayReportService;

	public void deleteProcess(String deleteDate){
		
		Date dDate = DateValueUtil.getInstance().stringToDate(deleteDate);
		
		// 刪除 adm_bonus_bill_report
		this.deleteAdmBonusBillReport(dDate);
		log.info(">>>> deleteAdmBonusBillReport ok");
		// 刪除 adm_bonus_detail_report
		this.deleteAdmBonusDetailReport(dDate);
		log.info(">>>> deleteAdmBonusDetailReport ok");
		// 刪除 pfbx_bonus_day_report
		this.deletePfbxBonusDayReport(dDate);
		log.info(">>>> deletePfbxBonusDayReport ok");
		// 刪除 pfd_bonus_day_report
		//this.deletePfdBonusDayReport(dDate);
		
				
	}
	
	private void deleteAdmBonusBillReport(Date deleteDate){
		admBonusBillReportService.deleteAdmBonusBillReport(deleteDate);
	}
	
	private void deleteAdmBonusDetailReport(Date deleteDate){
		admBonusDetailReportService.deleteAdmBonusDetailReport(deleteDate);
	}

	private void deletePfbxBonusDayReport(Date deleteDate){
		pfbxBonusDayReportService.deletePfbxBonusDayReport(deleteDate);
	}
	
	
	//private void deletePfdBonusDayReport(Date deleteDate){
		//pfdBonusDayReportService.deletePfdBonusDayReport(deleteDate);
	//}
	

	public void setAdmBonusBillReportService(
			IAdmBonusBillReportService admBonusBillReportService) {
		this.admBonusBillReportService = admBonusBillReportService;
	}

	public void setAdmBonusDetailReportService(
			IAdmBonusDetailReportService admBonusDetailReportService) {
		this.admBonusDetailReportService = admBonusDetailReportService;
	}

	public void setPfbxBonusDayReportService(
			IPfbxBonusDayReportService pfbxBonusDayReportService) {
		this.pfbxBonusDayReportService = pfbxBonusDayReportService;
	}

	
	//public void setPfdBonusDayReportService(
		//	IPfdBonusDayReportService pfdBonusDayReportService) {
		//this.pfdBonusDayReportService = pfdBonusDayReportService;
	//}

	

	

	
	
}
