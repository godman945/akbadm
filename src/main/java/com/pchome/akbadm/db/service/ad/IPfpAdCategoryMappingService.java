package com.pchome.akbadm.db.service.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpAdCategoryMappingService extends IBaseService<PfpAdCategoryMapping, String> {
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdSeq(String adSeq);

    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdCode(String code);

    public Map<String, StringBuilder> selectPfpAdCategoryMappingBufferMaps();
}
