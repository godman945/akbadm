package com.pchome.akbadm.struts2.action.addition;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.order.IPfpOrderDetailService;
import com.pchome.akbadm.db.service.order.IPfpOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.user.IPfpUserMemberRefService;
import com.pchome.akbadm.db.service.user.IPfpUserService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class DeleteTestAccountAction extends BaseCookieAction{

	// Ray , local
	private final String[] ips = {"10.50.111.9","127.0.0.1"};
	
	private IPfpCustomerInfoService pfpCustomerInfoService;
	//private IPfdContractService pfdContractService;
	//private IPfdVirtualRecordService pfdVirtualRecordService;
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfpUserMemberRefService pfpUserMemberRefService;
	private IPfpUserService pfpUserService;
	private IPfpOrderDetailService pfpOrderDetailService;
	private IPfpOrderService pfpOrderService;
	private IAdmRecognizeRecordService admRecognizeRecordService;
	
	
	
	private boolean isAdmin = false;
	private String pcId;
	private String showMsg;
	private StringBuffer deleteRecord = new StringBuffer();
	
	public String execute() {		
		return SUCCESS;
	}
	
	public String deleteTestAccountAjax() {
		
		if(StringUtils.isNotBlank(pcId)){
			
			// 檢查 IP
			for (String ip : ips) {
				if (ip.equals(this.getRequest().getRemoteAddr())) {
					isAdmin = true;
					break;
				}
			}
			
			if(isAdmin){
				
				this.deleteTestAccount();
				
			}else{
				showMsg = " 並不是有效的 IP: "+this.getRequest().getRemoteAddr()+" 位置 !!";
			}
		}
		else{
			showMsg = " 並沒有輸入 PCId !!";
		}
		
		//log.info(" \n showMsg: "+showMsg);
		//log.info(" \n deleteRecord: "+deleteRecord.toString());
		
		return SUCCESS;
	}
	
	/**
	 * 刪除測試帳戶(只刪除申請中的帳戶) 
	 * 1. 先找申請中的帳戶
	 * 2. 先刪經銷商的關聯
	 * 3. 刪掉訂單資料
	 * 4. 刪掉使用者
	 * 5. 刪掉攤提記錄表
	 * 6. 刪掉帳戶資料
	 * 7. 回復經銷商合約後付金額
	 */
	@Transactional
	private void deleteTestAccount() {		
		
		deleteRecord.append(" \n ").append(" pcId: ").append(pcId).append(" \n ");
		
		try{
			
			// 先找申請中的帳戶
			List<PfpCustomerInfo> pfpCustomerInfos = pfpCustomerInfoService.findPfpCustomerInfo(pcId);
			
			if(!pfpCustomerInfos.isEmpty()){
				
				String pfpCustomerInfoId = null;
				for(PfpCustomerInfo info:pfpCustomerInfos){
					
					pfpCustomerInfoId = info.getCustomerInfoId();
					
					deleteRecord.append(" pfpCustomerInfoId: ").append(pfpCustomerInfoId).append(" \n ");
					
					this.deletePfdUserAdAccountRef(pfpCustomerInfoId);				

					this.deleteOrder(pfpCustomerInfoId);
					
					this.deleteUser(pfpCustomerInfoId);
					
					this.deleteRecognizeRecord(pfpCustomerInfoId);
					
					this.deletePfpCustomerInfo(pfpCustomerInfoId);
					
					showMsg = " 刪除帳戶成功!! \n " + deleteRecord.toString();
				}
				
			}else{
				
				showMsg = " 找不到該帳戶資料!! \n ";
			}
			
			
		}catch(Exception e){			
			
			showMsg = " 刪除帳戶失敗!! \n ";
			showMsg += e;
		}
	}

	/**
	 * 刪經銷商的關聯
	 */
	private void deletePfdUserAdAccountRef(String pfpCustomerInfoId){
		int deleteSize = pfdUserAdAccountRefService.deletePfdUserAdAccountRef(pfpCustomerInfoId);
		deleteRecord.append(" delete PfdUserAdAccountRef: ").append(deleteSize).append(" \n ");
	}
	
	/**
	 * 刪掉訂單資料
	 */
	private void deleteOrder(String pfpCustomerInfoId) {
		
		List<PfpOrder> pfpOrders =  pfpOrderService.findPfpOrder(pfpCustomerInfoId);
		for(PfpOrder order:pfpOrders){
			int deleteDetailSize = pfpOrderDetailService.deletePfpOrderDetail(order.getOrderId());
			deleteRecord.append(" delete PfpOrderDetail: ").append(deleteDetailSize).append(" \n ");
			int deleteOrderSize = pfpOrderService.deletePfpOrder(order.getOrderId());
			deleteRecord.append(" delete PfpOrder: ").append(deleteOrderSize).append(" \n ");
		}
	}
	
	/**
	 * 刪掉使用者
	 */
	private void deleteUser(String pfpCustomerInfoId){
		int deleteRefSize = pfpUserMemberRefService.deletePfpUserMemberRef(pcId);
		deleteRecord.append(" delete PfpUserMemberRef: ").append(deleteRefSize).append(" \n ");
		
		List<PfpUser> pfpUsers = pfpUserService.findPfpUser(pfpCustomerInfoId);
		
		for(PfpUser user:pfpUsers){			
			int deleteUserSize = pfpUserService.deletePfpUser(user.getUserId());
			deleteRecord.append(" delete PfpUser: ").append(deleteUserSize).append(" \n ");
		}
	}
	
	/**
	 *  刪掉攤提記錄表
	 */
	public void deleteRecognizeRecord(String pfpCustomerInfoId) {
		
		List<AdmRecognizeRecord> records = admRecognizeRecordService.findRecognizeRecords(pfpCustomerInfoId);
		
		for(AdmRecognizeRecord record:records){
			int deleteSize = admRecognizeRecordService.deleteAdmRecognizeRecord(record.getRecognizeOrderId());
			deleteRecord.append(" delete AdmRecognizeRecord: ").append(deleteSize).append(" \n ");
		}
		
	}
	
	/**
	 *  刪掉帳戶資料
	 */
	private void deletePfpCustomerInfo(String pfpCustomerInfoId) {
		int deleteSize = pfpCustomerInfoService.deletePfpCustomerInfo(pfpCustomerInfoId);
		deleteRecord.append(" delete PfpCustomerInfo: ").append(deleteSize).append(" \n ");
	}
	
	/**
	 * 回復經銷商合約後付金額
	 * 1. 回復後付金額
	 * 2. 刪掉後付記錄
	 */
	private void reSetContract(String pfpCustomerInfoId) {
		// 考量新增TABLE 關係, 暫時用手動更新
	}
	
	public void setPfpCustomerInfoService(
			IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

//	public void setPfdContractService(IPfdContractService pfdContractService) {
//		this.pfdContractService = pfdContractService;
//	}
//
//	public void setPfdVirtualRecordService(
//			IPfdVirtualRecordService pfdVirtualRecordService) {
//		this.pfdVirtualRecordService = pfdVirtualRecordService;
//	}

	public void setPfpUserMemberRefService(
			IPfpUserMemberRefService pfpUserMemberRefService) {
		this.pfpUserMemberRefService = pfpUserMemberRefService;
	}

	public void setPfpOrderDetailService(
			IPfpOrderDetailService pfpOrderDetailService) {
		this.pfpOrderDetailService = pfpOrderDetailService;
	}

	public void setPfpOrderService(IPfpOrderService pfpOrderService) {
		this.pfpOrderService = pfpOrderService;
	}

	public void setPfdUserAdAccountRefService(
			IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfpUserService(IPfpUserService pfpUserService) {
		this.pfpUserService = pfpUserService;
	}

	public void setPcId(String pcId) {
		this.pcId = pcId;
	}

	public String getShowMsg() {
		return showMsg;
	}

	public void setAdmRecognizeRecordService(
			IAdmRecognizeRecordService admRecognizeRecordService) {
		this.admRecognizeRecordService = admRecognizeRecordService;
	}
	
}
