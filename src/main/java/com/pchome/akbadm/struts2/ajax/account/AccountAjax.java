package com.pchome.akbadm.struts2.ajax.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;

import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.vo.PfpCustomerInfoVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumAdStatus;

public class AccountAjax extends BaseCookieAction{

	private static final long serialVersionUID = 1L;

	private IPfpCustomerInfoService pfpCustomerInfoService;
	private IPfpAdActionService pfpAdActionService;

	private List<PfpCustomerInfoVO> customerInfos;
	
	private String pfdCustomerId;
	private String keyword;
	private String userAccount;
	private String userEmail;
	private String customerInfoStatus;
	private int pageNo = 1;       								//目前頁數
	private int pageSize = 20;     								//每頁幾筆
	private int pageCount = 0;    								//共幾頁
	private int totalCount = 0;   								//共幾筆
	
	public String accountSearchAjax() throws Exception{
		
		List<PfpCustomerInfo> allCustomerInfo = pfpCustomerInfoService.getExistentCustomerInfo();
		
		totalCount = allCustomerInfo.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));		
		
		List<Object> objects = pfpCustomerInfoService.getCustomerInfos(keyword, userAccount, userEmail, customerInfoStatus, pageNo, pageSize,pfdCustomerId);
		
		customerInfos = new ArrayList<PfpCustomerInfoVO>();

		DecimalFormat decimalFormat = new DecimalFormat("#.#");

		for(Object obs:objects){

			Object[] ob = (Object[])obs;

			PfpCustomerInfo customerInfo = pfpCustomerInfoService.getCustomerInfo(ob[0].toString());

			if(customerInfo != null){
				
				String customerInfoId = customerInfo.getCustomerInfoId();

				PfpCustomerInfoVO custInfoVO = new PfpCustomerInfoVO();

				custInfoVO.setCustomerInfoId(customerInfoId);
				custInfoVO.setCustomerInfoTitle(customerInfo.getCustomerInfoTitle());
				custInfoVO.setCategory(customerInfo.getCategory());
				custInfoVO.setIndustry(customerInfo.getIndustry());
				custInfoVO.setStatus(customerInfo.getStatus());
				custInfoVO.setCreateDate(customerInfo.getCreateDate());
				custInfoVO.setPfdUserAdAccountRefs(customerInfo.getPfdUserAdAccountRefs());
				custInfoVO.setMemberId(customerInfo.getMemberId());
				if (customerInfo.getActivateDate()!=null) {
					custInfoVO.setActivateDate(customerInfo.getActivateDate());
				}
				custInfoVO.setRemain(customerInfo.getRemain());
				custInfoVO.setBlack(customerInfo.getBlack());

				//計算廣告剩餘天數 = 帳戶餘額 / 所有廣告每日花費上限加總
				double remain = customerInfo.getRemain(); //餘額
				double totalMax = 0; //廣告每日花費上限加總

				Map<String, String> conditionMap = new HashMap<String, String>();
				conditionMap.put("customerInfoId", customerInfoId);
				List<PfpAdAction> adActionList = pfpAdActionService.getAdActionByConditions(conditionMap);
				for (int k=0; k<adActionList.size(); k++) {
					int adActionStatus = adActionList.get(k).getAdActionStatus();
					if (adActionStatus!=EnumAdStatus.UnDone.getStatusId() &&
							adActionStatus!=EnumAdStatus.Pause.getStatusId() &&
							adActionStatus!=EnumAdStatus.Close.getStatusId()) {

						totalMax += adActionList.get(k).getAdActionMax();
					}
				}

				double remainDays = 0;
				if ((remain - totalMax)>0 && totalMax>0) {
					remainDays = (remain - totalMax) / totalMax;				
				}

				custInfoVO.setRemainDays(decimalFormat.format(remainDays));

				customerInfos.add(custInfoVO);
			}
			
		}
		
		return SUCCESS;
	}
	
	public List<PfpCustomerInfoVO> getCustomerInfos() {
		return customerInfos;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setCustomerInfoStatus(String customerInfoStatus) {
		this.customerInfoStatus = customerInfoStatus;
	}

	public int getPageNo() {
		return pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
	}

	public void setPfpAdActionService(IPfpAdActionService pfpAdActionService) {
		this.pfpAdActionService = pfpAdActionService;
	}

	public String getPfdCustomerId() {
		return pfdCustomerId;
	}

	public void setPfdCustomerId(String pfdCustomerId) {
		this.pfdCustomerId = pfdCustomerId;
	}
	
	
	
}
