package com.pchome.akbadm.db.dao.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;

public interface IAdmRecognizeDetailDAO extends IBaseDAO<AdmRecognizeDetail, String>{
	
	public List<AdmRecognizeDetail> findRecognizeDetail(Date date) throws Exception;
	
	//public List<AdmRecognizeDetail> findBeforeCostDateRecognizeDetail(String customerInfoId, Date date);
	
	public List<AdmRecognizeDetail> findAdvanceRecognizeDetail(String customerInfoId, Date startDate, String payType);
	
	public List<AdmRecognizeDetail> findLaterRecognizeDetail(String customerInfoId, Date startDate, String payType);
	
	public Integer deleteRecordAfterDate(Date date);
	
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date); 
	
	public List<AdmRecognizeDetail> searchRecognizeReport(Date date, List<Object> pfpCustomerInfoId); 
	
	public List<AdmRecognizeDetail> findRecognizeDetail(String customerInfoId, Date date, String orderType);
	
	public List<AdmRecognizeDetail> findRecognizeDetail(String recordId, String customerInfoId, String orderType);
	
	/**
	 * 經銷商實際廣告費用
	 */
	public float findPfdCustomerInfoAdClickCost(String pfdCustomerInfoId, Date startDate, Date endDate, String orderType);

	/**
	 * 廣告商實際廣告費用
	 */
	public float findPfpCustomerInfoAdClickCost(String pfdCustomerInfoId, String pfpCustomerInfoId, Date startDate, Date endDate, String orderType);
	
	public List<AdmRecognizeDetail> findAdmRecognizeDetails(Date spendDate);
	
	public List<Object[]> findRecognizePrice(Date spendDate);
	
	public List<Object[]> findRecognizePriceBeforeDate(Date spendDate);
	
	// Pfp總廣告點擊費用
	public float findPfpRecognizeDetail(String pfpId, String orderType, Date startDate, Date endDate);
	// Pfp總廣告點擊費用(只算禮金+回饋金)
	public float findPfpRecognizeDetailForFree(String pfpId, Date startDate, Date endDate);
	// Pfd總廣告點擊費用
	public float findPfdAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate);
	// Pfd總廣告點擊費用(只算禮金+回饋金)
	public float findPfdRecognizeDetailForFree(String pfdId, Date startDate, Date endDate);
	// Pfb總廣告點擊費用
	public float findPfbAdPvClkPrice(String pfdId, String orderType, Date startDate, Date endDate);
	//Pfb總廣告點擊費用(只算禮金+回饋金)
	public float findPfbRecognizeDetailForFree(String pfdId, Date startDate, Date endDate);
	
	public List<AdmRecognizeDetail> findThisDayAdmRecognizeDetail(String recognizeRecordId, String pfpCustomerInfoId, Date costDate, String orderType);
	
	public List<Object> findRecognizeDetailExceptRefund(String date) throws Exception;
}
