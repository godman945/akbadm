package com.pchome.rmi.bonus;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.accesslog.AdmAccesslogService;
import com.pchome.akbadm.db.service.pfbx.account.PfbxCustomerInfoService;
import com.pchome.akbadm.factory.pfbx.bonus.ApplyOrderProcess;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;


public class PfbBonusApplyOrderProcessProviderImp implements IPfbBonusApplyOrderProcessProvider{

	
	private ApplyOrderProcess applyOrderProcess;
	private PfbxCustomerInfoService pfbxCustomerInfoService;
	private AdmAccesslogService accesslogService;
	
	
	@Override
	public String pfbApplyOrderCreate(String pfbId, String applyMoney,String memberId,String address) {
		
		// TODO Auto-generated method stub
		String returnValue="";
		PfbxCustomerInfo pfbxCustomerInfo=pfbxCustomerInfoService.findPfbxCustomerInfo(pfbId);
		returnValue=applyOrderProcess.doApplyStatusProcess(pfbxCustomerInfo, EnumPfbApplyStatus.APPLY, null, Float.valueOf(applyMoney),null, null,memberId,address);
		
		return returnValue;
		
	}

	public Integer pfbApprovePayment(String memberId, String pfbCustomerId, String orderId, String ip) {
		
		return   accesslogService.addAdmAccesslog(
				   	EnumAccesslogChannel.PFB,
	                EnumAccesslogAction.PFB_CLICK,
	                EnumAccesslogAction.PFB_CLICK.getMessage() + "-->" + "確認請款收據與金額",
	                memberId,
	                orderId,
	                pfbCustomerId,
	                null,
	                ip,
	                EnumAccesslogEmailStatus.NO);
	}

	
	

	public AdmAccesslogService getAccesslogService() {
		return accesslogService;
	}

	public void setAccesslogService(AdmAccesslogService accesslogService) {
		this.accesslogService = accesslogService;
	}

	public void setApplyOrderProcess(ApplyOrderProcess applyOrderProcess) {
		this.applyOrderProcess = applyOrderProcess;
	}


	public void setPfbxCustomerInfoService(
			PfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}
	
}