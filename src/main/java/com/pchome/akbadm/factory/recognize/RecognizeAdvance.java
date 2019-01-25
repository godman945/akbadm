package com.pchome.akbadm.factory.recognize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpRefundOrder;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeRecordService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;

public class RecognizeAdvance extends ARecognize{

	@Override
	protected List<AdmRecognizeDetail> findRecognizeDetail(String pfpCustomerInfoId, String startDate) {
//		log.info(" Advance: ");
		return super.admRecognizeDetailService.findBeforeDateRecognizeDetail(pfpCustomerInfoId, startDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	}

	@Override
	protected List<AdmRecognizeRecord> findRecognizeRecord(String pfpCustomerInfoId, String startDate) {
		return super.admRecognizeRecordService.findRecognizeRecordAfterDate(pfpCustomerInfoId, startDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	}

	@Override
	protected float findAdClkPrice(String customerInfoId, Date startDate, Date endDate) {
		return super.adActionReportService.findAdClkPrice(customerInfoId, startDate, endDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	}

	@Override
	protected List<AdmRecognizeRecord> findAvailableRecord(String pfpCustomerInfoId) {
		return super.admRecognizeRecordService.findAvailableRecord(pfpCustomerInfoId, EnumPfdAccountPayType.ADVANCE.getPayType());
	}

	@Override
	protected void recognizeRefund(PfpCustomerInfo customerInfo, Date spendDate) {
		List<PfpRefundOrder> advanceRefundOrder = new ArrayList<PfpRefundOrder>();
		try {
			advanceRefundOrder = this.findAdvanceRefundOrder(customerInfo.getCustomerInfoId(), spendDate);
		
			if( (!advanceRefundOrder.isEmpty()) && (advanceRefundOrder.size() != 0) ){
				PfdUserAdAccountRef ref = null;
				String pfpCustomerInfoId = customerInfo.getCustomerInfoId();
				
				float detailCostPrice;
				float detailCostTax;
				
				for (PfpRefundOrder pfpRefundOrder : advanceRefundOrder) {
					// 攤提交易記錄
					AdmRecognizeRecord admRecognizeRecord = admRecognizeRecordService.findRecognizeRecords(pfpCustomerInfoId, pfpRefundOrder.getOrderId(), EnumOrderType.SAVE).get(0);
					
					detailCostPrice = admRecognizeRecord.getOrderRemain();
					detailCostTax = admRecognizeRecord.getTaxRemain();
					
					admRecognizeRecord.setOrderRemain(0);
					admRecognizeRecord.setTaxRemain(0);
					admRecognizeRecord.setOrderRemainZero("Y");
					admRecognizeRecord.setUpdateDate(new Date());
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
																	detailCostPrice,
																	detailCostTax,
																	admRecognizeRecord.getOrderType(),
																	spendDate,
																	admRecognizeRecord.getOrderRemain(),
																	admRecognizeRecord.getTaxRemain());
		
				}
			}
		} catch (Exception e) {
			log.error("Error recognizeAdvanceRefund : "+ e);
		}
	}
	
	private List<PfpRefundOrder> findAdvanceRefundOrder(String customerInfoId, Date spendDate) throws Exception  { 
		return pfpRefundOrderService.findAdvanceRefundOrder(customerInfoId, spendDate);
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
