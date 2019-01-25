package com.pchome.akbadm.db.dao.recognize;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.vo.AdActionDalilyReportVO;

public interface IAdmRecognizeRecordDAO extends IBaseDAO<AdmRecognizeRecord, String>{

	public List<AdmRecognizeRecord> findAdvanceRecognizeRecord(String customerInfoId);
	
	public List<AdmRecognizeRecord> findLaterRecognizeRecord(String customerInfoId);
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId);
	
	public List<AdmRecognizeRecord> findAdvanceRecognizeRecord(String customerInfoId, Date startDate);
	
	public List<AdmRecognizeRecord> findLaterRecognizeRecord(String customerInfoId, Date startDate);
	
	public List<AdmRecognizeRecord> findAfterSaveDateRecognizeRecord(String customerInfoId);

	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId,	String recognizeOrderId, String orderType);
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId,	String recognizeOrderId, String orderType, int orderPrice, Date saveDate);

	public List<AdmRecognizeRecord> findRecognizeRecords(Date saveDate);
	
	public List<Object[]> findRecognizeRemain(Date saveDate);
	
	public List<Object[]> findPfpRemain(Date saveDate);
	
	/**
	 * 在統計計算贈送廣告金之前，先刪除 "新戶好事成金(FRA201402240001)"、"續攤享禮金(FRA201402240002)"
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	public Integer deleteRecognizeRecordBySaveDate(Date saveDate, String customerInfoId) throws Exception;
	
	/**
	 * 刪除所有帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000() throws Exception;

	/**
	 * 依日期刪除帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000(Date saveDate) throws Exception;
	
	/**
	 * 依日期、帳號刪除帳戶未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000(Date saveDate, String customerInfoId) throws Exception;

	//public Integer deleteRecordAfterDate(Date date);

	public List<AdmRecognizeRecord> findRecognizeRecords(Date sDate, Date eDate, String pfpCustInfoId) throws Exception;
	
	public TreeMap<String, List<AdmRecognizeRecord>> getRecognizeRecordByCustomerInfoIdList(String startDate, String endDate, List<String> pfpAdCustomerInfoIds, List<String> orderTypes) throws Exception;
	
	public List<AdmRecognizeRecord> findRecognizeRecords(Date sDate, Date eDate, String pfpCustomerInfoId, String OrderType);
	
	public Integer deleteAdmRecognizeRecord(String orderId);

	public List<AdmRecognizeRecord> findRecognizeRecords(Map<String, Object> conditionMap) throws Exception;
	
	public Integer deleteRecordAfterDate(Date startDate);
	
	//取得每日廣告花費日誌
	public List<AdActionDalilyReportVO> getAdAdmRecognizeDalilyReport() throws Exception;
}
