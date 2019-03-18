package com.pchome.akbadm.factory.recognize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.soft.util.DateValueUtil;

/**
 * 攤提流程
 * 1. 還原指定日期前一天剩餘金額
 * 2. 攤提廣告花費
 */
public abstract class ARecognize {

	protected Logger log = LogManager.getRootLogger();

	protected IAdmRecognizeRecordService admRecognizeRecordService;
	protected IAdmRecognizeDetailService admRecognizeDetailService;
	protected IPfpAdActionReportService adActionReportService;
	protected IPfpRefundOrderService pfpRefundOrderService;
	protected RecordFactory recordFactory;

	protected abstract List<AdmRecognizeDetail> findRecognizeDetail(String pfpCustomerInfoId, String startDate);

	protected abstract List<AdmRecognizeRecord> findRecognizeRecord(String pfpCustomerInfoId, String startDate);

	protected abstract float findAdClkPrice(String customerInfoId, Date startDate, Date endDate);

	protected abstract List<AdmRecognizeRecord> findAvailableRecord(String pfpCustomerInfoId);

	protected abstract void recognizeRefund(PfpCustomerInfo customerInfo, Date spendDate);
	
	public void recognizeProcess(PfpCustomerInfo customerInfo, String startDate){

		this.goBackRecordRemain2(customerInfo.getCustomerInfoId(), startDate);
		//this.goBackRecordRemain(customerInfo.getCustomerInfoId(), startDate);

		this.startRecognize(customerInfo, startDate);
	}

	/**
	 * 還原指定日期交易金額攤提前的餘額
	 * 1. 還原交易記錄
	 * 2. 還原交易記錄剩餘金額
	 * 3. 有問題，不能跑這個，不然攤提會錯
	 */
	private void goBackRecordRemain(String pfpCustomerInfoId, String startDate){
		Date today = new Date();

		// 指定日期前的交易記錄
		List<AdmRecognizeRecord> records = this.findRecognizeRecord(pfpCustomerInfoId, startDate);

		// 還原交易當時金額
		for(AdmRecognizeRecord record:records){
			float remain = record.getOrderPrice();

			record.setOrderRemain(remain);
			record.setOrderRemainZero("N");
			record.setUpdateDate(today);
			admRecognizeRecordService.saveOrUpdate(record);
		}

		// 指定日期前的攤提明細
		List<AdmRecognizeDetail> recognizeDetails = this.findRecognizeDetail(pfpCustomerInfoId, startDate);
		// 最後一天花費日期
		String costLastDate = null;

		if(!recognizeDetails.isEmpty()){

			costLastDate = DateValueUtil.getInstance().dateToString(recognizeDetails.get(0).getCostDate());

			// 還原交易記錄剩餘金額
			for(AdmRecognizeDetail detail:recognizeDetails){

//				log.info(" RecordId: "+detail.getAdmRecognizeRecord().getRecognizeRecordId());

				String costDate = DateValueUtil.getInstance().dateToString(detail.getCostDate());

				if(costLastDate.equals(costDate)){

					// 被攤提二筆以上，餘額會有歸零情況
					if(detail.getRecordRemain() <= 0){
						detail.getAdmRecognizeRecord().setOrderRemainZero("Y");
					}else{
						detail.getAdmRecognizeRecord().setOrderRemainZero("N");
					}
//					log.info(" RecordRemain: "+detail.getRecordRemain());

					detail.getAdmRecognizeRecord().setOrderRemain(detail.getRecordRemain());
					detail.getAdmRecognizeRecord().setTaxRemain(detail.getTaxRemain());
				}

				detail.getAdmRecognizeRecord().setUpdateDate(today);
				admRecognizeRecordService.saveOrUpdate(detail.getAdmRecognizeRecord());

			}
		}
	}

