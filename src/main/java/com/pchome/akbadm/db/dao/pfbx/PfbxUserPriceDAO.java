package com.pchome.akbadm.db.dao.pfbx;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbxUserPrice;

public class PfbxUserPriceDAO extends BaseDAO<PfbxUserPrice, String> implements IPfbxUserPriceDAO {
    @SuppressWarnings("unchecked")
    public List<PfbxUserPrice> selectPfbxUserPriceByStatus(String status) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbxUserPrice ");
        hql.append("where pfbxCustomerInfo.status = ? ");

        return this.getHibernateTemplate().find(hql.toString(), status);
    }
}
