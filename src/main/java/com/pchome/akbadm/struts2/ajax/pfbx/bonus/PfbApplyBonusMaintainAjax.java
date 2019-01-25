package com.pchome.akbadm.struts2.ajax.pfbx.bonus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfbxBank;
import com.pchome.akbadm.db.pojo.PfbxBonusApply;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPersonal;
import com.pchome.akbadm.db.service.pfbx.bonus.IPfbxBonusApplyService;
import com.pchome.akbadm.db.vo.pfbx.bonus.PfbxApplyBonusVo;
import com.pchome.akbadm.factory.pfbx.bonus.ApplyOrderProcess;
import com.pchome.akbadm.factory.pfbx.bonus.CheckStatusFactory;
import com.pchome.akbadm.quartzs.PfbBonusApplyOrderProcessJob;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.account.EnumPfbxAccountCategory;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceCheckStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyInvoiceStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbApplyStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbBankStatus;
import com.pchome.enumerate.pfbx.bonus.EnumPfbPersonalStatus;

public class PfbApplyBonusMaintainAjax extends BaseCookieAction
{
	private String startDate; 	// 開始日期
	private String endDate; 	// 結束日期
	private String keyword; 	// 關鍵字查詢
	private String category; 	// 帳戶類型
	private String status; 		// 分潤狀態
	
	private String applyId;		// 申請單id
	private String upType;		// update applyStatus or InvoiceStatus
	private String checkStatus; // 更新狀態
	private String checkNote;	// 更新失敗訊息
	
	private String errorMsg;
	
	private EnumPfbApplyStatus[] enumPfbApplyStatus = null; // 申請單審核狀態
	private EnumPfbApplyInvoiceStatus[] enumPfbApplyInvoiceStatus = null; // 發票/收據審核狀態
	private EnumPfbApplyInvoiceCheckStatus[] enumPfbApplyInvoiceCheckStatus = null;	// 發票/收據確認狀態

	//service類
	private IPfbxBonusApplyService pfbxBonusApplyService;

	private ApplyOrderProcess applyOrderProcess;
	
	private PfbBonusApplyOrderProcessJob pfbBonusApplyOrderProcessJob;
	
	private CheckStatusFactory checkStatusFactory;	//處理num
	
	private List<PfbxApplyBonusVo> pfbxApplyBonusVos;	//畫面欄位
	
	private Map<String,Object> dataMap;

