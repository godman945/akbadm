package com.pchome.akbadm.factory.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdVirtualRecord;
import com.pchome.akbadm.db.service.customerInfo.IPfdVirtualRecordService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.sequence.EnumSequenceTableName;

/**
 * 後付P幣加值記錄
 */
public class RecordVirtual extends ARecord{

	private IPfdVirtualRecordService pfdVirtualRecordService;
	
	@Override
	public void creatRecognizeRecord(String customerInfoId, Date saveDate) {
		
		List<PfdVirtualRecord> virtualRecord = pfdVirtualRecordService.findPfdVirtualRecord(customerInfoId, saveDate);
		
		for(PfdVirtualRecord record:virtualRecord){

			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_RECOGNIZE_RECORD);
			
			recognizeRecordService.createAdmRecognizeRecord(recordId,
															customerInfoId,
															record.getAddMoney(), 
															0,
															EnumOrderType.VIRTUAL,
															record.getRecordId(), 
															saveDate,
															null);
		}
		
	}

	public void setSequenceService(ISequenceService sequenceService) {
		super.sequenceService = sequenceService;
	}

	public void setRecognizeRecordService(
			IAdmRecognizeRecordService recognizeRecordService) {
		super.recognizeRecordService = recognizeRecordService;
	}
	
	public void setPfdVirtualRecordService(IPfdVirtualRecordService pfdVirtualRecordService) {
		this.pfdVirtualRecordService = pfdVirtualRecordService;
	}
}
