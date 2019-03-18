package com.pchome.akbadm.db.dao.mailbox;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpMailbox;

public class PfpMailboxDAO extends BaseDAO<PfpMailbox, Integer> implements IPfpMailboxDAO {
    @SuppressWarnings("unchecked")
    public List<PfpMailbox> selectPfpMailbox(String customerInfoId, String category) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpMailbox ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");

        return (List<PfpMailbox>) this.getHibernateTemplate().find(hql.toString(), customerInfoId, category);
    }

    @SuppressWarnings("unchecked")
    public List<PfpMailbox> selectPfpMailboxBySend(String send) {
        StringBuffer hql = new StringBuffer();
        hql.append("from PfpMailbox ");
        hql.append("where send = ? ");

        return (List<PfpMailbox>) this.getHibernateTemplate().find(hql.toString(), send);
    }

    public int deletePfpMailbox(String customerInfoId, String category) {
        StringBuffer hql = new StringBuffer();
        hql.append("delete from PfpMailbox ");
        hql.append("where customerInfoId = ? ");
        hql.append("    and category = ? ");

        return this.getHibernateTemplate().bulkUpdate(hql.toString(), customerInfoId, category);
    }
}