package com.pchome.akbadm.db.service.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpCatalogProdEcService extends IBaseService<PfpCatalogProdEc, String> {
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus);

    public List<Map<String, Object>> selectPfpCatalogByStatus(String pfpCustomerInfoId, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus);

    public List<Map<String, Object>> selectPfpCatalogProdEc(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus, int pageNo, int pageSize);

    public int selectCount(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus);
}
