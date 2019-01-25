package com.pchome.akbadm.db.service.feedback;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.feedback.IAdmFeedbackRecordDAO;
import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.soft.util.DateValueUtil;

public class AdmFeedbackRecordService extends BaseService<AdmFeedbackRecord, Integer> implements IAdmFeedbackRecordService{

	public List<AdmFeedbackRecord> findValidFeedbackRecord(String accountName, String sDate, String eDate){
		
		Date startDate = DateValueUtil.getInstance().stringToDate(sDate);
		Date endDate = DateValueUtil.getInstance().stringToDate(eDate);
		
		return ((IAdmFeedbackRecordDAO)dao).findValidFeedbackRecord(accountName, startDate, endDate);
	}
	
	public AdmFeedbackRecord findFeedbackRecord(String recordId) {
		
		List<AdmFeedbackRecord> records = ((IAdmFeedbackRecordDAO)dao).findFeedbackRecord(recordId);
		
		if(records.isEmpty()){
			return null;
		}else{
			return records.get(0);
		}
		
	}
	
	public List<AdmFeedbackRecord> findFeedbackRecord(String customerInfoId, Date date) {
		return ((IAdmFeedbackRecordDAO)dao).findFeedbackRecord(customerInfoId, date);
	}

	public List<AdmFeedbackRecord> findFeedbackRecord(Map<String, String> conditionMap) throws Exception {
		return ((IAdmFeedbackRecordDAO) dao).findFeedbackRecord(conditionMap);
	}
}
