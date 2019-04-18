package com.pchome.akbadm.factory.recognize;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

/**
 * 攤提計算
 * 1. 刪掉記錄
 * 2. 新增攤提交易記錄
 * 3. 開始攤提
 * 4. 更新攤提交易明細
 */
public class RecognizeProcess {

	protected Logger log = LogManager.getRootLogger();

	private IAdmRecognizeRecordService admRecognizeRecordService;
	private IAdmRecognizeDetailService admRecognizeDetailService;
	private ARecognize recognizeAdvance;
	private ARecognize recognizeLater;

	public void startProcess(List<PfpCustomerInfo> customerInfos, String startDate) {

		if(StringUtils.isBlank(startDate)){
			startDate = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		}

		// 刪掉記錄
		this.deleteRecord(startDate);

		// 開始攤提
		for(PfpCustomerInfo customerInfo:customerInfos){

			//if(this.isTest(customerInfo.getCustomerInfoId())){

				// 過濾要攤提帳戶
				if(customerInfo.getRecognize().equals("Y")){

					//log.info("=============================================================");
					//log.info(" customerInfoId: "+customerInfo.getCustomerInfoId());

					// 取帳戶付款狀態(預設是預付)
					String payType = null;
					for(PfdUserAdAccountRef ref:customerInfo.getPfdUserAdAccountRefs()){
						payType = ref.getPfpPayType();
					}

					if(EnumPfdAccountPayType.LATER.getPayType().equals(payType)){
						// 後付攤提
						log.info(" Later customerInfoId: "+customerInfo.getCustomerInfoId());
						recognizeLater.recognizeProcess(customerInfo, startDate);
					}else{
						// 預付攤提
						log.info(" Advance customerInfoId: "+customerInfo.getCustomerInfoId());
						recognizeAdvance.recognizeProcess(customerInfo, startDate);
					}

				}
			//}

		}
	}


	/**
	 * 1. 刪掉攤提交易記錄
	 * 2. 刪掉攤提明細
	 */
	private void deleteRecord(String startDate){

		// 刪掉攤提明細
		admRecognizeDetailService.deleteRecordAfterDate(startDate);

		// 刪掉攤提交易記錄
		admRecognizeRecordService.deleteRecordAfterDate(startDate);

	}

//	/**
//	 * 測試用
//	 * 1. 自行加帳戶判斷
//	 * 2. 測試完註解掉 false 打開 true
//	 */
//	private boolean isTest(String customerInfoId){
//
//		boolean isTest = false;
//		//boolean isTest = true;
//
//		// 測試帳戶用
//		if(!isTest){
//
//			if(customerInfoId.equals("AC2013071700001")){
//
//				log.info(" customerInfoId: "+customerInfoId);
//
//				isTest = true;
//			}
//		}
//
//		return isTest;
//	}

	public void setAdmRecognizeRecordService(IAdmRecognizeRecordService admRecognizeRecordService) {
		this.admRecognizeRecordService = admRecognizeRecordService;
	}

	public void setAdmRecognizeDetailService(IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setRecognizeAdvance(ARecognize recognizeAdvance) {
		this.recognizeAdvance = recognizeAdvance;
	}

	public void setRecognizeLater(ARecognize recognizeLater) {
		this.recognizeLater = recognizeLater;
	}

}
