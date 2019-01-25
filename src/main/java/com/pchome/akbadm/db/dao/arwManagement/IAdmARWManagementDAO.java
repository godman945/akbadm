package com.pchome.akbadm.db.dao.arwManagement;

import java.text.ParseException;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmArwValue;
import com.pchome.akbadm.db.vo.ad.AdmARWManagementVO;

public interface IAdmARWManagementDAO extends IBaseDAO<AdmArwValue,Integer>{

	/**
	 * 取得ARW 廣告權重列表資料
	 * @param vo
	 * @return
	 */
	List<Object> getARWDataList(AdmARWManagementVO vo);

	/**
	 * 刪除ARW 廣告權重資料
	 * @param vo
	 */
	void deleteARWData(AdmARWManagementVO vo);

	/**
	 * 新增ARW 廣告權重資料
	 * @param vo
	 * @throws ParseException
	 */
	void addARWData(AdmARWManagementVO vo) throws ParseException;

	/**
	 * 更新ARW 廣告權重資料
	 * @param vo
	 * @throws ParseException
	 */
	void updateARWData(AdmARWManagementVO vo) throws ParseException;

	/**
	 * 取得下拉選單PFP帳號名稱資料，排除已新增ARW權限的帳號
	 * @param vo 
	 * @return
	 */
	List<Object> getPfpCustomerList(AdmARWManagementVO vo);

	/**
	 * 取得下拉選單PFD經銷商帳號名稱資料
	 * @return
	 */
	List<Object> getPfdCustomerList();
	
}
