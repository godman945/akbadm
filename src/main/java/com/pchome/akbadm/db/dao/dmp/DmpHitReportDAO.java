package com.pchome.akbadm.db.dao.dmp;

import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.DmpHitReport;

public class DmpHitReportDAO extends BaseDAO<DmpHitReport, String> implements IDmpHitReportDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<DmpHitReport> getByRecordDate(String startDate, String endDate) {
        String hql = "from DmpHitReport where recordDate >= ? and recordDate <= ?";
        return this.getHibernateTemplate().find(hql, startDate, endDate);
    }

    @Override
    public int deleteByRecordDate(String recordDate) {
        String hql = "delete from DmpHitReport where recordDate = ?";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate);
    }
}