	/**
	 * 還原指定日期交易金額攤提前的餘額
	 * 1. 還原交易記錄
	 * 2. 還原交易記錄剩餘金額
	 */
	private void goBackRecordRemain2(String pfpCustomerInfoId, String startDate){
		Date today = new Date();
		
		// 指定日期前的交易記錄
		List<AdmRecognizeRecord> records = this.findRecognizeRecord(pfpCustomerInfoId, startDate);

		// 還原交易當時金額
		for(AdmRecognizeRecord record:records){
			
			List<AdmRecognizeDetail> detailList = new ArrayList<AdmRecognizeDetail>(record.getAdmRecognizeDetails());
			
			if(detailList.isEmpty()){	//若沒有交易明細且餘額不等於儲值金額，還原儲值記錄
				float remain = record.getOrderPrice();
				float orderRemain = record.getOrderRemain();
				
				if(remain != orderRemain){
					float tax = record.getTax();
					record.setOrderRemain(remain);
					record.setTaxRemain(tax);
					record.setOrderRemainZero("N");
					record.setUpdateDate(today);
					admRecognizeRecordService.saveOrUpdate(record);	
				}
			} else {	//取得最後一次交易明細還原儲值記錄餘額
				Date checkDay = DateValueUtil.getInstance().stringToDate("2010-01-01");
				AdmRecognizeDetail backDteail = new AdmRecognizeDetail();
				
				for(AdmRecognizeDetail admRecognizeDetail:detailList){
					if(admRecognizeDetail.getCostDate().getTime() >= checkDay.getTime()){
						backDteail = admRecognizeDetail;
						checkDay = admRecognizeDetail.getCostDate();
					}
				}
				
				if(backDteail.getRecordRemain() != record.getOrderRemain()){
					record.setOrderRemain(backDteail.getRecordRemain());
					record.setTaxRemain(backDteail.getTaxRemain());
					if(backDteail.getRecordRemain() <= 0){
						record.setOrderRemainZero("Y");
					} else {
						record.setOrderRemainZero("N");
					}
					record.setUpdateDate(today);
					admRecognizeRecordService.saveOrUpdate(record);	
				}
				
			}

		}
	}
	
	private void startRecognize(PfpCustomerInfo customerInfo, String startDate){
		// 攤提到昨天
		String endDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		//endDate = "2015-03-04";

		long diffyDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);

		String customerInfoId = customerInfo.getCustomerInfoId();
		Date spendDate = null;
		float adClkPrice = 0;

