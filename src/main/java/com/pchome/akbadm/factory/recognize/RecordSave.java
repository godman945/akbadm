package com.pchome.akbadm.factory.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfpOrder;
import com.pchome.akbadm.db.service.order.IPfpOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.sequence.EnumSequenceTableName;

/**
 * 預付儲值記錄
 */
public class RecordSave extends ARecord{

	private IPfpOrderService orderService;
	
	@Override
	public void creatRecognizeRecord(String customerInfoId, Date saveDate) {
		
		List<PfpOrder> orders = orderService.successOrder(customerInfoId, saveDate);
		
		for(PfpOrder order:orders){
			
			String recordId = sequenceService.getId(EnumSequenceTableName.ADM_RECOGNIZE_RECORD);
			
			recognizeRecordService.createAdmRecognizeRecord(recordId,
															customerInfoId,
															order.getOrderPrice(), 
															order.getTax(),
															EnumOrderType.SAVE,
															order.getOrderId(), 
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

	public void setOrderService(IPfpOrderService orderService) {
		this.orderService = orderService;
	}
}
