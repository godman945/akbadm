package com.pchome.akbadm.struts2.ajax.feedback;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.service.feedback.IAdmFeedbackRecordService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.feedback.EnumFeedbackStatus;
import com.pchome.enumerate.feedback.EnumSelCalculateCategory;

public class FeedbackRecordAjax extends BaseCookieAction{

	private IAdmFeedbackRecordService feedbackRecordService;
	private EnumSelCalculateCategory[] enumSelCalculateCategory = EnumSelCalculateCategory.values();

	private String accountName;
	private String sDate;
	private String eDate;
	
	private String recordIds;
	
	private List<AdmFeedbackRecord> feedbackRecords;
	
	
	public String searchFeedbackRecordAjax() {
		
		if(StringUtils.isNotBlank(sDate) && StringUtils.isNotBlank(eDate)){
			feedbackRecords = feedbackRecordService.findValidFeedbackRecord(accountName, sDate, eDate);
		}
	
		
		return SUCCESS;
	}
	
	@Transactional
	public String feedbackRecordDeleteAction() {
		
		if(StringUtils.isNotBlank(recordIds)){
			
			String[] ids = recordIds.split(",");
			
			for(String id:ids){
				AdmFeedbackRecord record = feedbackRecordService.findFeedbackRecord(id);
				record.setStatus(EnumFeedbackStatus.DELECT.getStatus());
				record.setUpdateDate(new Date());
				feedbackRecordService.saveOrUpdate(record);
			}
			
		}
		
		if(StringUtils.isNotBlank(sDate) && StringUtils.isNotBlank(eDate)){
			feedbackRecords = feedbackRecordService.findValidFeedbackRecord(accountName, sDate, eDate);
		}
		
		return SUCCESS;
	}
	
	public void setFeedbackRecordService(IAdmFeedbackRecordService feedbackRecordService) {
		this.feedbackRecordService = feedbackRecordService;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setSDate(String sDate) {
		this.sDate = sDate;
	}

	public void setEDate(String eDate) {
		this.eDate = eDate;
	}

	public List<AdmFeedbackRecord> getFeedbackRecords() {
		return feedbackRecords;
	}

	public void setRecordIds(String recordIds) {
		this.recordIds = recordIds;
	}

	public EnumSelCalculateCategory[] getEnumSelCalculateCategory() {
		return enumSelCalculateCategory;
	}
	
}
