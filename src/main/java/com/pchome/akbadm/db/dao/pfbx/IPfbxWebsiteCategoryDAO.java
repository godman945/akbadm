package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;

public interface IPfbxWebsiteCategoryDAO extends IBaseDAO<PfbxWebsiteCategory, String> {
	public List<PfbxWebsiteCategory> findPfbxWebsiteCategoryAll();

    public List<PfbxWebsiteCategory> findByCode(String code);
    
    public List<PfbxWebsiteCategory> findChildByCode(String parentId);
    
    public List<PfbxWebsiteCategory> findById(String id);
    
    public Integer getNewId();
    
    public List<PfbxWebsiteCategory> getFirstLevelPfbxWebsiteCategory();
}
