package com.pchome.akbadm.db.service.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpCatalogLogo;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpCatalogLogoService extends IBaseService<PfpCatalogLogo, String> {
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String status);

    public List<Map<String, Object>> selectCatalogLogo(String pfpCustomerInfoId, String status, int pageNo, int pageSize);

    public int selectCount(String pfpCustomerInfoId, String status);
}
