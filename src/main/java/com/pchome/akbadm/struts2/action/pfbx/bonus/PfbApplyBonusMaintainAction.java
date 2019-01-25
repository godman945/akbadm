package com.pchome.akbadm.struts2.action.pfbx.bonus;

import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBankService;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxPersonalService;
import com.pchome.akbadm.db.vo.pfbx.bonus.PfbxApplyBonusCheckVo;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;

public class PfbApplyBonusMaintainAction extends BaseCookieAction{
	
	private IPfbxBankService pfbxBankService;
	private IPfbxPersonalService pfbxPersonalService;
	
	private EnumPfbxAccountCategory[] enumPfbxAccountCategory = null;		// pfb 帳戶類型 enum
	private EnumPfbApplyStatus[] enumPfbApplyBonus = null;					// pfb 申請單狀態 enum
	private EnumPfbApplyInvoiceStatus[] enumPfbApplyCheck = null;			// pfb 審核狀態 enum

	private String pfbxApplyBonusId;										// pfb 申請單流水號
	private PfbxApplyBonusCheckVo pfbxApplyBonusCheckVo;					// 頁面資料
	
	private String bankCheckStatus;			// 銀行審核狀態
	private String personalCheckStatus;		// 個人審核狀態
	private String applyCheckStatus;		// 請款審核狀態
	
	private String bankCheckNote;			// 銀行退回原因
	private String personalCheckNote;		// 個人退回原因
	private String applyCheckNote;			// 個人退回原因
	
	public String execute(){
		
		enumPfbxAccountCategory = EnumPfbxAccountCategory.values();
		
		enumPfbApplyBonus = EnumPfbApplyStatus.values();
		
		
		return SUCCESS;
	}
	
	public String pfbApplyBonusCheckAction(){
	
		//log.info(" pfbxApplyBonusId: "+pfbxApplyBonusId);
		
		enumPfbApplyCheck = EnumPfbApplyInvoiceStatus.values();
		
		enumPfbxAccountCategory = EnumPfbxAccountCategory.values();
		
		//pfbxApplyBonusCheckVo = pfbxApplyBonusService.findPfbxApplyBonusCheckVo(pfbxApplyBonusId);
		
		return SUCCESS;
	}
	
