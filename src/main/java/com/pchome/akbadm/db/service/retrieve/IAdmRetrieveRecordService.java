package com.pchome.akbadm.db.service.retrieve;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;
import com.pchome.akbadm.db.service.IBaseService;

public interface IAdmRetrieveRecordService extends IBaseService<AdmRetrieveRecord, Integer>{

	public List<AdmRetrieveRecord> findRetrieveRecord(Map<String, String> conditionMap) throws Exception;
}
