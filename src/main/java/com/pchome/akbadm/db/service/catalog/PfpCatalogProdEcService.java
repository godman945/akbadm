package com.pchome.akbadm.db.service.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.catalog.IPfpCatalogProdEcDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogProdEc;
import com.pchome.akbadm.db.service.BaseService;

public class PfpCatalogProdEcService extends BaseService<PfpCatalogProdEc, String> implements IPfpCatalogProdEcService {
    @Override
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        return ((IPfpCatalogProdEcDAO) dao).selectPfpCustomerInfoByStatus(catalogUploadStatus, catalogDeleteStatus, catalogProdEcStatus, catalogProdEcCheckStatus);
    }

    @Override
    public List<Map<String, Object>> selectPfpCatalogByStatus(String pfpCustomerInfoId, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        return ((IPfpCatalogProdEcDAO) dao).selectPfpCatalogByStatus(pfpCustomerInfoId, catalogUploadStatus, catalogDeleteStatus, catalogProdEcStatus, catalogProdEcCheckStatus);
    }

    @Override
    public List<Map<String, Object>> selectPfpCatalogProdEc(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus, int pageNo, int pageSize) {
        return ((IPfpCatalogProdEcDAO) dao).selectPfpCatalogProdEc(pfpCustomerInfoId, catalogSeq, catalogUploadStatus, catalogDeleteStatus, catalogProdEcStatus, catalogProdEcCheckStatus, pageNo, pageSize);
    }

    @Override
    public int selectCount(String pfpCustomerInfoId, String catalogSeq, String catalogUploadStatus, String catalogDeleteStatus, String catalogProdEcStatus, String catalogProdEcCheckStatus) {
        return ((IPfpCatalogProdEcDAO) dao).selectCount(pfpCustomerInfoId, catalogSeq, catalogUploadStatus, catalogDeleteStatus, catalogProdEcStatus, catalogProdEcCheckStatus);
    }
}