	public String searchPfbApplyBonusAjax() throws Exception {
		log.info(">>> startDate: " + startDate);
		log.info(">>> endDate: " + endDate);
		log.info(">>> keyword: " + keyword);
		log.info(">>> category: " + category);
		log.info(">>> status: " + status);
		log.info(">>> applyId: " + applyId);
		log.info(">>> upType: " + upType);
		log.info(">>> checkStatus: " + checkStatus);
		log.info(">>> checkNote: " + checkNote);
		
		enumPfbApplyStatus = EnumPfbApplyStatus.values();
		enumPfbApplyInvoiceStatus = EnumPfbApplyInvoiceStatus.values();
		enumPfbApplyInvoiceCheckStatus = EnumPfbApplyInvoiceCheckStatus.values();
		errorMsg = "";
		
		if (StringUtils.isNotBlank(upType)) {
			PfbxCustomerInfo customerInfo = pfbxBonusApplyService.getCustomerInfoByApplyId(applyId);
			
			if(StringUtils.equals(upType, "invoice")) { // 更新發票/收據 審核狀態
			
				EnumPfbApplyInvoiceStatus enumInvoiceStatus = checkStatusFactory.getOneEnumInEnumPfbApplyInvoiceStatus(checkStatus);
				applyOrderProcess.doInvoiceStatusProcess(customerInfo, enumInvoiceStatus, applyId, checkNote);
				
			} else if(StringUtils.equals(upType, "invoiceCheck")) { // 更新請款同意 確認狀態
			
				EnumPfbApplyInvoiceCheckStatus enumInvoiceCheckStatus = checkStatusFactory.getOneEnumInEnumPfbApplyInvoiceCheckStatus(checkStatus);
				applyOrderProcess.doInvoiceCheckStatusProcess(customerInfo, enumInvoiceCheckStatus, applyId);
				
			} else if (StringUtils.equals(upType, "apply")) { // 更新請款狀態
			
				EnumPfbApplyStatus enumPfbApplyStatus = checkStatusFactory.getOneEnumPfbApplyStatus(checkStatus);
				PfbxBonusApply apply = pfbxBonusApplyService.get(applyId);
				
				if (checkStatus.equals(apply.getApplyStatus())) { // 更新說明原因
					
					if (StringUtils.equals(EnumPfbApplyStatus.APPLYFAIL.getStatus(), checkStatus)) {
						applyOrderProcess.applyOrderApplyFail(customerInfo, enumPfbApplyStatus, applyId, checkNote);
						errorMsg = "更新成功";
					} else if (StringUtils.equals(EnumPfbApplyStatus.PAYFAIL.getStatus(), checkStatus)) {
						applyOrderProcess.applyOrderPayFail(customerInfo, enumPfbApplyStatus, applyId, checkNote);
						errorMsg = "更新成功";
					} else {
						errorMsg = "請款狀態不可修改為當前的請款狀態!!";
					}
					
				} else {
					
					// 請款狀態為申請失敗及付款失敗時不可修改為其他狀態
					if (EnumPfbApplyStatus.APPLYFAIL.getStatus().equals(apply.getApplyStatus())
							|| EnumPfbApplyStatus.PAYFAIL.getStatus().equals(apply.getApplyStatus())) {
						errorMsg = "請款狀態為申請失敗及付款失敗時不可修改為其他狀態";
					} else {
						
						boolean flag = false;
						
						//請款狀態如果是更改為付款中或付款成功，檢查付款人資料及銀行帳戶資料有無審核過
						if (StringUtils.equals(EnumPfbApplyStatus.WAIT_PAY.getStatus(), checkStatus)
								|| StringUtils.equals(EnumPfbApplyStatus.SUCCESS.getStatus(), checkStatus)) {
							
							PfbxBank pfbxBank = apply.getPfbxBank();
							if (pfbxBank != null) {
								String checkStatus = pfbxBank.getCheckStatus();
								if (!StringUtils.equals(EnumPfbBankStatus.SUCCESS.getStatus(), checkStatus)) {
									flag = true;
									errorMsg = "銀行資料尚未審核成功";
								}
							} else {
								flag = true;
								errorMsg = "無銀行資料";
							}
							
							// 個人戶多判斷收款人資料
							if (StringUtils.equals(EnumPfbxAccountCategory.PERSONAL.getCategory(), customerInfo.getCategory())) {
								PfbxPersonal pfbxPersonal = apply.getPfbxPersonal();
								if (pfbxPersonal != null) {
									String checkStatus = pfbxPersonal.getCheckStatus();
									if (!StringUtils.equals(EnumPfbPersonalStatus.SUCCESS.getStatus(), checkStatus)) {
										flag = true;
										errorMsg = "領款人資料尚未審核成功";
									}
								} else {
									flag = true;
									errorMsg = "無領款人資料";
								}
							}
							
						}
						
						if (!flag) {
							applyOrderProcess.doApplyStatusProcess(customerInfo, enumPfbApplyStatus, applyId,
									apply.getApplyMoney(), checkNote, new Date(),
									super.getSession().get(SessionConstants.SESSION_USER_ID).toString(),
									request.getRemoteAddr());
							errorMsg = "更新成功";
						}
						
					}
					
				}
				
			}
		}
		
		pfbxApplyBonusVos = pfbxBonusApplyService.getPfbxApplyBonusVos(startDate, endDate, keyword, category, status);
		
//		for(PfbxApplyBonusVo vo : pfbxApplyBonusVos){
//			log.info("...inv Note=" + vo.getPfbApplyInvoiceNote());
//		}
		return SUCCESS;
	}

