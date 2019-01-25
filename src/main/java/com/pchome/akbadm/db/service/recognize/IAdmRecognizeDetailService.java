package com.pchome.akbadm.db.service.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.RecognizeExceptRefundVO;
import com.pchome.enumerate.recognize.EnumOrderType;

public interface IAdmRecognizeDetailService extends IBaseService<AdmRecognizeDetail,String>{
	
	/**
	 * 建立攤提明細
	 */
	public void createRecognizeDetail(PfdUserAdAccountRef ref, AdmRecognizeRecord admRecognizeRecord, float costPrice, float tax, String orderType, Date costDate, float recordRemain, float taxRemain);
	
	public List<AdmRecognizeDetail> findRecognizeDetailToBilling(String startDate) throws Exception;
	
	public List<AdmRecognizeDetail> findBeforeDateRecognizeDetail(String customerInfoId, String startDate, String payType);
	
	public Integer deleteRecordAfterDate(String startDate);
	
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date); 

	public List<AdmRecognizeDetail> searchRecognizeReport(Date date, List<Object> pfpCustomerInfoId); 

	public List<AdmRecognizeDetail> findRecognizeDetail(String customerInfoId, Date date, EnumOrderType enumOrderType);
	
	public List<AdmRecognizeDetail> findRecognizeDetail(String recordId, String customerInfoId, EnumOrderType enumOrderType);
	
	/**
	 * 經銷商實際廣告費用
	 */
	public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, String startDate, String endDate, String orderType);
	
	/**
	 * 廣告商實際廣告費用
	 */
	public float findPfpCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, String startDate, String endDate, String orderType);
	
	public List<AdmRecognizeDetail> findAdmRecognizeDetails(String spendDate);
	
	// Pfp總廣告點擊費用(去掉經銷商)
	public float findPfpAdPvClkPrice(String pfpId, String orderType, Date startDate, Date endDate);
	// Pfp總廣告點擊費用(只算禮金+回饋金)(去掉經銷商)
	public float findPfpAdPvClkPriceOrderTypeForFree(String pfpId, Date startDate, Date endDate);
	// Pfd總廣告點擊費用(只撈經銷商)
	public float findPfdAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate);
	// Pfd總廣告點擊費用(只算禮金+回饋金)
	public float findPfdAdPvClkPriceOrderTypeForFree(String pfdId, Date startDate, Date endDate);
	// Pfb總廣告點擊費用(全部)
	public float findPfbAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate);
	// Pfb總廣告點擊費用(只算禮金+回饋金)
	public float findPfbAdPvClkPriceOrderTypeForFree(String pfdId, Date startDate, Date endDate);
	
	public List<RecognizeExceptRefundVO> findRecognizeDetailExceptRefundToBilling(String startDate) throws Exception;
	
}
