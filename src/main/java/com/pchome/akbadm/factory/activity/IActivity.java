package com.pchome.akbadm.factory.activity;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmFreeAction;

public interface IActivity {
	
	public AdmFreeAction activityCondition(List<Object> conditions);

	public void addActionRecord(String customerInfoId, AdmFreeAction freeAction, Date recordDate);
	
	public void addRecognizeRecord(String customerInfoId, AdmFreeAction freeAction, Date recordDate);
	
	public void addBoard(String customerInfoId);
}
