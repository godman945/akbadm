package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;

public interface IPfpAdCategoryMappingDAO extends IBaseDAO<PfpAdCategoryMapping,String> {
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdSeq(String adSeq);
    
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByCode(String code);
}
