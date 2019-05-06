package com.pchome.akbadm.db.service.ad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.ad.IPfpAdCategoryMappingDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.service.BaseService;

public class PfpAdCategoryMappingService extends BaseService<PfpAdCategoryMapping, String> implements IPfpAdCategoryMappingService {
    @Override
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdSeq(String adSeq) {
        return ((IPfpAdCategoryMappingDAO)dao).selectPfpAdCategoryMappingByAdSeq(adSeq);
    }

    @Override
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdCode(String code) {
        return ((IPfpAdCategoryMappingDAO)dao).selectPfpAdCategoryMappingByCode(code);
    }

    @Override
    public Map<String, StringBuilder> selectPfpAdCategoryMappingBufferMaps() {
        Map<String, StringBuilder> map = new HashMap<>();
        StringBuilder adClass = null;

        List<PfpAdCategoryMapping> list = ((IPfpAdCategoryMappingDAO)dao).loadAll();
        for (PfpAdCategoryMapping bean: list) {
            adClass = map.get(bean.getAdSeq());
            if (adClass == null) {
                adClass = new StringBuilder();
            }
            if (adClass.length() > 0) {
                adClass.append(";");
            }
            adClass.append(bean.getCode());
            map.put(bean.getAdSeq(), adClass);
        }

        return map;
    }
}
