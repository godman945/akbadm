package com.pchome.akbadm.factory.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.service.feedback.IAdmFeedbackRecordService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.sequence.EnumSequenceTableName;

/**
 * 預付回饋金贈送記錄
 */
public class RecordFeedback extends ARecord{

	private IAdmFeedbackRecordService feedbackRecordService;

	@Override
	public void creatRecognizeRecord(String customerInfoId, Date saveDate) {

//		log.info(" Feedback date: "+DateValueUtil.getInstance().dateToString(saveDate));
		List<AdmFeedbackRecord> records = feedbackRecordService.findFeedbackRecord(customerInfoId, saveDate);

//		log.info(" records: "+records);

		for(AdmFeedbackRecord record:records){

			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_RECOGNIZE_RECORD);

			recognizeRecordService.createAdmRecognizeRecord(recordId,
															customerInfoId,
															record.getFeedbackMoney(),
															0,
															EnumOrderType.FEEDBACK,
															record.getFeedbackRecordId(),
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

	public void setFeedbackRecordService(
			IAdmFeedbackRecordService feedbackRecordService) {
		this.feedbackRecordService = feedbackRecordService;
	}
}
