package com.pchome.akbadm.db.service.feedback;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmFeedbackRecordService extends IBaseService<AdmFeedbackRecord, Integer>{

	public List<AdmFeedbackRecord> findValidFeedbackRecord(String accountName, String sDate, String eDate);
	
	public AdmFeedbackRecord findFeedbackRecord(String recordId);
	
	public List<AdmFeedbackRecord> findFeedbackRecord(String customerInfoId, Date date);

	public List<AdmFeedbackRecord> findFeedbackRecord(Map<String, String> conditionMap) throws Exception;
}
