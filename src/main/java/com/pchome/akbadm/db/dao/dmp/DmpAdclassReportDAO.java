package com.pchome.akbadm.db.dao.dmp;

import java.util.List;

import org.hibernate.Query;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.DmpAdclassReport;

public class DmpAdclassReportDAO extends BaseDAO<DmpAdclassReport, String> implements IDmpAdclassReportDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<DmpAdclassReport> getByRecordDate(String recordDate, int firstResult, int maxResults) {
        String hql = "from DmpAdclassReport where recordDate = ? order by counter desc";

        Query query = this.getSession().createQuery(hql.toString());
        query.setParameter(0, recordDate);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);

        return query.list();
    }

    @Override
    public int deleteByRecordDate(String recordDate) {
        String hql = "delete from DmpAdclassReport where recordDate = ?";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate);
    }
}