	public String updateCheckStatusAction(){
		
		log.info(" pfbxApplyBonusId: "+pfbxApplyBonusId);
		log.info(" bankCheckStatus: "+bankCheckStatus);
		log.info(" personalCheckStatus: "+personalCheckStatus);
		log.info(" applyCheckStatus: "+applyCheckStatus);
		log.info(" bankCheckNote: "+bankCheckNote);
		log.info(" personalCheckNote: "+personalCheckNote);
		log.info(" applyCheckNote: "+applyCheckNote);
		
//		PfbxApplyBonus pfbxApplyBonus = pfbxApplyBonusService.findPfbxApplyBonus(pfbxApplyBonusId);
//		 
//		if(pfbxApplyBonus != null ){
//			
//			Date today = new Date();
//			// 銀行審核
//			pfbxApplyBonus.getPfbxBank().setCheckStatus(bankCheckStatus);
//			pfbxApplyBonus.getPfbxBank().setCheckNote(bankCheckNote);
//			pfbxApplyBonus.getPfbxBank().setUpdateDate(today);
//			
//			pfbxBankService.saveOrUpdate(pfbxApplyBonus.getPfbxBank());
//			
//			if(pfbxApplyBonus.getPfbxBank().getPfbxCustomerInfo().getCategory().equals(EnumPfbxAccountCategory.PERSONAL.getCategory())){
//				
//				// 個人審核
//				pfbxApplyBonus.getPfbxPersonal().setCheckStatus(personalCheckStatus);
//				pfbxApplyBonus.getPfbxPersonal().setCheckNote(personalCheckNote);
//				pfbxApplyBonus.getPfbxPersonal().setUpdateDate(today);
//			}			
//			
//			pfbxPersonalService.saveOrUpdate(pfbxApplyBonus.getPfbxPersonal());
//			
//			// 請款審核
//			pfbxApplyBonus.setCheckStatus(applyCheckStatus);
//			pfbxApplyBonus.setCheckNote(applyCheckNote);
//			pfbxApplyBonus.setUpdateDate(today);			
//			
//			/**
//			 * 1. 所有審核通過，請款單狀態才能變更成-->待付款
//			 * 2. 只要有一個審核失敗，請款單狀態變更成-->資料不符
//			 * 3. 公司戶不需要審核個人資料
//			 */
//			if(EnumPfbApplyCheck.PASS.getStatus().equals(bankCheckStatus) &&
//					EnumPfbApplyCheck.PASS.getStatus().equals(applyCheckStatus)){
//				// 審核通過 - 等待付款
//				pfbxApplyBonus.setApplyStatus(EnumPfbApplyBonus.WAIT_PAY.getStatus());
//			}
//			
//			if(pfbxApplyBonus.getPfbxBank().getPfbxCustomerInfo().getCategory().equals(EnumPfbxAccountCategory.PERSONAL.getCategory()) &&
//					EnumPfbApplyCheck.PASS.getStatus().equals(personalCheckStatus)){
//				// 審核通過 - 等待付款
//				pfbxApplyBonus.setApplyStatus(EnumPfbApplyBonus.WAIT_PAY.getStatus());
//			}
//			
//			if(EnumPfbApplyCheck.REJECT.getStatus().equals(bankCheckStatus) || 
//					EnumPfbApplyCheck.REJECT.getStatus().equals(applyCheckStatus)){
//				// 審核失敗
//				pfbxApplyBonus.setApplyStatus(EnumPfbApplyBonus.REJECT.getStatus());
//			}
//			
//			if(pfbxApplyBonus.getPfbxBank().getPfbxCustomerInfo().getCategory().equals(EnumPfbxAccountCategory.PERSONAL.getCategory()) &&
//					EnumPfbApplyCheck.REJECT.getStatus().equals(personalCheckStatus)){
//				// 審核失敗
//				pfbxApplyBonus.setApplyStatus(EnumPfbApplyBonus.REJECT.getStatus());
//			}
//			
//			pfbxApplyBonusService.saveOrUpdate(pfbxApplyBonus);
//		}
		
		return SUCCESS;
	}


	public void setPfbxBankService(IPfbxBankService pfbxBankService) {
		this.pfbxBankService = pfbxBankService;
	}

	public void setPfbxPersonalService(IPfbxPersonalService pfbxPersonalService) {
		this.pfbxPersonalService = pfbxPersonalService;
	}

	public EnumPfbApplyStatus[] getEnumPfbApplyBonus() {
		return enumPfbApplyBonus;
	}

	public EnumPfbApplyInvoiceStatus[] getEnumPfbApplyCheck() {
		return enumPfbApplyCheck;
	}

	public EnumPfbxAccountCategory[] getEnumPfbxAccountCategory() {
		return enumPfbxAccountCategory;
	}

	public void setPfbxApplyBonusId(String pfbxApplyBonusId) {
		this.pfbxApplyBonusId = pfbxApplyBonusId;
	}

	public PfbxApplyBonusCheckVo getPfbxApplyBonusCheckVo() {
		return pfbxApplyBonusCheckVo;
	}

	public void setBankCheckStatus(String bankCheckStatus) {
		this.bankCheckStatus = bankCheckStatus;
	}

	public void setPersonalCheckStatus(String personalCheckStatus) {
		this.personalCheckStatus = personalCheckStatus;
	}

	public void setApplyCheckStatus(String applyCheckStatus) {
		this.applyCheckStatus = applyCheckStatus;
	}

	public void setBankCheckNote(String bankCheckNote) {
		this.bankCheckNote = bankCheckNote;
	}

	public void setPersonalCheckNote(String personalCheckNote) {
		this.personalCheckNote = personalCheckNote;
	}

	public void setApplyCheckNote(String applyCheckNote) {
		this.applyCheckNote = applyCheckNote;
	}

}
