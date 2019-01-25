package com.pchome.akbadm.db.service.pfbx;

import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfbxWebsiteCategoryService extends IBaseService<PfbxWebsiteCategory,String> {
	public Map<String, List<Map<String, String>>> findPfbxWebsiteCategoryAll();

    public Map<String,String> getPfbxWebsiteCategoryNameByCodeMap();

    public List<PfbxWebsiteCategory> getPfbxWebsiteCategoryAll();
    
    public PfbxWebsiteCategory findByCode(String code);
    
    public List<PfbxWebsiteCategory> findChildByCode(String parentId);
    
    public PfbxWebsiteCategory findById(String id);
    
    public Integer getNewId();
    
    public List<PfbxWebsiteCategory> getFirstLevelPfpPfbxWebsiteCategory();
}
