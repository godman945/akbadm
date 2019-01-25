package com.pchome.akbadm.db.service.recognize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.pchome.akbadm.db.dao.recognize.IAdmRecognizeDetailDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.RecognizeExceptRefundVO;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class AdmRecognizeDetailService extends BaseService<AdmRecognizeDetail,String> implements IAdmRecognizeDetailService{

	public void createRecognizeDetail(PfdUserAdAccountRef ref, AdmRecognizeRecord admRecognizeRecord, 
			float costPrice, float tax, String orderType, Date costDate, float recordRemain, float taxRemain){
		
		List<AdmRecognizeDetail> detailList = ((IAdmRecognizeDetailDAO)dao).findThisDayAdmRecognizeDetail(admRecognizeRecord.getRecognizeRecordId(), admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoId(), costDate, orderType);
		
		if(!detailList.isEmpty()){
			AdmRecognizeDetail recognizeDetail = detailList.get(0);
			
			float totalCostOrice = recognizeDetail.getCostPrice() + costPrice;
			float totalTax = recognizeDetail.getTax() + tax;
			
			recognizeDetail.setCostPrice(totalCostOrice);
			recognizeDetail.setTax(totalTax);
			recognizeDetail.setRecordRemain(recordRemain);
			recognizeDetail.setTaxRemain(taxRemain);
			recognizeDetail.setUpdateDate(new Date());
			
			((IAdmRecognizeDetailDAO)dao).saveOrUpdate(recognizeDetail);
		} else {
			AdmRecognizeDetail recognizeDetail = new AdmRecognizeDetail();
			
			if(ref != null){
				recognizeDetail.setPfdCustomerInfoId(ref.getPfdCustomerInfo().getCustomerInfoId());
				recognizeDetail.setPfdUserId(ref.getPfdUser().getUserId());
			}
			
			recognizeDetail.setCustomerInfoId(admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoId());		
			recognizeDetail.setAdmRecognizeRecord(admRecognizeRecord);
			//recognizeDetail.setPayType(ref.getPfpPayType());
			recognizeDetail.setCostPrice(costPrice);
			recognizeDetail.setTax(tax);
			recognizeDetail.setOrderType(orderType);
			recognizeDetail.setCostDate(costDate);
			recognizeDetail.setRecordRemain(recordRemain);
			recognizeDetail.setTaxRemain(taxRemain);
			
			Date today = new Date();
			recognizeDetail.setUpdateDate(today);
			recognizeDetail.setCreateDate(today);
			
			((IAdmRecognizeDetailDAO)dao).saveOrUpdate(recognizeDetail);
		}
		
	}
	
	public List<AdmRecognizeDetail> findRecognizeDetailToBilling(String startDate) throws Exception{
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IAdmRecognizeDetailDAO)dao).findRecognizeDetail(date);
	}
	
	public List<AdmRecognizeDetail> findBeforeDateRecognizeDetail(String customerInfoId, String startDate, String payType) {
		
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		
		if(EnumPfdAccountPayType.ADVANCE.getPayType().equals(payType)){
			return ((IAdmRecognizeDetailDAO)dao).findAdvanceRecognizeDetail(customerInfoId, date, payType);
		}else{
			return ((IAdmRecognizeDetailDAO)dao).findLaterRecognizeDetail(customerInfoId, date, payType);
		}
	}
	
	public Integer deleteRecordAfterDate(String startDate){
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		return ((IAdmRecognizeDetailDAO)dao).deleteRecordAfterDate(date);
	}
	
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date){		
		
		return ((IAdmRecognizeDetailDAO)dao).searchRecognizeReport(date);
	}
	
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date, List<Object> pfpCustomerInfoId){		
		
		return ((IAdmRecognizeDetailDAO)dao).searchRecognizeReport(date, pfpCustomerInfoId);
	}
	
	public List<AdmRecognizeDetail> findRecognizeDetail(String customerInfoId, Date date, EnumOrderType enumOrderType){
		
		return ((IAdmRecognizeDetailDAO)dao).findRecognizeDetail(customerInfoId, date, enumOrderType.getTypeTag());
	}
	
	public List<AdmRecognizeDetail> findRecognizeDetail(String recordId, String customerInfoId, EnumOrderType enumOrderType){
		
		return ((IAdmRecognizeDetailDAO)dao).findRecognizeDetail(recordId, customerInfoId, enumOrderType.getTypeTag());
	}
	
	public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String startDate, String endDate, String orderType) {
		return ((IAdmRecognizeDetailDAO)dao).findPfdCustomerInfoAdClickCost(pfdCustomerInfoId, 
				DateValueUtil.getInstance().stringToDate(startDate), 
				DateValueUtil.getInstance().stringToDate(endDate), 
				orderType);
	}

	public float findPfpCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String orderType) {
		return ((IAdmRecognizeDetailDAO)dao).findPfpCustomerInfoAdClickCost(pfdCustomerInfoId, 
				pfpCustomerInfoId,
				DateValueUtil.getInstance().stringToDate(startDate), 
				DateValueUtil.getInstance().stringToDate(endDate), 
				orderType);
	}
	
	public List<AdmRecognizeDetail> findAdmRecognizeDetails(String spendDate) {
		return ((IAdmRecognizeDetailDAO) dao).findAdmRecognizeDetails(DateValueUtil.getInstance().stringToDate(spendDate));
	}
	
	public float findPfpAdPvClkPrice(String pfpId, String orderType, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfpRecognizeDetail(pfpId, orderType, startDate, endDate);
	}
	
	public float findPfpAdPvClkPriceOrderTypeForFree(String pfpId, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfpRecognizeDetailForFree(pfpId, startDate, endDate);
	}
	
	public float findPfdAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfdAdPvClkPrice(pfdId, orderType, startDate, endDate);
	}
	
	public float findPfdAdPvClkPriceOrderTypeForFree(String pfdId, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfdRecognizeDetailForFree(pfdId, startDate, endDate);
	}
	
	public float findPfbAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfbAdPvClkPrice(pfdId, orderType, startDate, endDate);
	}
	
	public float findPfbAdPvClkPriceOrderTypeForFree(String pfdId, Date startDate, Date endDate){
		return ((IAdmRecognizeDetailDAO) dao).findPfbRecognizeDetailForFree(pfdId, startDate, endDate);
	}
	
	
	public List<RecognizeExceptRefundVO> findRecognizeDetailExceptRefundToBilling(String startDate) throws Exception{
//		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		
		List<RecognizeExceptRefundVO> vos = null;
		
		List<Object> records = ((IAdmRecognizeDetailDAO)dao).findRecognizeDetailExceptRefund(startDate);
		
		log.info(" records: "+records.size());
		
		if(records != null && !records.isEmpty()){
			RecognizeExceptRefundVO lastVo = null;
			vos = new ArrayList<RecognizeExceptRefundVO>();
			
			for(Object object:records){	
				if(object != null){
					lastVo = new RecognizeExceptRefundVO();
					Object[] ob = (Object[])object;
					
					lastVo.setRecognizeOrderId(ob[0].toString());
					lastVo.setCostDate(ob[1].toString());
					lastVo.setCostPrice((float)ob[2]);
					lastVo.setTax((float)ob[3]);
					lastVo.setCustomerInfoId(ob[4].toString());
				
					vos.add(lastVo);
				}
			}
			
			log.info(" vos: "+vos.size());
		}
		
		return vos;
	}
	
	
	
}
