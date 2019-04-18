package com.pchome.akbadm.db.dao.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalog;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public interface IPfpCatalogDAO extends IBaseDAO<PfpCatalog, String> {

	/**
	 * 取得商品目錄，自動排程上傳清單
	 */
	List<Map<String, Object>> getCatalogJobList();

	/**
	 * 更新目錄資料
	 * 狀態更新為已完成，重新算商品count量
	 * @param vo
	 */
	void updatePfpCatalogProdNumAndUploadStatus(PfpCatalogVO vo);

	/**
	 * 更新目錄資料
	 * 依傳入狀態更新
	 * @param vo
	 */
	void updatePfpCatalogUploadStatus(PfpCatalogVO vo);
}