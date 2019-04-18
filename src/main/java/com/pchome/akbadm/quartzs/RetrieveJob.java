package com.pchome.akbadm.quartzs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmFeedbackRecord;
import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.AdmRetrieveRecord;
import com.pchome.akbadm.db.service.feedback.IAdmFeedbackRecordService;
import com.pchome.akbadm.db.service.free.IAdmFreeActionService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.retrieve.IAdmRetrieveRecordService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.enumerate.retrieve.EnumRetrieveRecordType;

/**
 * 檢查廣告金及回饋金是否到期，到期的寫入 adm_retrieve_record 待回收
 */
@Transactional
public class RetrieveJob {

	private Logger log = LogManager.getRootLogger();

	private IAdmFreeActionService admFreeActionService; //廣告金
	private IAdmFeedbackRecordService feedbackRecordService; //回饋金
	private IAdmRecognizeRecordService admRecognizeRecordService; //攤提
	private IAdmRetrieveRecordService admRetrieveRecordService; //回收

	public void  process() {

		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		try {

			//part1. 查詢到期且尚未回收的廣告金 Gift
			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("inviledDate", df.format(now));
			conditionMap.put("retrievedFlag", "n");

			List<AdmFreeAction> admFreeActionList = admFreeActionService.findFreeAction(conditionMap);
			log.info(">>> admFreeActionList.size() = " + admFreeActionList.size());

			for (int i=0; i<admFreeActionList.size(); i++) {
				AdmFreeAction admFreeAction = admFreeActionList.get(i);
				String actionId = admFreeAction.getActionId(); //adm_recognize_record.recognize_order_id
				log.info(">>> actionId = " + actionId);
				int giftMoney = (int) admFreeAction.getGiftMoney();
				log.info(">>> giftMoney = " + giftMoney);

				log.info(">>> admFreeAction.getAdmFreeRecords().size() = " + admFreeAction.getAdmFreeRecords().size());
				Iterator<AdmFreeRecord> it = admFreeAction.getAdmFreeRecords().iterator();

				while (it.hasNext()) {
					AdmFreeRecord admFreeRecord = it.next();
					String pfpCustomerInfoId = admFreeRecord.getCustomerInfoId();
					log.info(">>> pfpCustomerInfoId = " + pfpCustomerInfoId);

					//到 adm_recognize_record 計算要回收的金額
					List<AdmRecognizeRecord> admRecognizeRecordList = admRecognizeRecordService.findRecognizeRecords(pfpCustomerInfoId, actionId, EnumOrderType.GIFT);
					log.info(">>> admRecognizeRecordList.size() = " + admRecognizeRecordList.size());

					int spendMoney = 0;
					for (int k=0; k<admRecognizeRecordList.size(); k++) {
						AdmRecognizeRecord admRecognizeRecord = admRecognizeRecordList.get(k);
						log.info(">>> recognizeRecordId = " + admRecognizeRecord.getRecognizeRecordId());
						
						Iterator<AdmRecognizeDetail> it2 = admRecognizeRecord.getAdmRecognizeDetails().iterator();
						while (it2.hasNext()) {
							AdmRecognizeDetail admRecognizeDetail = it2.next();
							log.info(">>> recordDetailId = " + admRecognizeDetail.getRecordDetailId());
							spendMoney += admRecognizeDetail.getCostPrice();
						}
					}
					log.info(">>> spendMoney = " + spendMoney);

					int retrieveMoney = giftMoney - spendMoney;
					log.info(">>> retrieveMoney = " + retrieveMoney);

					if(retrieveMoney > 0){
						//寫入回收資料 adm_retrieve_record
						AdmRetrieveRecord admRetrieveRecord = new AdmRetrieveRecord();
						admRetrieveRecord.setActionId(actionId);
						admRetrieveRecord.setRecordType(EnumRetrieveRecordType.GIFT.getCode());
						admRetrieveRecord.setPfpCustomerInfoId(pfpCustomerInfoId);
						admRetrieveRecord.setGiftDate(admFreeRecord.getRecordDate());
						admRetrieveRecord.setGiftMoney(giftMoney);
						admRetrieveRecord.setSpendMoney(spendMoney);
						admRetrieveRecord.setRetrieveMoney(retrieveMoney);
						admRetrieveRecord.setInviledDate(admFreeAction.getInviledDate());
						admRetrieveRecord.setRecordDate(now);
						admRetrieveRecord.setCreateDate(now);
						
						admRetrieveRecordService.save(admRetrieveRecord);	
					}
				}
			}

			//part2. 查詢到期且尚未回收的回饋金 Feedback
			List<AdmFeedbackRecord> admFeedbackRecordList = feedbackRecordService.findFeedbackRecord(conditionMap);
			log.info(">>> admFeedbackRecordList.size() = " + admFeedbackRecordList.size());

			for (int i=0; i<admFeedbackRecordList.size(); i++) {
				AdmFeedbackRecord admFeedbackRecord = admFeedbackRecordList.get(i);
				String pfpCustomerInfoId = admFeedbackRecord.getPfpCustomerInfo().getCustomerInfoId();
				log.info(">>> pfpCustomerInfoId = " + pfpCustomerInfoId);
				String feedbackRecordId = admFeedbackRecord.getFeedbackRecordId(); //adm_recognize_record.recognize_order_id
				log.info(">>> feedbackRecordId = " + feedbackRecordId);
				int feedbackMoney = (int) admFeedbackRecord.getFeedbackMoney();
				log.info(">>> feedbackMoney = " + feedbackMoney);

				//到 adm_recognize_record 計算要回收的金額
				List<AdmRecognizeRecord> admRecognizeRecordList = admRecognizeRecordService.findRecognizeRecords(pfpCustomerInfoId, feedbackRecordId, EnumOrderType.FEEDBACK);
				log.info(">>> admRecognizeRecordList.size() = " + admRecognizeRecordList.size());

				int spendMoney = 0;
				for (int k=0; k<admRecognizeRecordList.size(); k++) {
					AdmRecognizeRecord admRecognizeRecord = admRecognizeRecordList.get(k);
					log.info(">>> recognizeRecordId = " + admRecognizeRecord.getRecognizeRecordId());
					
					Iterator<AdmRecognizeDetail> it2 = admRecognizeRecord.getAdmRecognizeDetails().iterator();
					while (it2.hasNext()) {
						AdmRecognizeDetail admRecognizeDetail = it2.next();
						log.info(">>> recordDetailId = " + admRecognizeDetail.getRecordDetailId());
						spendMoney += admRecognizeDetail.getCostPrice();
					}
				}
				log.info(">>> spendMoney = " + spendMoney);

				int retrieveMoney = feedbackMoney - spendMoney;
				log.info(">>> retrieveMoney = " + retrieveMoney);

				if(retrieveMoney > 0){
					//寫入回收資料 adm_retrieve_record
					AdmRetrieveRecord admRetrieveRecord = new AdmRetrieveRecord();
					admRetrieveRecord.setActionId(feedbackRecordId);
					admRetrieveRecord.setRecordType(EnumRetrieveRecordType.FEEDBACK.getCode());
					admRetrieveRecord.setPfpCustomerInfoId(pfpCustomerInfoId);
					admRetrieveRecord.setGiftDate(admFeedbackRecord.getGiftDate());
					admRetrieveRecord.setGiftMoney(feedbackMoney);
					admRetrieveRecord.setSpendMoney(spendMoney);
					admRetrieveRecord.setRetrieveMoney(retrieveMoney);
					admRetrieveRecord.setInviledDate(admFeedbackRecord.getInviledDate());
					admRetrieveRecord.setRecordDate(now);
					admRetrieveRecord.setCreateDate(now);
					
					admRetrieveRecordService.save(admRetrieveRecord);		
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void setFeedbackRecordService(IAdmFeedbackRecordService feedbackRecordService) {
		this.feedbackRecordService = feedbackRecordService;
	}

	public void setAdmFreeActionService(IAdmFreeActionService admFreeActionService) {
		this.admFreeActionService = admFreeActionService;
	}

	public void setAdmRecognizeRecordService(IAdmRecognizeRecordService admRecognizeRecordService) {
		this.admRecognizeRecordService = admRecognizeRecordService;
	}

	public void setAdmRetrieveRecordService(IAdmRetrieveRecordService admRetrieveRecordService) {
		this.admRetrieveRecordService = admRetrieveRecordService;
	}

    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        RetrieveJob job = context.getBean(RetrieveJob.class);
        job.process();
    }
}
