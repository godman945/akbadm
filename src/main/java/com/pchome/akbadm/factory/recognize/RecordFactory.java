package com.pchome.akbadm.factory.recognize;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.enumerate.recognize.EnumOrderType;

/**
 * 記錄每筆交易金額
 * 1. 預付儲值
 * 2. 預付禮金贈送
 * 3. 預付回饋金贈送
 * 4. 後付P幣加值
 */
public class RecordFactory {

	protected Logger log = LogManager.getRootLogger();
	
	private ARecord recordSave;	
	private ARecord recordGift;
	private ARecord recordFeedback;
	private ARecord recordVirtual;
	
	public ARecord get(EnumOrderType enumOrderType){
		
		ARecord recordItem = null;
		
		switch(enumOrderType){
		
		case SAVE:
			recordItem = recordSave;
			break;
		case GIFT:
			recordItem = recordGift;
			break;
		case FEEDBACK:
			recordItem = recordFeedback;
			break;
		case VIRTUAL:
			recordItem = recordVirtual;
			break;
		default:
			break;
		}
		
		return recordItem;
	}

	public void setRecordSave(ARecord recordSave) {
		this.recordSave = recordSave;
	}

	public void setRecordGift(ARecord recordGift) {
		this.recordGift = recordGift;
	}

	public void setRecordFeedback(ARecord recordFeedback) {
		this.recordFeedback = recordFeedback;
	}

	public void setRecordVirtual(ARecord recordVirtual) {
		this.recordVirtual = recordVirtual;
	}
	
}
