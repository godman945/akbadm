package com.pchome.akbadm.db.service.ad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdSpecificWebsiteDAO;
import com.pchome.akbadm.db.pojo.PfpAdSpecificWebsite;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdSpecificWebsiteService extends BaseService<PfpAdSpecificWebsite, String> implements IPfpAdSpecificWebsiteService {
    @Override
    public Map<String, String[]> selectCategoryCodeMap() {
        Map<String, List<String>> tempMap = new HashMap<>();
        String actionId = null;
        List<String> categoryCodeList = null;

        List<PfpAdSpecificWebsite> list = ((IPfpAdSpecificWebsiteDAO)dao).loadAll();
        for (PfpAdSpecificWebsite bean: list) {
            actionId = bean.getPfpAdAction().getAdActionSeq();
            categoryCodeList = tempMap.get(actionId);
            if (categoryCodeList == null) {
                categoryCodeList = new ArrayList();
            }
            categoryCodeList.add(bean.getPfbxWebsiteCategory().getCode());
            tempMap.put(actionId, categoryCodeList);
        }

        Map<String, String[]> map = new HashMap<>();
        for (String key: tempMap.keySet()) {
            map.put(key, tempMap.get(key).toArray(new String[0]));
        }

        return map;
    }
}