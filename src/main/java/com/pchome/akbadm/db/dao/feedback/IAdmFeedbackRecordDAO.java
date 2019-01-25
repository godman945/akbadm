package com.pchome.akbadm.db.dao.feedback;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;

public interface IAdmFeedbackRecordDAO extends IBaseDAO<AdmFeedbackRecord,Integer>{

	public List<AdmFeedbackRecord> findValidFeedbackRecord(String accountName, Date sDate, Date eDate);
	
	public List<AdmFeedbackRecord> findFeedbackRecord(String recordId);
	
	public List<AdmFeedbackRecord> findFeedbackRecord(String customerInfoId, Date date);

	public List<AdmFeedbackRecord> findFeedbackRecord(Map<String, String> conditionMap) throws Exception;
}
