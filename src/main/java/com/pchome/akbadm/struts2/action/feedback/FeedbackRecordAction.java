package com.pchome.akbadm.struts2.action.feedback;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpTransDetail;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.feedback.IAdmFeedbackRecordService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.feedback.EnumFeedbackStatus;
import com.pchome.enumerate.feedback.EnumSelAccountCategory;
import com.pchome.enumerate.feedback.EnumSelCalculateCategory;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.rmi.sequence.EnumSequenceTableName;
import com.pchome.soft.util.DateValueUtil;

public class FeedbackRecordAction extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IPfpCustomerInfoService customerInfoService;
	private IAdmFeedbackRecordService feedbackRecordService;
	private ISequenceService sequenceService;
	private IPfpTransDetailService transDetailService;
	private IBoardProvider boardProvider;
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private IPfdBoardService pfdBoardService;

	private EnumSelAccountCategory[] enumSelAccountCategory = EnumSelAccountCategory.values();	
	private EnumSelCalculateCategory[] enumSelCalculateCategory = EnumSelCalculateCategory.values();
	private List<PfdCustomerInfo> pfdCustomerInfoList;
	
	private String sDate;
	private String eDate;
	
	private String selAccountCategory;					// 選擇帳戶種類
	private String memberId;							// 帳戶負責人
	private float feedbackMoney;						// 回饋金金額
	private String giftDate;							// 贈送日期
	private String reason;								// 贈送理由
	private String selCalculateCategory;				// 回饋金入帳時間 1.排程 2.立即
	private String author;								// 建立者
	private String inviledDate;                         // 到期日期
	private String errorMessage;						// 錯誤訊息
	private String[] pfdAccount;						//經銷商帳戶
	
	public String execute(){
		
		sDate = DateValueUtil.getInstance().getDateValue(-30, DateValueUtil.DBPATH);
		eDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH);
		
		return SUCCESS;
	}
	
	public String feedbackRecordAddAction() {
		selAccountCategory = EnumSelAccountCategory.ONLY.getCategory();
		selCalculateCategory = EnumSelCalculateCategory.QUARTZS.getCategory();
		pfdCustomerInfoList = pfdCustomerInfoService.findPfdValidCustomerInfo();
		return SUCCESS;
	}
	
	public String feedbackRecordCreateAction() {
		
		List<PfpCustomerInfo> customerInfos = null;		
		
		log.info(" selAccountCategory = "+selAccountCategory);
		log.info(" memberId = "+memberId);
		log.info(" selCalculateCategory = "+selCalculateCategory);
		
		if(this.validCreatData()){

			if(selAccountCategory.equals(EnumSelAccountCategory.ALL.getCategory())){
				
				List<String> pfdIdList = new ArrayList<String>();
				for(String pfdId:pfdAccount){
					pfdIdList.add(pfdId);
				}
				
				customerInfos = pfdUserAdAccountRefService.findPfdUserAdAccountRefByPfdId(pfdIdList);
			// 指定某一間 PFP 帳戶
			}else if(selAccountCategory.equals(EnumSelAccountCategory.ONLY.getCategory()) && 
					StringUtils.isNotBlank(memberId)){
				
				customerInfos = customerInfoService.findValidCustomerInfoByMemberId(memberId);
				
				PfdUserAdAccountRef pfdUserAdAccountRef = pfdUserAdAccountRefService.findPfdUserAdAccountRefByPfpId(customerInfos.get(0).getCustomerInfoId());
				
				//AC2013071700001這個帳戶dd要儲值，所以要排除掉
				if(!StringUtils.equals(pfdUserAdAccountRef.getPfdCustomerInfo().getCustomerInfoId(), "PFDC20140520001") &&
						!StringUtils.equals(customerInfos.get(0).getCustomerInfoId(),"AC2013071700001")){
					errorMessage = "此帳號不屬於PChome直客管理經銷商，故不適用贈送禮金。";
					pfdCustomerInfoList = pfdCustomerInfoService.findPfdValidCustomerInfo();
					return INPUT;
				}
				
			}
			
			this.createFeedbackRecord(customerInfos);
		}
		
		return SUCCESS;
	}
	
	private boolean validCreatData(){
		
		boolean isCreate = false;
		
		if(StringUtils.isNotBlank(selAccountCategory) && feedbackMoney > -1 && 
				StringUtils.isNotBlank(giftDate) && StringUtils.isNotBlank(reason) &&
				StringUtils.isNotBlank(selCalculateCategory) && StringUtils.isNotBlank(author)){
			isCreate = true;
		}	
//		else{
//			log.info("selAccountCategory "+selAccountCategory);
//			log.info("feedbackMoney "+feedbackMoney);
//			log.info("giftDate "+giftDate);
//			log.info("reason "+reason);
//			log.info("selCalculateCategory "+selCalculateCategory);
//			log.info("author "+author);
//		}
		
		return isCreate;
	}
	
	private void createFeedbackRecord(List<PfpCustomerInfo> customerInfos){
		
		Date today = new Date();
		
		for(PfpCustomerInfo customerInfo:customerInfos){
		
			// 新增回饋金記錄
			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_FEEDBACK_RECORD);
			AdmFeedbackRecord feedbackRecord = new AdmFeedbackRecord();
			
			feedbackRecord.setFeedbackRecordId(recordId);
			feedbackRecord.setPfpCustomerInfo(customerInfo);
			feedbackRecord.setFeedbackMoney(feedbackMoney);
			feedbackRecord.setReason(reason);
			feedbackRecord.setGiftDate(DateValueUtil.getInstance().stringToDate(giftDate));
			if (StringUtils.isNotBlank(inviledDate)) {
				feedbackRecord.setInviledDate(DateValueUtil.getInstance().stringToDate(inviledDate));
			}
			feedbackRecord.setStatus(EnumFeedbackStatus.START.getStatus());
			feedbackRecord.setRetrievedFlag("n");
			feedbackRecord.setCalculateCategory(selCalculateCategory);
			feedbackRecord.setEditor(author);
			feedbackRecord.setAuthor(author);
			feedbackRecord.setUpdateDate(today);
			feedbackRecord.setCreateDate(today);
			
			if(selCalculateCategory.equals(EnumSelCalculateCategory.NOW.getCategory())){				
				this.updateCustomerInfo(customerInfo);
				this.createTransDetail(customerInfo);
				
				// 刪除餘額不足公告
				boardProvider.delete(customerInfo.getCustomerInfoId(), EnumCategory.REMAIN_NOT_ENOUGH);
				
				//查看是否有經銷商，有的話也要刪除經銷商公告
    			try {
					List<PfdUserAdAccountRef> PfdUserAdAccountRefList = pfdUserAdAccountRefService.findPfdUserIdByPfpCustomerInfoId(customerInfo.getCustomerInfoId());
					
					if (PfdUserAdAccountRefList.size()>0) {
						pfdBoardService.deletePfdBoardByDeleteId(customerInfo.getCustomerInfoId());
					}
				} catch (Exception e) {
					log.error(customerInfo.getCustomerInfoId(), e);
				}
			}		
			
			feedbackRecordService.saveOrUpdate(feedbackRecord);
			
		}
	}
	
	private void createTransDetail(PfpCustomerInfo customerInfo){
		
		PfpTransDetail transDetail = new PfpTransDetail();
		Date today = new Date();
		
		transDetail.setPfpCustomerInfo(customerInfo);
		transDetail.setTransDate(DateValueUtil.getInstance().stringToDate(giftDate));
		transDetail.setTransContent(EnumTransType.FEEDBACK_MONEY.getChName());
		transDetail.setTransType(EnumTransType.FEEDBACK_MONEY.getTypeId());
		transDetail.setIncomeExpense("+");
		transDetail.setTransPrice(feedbackMoney);
		transDetail.setTotalSavePrice(customerInfo.getTotalAddMoney());
		transDetail.setTotalSpendPrice(customerInfo.getTotalSpend());
		transDetail.setRemain(customerInfo.getRemain());
		transDetail.setTotalRetrievePrice(customerInfo.getTotalRetrieve());
		transDetail.setTax(0);	
		
		transDetail.setUpdateDate(today);
		transDetail.setCreateDate(today);
		
		transDetailService.saveOrUpdate(transDetail);
	}
	
	private void updateCustomerInfo(PfpCustomerInfo customerInfo){
		
		Date today = new Date();
		
		float totalAddMoney = customerInfo.getTotalAddMoney() + feedbackMoney;
		float remain = customerInfo.getRemain() + feedbackMoney;
		
		customerInfo.setRemain(remain);
		customerInfo.setLaterRemain(remain);
		customerInfo.setTotalAddMoney(totalAddMoney);
		customerInfo.setUpdateDate(today);
		
		customerInfoService.saveOrUpdate(customerInfo);		
	}
	
	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}

	public void setFeedbackRecordService(IAdmFeedbackRecordService feedbackRecordService) {
		this.feedbackRecordService = feedbackRecordService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setTransDetailService(IPfpTransDetailService transDetailService) {
		this.transDetailService = transDetailService;
	}

	public void setBoardProvider(IBoardProvider boardProvider) {
		this.boardProvider = boardProvider;
	}

	public void setPfdUserAdAccountRefService(
			IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfdCustomerInfoService(
			IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public String getSDate() {
		return sDate;
	}

	public String getEDate() {
		return eDate;
	}

	public EnumSelAccountCategory[] getEnumSelAccountCategory() {
		return enumSelAccountCategory;
	}

	public EnumSelCalculateCategory[] getEnumSelCalculateCategory() {
		return enumSelCalculateCategory;
	}

	public void setSelAccountCategory(String selAccountCategory) {
		this.selAccountCategory = selAccountCategory;
	}

	public void setSelCalculateCategory(String selCalculateCategory) {
		this.selCalculateCategory = selCalculateCategory;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public void setFeedbackMoney(float feedbackMoney) {
		this.feedbackMoney = feedbackMoney;
	}

	public void setGiftDate(String giftDate) {
		this.giftDate = giftDate;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setInviledDate(String inviledDate) {
		this.inviledDate = inviledDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public IBoardProvider getBoardProvider() {
		return boardProvider;
	}

	public String getSelAccountCategory() {
		return selAccountCategory;
	}

	public String getMemberId() {
		return memberId;
	}

	public float getFeedbackMoney() {
		return feedbackMoney;
	}

	public String getGiftDate() {
		return giftDate;
	}

	public String getReason() {
		return reason;
	}

	public String getSelCalculateCategory() {
		return selCalculateCategory;
	}

	public String getAuthor() {
		return author;
	}

	public String getInviledDate() {
		return inviledDate;
	}

	public List<PfdCustomerInfo> getPfdCustomerInfoList() {
		return pfdCustomerInfoList;
	}

	public void setPfdAccount(String[] pfdAccount) {
		this.pfdAccount = pfdAccount;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}
	
}
