package com.pchome.akbadm.db.dao.ad;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfbStyleInfo;

public class PfbStyleInfoDAO extends BaseDAO<PfbStyleInfo, String> implements IPfbStyleInfoDAO {
    @SuppressWarnings("unchecked")
    public List<PfbStyleInfo> selectValidPfbStyleInfo() {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfbStyleInfo ");
        hql.append("where status = 1 ");
        hql.append("    and pfbWebInfo.status = 1");

        return (List<PfbStyleInfo>) super.getHibernateTemplate().find(hql.toString());
    }
}