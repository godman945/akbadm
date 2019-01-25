package com.pchome.akbadm.db.service.retrieve;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.retrieve.IAdmRetrieveRecordDAO;
import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;
import com.pchome.akbadm.db.service.BaseService;

public class AdmRetrieveRecordService extends BaseService<AdmRetrieveRecord, Integer> implements IAdmRetrieveRecordService{

	public List<AdmRetrieveRecord> findRetrieveRecord(Map<String, String> conditionMap) throws Exception {
		return ((IAdmRetrieveRecordDAO) dao).findRetrieveRecord(conditionMap);
	}
}
