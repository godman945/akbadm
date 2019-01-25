package com.pchome.akbadm.db.service.free;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.free.IAdmFreeRecordDAO;
import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.enumerate.factory.EnumActivityType;
import com.pchome.soft.util.DateValueUtil;

public class AdmFreeRecordService extends BaseService<AdmFreeRecord, String> implements IAdmFreeRecordService{
	
	public List<AdmFreeRecord> findRecords(String customerInfoId, EnumActivityType enumActivityType) {
		
		return ((IAdmFreeRecordDAO)dao).findRecords(customerInfoId, enumActivityType.getActivityId());
	}
	
	public void createFreeRecord(AdmFreeAction freeAction, String customerInfoId, Date recordDate) {
		
		AdmFreeRecord freeRecord = new AdmFreeRecord();
		
		Date today = new Date();
		freeRecord.setAdmFreeAction(freeAction);
		freeRecord.setCustomerInfoId(customerInfoId);
		freeRecord.setRecordDate(recordDate);
		freeRecord.setUpdateDate(today);
		freeRecord.setCreateDate(today);
		
		((IAdmFreeRecordDAO)dao).saveOrUpdate(freeRecord);		
	}
	
	public List<AdmFreeRecord> findFreeActionRecord(String customerInfoId, Date date) {
		return ((IAdmFreeRecordDAO)dao).findFreeActionRecord(customerInfoId, date);
	}
	
	public Integer deleteRecordAfterDate(String date) {
		Date deleteDate = DateValueUtil.getInstance().stringToDate(date);
		return ((IAdmFreeRecordDAO)dao).deleteRecordAfterDate(deleteDate);
	}
	
	public Integer deleteRecordAfterDate(Date date, String customerInfoId, String actionId) {
		return ((IAdmFreeRecordDAO)dao).deleteRecordAfterDate(date, customerInfoId, actionId);
	}
}
