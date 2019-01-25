package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.pojo.PfpAdCategoryNew;

public interface IPfpAdCategoryNewDAO {
    // 查詢所有廣告
    public List<PfpAdCategoryNew> findPfpAdCategoryNewAll();

    public List<PfpAdCategoryNew> findByCode(String code);
    
    public List<PfpAdCategoryNew> findChildByCode(String parentId);
    
    public List<PfpAdCategoryNew> findById(String id);
    
    public Integer getNewId();
    
    public void saveOrUpdate(PfpAdCategoryNew pfpAdCategoryNew);
    
    public List<PfpAdCategoryNew> getFirstLevelPfpAdCategoryNew();
}
