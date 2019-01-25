package com.pchome.akbadm.db.service.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.enumerate.factory.EnumActivityType;

public interface IAdmFreeRecordService extends IBaseService<AdmFreeRecord, String>{
	
	public List<AdmFreeRecord> findRecords(String customerInfoId, EnumActivityType enumActivityType);
	
	public void createFreeRecord(AdmFreeAction freeAction, String customerInfoId, Date recordDate);
	
	public List<AdmFreeRecord> findFreeActionRecord(String customerInfoId, Date date);
	
	public Integer deleteRecordAfterDate(String date);
	
	public Integer deleteRecordAfterDate(Date date, String customerInfoId, String actionId);
}
