package com.pchome.akbadm.db.service.admBrandCorrespond;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.AdmBrandCorrespond;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.ad.AdmBrandCorrespondVO;

public interface IAdmBrandCorrespondService extends IBaseService<AdmBrandCorrespond, Integer>{

	/**
	 * 取得 品牌對應關鍵字 資料
	 * @param admBrandCorrespondVO
	 * @return
	 */
	List<AdmBrandCorrespondVO> getBrandCorrespondList(AdmBrandCorrespondVO admBrandCorrespondVO);

	/**
	 * 檢查新增的資料是否重複
	 * @param admBrandCorrespond
	 * @return
	 */
	int checkBrandCorrespondTableData(AdmBrandCorrespond admBrandCorrespond);

	/**
	 * 新增品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	void insertBrandCorrespond(AdmBrandCorrespond admBrandCorrespond);

	/**
	 * 更新品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	void updateBrandCorrespond(AdmBrandCorrespond admBrandCorrespond);

	/**
	 * 刪除品牌對應關鍵字資料
	 * @param admBrandCorrespond
	 */
	void deleteBrandCorrespondData(AdmBrandCorrespond admBrandCorrespond);

	/**
	 * 下載品牌對應關鍵字資料
	 * @param admBrandCorrespondVO
	 * @return
	 */
	StringBuffer makeDownloadReportData(AdmBrandCorrespondVO admBrandCorrespondVO);

	/**
	 * 處理品牌對應關鍵字api
	 * @param admBrandCorrespondVO
	 * @return
	 */
	Map<String, Object> getBrandCorrespondListApi(AdmBrandCorrespondVO admBrandCorrespondVO);

}
