package com.pchome.akbadm.db.service.recognize;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.dao.customerInfo.IPfdUserAdAccountRefDAO;
import com.pchome.akbadm.db.dao.recognize.IAdmRecognizeRecordDAO;
import com.pchome.akbadm.db.pojo.AdmRecognizeRecord;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.vo.AdmRecognizeRecordVO;
import com.pchome.akbadm.db.vo.AdActionDalilyReportVO;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class AdmRecognizeRecordService extends BaseService<AdmRecognizeRecord,String> implements IAdmRecognizeRecordService{

	private IPfpCustomerInfoService customerInfoService;
	private IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO;

	public void createAdmRecognizeRecord(String recordId, String customerInfoId, float orderPrice, float tax, EnumOrderType enumOrderType, String orderId, Date date, String giftSno) {
		
		AdmRecognizeRecord recognizeRecord = new AdmRecognizeRecord();
		PfpCustomerInfo customerInfo = customerInfoService.getCustomerInfo(customerInfoId);
		
		recognizeRecord.setRecognizeRecordId(recordId);
		recognizeRecord.setPfpCustomerInfo(customerInfo);
		recognizeRecord.setOrderPrice(orderPrice);
		recognizeRecord.setTax(tax);
		recognizeRecord.setOrderRemain(orderPrice);
		recognizeRecord.setTaxRemain(tax);
		recognizeRecord.setOrderRemainZero("N");
		recognizeRecord.setOrderType(enumOrderType.getTypeTag());
		recognizeRecord.setRecognizeOrderId(orderId);
		recognizeRecord.setSaveDate(date);
		
		if(StringUtils.isNotBlank(giftSno)){
			recognizeRecord.setGiftSno(giftSno);
		}
		
		Date today = new Date();
		recognizeRecord.setCreateDate(today);
		recognizeRecord.setUpdateDate(today);
		
		((IAdmRecognizeRecordDAO)dao).saveOrUpdate(recognizeRecord);
	}
	
	public List<AdmRecognizeRecord> findAvailableRecord(String customerInfoId, String payType){
		
		if(EnumPfdAccountPayType.ADVANCE.getPayType().equals(payType)){
			return ((IAdmRecognizeRecordDAO)dao).findAdvanceRecognizeRecord(customerInfoId);
		}else{
			return ((IAdmRecognizeRecordDAO)dao).findLaterRecognizeRecord(customerInfoId);
		}
	}
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId) {
		return ((IAdmRecognizeRecordDAO)dao).findRecognizeRecords(customerInfoId);
	}
	
	public List<AdmRecognizeRecord> findRecognizeRecordAfterDate(String customerInfoId, String startDate, String payType) {
		Date date = DateValueUtil.getInstance().stringToDate(startDate);
		
		if(EnumPfdAccountPayType.ADVANCE.getPayType().equals(payType)){
			return ((IAdmRecognizeRecordDAO)dao).findAdvanceRecognizeRecord(customerInfoId, date);
		}else{
			return ((IAdmRecognizeRecordDAO)dao).findLaterRecognizeRecord(customerInfoId, date);
		}
	}
	
	public List<AdmRecognizeRecord> findAfterSaveDateRecognizeRecord(String customerInfoId) {
		return ((IAdmRecognizeRecordDAO)dao).findAfterSaveDateRecognizeRecord(customerInfoId);
	}
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId, String recognizeOrderId, EnumOrderType enumOrderType){
		return ((IAdmRecognizeRecordDAO)dao).findRecognizeRecords(customerInfoId, recognizeOrderId, enumOrderType.getTypeTag());
	}

	public List<AdmRecognizeRecord> findRecognizeRecords(String customerInfoId, String recognizeOrderId, EnumOrderType enumOrderType, int orderPrice, Date saveDate){
		return ((IAdmRecognizeRecordDAO)dao).findRecognizeRecords(customerInfoId, recognizeOrderId, enumOrderType.getTypeTag(), orderPrice, saveDate);
	}
	
	public List<AdmRecognizeRecordVO> findRecognizeRecords(String sDate, String eDate,
			String pfdCustomerInfoId, String payType) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		NumberFormat nf = new DecimalFormat("###,###,###,###");

		String pfpCustInfoId = "";
		if (StringUtils.isNotEmpty(pfdCustomerInfoId)) {
			List<PfdUserAdAccountRef> refList = pfdUserAdAccountRefDAO.findPfdUserAdAccountRef(pfdCustomerInfoId);
			
			for (int i=0; i<refList.size(); i++) {
				pfpCustInfoId += "'" + refList.get(i).getPfpCustomerInfo().getCustomerInfoId() + "',";
			}
			if (!pfpCustInfoId.equals("")) {
				pfpCustInfoId = pfpCustInfoId.substring(0, pfpCustInfoId.length()-1);				
			}
		}
		log.info(">>> pfpCustInfoId = " + pfpCustInfoId);

		//有選 PFD 帳戶卻找不到對應的 PFP 帳戶 -> 不查詢
		if (StringUtils.isNotEmpty(pfdCustomerInfoId) && StringUtils.isEmpty(pfpCustInfoId)) {
			return null;
		}

		Date startDate = DateValueUtil.getInstance().stringToDate(sDate);
		Date endDate = DateValueUtil.getInstance().stringToDate(eDate);

		List<AdmRecognizeRecord> dataList = ((IAdmRecognizeRecordDAO)dao).findRecognizeRecords(startDate, endDate, pfpCustInfoId);
		log.info(">>> dataList.size() = " + dataList.size());

		Map<String, String> orderTypeMap = new LinkedHashMap<String, String>();
		for (EnumOrderType e : EnumOrderType.values()) {
			orderTypeMap.put(e.getTypeTag(), e.getTypeName());
		}

		Map<String, String> payTypeMap = new LinkedHashMap<String, String>();
		payTypeMap.put(EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfdAccountPayType.ADVANCE.getPayName());
		payTypeMap.put(EnumPfdAccountPayType.LATER.getPayType(), EnumPfdAccountPayType.LATER.getPayName());

		List<AdmRecognizeRecordVO> voList = new ArrayList<AdmRecognizeRecordVO>();

		AdmRecognizeRecord admRecognizeRecord = null;
		for (int i=0; i<dataList.size(); i++) {

			admRecognizeRecord = dataList.get(i);

			if (StringUtils.isNotBlank(payType)) { //有付款方式條件時要過濾
				//只有被 PFD 管理的 PFP 才會有付款方式的設定
				if (admRecognizeRecord.getPfpCustomerInfo().getPfdUserAdAccountRefs().size() > 0) {
					PfdUserAdAccountRef pfdUserAdAccountRef = new ArrayList<PfdUserAdAccountRef>(admRecognizeRecord.getPfpCustomerInfo().getPfdUserAdAccountRefs()).get(0);
					if (payType.equals(pfdUserAdAccountRef.getPfpPayType())) {

						AdmRecognizeRecordVO vo = new AdmRecognizeRecordVO();
						vo.setSaveDate(sdf.format(admRecognizeRecord.getSaveDate()));
						vo.setMemberId(admRecognizeRecord.getPfpCustomerInfo().getMemberId());
						vo.setCustomerInfoTitle(admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoTitle());
						vo.setOrderType(orderTypeMap.get(admRecognizeRecord.getOrderType()));
						vo.setOrderPrice(nf.format(admRecognizeRecord.getOrderPrice()));		
						vo.setTotalOrderPrice(nf.format(admRecognizeRecord.getOrderPrice()+admRecognizeRecord.getTax()));
						vo.setTax(nf.format(admRecognizeRecord.getTax()));
						vo.setPfdCustInfoTitle(pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
						vo.setPfdUserName(pfdUserAdAccountRef.getPfdUser().getUserName());
						vo.setPayType(payTypeMap.get(payType));
						
						voList.add(vo);

					} else {
						//該 PFP 帳戶的付款方式不是使用者要的 -> 無視
					}
				} else {
					//該 PFP 帳戶沒有經銷商關聯 -> 無視
				}
			} else {

				AdmRecognizeRecordVO vo = new AdmRecognizeRecordVO();
				vo.setSaveDate(sdf.format(admRecognizeRecord.getSaveDate()));
				vo.setMemberId(admRecognizeRecord.getPfpCustomerInfo().getMemberId());
				vo.setCustomerInfoTitle(admRecognizeRecord.getPfpCustomerInfo().getCustomerInfoTitle());
				vo.setOrderType(orderTypeMap.get(admRecognizeRecord.getOrderType()));
				vo.setOrderPrice(nf.format(admRecognizeRecord.getOrderPrice()));
				vo.setTax(nf.format(admRecognizeRecord.getTax()));
				vo.setTotalOrderPrice(nf.format(admRecognizeRecord.getOrderPrice()+admRecognizeRecord.getTax()));
				//付款條件要特殊處理
				if (admRecognizeRecord.getPfpCustomerInfo().getPfdUserAdAccountRefs().size() > 0) {
					PfdUserAdAccountRef pfdUserAdAccountRef = new ArrayList<PfdUserAdAccountRef>(admRecognizeRecord.getPfpCustomerInfo().getPfdUserAdAccountRefs()).get(0);
					vo.setPfdCustInfoTitle(pfdUserAdAccountRef.getPfdCustomerInfo().getCompanyName());
					vo.setPfdUserName(pfdUserAdAccountRef.getPfdUser().getUserName());
					vo.setPayType(payTypeMap.get(pfdUserAdAccountRef.getPfpPayType()));
				} else {
					vo.setPfdCustInfoTitle("");
					vo.setPfdUserName("");
					vo.setPayType("");					
				}

				voList.add(vo);
			}
		}

		log.info(">>> voList.size() = " + voList.size());

		return voList;
	}

	/**
	 * 在統計計算贈送廣告金之前，先刪除 "新戶好事成金(FRA201402240001)"、"續攤享禮金(FRA201402240002)"
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	public Integer deleteRecognizeRecordBySaveDate(Date saveDate, String customerInfoId) throws Exception {
		return ((IAdmRecognizeRecordDAO)dao).deleteRecognizeRecordBySaveDate(saveDate, customerInfoId);
	}
	
	/**
	 * 刪除所有帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000() throws Exception {
		return ((IAdmRecognizeRecordDAO)dao).deleteRecognizeRecordForG6000();
	}

	/**
	 * 依日期刪除帳號未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000(Date saveDate) throws Exception{
		return ((IAdmRecognizeRecordDAO)dao).deleteRecognizeRecordForG6000(saveDate);
	}
	
	/**
	 * 依日期、帳號刪除帳戶未開通的 "產業限定獨享(FRA201402240003)" 攤提資料
	 * 因為憑序號新增時(活動"產業限定獨享"-FRA201402240003)，會新增一筆攤提資料 adm_recognize_record
	 * 所以，在每日排程計算時(AM03:30)，需要將未開通完成的資料清除
	 * @param saveDate
	 * @param customerInfoId
	 * @return
	 */
	public Integer deleteRecognizeRecordForG6000(Date saveDate, String customerInfoId) throws Exception {
		return ((IAdmRecognizeRecordDAO)dao).deleteRecognizeRecordForG6000(saveDate, customerInfoId);
	}
	
	public List<AdmRecognizeRecord> findRecognizeRecords(String sDate, String eDate, String pfpCustomerInfoId, EnumOrderType enumOrderType) {
		return ((IAdmRecognizeRecordDAO)dao).findRecognizeRecords(DateValueUtil.getInstance().stringToDate(sDate), 
																	DateValueUtil.getInstance().stringToDate(eDate), 
																	pfpCustomerInfoId, 
																	enumOrderType.getTypeTag());
	}
	
	public void setPfdUserAdAccountRefDAO(IPfdUserAdAccountRefDAO pfdUserAdAccountRefDAO) {
		this.pfdUserAdAccountRefDAO = pfdUserAdAccountRefDAO;
	}

	public void setCustomerInfoService(IPfpCustomerInfoService customerInfoService) {
		this.customerInfoService = customerInfoService;
	}
	
	public Integer deleteAdmRecognizeRecord(String orderId) {
		return ((IAdmRecognizeRecordDAO)dao).deleteAdmRecognizeRecord(orderId);
	}
	
	public Integer deleteRecordAfterDate(String startDate) {		
		return ((IAdmRecognizeRecordDAO)dao).deleteRecordAfterDate(DateValueUtil.getInstance().stringToDate(startDate));
	}

	//取得每日廣告花費日誌
	public List<AdActionDalilyReportVO> getAdAdmRecognizeDalilyReport() throws Exception {
	    return ((IAdmRecognizeRecordDAO)dao).getAdAdmRecognizeDalilyReport();
	}
}

