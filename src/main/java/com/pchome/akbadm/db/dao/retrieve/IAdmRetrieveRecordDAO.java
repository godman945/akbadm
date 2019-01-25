package com.pchome.akbadm.db.dao.retrieve;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;

public interface IAdmRetrieveRecordDAO extends IBaseDAO<AdmRetrieveRecord, Integer>{

	public List<AdmRetrieveRecord> findRetrieveRecord(Map<String, String> conditionMap) throws Exception;
}
