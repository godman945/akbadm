package com.pchome.akbadm.factory.recognize;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;

/**
 * 每筆交易記錄抽象類別
 */
public abstract class ARecord {

	protected Logger log = LogManager.getRootLogger();
	
	protected ISequenceService sequenceService;
	protected IAdmRecognizeRecordService recognizeRecordService;
	
	// 新增每筆交易記錄
	protected abstract void creatRecognizeRecord(String customerInfoId, Date saveDate);
	
	public void createRecord(String customerInfoId, Date saveDate) {
		this.creatRecognizeRecord(customerInfoId, saveDate);
	}
}
