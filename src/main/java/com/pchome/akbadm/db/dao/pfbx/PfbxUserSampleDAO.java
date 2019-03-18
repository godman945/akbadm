package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserSample;

public class PfbxUserSampleDAO extends BaseDAO<PfbxUserSample, String> implements IPfbxUserSampleDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<PfbxUserSample> selectPfbxUserSampleByStatus(String status) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxUserSample ");
        hql.append("where pfbxCustomerInfo.status = ? ");
        hql.append("order by sort desc ");

        return (List<PfbxUserSample>) this.getHibernateTemplate().find(hql.toString(), status);
    }
}
