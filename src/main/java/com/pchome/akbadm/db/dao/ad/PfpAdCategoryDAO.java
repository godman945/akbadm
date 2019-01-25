package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdCategory;

public class PfpAdCategoryDAO extends BaseDAO<PfpAdCategory,String> implements IPfpAdCategoryDAO {

    @SuppressWarnings("unchecked")
    public List<PfpAdCategory> getAllPfpAdCategory() throws Exception {
        return super.getHibernateTemplate().find("from PfpAdCategory");
    }
}
