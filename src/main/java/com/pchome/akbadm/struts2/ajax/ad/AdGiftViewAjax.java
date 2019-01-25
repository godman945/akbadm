package com.pchome.akbadm.struts2.ajax.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.free.IAdmFreeActionService;
import com.pchome.akbadm.db.vo.ad.AdmFreeActionVO;
import com.pchome.akbadm.struts2.BaseCookieAction;


public class AdGiftViewAjax extends BaseCookieAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IAdmFreeActionService admFreeActionService;
	private List<AdmFreeActionVO> dataList;
	
	private String searchStartDate;
	private String searchEndDate;
	private String inviledStartDate;
	private String inviledEndDate;
	private String payment;
	private String giftStyle;
	
	
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 50;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private int totalCount = 0;   				// 初始化共幾筆
	private int totalSize = 0;						
	
	public String adGiftViewTableAjax() throws Exception{
		
		List<AdmFreeActionVO> totalData = null;
		
		Map<String, String> conditionMap1 = new HashMap<String, String>();
		Map<String, String> conditionMap2 = new HashMap<String, String>();

		if(StringUtils.isNotEmpty(searchStartDate)){
			conditionMap1.put("searchStartDate", searchStartDate);
		}
		if(StringUtils.isNotEmpty(searchEndDate)){
			conditionMap1.put("searchEndDate", searchEndDate);
		}
		if(StringUtils.isNotEmpty(inviledStartDate)){
			conditionMap1.put("inviledStartDate", inviledStartDate);
		}
		if(StringUtils.isNotEmpty(inviledEndDate)){
			conditionMap1.put("inviledEndDate", inviledEndDate);
		}
		if(StringUtils.isNotEmpty(payment)){
			conditionMap1.put("payment", payment);
		}
		if(StringUtils.isNotEmpty(giftStyle)){
			conditionMap1.put("giftStyle", giftStyle);
		}
		log.info(">>>>>>>>>>>>>>>>...giftStyle=" + giftStyle);
		conditionMap1.put("pageSize", String.valueOf(pageSize));
		
		conditionMap2 = conditionMap1;
		conditionMap1.put("page", "-1");
		conditionMap2.put("page", String.valueOf(pageNo));
		
		//結束日期調整：結束日期由String 轉 Date 時，會變成當天 00:00，造成 SQL 找不到結束日期那天的資料，所以必須將結束日期加一天，才查的到節日日期那天的資料
		/*Date dEnddate = new Date();
		if(StringUtils.isNotBlank(endDate)) {
			dEnddate = DateValueUtil.getInstance().stringToDate(endDate);
		}
		Calendar cEnddate = Calendar.getInstance();
		cEnddate.setTime(dEnddate);*/
		
		totalData = admFreeActionService.getAdmFreeActionData(conditionMap1);
		
		dataList = admFreeActionService.getAdmFreeActionData(conditionMap2);
		
		totalCount = totalData.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		totalSize = totalCount;

			
		return SUCCESS;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public String getInviledStartDate() {
		return inviledStartDate;
	}

	public String getInviledEndDate() {
		return inviledEndDate;
	}

	public String getPayment() {
		return payment;
	}

	public String getGiftStyle() {
		return giftStyle;
	}

	public void setAdmFreeActionService(IAdmFreeActionService admFreeActionService) {
		this.admFreeActionService = admFreeActionService;
	}

	public List<AdmFreeActionVO> getDataList() {
		return dataList;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public void setInviledStartDate(String inviledStartDate) {
		this.inviledStartDate = inviledStartDate;
	}

	public void setInviledEndDate(String inviledEndDate) {
		this.inviledEndDate = inviledEndDate;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public void setGiftStyle(String giftStyle) {
		this.giftStyle = giftStyle;
	}
	
}
