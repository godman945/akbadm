package com.pchome.akbadm.factory.recognize;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;

/**
 * 每筆交易記錄抽象類別
 */
public abstract class ARecord {

	protected Log log = LogFactory.getLog(this.getClass());
	
	protected ISequenceService sequenceService;
	protected IAdmRecognizeRecordService recognizeRecordService;
	
	// 新增每筆交易記錄
	protected abstract void creatRecognizeRecord(String customerInfoId, Date saveDate);
	
	public void createRecord(String customerInfoId, Date saveDate) {
		this.creatRecognizeRecord(customerInfoId, saveDate);
	}
}
