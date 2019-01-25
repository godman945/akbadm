package com.pchome.akbadm.db.service.ad;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfpAdCategoryNew;

public interface IPfpAdCategoryNewService {
    public Map<String, List<Map<String, String>>> findPfpAdCategoryNewAll();

    public Map<String,String> getPfpAdCategoryNewNameByCodeMap();

    public PfpAdCategoryNew findByCode(String code);
    
    public List<PfpAdCategoryNew> findChildByCode(String parentId);
    
    public PfpAdCategoryNew findById(String id);
    
    public Integer getNewId();
    
    public void saveOrUpdate(PfpAdCategoryNew pfpAdCategoryNew);
    
    public List<PfpAdCategoryNew> getFirstLevelPfpAdCategoryNew();
}
