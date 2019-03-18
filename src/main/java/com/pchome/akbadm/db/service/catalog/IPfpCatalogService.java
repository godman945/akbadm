package com.pchome.akbadm.db.service.catalog;

import com.pchome.akbadm.db.pojo.PfpCatalog;
import com.pchome.akbadm.db.service.IBaseService;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public interface IPfpCatalogService extends IBaseService<PfpCatalog, String> {

	/**
	 * 更新目錄資料
	 * 狀態更新為已完成，重新算商品count量
	 * @param pfpCatalogVO
	 */
	void updatePfpCatalogProdNumAndUploadStatus(PfpCatalogVO pfpCatalogVO);

	/**
	 * 更新目錄資料
	 * 依傳入狀態更新
	 * @param pfpCatalogVO
	 */
	void updatePfpCatalogUploadStatus(PfpCatalogVO pfpCatalogVO);
}