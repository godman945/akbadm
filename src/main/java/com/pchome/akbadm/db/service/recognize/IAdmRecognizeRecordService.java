package com.pchome.akbadm.db.service.recognize;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.AdmRecognizeRecordVO;
import com.pchome.akbadm.db.vo.AdActionDalilyReportVO;
import com.pchome.enumerate.recognize.EnumOrderType;

public interface IAdmRecognizeRecordService extends IBaseService<AdmRecognizeRecord,String>{
	
	public void createAdmRecognizeRecord(String recordId, String customerInfoId, float orderPrice, float tax, EnumOrderType enumOrderType, String orderId, Date date, String giftSno);
	
	public List<AdmRecognizeRecord> findAvailableRecord(String customerInfoId, String payType); 
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId);
	
	public List<AdmRecognizeRecord> findRecognizeRecordAfterDate(String customerInfoId, String startDate, String payType);
	
	public List<AdmRecognizeRecord> findAfterSaveDateRecognizeRecord(String customerInfoId);
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId, String recognizeOrderId, EnumOrderType enumOrderType);

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

	public Integer deleteRecordAfterDate(String startDate);

	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId, String recognizeOrderId, EnumOrderType enumOrderType, int orderPrice, Date saveDate);
	
	public List<AdmRecognizeRecordVO> findRecognizeRecords(String sDate, String eDate,
			String pfdCustomerInfoId, String payType) throws Exception;

	public List<AdmRecognizeRecord> findRecognizeRecords(String sDate, String eDate, String pfpCustomerInfoId, EnumOrderType enumOrderType);
	
	public Integer deleteAdmRecognizeRecord(String orderId);
	
	//取得每日廣告花費日誌
	public List<AdActionDalilyReportVO> getAdAdmRecognizeDalilyReport() throws Exception;
}
