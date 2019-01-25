package com.pchome.akbadm.db.dao.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;

public interface IAdmFreeRecordDAO extends IBaseDAO<AdmFreeRecord, String>{
	
	public List<AdmFreeRecord> findRecords(String customerInfoId, String freeActionId);
	
	public List<AdmFreeRecord> findFreeActionRecord(String customerInfoId, Date date);
	
	public Integer deleteRecordAfterDate(Date date);
	
	public Integer deleteRecordAfterDate(Date date, String customerInfoId, String actionId);
	
}
