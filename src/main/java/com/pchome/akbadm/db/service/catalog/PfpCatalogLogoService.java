package com.pchome.akbadm.db.service.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.catalog.IPfpCatalogLogoDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogLogo;
import com.pchome.akbadm.db.service.BaseService;

public class PfpCatalogLogoService extends BaseService<PfpCatalogLogo, String> implements IPfpCatalogLogoService {
    @Override
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String status) {
        return ((IPfpCatalogLogoDAO) dao).selectPfpCustomerInfoByStatus(status);
    }

    @Override
    public List<Map<String, Object>> selectCatalogLogo(String pfpCustomerInfoId, String status, int pageNo, int pageSize) {
        return ((IPfpCatalogLogoDAO) dao).selectCatalogLogo(pfpCustomerInfoId, status, pageNo, pageSize);
    }

    @Override
    public int selectCount(String pfpCustomerInfoId, String status) {
        return ((IPfpCatalogLogoDAO) dao).selectCount(pfpCustomerInfoId, status);
    }
}
