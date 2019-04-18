package com.pchome.akbadm.db.service.catalog;

import com.pchome.akbadm.db.dao.catalog.IPfpCatalogDAO;
import com.pchome.akbadm.db.pojo.PfpCatalog;
import com.pchome.akbadm.db.service.BaseService;
import com.pchome.akbadm.db.vo.catalog.PfpCatalogVO;

public class PfpCatalogService extends BaseService<PfpCatalog, String> implements IPfpCatalogService {

	/**
	 * 更新目錄資料
	 * 狀態更新為已完成，重新算商品count量
	 * @param pfpCatalogVO
	 */
	@Override
	public void updatePfpCatalogProdNumAndUploadStatus(PfpCatalogVO vo) {
		((IPfpCatalogDAO) dao).updatePfpCatalogProdNumAndUploadStatus(vo);
	}

	/**
	 * 更新目錄資料
	 * 依傳入狀態更新
	 * @param pfpCatalogVO
	 */
	@Override
	public void updatePfpCatalogUploadStatus(PfpCatalogVO vo) {
		((IPfpCatalogDAO) dao).updatePfpCatalogUploadStatus(vo);
	}
}