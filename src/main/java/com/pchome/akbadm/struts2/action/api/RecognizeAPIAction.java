package com.pchome.akbadm.struts2.action.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.pchome.akbadm.db.pojo.AdmRecognizeDetail;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.vo.RecognizeExceptRefundVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.soft.util.DateValueUtil;
import com.pchome.soft.util.SpringECcoderUtil;

public class RecognizeAPIAction extends BaseCookieAction{

	private SpringECcoderUtil springECcoderUtil;
	private IAdmRecognizeDetailService admRecognizeDetailService;
	private IAdmAccesslogService accesslogService;
	
	private String channelId;
	private String password;
	
	private String date;
	
	private List<Object> DATA;  				// 回傳攤提 json 
	
	public String getRecognizeDate() throws Exception {
		
		log.info(" date = "+date);
		
		DATA = new ArrayList<Object>();
		
		
		List<RecognizeExceptRefundVO> recognizeDetails = admRecognizeDetailService.findRecognizeDetailExceptRefundToBilling(date);
		
//		List<AdmRecognizeDetail> recognizeDetails = admRecognizeDetailService.findRecognizeDetailToBilling(date);
		
		for(RecognizeExceptRefundVO recognizeDetail:recognizeDetails){

			String orderId = recognizeDetail.getRecognizeOrderId();	
			String pkey = springECcoderUtil.encoderEcString(channelId+password+orderId);
			String year = recognizeDetail.getCostDate().substring(0, 4);
			String month = recognizeDetail.getCostDate().substring(5, 7);
			String day = recognizeDetail.getCostDate().substring(8, 10);
			
			LinkedHashMap<String, Object> recognizeData = new LinkedHashMap<String, Object>();
			recognizeData.put("pkey", pkey);
			recognizeData.put("year", year);
			recognizeData.put("month", month);
			recognizeData.put("day", day);
			recognizeData.put("cost", recognizeDetail.getCostPrice()+recognizeDetail.getTax());
			
			DATA.add(recognizeData);
			
			log.info(" pkey = "+pkey);
			log.info(" year-month-day = "+year+"-"+month+"-"+day);
			log.info(" customerInfoId = "+recognizeDetail.getCustomerInfoId());
			log.info(" cost = "+recognizeDetail.getCostPrice());
			
//			if(recognizeDetail.getCostPrice() <= 0){
//				
//				String message = EnumAccesslogAction.WARNING.getMessage()+"：無攤提資料";
//				
//				accesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, 
//						EnumAccesslogAction.WARNING, 
//						message, 
//						"api", 
//						null,  
//						recognizeDetail.getCustomerInfoId(), 
//						null,  
//						request.getRemoteAddr(), 
//						EnumAccesslogEmailStatus.YES);
//			}
		}			
		
		log.info(" DATA = "+DATA);	
		
		// Accesslog 記錄
		if(DATA.isEmpty()){
			String message = EnumAccesslogAction.WARNING.getMessage()+"：無攤提資料";
			
			accesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, 
					EnumAccesslogAction.WARNING, 
					message, 
					"api", 
					null, 
					"", 
					null, 
					request.getRemoteAddr(), 
					EnumAccesslogEmailStatus.YES);
			
		}
		
		return SUCCESS;
	}
	
	public void setSpringECcoderUtil(SpringECcoderUtil springECcoderUtil) {
		this.springECcoderUtil = springECcoderUtil;
	}

	public void setAdmRecognizeDetailService(IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setAccesslogService(IAdmAccesslogService accesslogService) {
		this.accesslogService = accesslogService;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Object> getDATA() {
		return DATA;
	}
	
}