		// 逐天處理
		for (int i = 0; i < diffyDay; i++) {
			spendDate = DateValueUtil.getInstance().getDateForStartDateAddDay(startDate, i);

			this.createRecognizeRecord(customerInfoId, spendDate);

			//先扣當日退費
			this.recognizeRefund(customerInfo, spendDate);
			
			// 實際廣告費用(不含稅)
			adClkPrice = this.findAdClkPrice(customerInfoId, spendDate, spendDate);
			if(customerInfoId.equals("AC2013071700006")){
				log.info("ALEX adm_recognize_detail >>>>>>> : adClkPrice: "+customerInfoId+":"+adClkPrice);	
			}
			
			adClkPrice = (float)Math.floor(adClkPrice);
//			log.info(" spendDate: "+DateValueUtil.getInstance().dateToString(spendDate));

			// 模擬每日廣告費用
			//if(customerInfoId.equals("AC2013071700001")){
			//	log.info("AC2013071700001  --");
			//	adClkPrice = 563;
			//}

			// 攤提廣告費用
			if(adClkPrice > 0){
				this.recognizeSpendCost(customerInfo, adClkPrice, spendDate);
			}
			
		}
	}

	/**
	 * 新增攤提交易記錄
	 */
	private void createRecognizeRecord(String customerInfoId, Date saveDate){

		for(EnumOrderType orderType:EnumOrderType.values()){

			ARecord recordItem = recordFactory.get(orderType);

			if(recordItem != null){
				recordItem.createRecord(customerInfoId, saveDate);
			}

		}

	}

	/**
	 * 攤提交易金額
	 */
	private void recognizeSpendCost(PfpCustomerInfo customerInfo, float adClkPrice, Date spendDate) {

		PfdUserAdAccountRef ref = null;
		String pfpCustomerInfoId = customerInfo.getCustomerInfoId();

		// 攤提交易記錄
		List<AdmRecognizeRecord> admRecognizeRecords = this.findAvailableRecord(pfpCustomerInfoId);

//		log.info(" admRecognizeRecords: "+admRecognizeRecords.size());

		// 攤提廣告費用
		float recognizeCost = 0;
		float lastOrderRemain = 0;	// 上一筆訂單餘額(未稅)
		float lastTaxRemain = 0;	// 上一筆稅的餘額

		for(AdmRecognizeRecord admRecognizeRecord:admRecognizeRecords){
			if(admRecognizeRecord.getOrderRemain() > adClkPrice){
				recognizeCost = adClkPrice;
			}
			else{
				recognizeCost =  admRecognizeRecord.getOrderRemain();
			}
			lastOrderRemain = admRecognizeRecord.getOrderRemain();
			lastTaxRemain = admRecognizeRecord.getTaxRemain();

//			log.info(" befor recognizeCost: "+recognizeCost);
//			log.info(" record.orderRemain: "+lastOrderRemain);
//			log.info(" record.taxRemain: "+lastTaxRemain);

			// 含稅廣告費用
			float taxAdClkPrice = this.getTaxAdClkPrice(recognizeCost, lastOrderRemain, lastTaxRemain);
//			log.info(" taxAdClkPrice: "+taxAdClkPrice);
			// 廣告費用稅
			float tax = taxAdClkPrice - recognizeCost;
//			log.info(" tax: "+tax);

			adClkPrice -= admRecognizeRecord.getOrderRemain();
//			log.info(" adClkPrice: "+adClkPrice);

			if(adClkPrice < 0){
				// 廣告費已攤提完畢
				float taxRemain = admRecognizeRecord.getTaxRemain() - tax;
				admRecognizeRecord.setOrderRemain(Math.abs(adClkPrice));
				admRecognizeRecord.setTaxRemain(taxRemain);
				admRecognizeRecord.setUpdateDate(new Date());
			}
			else{
				admRecognizeRecord.setOrderRemain(0);
				admRecognizeRecord.setTaxRemain(0);
				admRecognizeRecord.setOrderRemainZero("Y");
				admRecognizeRecord.setUpdateDate(new Date());
			}

			admRecognizeRecordService.saveOrUpdate(admRecognizeRecord);

			// 取經銷商帳戶
			if(customerInfo.getPfdUserAdAccountRefs() != null &&
					!customerInfo.getPfdUserAdAccountRefs().isEmpty()){

				for(PfdUserAdAccountRef userAdAccountRef:customerInfo.getPfdUserAdAccountRefs()){
					ref = userAdAccountRef;
				}
			}

//			log.info(" after recognizeCost: "+recognizeCost);

			// 攤提交易明細記錄
			admRecognizeDetailService.createRecognizeDetail(ref,
															admRecognizeRecord,
															recognizeCost,
															tax,
															admRecognizeRecord.getOrderType(),
															spendDate,
															admRecognizeRecord.getOrderRemain(),
															admRecognizeRecord.getTaxRemain());
			if(adClkPrice <= 0){
				break;
			}

		}

	}

	/**
	 * 1. 檢查是否外加稅金
	 * 2. 公式：含稅廣告點擊費 = 含稅餘額 - ((未含稅餘額 - 未含廣告點擊費) * 1.05)
	 */
	private float getTaxAdClkPrice(float adClkPrice, float orderRemain, float taxRemain){
		float taxAdClkPrice = 0;

		if(taxRemain <= 0){
			taxAdClkPrice = adClkPrice;
		}else{
			taxAdClkPrice = (orderRemain+taxRemain) - Math.round((orderRemain - adClkPrice)*1.05);
		}

		return taxAdClkPrice;
	}
}
