package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategoryMapping;

public class PfpAdCategoryMappingDAO extends BaseDAO<PfpAdCategoryMapping,String> implements IPfpAdCategoryMappingDAO {
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByAdSeq(String adSeq) {
        String hql = "from PfpAdCategoryMapping where ad_seq = ? order by code";
        return (List<PfpAdCategoryMapping>) this.getHibernateTemplate().find(hql, adSeq);
    }
    
    @SuppressWarnings("unchecked")
    public List<PfpAdCategoryMapping> selectPfpAdCategoryMappingByCode(String code) {
        String hql = "from PfpAdCategoryMapping where code like ? ";
        code ="%" + code + "%";
        return (List<PfpAdCategoryMapping>) this.getHibernateTemplate().find(hql, code);
    }
}
