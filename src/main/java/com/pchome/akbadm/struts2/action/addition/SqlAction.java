package com.pchome.akbadm.struts2.action.addition;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdGroup;
import com.pchome.akbadm.db.pojo.PfpAdKeyword;
import com.pchome.akbadm.db.service.ad.IPfpAdGroupService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdStatus;

public class SqlAction extends BaseCookieAction {
 
	private IPfpAdGroupService pfpAdGroupService;
	
	private String showMsg;
	
	public String execute() {
		
		
		return SUCCESS;
	}
	
	/**
	 * 修正廣告狀態
	 * 1. 檢查每個廣告群組下, 是否關鍵字或播放明細狀態不是被關閉
	 * 2. 關鍵字或播放明細其一狀態是關閉, 該廣告群組狀態為未完成	 
	 * 3. 所有資料需要記錄下來, 提供企劃核對
	 */
	public String updateAdGroupStatusAjax(){
		
		StringBuffer proccessLogs = new StringBuffer();
		List<PfpAdGroup> pfpAdGroups = pfpAdGroupService.findAdGroup(EnumAdStatus.Open);
		
		for(PfpAdGroup group:pfpAdGroups){
			
			if(group.getPfpAds().isEmpty() || group.getPfpAdKeywords().isEmpty()){
				
				this.addAdGroupLogs(proccessLogs, group);
				
				//this.updateAdGroupStatus(group);
			}
			
		}
		
		showMsg = proccessLogs.toString();
		
		log.info(" showMsg: "+showMsg);
		
		return SUCCESS;
	}
	
	private void addAdGroupLogs(StringBuffer proccessLogs, PfpAdGroup adGroup){
		
		proccessLogs.append("<br>\n")
		.append(" CustomerInfoId: ").append(adGroup.getPfpAdAction().getPfpCustomerInfo().getCustomerInfoId())
		.append("  ")
		.append(" AdGroupId: ").append(adGroup.getAdGroupSeq())
		.append("  ")
		.append(" AdGroupName: ").append(adGroup.getAdGroupName());
	}
	
	/**
	 * 更新廣告分類狀態
	 */
//	private void updateAdGroupStatus(PfpAdGroup adGroup) {
//		
//		adGroup.setAdGroupStatus(EnumAdStatus.UnDone.getStatusId());
//		pfpAdGroupService.saveOrUpdate(adGroup);
//	}
	
	public void setPfpAdGroupService(IPfpAdGroupService pfpAdGroupService) {
		this.pfpAdGroupService = pfpAdGroupService;
	}

	public String getShowMsg() {
		return showMsg;
	}
}
