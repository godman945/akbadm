package com.pchome.akbadm.db.dao.admBrandCorrespond;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmBrandCorrespond;
import com.pchome.akbadm.db.vo.ad.AdmBrandCorrespondVO;

public interface IAdmBrandCorrespondDAO extends IBaseDAO<AdmBrandCorrespond,Integer>{

	/**
	 * 取得 品牌對應關鍵字 資料
	 * @param admBrandCorrespondVO
	 * @return
	 */
	List<Object> getBrandCorrespondList(AdmBrandCorrespondVO admBrandCorrespondVO);

	/**
	 * 檢查新增的資料是否重複
	 * @param admBrandCorrespond
	 * @return
	 */
	List<AdmBrandCorrespondVO> checkBrandCorrespondTableData(AdmBrandCorrespond admBrandCorrespond);

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

}