	/**
	 * 重跑16號請款排程
	 * 將請款狀態為申請失敗的相關資料，調整回申請中重新執行
	 * @return
	 */
	public String renewRunPaymentRequestAjax() {
		//dataMap中的資料將會被Struts2轉換成JSON字串，所以用Map<String,Object>
		dataMap = new HashMap<String, Object>();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM"); // 現在年月
		Date date = new Date();
		String startDate = dateFormat.format(date) + "-16 00:00:00"; // 開始日期
		String endDate = dateFormat.format(date) + "-20 23:59:59"; // 結束日期
		String createDate = dateFormat.format(date) + "-01 00:00:00"; // 建立日期為當月份，如果只判斷更新日期的話，有去異動舊資料可能出錯
		
		dataMap.put("status", "SUCCESS");
		
		// Step 1.找出當月16號排程跑完，狀態為申請失敗的請款案件，沒有資料就不做步驟2、3了
		List<PfbxBonusApply> pfbxBonusApplyFailList = pfbxBonusApplyService.findApplyOrderByStatusAndUpdateDate(EnumPfbApplyStatus.APPLYFAIL, startDate, endDate, createDate);
        if (pfbxBonusApplyFailList == null) {
            log.info("apply order status Apply Fail not find");
            dataMap.put("status", "NODATA");
            dataMap.put("msg", "本月份無申請失敗資料。");
            return SUCCESS;
		}
        
		// Step 2.將相關資料調整回申請中，處理每一筆申請失敗案件
		for (PfbxBonusApply pfbxBonusApplyFail : pfbxBonusApplyFailList) {
			applyOrderProcess.doApplyFailResetProcess(pfbxBonusApplyFail.getPfbxCustomerInfo(), pfbxBonusApplyFail.getApplyId(), super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), request.getRemoteAddr());
		}

		// Step 3.執行每個月的16號排程
        pfbBonusApplyOrderProcessJob.autoMonthDay16ChangeInvoiceWaitPay();
        pfbBonusApplyOrderProcessJob.autoMonthDay20ChangeInvoiceFail();
		
        dataMap.put("msg", "重跑完成。");
		return SUCCESS;
    }
	
	/**
	 * 用來判斷是否顯示"重跑請款"按鈕
	 * 取得今天幾號
	 * @return
	 */
	public int getDateDay() {
		DateFormat dateFormat = new SimpleDateFormat("dd");
		Date date = new Date();
		return Integer.parseInt(dateFormat.format(date));
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<PfbxApplyBonusVo> getPfbxApplyBonusVos() {
		return pfbxApplyBonusVos;
	}

	public void setPfbxBonusApplyService(IPfbxBonusApplyService pfbxBonusApplyService) {
		this.pfbxBonusApplyService = pfbxBonusApplyService;
	}

	public EnumPfbApplyStatus[] getEnumPfbApplyStatus() {
		return enumPfbApplyStatus;
	}

	public EnumPfbApplyInvoiceStatus[] getEnumPfbApplyInvoiceStatus() {
		return enumPfbApplyInvoiceStatus;
	}
	
	public EnumPfbApplyInvoiceCheckStatus[] getEnumPfbApplyInvoiceCheckStatus() {
		return enumPfbApplyInvoiceCheckStatus;
	}

	public void setUpType(String upType) {
		this.upType = upType;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public void setCheckStatusFactory(CheckStatusFactory checkStatusFactory) {
		this.checkStatusFactory = checkStatusFactory;
	}

	public void setApplyOrderProcess(ApplyOrderProcess applyOrderProcess) {
		this.applyOrderProcess = applyOrderProcess;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public void setPfbBonusApplyOrderProcessJob(PfbBonusApplyOrderProcessJob pfbBonusApplyOrderProcessJob) {
		this.pfbBonusApplyOrderProcessJob = pfbBonusApplyOrderProcessJob;
	}

}
