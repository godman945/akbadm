package com.pchome.akbadm.factory.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class RecognizeLater extends ARecognize{

	@Override
	protected List<AdmRecognizeDetail> findRecognizeDetail(String pfpCustomerInfoId, String startDate) {
//		log.info(" Later: ");
		return super.admRecognizeDetailService.findBeforeDateRecognizeDetail(pfpCustomerInfoId, startDate, EnumPfdAccountPayType.LATER.getPayType());
	}

	@Override
	protected List<AdmRecognizeRecord> findRecognizeRecord(String pfpCustomerInfoId, String startDate) {
		return super.admRecognizeRecordService.findRecognizeRecordAfterDate(pfpCustomerInfoId, startDate, EnumPfdAccountPayType.LATER.getPayType());
	}

	@Override
	protected float findAdClkPrice(String customerInfoId, Date startDate, Date endDate) {
		return adActionReportService.findAdClkPrice(customerInfoId, startDate, endDate, EnumPfdAccountPayType.LATER.getPayType());
	}

	@Override
	protected List<AdmRecognizeRecord> findAvailableRecord(String pfpCustomerInfoId) {
		return super.admRecognizeRecordService.findAvailableRecord(pfpCustomerInfoId, EnumPfdAccountPayType.LATER.getPayType());
	}

	@Override
	protected void recognizeRefund(PfpCustomerInfo customerInfo, Date spendDate) {
		float refundPrice = this.findRefundPrice(customerInfo.getCustomerInfoId(), spendDate, spendDate); 
		if(refundPrice > 0){
			PfdUserAdAccountRef ref = null;
			String pfpCustomerInfoId = customerInfo.getCustomerInfoId();
	
			// 攤提交易記錄
			List<AdmRecognizeRecord> admRecognizeRecords = this.findAvailableRecord(pfpCustomerInfoId);
	
			// 攤提廣告費用
			float recognizeRefund = 0;
	
			for(AdmRecognizeRecord admRecognizeRecord:admRecognizeRecords){
				if(admRecognizeRecord.getOrderRemain() > refundPrice){
					recognizeRefund = refundPrice;
				}
				else{
					recognizeRefund =  admRecognizeRecord.getOrderRemain();
				}
	
				refundPrice -= admRecognizeRecord.getOrderRemain();
	
				if(refundPrice < 0){
					// 廣告費已攤提完畢
					admRecognizeRecord.setOrderRemain(Math.abs(refundPrice));
					admRecognizeRecord.setTaxRemain(0);
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
	
				// 攤提交易明細記錄
				admRecognizeDetailService.createRecognizeDetail(ref,
																admRecognizeRecord,
																recognizeRefund,
																0,
																admRecognizeRecord.getOrderType(),
																spendDate,
																admRecognizeRecord.getOrderRemain(),
																admRecognizeRecord.getTaxRemain());
				if(refundPrice <= 0){
					break;
				}
	
			}

		}
	}
	
	private float findRefundPrice(String customerInfoId, Date startDate, Date endDate) { 
		return pfpRefundOrderService.findRefundPrice(customerInfoId, startDate, endDate, EnumPfdAccountPayType.LATER.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
	}
	
	public void setAdmRecognizeRecordService(IAdmRecognizeRecordService admRecognizeRecordService) {
		super.admRecognizeRecordService = admRecognizeRecordService;
	}

	public void setAdmRecognizeDetailService(IAdmRecognizeDetailService admRecognizeDetailService) {
		super.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setAdActionReportService(IPfpAdActionReportService adActionReportService) {
		super.adActionReportService = adActionReportService;
	}

	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}
	
	public void setRecordFactory(RecordFactory recordFactory) {
		super.recordFactory = recordFactory;
	}
}
