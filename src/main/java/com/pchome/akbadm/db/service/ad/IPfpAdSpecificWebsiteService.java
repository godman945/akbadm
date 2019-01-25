package com.pchome.akbadm.db.service.ad;

import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdSpecificWebsite;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdSpecificWebsiteService extends IBaseService <PfpAdSpecificWebsite, String> {
    public Map<String, String[]> selectCategoryCodeMap();
}
