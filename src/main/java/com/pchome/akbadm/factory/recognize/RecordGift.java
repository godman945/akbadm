package com.pchome.akbadm.factory.recognize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmFreeGift;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.service.free.IAdmFreeGiftService;
import com.pchome.akbadm.db.service.free.IAdmFreeRecordService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.sequence.EnumSequenceTableName;

/**
 * 預付禮金贈送記錄
 */
public class RecordGift extends ARecord{

	private IAdmFreeRecordService admFreeRecordService;
	private IAdmFreeGiftService admFreeGiftService;

	@Override
	public void creatRecognizeRecord(String customerInfoId, Date saveDate) {

		List<AdmFreeRecord> records = admFreeRecordService.findFreeActionRecord(customerInfoId, saveDate);

		for(AdmFreeRecord record:records){

			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_RECOGNIZE_RECORD);
			String giftSno = null;
			String freeActionId = record.getAdmFreeAction().getActionId();
			String shared = record.getAdmFreeAction().getShared();
			
			AdmFreeGift admFreeGift = null;
			
			if(StringUtils.equals(shared, "Y")){
				List<AdmFreeGift> list = new ArrayList<AdmFreeGift>(record.getAdmFreeAction().getAdmFreeGifts());
				if(list != null){
					admFreeGift = list.get(0);
				}
			} else {
				admFreeGift = admFreeGiftService.findAdmFreeGiftSno(freeActionId,customerInfoId, saveDate);
			}
			

			if(admFreeGift != null){
				giftSno = admFreeGift.getGiftSno();
			}

//			log.info(">>> giftSno: "+giftSno);

			recognizeRecordService.createAdmRecognizeRecord(recordId,
															customerInfoId,
															record.getAdmFreeAction().getGiftMoney(),
															0,
															EnumOrderType.GIFT,
															record.getAdmFreeAction().getActionId(),
															saveDate,
															giftSno);
		}

	}

	public void setSequenceService(ISequenceService sequenceService) {
		super.sequenceService = sequenceService;
	}

	public void setRecognizeRecordService(
			IAdmRecognizeRecordService recognizeRecordService) {
		super.recognizeRecordService = recognizeRecordService;
	}

	public void setAdmFreeRecordService(IAdmFreeRecordService admFreeRecordService) {
		this.admFreeRecordService = admFreeRecordService;
	}

	public void setAdmFreeGiftService(IAdmFreeGiftService admFreeGiftService) {
		this.admFreeGiftService = admFreeGiftService;
	}


}
