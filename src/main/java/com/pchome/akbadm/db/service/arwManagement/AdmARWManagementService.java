package com.pchome.akbadm.db.service.arwManagement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.arwManagement.IAdmARWManagementDAO;
import com.pchome.akbadm.db.pojo.AdmArwValue;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.ad.AdmARWManagementVO;

public class AdmARWManagementService extends BaseService<AdmArwValue, Integer> implements IAdmARWManagementService{

	/**
	 * 取得ARW 廣告權重列表資料
	 */
	@Override
	public List<AdmARWManagementVO> getARWDataList(AdmARWManagementVO vo) {

		List<Object> list = ((IAdmARWManagementDAO) dao).getARWDataList(vo);

		List<AdmARWManagementVO> resultData = new ArrayList<AdmARWManagementVO>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		for (int i = 0; i < list.size(); i++) {
			Object[] objArray = (Object[]) list.get(i);
			String pfdCustomerInfoId = (String) objArray[0];
			String customerInfoId = (String) objArray[1];
			String customerInfoTitle = (String) objArray[2];
			int arwValue = (int) objArray[3];
			int dateFlag = (int) objArray[4];
			String startDate = dateFormat.format((Date) objArray[5]);
			String endDate = dateFormat.format((Date) objArray[6]);

			AdmARWManagementVO queryDataVo = new AdmARWManagementVO();
			queryDataVo.setPfdCustomerInfoId(pfdCustomerInfoId); // PFD經銷商帳號
			queryDataVo.setCustomerInfoId(customerInfoId); // PFP帳戶編號
			queryDataVo.setCustomerInfoTitle(customerInfoTitle); // PFP帳戶名稱
			queryDataVo.setArwValue(arwValue); // ARW權重值
			queryDataVo.setDateFlag(dateFlag); // 走期狀態。 0:走期無限 1:檢查走期
			queryDataVo.setStartDate(startDate); // 走期開始日期
			queryDataVo.setEndDate(endDate); // 走期結束日期

			resultData.add(queryDataVo);
		}

		return resultData;
	}

	/**
	 * 刪除ARW 廣告權重資料
	 */
	@Override
	public void deleteARWData(AdmARWManagementVO vo) {
		((IAdmARWManagementDAO) dao).deleteARWData(vo);
	}

	/**
	 * 新增ARW 廣告權重資料
	 */
	@Override
	public void addARWData(AdmARWManagementVO vo) throws ParseException {
		((IAdmARWManagementDAO) dao).addARWData(vo);
	}

	/**
	 * 更新ARW 廣告權重資料
	 */
	@Override
	public void updateARWData(AdmARWManagementVO vo) throws ParseException {
		((IAdmARWManagementDAO) dao).updateARWData(vo);
	}

	/**
	 * 取得下拉選單PFP帳號名稱資料，排除已新增ARW權限的帳號
	 */
	@Override
	public List<Map<String, String>> getPfpCustomerList(AdmARWManagementVO vo) {
		List<Object> list = ((IAdmARWManagementDAO) dao).getPfpCustomerList(vo);

		List<Map<String, String>> resultData = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < list.size(); i++) {
			Object[] objArray = (Object[]) list.get(i);
			String customerInfoId = (String) objArray[0];
			String customerInfoTitle = (String) objArray[1];
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("customerInfoId", customerInfoId);
			map.put("customerInfoTitle", customerInfoTitle);
			
			resultData.add(map);
		}
		return resultData;
	}

	/**
	 * 取得下拉選單PFD經銷商帳號名稱資料
	 */
	@Override
	public List<Map<String, String>> getPfdCustomerList() {
		List<Object> list = ((IAdmARWManagementDAO) dao).getPfdCustomerList();

		List<Map<String, String>> resultData = new ArrayList<Map<String, String>>();
		
		for (int i = 0; i < list.size(); i++) {
			Object[] objArray = (Object[]) list.get(i);
			String pfdCustomerInfoId = (String) objArray[0];
			String pfdCustomerInfoTitle = (String) objArray[1];
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("pfdCustomerInfoId", pfdCustomerInfoId);
			map.put("pfdCustomerInfoTitle", pfdCustomerInfoTitle);
			
			resultData.add(map);
		}
		return resultData;
	}

}
