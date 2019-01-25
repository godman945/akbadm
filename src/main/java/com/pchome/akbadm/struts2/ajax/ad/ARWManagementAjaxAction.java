package com.pchome.akbadm.struts2.ajax.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.arwManagement.IAdmARWManagementService;
import com.pchome.akbadm.db.vo.ad.AdmARWManagementVO;
import com.pchome.akbadm.struts2.BaseAction;
public class ARWManagementAjaxAction  extends BaseAction {
	
	private IAdmARWManagementService admARWManagementService;
	private String pfdCustomerInfoId; // PFD經銷商帳號
	private Map<String,Object> dataMap;
	
	/**
	 * 依選取的PFD經銷商帳號，取得下拉選單PFP帳號名稱資料，排除已新增ARW權限的帳號
	 * @return
	 * @throws Exception
	 */
	public String selectPfpCustomerListAjax() throws Exception {

		dataMap = new HashMap<String, Object>();
		
		AdmARWManagementVO vo = new AdmARWManagementVO();
		vo.setPfdCustomerInfoId(pfdCustomerInfoId);
		
		List<Map<String, String>> pfpCustomerList = admARWManagementService.getPfpCustomerList(vo);
		
		dataMap.put("status", "SUCCESS");
		dataMap.put("dataMap", pfpCustomerList);
		
		return SUCCESS;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public IAdmARWManagementService getAdmARWManagementService() {
		return admARWManagementService;
	}

	public void setAdmARWManagementService(IAdmARWManagementService admARWManagementService) {
		this.admARWManagementService = admARWManagementService;
	}
	
}
