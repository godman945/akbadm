package com.pchome.akbadm.db.dao.report;

import java.util.Date;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpReport;

public class PfpReportDAO extends BaseDAO<PfpReport, Integer> implements IPfpReportDAO {
    public int deletePfpReport(Date reportDate) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete ");
        sql.append("from PfpReport ");
        sql.append("where reportTime = ? ");

        return this.getHibernateTemplate().bulkUpdate(sql.toString(), reportDate);
    }
}
