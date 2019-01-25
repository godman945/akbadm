package com.pchome.akbadm.struts2.action.ad;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.service.arwManagement.IAdmARWManagementService;
import com.pchome.akbadm.db.vo.ad.AdmARWManagementVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.ad.EnumDateFlag;

public class ARWManagementAction extends BaseCookieAction{
	private IAdmARWManagementService admARWManagementService;

	// 查詢結果
	private List<AdmARWManagementVO> dataList = new ArrayList<AdmARWManagementVO>();
	
	private String customerInfoId; // PFP帳戶編號
	private String customerInfoTitle; // PFP帳戶名稱
	private int arwValue; // ARW權重值
	private int dateFlag; // 走期狀態。 0:走期無限 1:檢查走期
	private String startDate; // 走期開始日期
	private String endDate; // 走期結束日期
	
	//下拉選單PFD經銷商帳號、名稱資料
	private List<Map<String, String>> pfdCustomerList = new ArrayList<Map<String, String>>();
	
	public String execute() throws Exception {
		//一進入就做查詢動作 全撈不帶值
		dataList = admARWManagementService.getARWDataList(new AdmARWManagementVO());
		return SUCCESS;
	}
	
	/**
	 * 點選新增按鈕後，到新增頁時執行部分
	 * @return
	 * @throws Exception
	 */
	public String arwManagementAddAction() throws Exception {
		pfdCustomerList = admARWManagementService.getPfdCustomerList();
		return SUCCESS;
	}
	
	/**
	 * 新增頁點選儲存
	 * @return
	 * @throws Exception
	 */
	public String arwManagementSave() throws Exception {
		AdmARWManagementVO vo = setVOValue();
		admARWManagementService.addARWData(vo);
		return SUCCESS;
	}
	
	/**
	 * 點選修改按鈕後，到修改頁時執行部分
	 * @return
	 * @throws Exception
	 */
	public String arwManagementModifyAction() throws Exception {
		AdmARWManagementVO vo = setVOValue();
		dataList = admARWManagementService.getARWDataList(vo);
		return SUCCESS;
	}

	/**
	 * 修改頁點選儲存
	 * @return
	 * @throws Exception
	 */
	public String arwManagementUpdate() throws Exception {
		AdmARWManagementVO vo = setVOValue();
		admARWManagementService.updateARWData(vo);
		return SUCCESS;
	}
	
	/**
	 * 刪除資料
	 * @return
	 * @throws Exception
	 */
	public String arwManagementDelete() throws Exception {
		AdmARWManagementVO vo = setVOValue();
		admARWManagementService.deleteARWData(vo);
		return SUCCESS;
	}

	/**
	 * 共用塞值到VO，之後有新加值寫在這即可
	 * @return
	 */
	private AdmARWManagementVO setVOValue() {
		AdmARWManagementVO vo = new AdmARWManagementVO();
		vo.setCustomerInfoId(customerInfoId);
		vo.setCustomerInfoTitle(customerInfoTitle);
		vo.setArwValue(arwValue);
		vo.setDateFlag(dateFlag);
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		return vo;
	}
	
	/**
	 * 走期狀態下拉選單
	 * @return
	 */
	public Map<String, String> getDateFlagMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();

		for (EnumDateFlag item : EnumDateFlag.values()) {
			map.put(String.valueOf(item.getFlag()), item.getName());
		}

		return map;
	}
	
	
	public List<Map<String, String>> getPfdCustomerList() {
		return pfdCustomerList;
	}

	public void setPfdCustomerList(List<Map<String, String>> pfdCustomerList) {
		this.pfdCustomerList = pfdCustomerList;
	}

	public IAdmARWManagementService getAdmARWManagementService() {
		return admARWManagementService;
	}

	public void setAdmARWManagementService(IAdmARWManagementService admARWManagementService) {
		this.admARWManagementService = admARWManagementService;
	}

	public List<AdmARWManagementVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdmARWManagementVO> dataList) {
		this.dataList = dataList;
	}

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getCustomerInfoTitle() {
		return customerInfoTitle;
	}

	public void setCustomerInfoTitle(String customerInfoTitle) {
		this.customerInfoTitle = customerInfoTitle;
	}

	public int getArwValue() {
		return arwValue;
	}

	public void setArwValue(int arwValue) {
		this.arwValue = arwValue;
	}

	public int getDateFlag() {
		return dateFlag;
	}

	public void setDateFlag(int dateFlag) {
		this.dateFlag = dateFlag;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
