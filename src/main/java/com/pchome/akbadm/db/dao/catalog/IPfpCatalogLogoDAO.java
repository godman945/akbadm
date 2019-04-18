package com.pchome.akbadm.db.dao.catalog;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpCatalogLogo;

public interface IPfpCatalogLogoDAO extends IBaseDAO<PfpCatalogLogo, String> {
    public List<Map<String, Object>> selectPfpCustomerInfoByStatus(String status);

    public List<Map<String, Object>> selectCatalogLogo(String pfpCustomerInfoId, String status, int pageNo, int pageSize);

    public int selectCount(String pfpCustomerInfoId, String status);
}
