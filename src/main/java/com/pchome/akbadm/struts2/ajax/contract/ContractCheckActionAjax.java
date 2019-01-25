package com.pchome.akbadm.struts2.ajax.contract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class ContractCheckActionAjax extends BaseCookieAction{

	private IPfdContractService pfdContractService;
	
	private String pfdCustomerInfoId; //經銷商序號
	private String contractId; //合約編號
	private String startDate; //起始日期
	private String endDate; //結束日期
	private PfdContract pfdContract;
	private List<PfdContract> dataList;
	private String message;
	
	public String checkAddContract() throws Exception {
		//檢查合約編碼是否重覆
		pfdContract = pfdContractService.findPfdContract(contractId);
		if (pfdContract != null){
			message ="合約編號重覆";
			return SUCCESS;
		}
		
		//合約開始日期要小於結束日期
		if(contractDateToInt(endDate) < contractDateToInt(startDate)){
			message ="合約結束日期不得早於合約起始日期";
			return SUCCESS;
		}
		
		//檢查該pfd的合約起訖日期是否有重疊
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd");
		dataList = pfdContractService.checkPfdContractOverlap(pfdCustomerInfoId,sdf0.parse(startDate),sdf0.parse(endDate));
		if (dataList.size() > 0){
			message ="合約起訖日期不能與其他合約日期重疊！請重新確認欄位";
			return SUCCESS;
		}
		
		return SUCCESS;
	}
	
	public int contractDateToInt(String contractDate) throws Exception{
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(contractDate);		
		String dateString = dateFormat.format(date);
		String[] dateArray=dateString.split("-");
		String dateStr="";		
		for (String string : dateArray) {
			dateStr = dateStr + string;
		}
		int startInt = Integer.parseInt(dateStr);
		
		return startInt;
	}

	
	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}
}
