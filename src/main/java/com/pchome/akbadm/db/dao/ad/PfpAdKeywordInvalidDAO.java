package com.pchome.akbadm.db.dao.ad;

import java.util.Date;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.PfpAdKeywordInvalid;

public class PfpAdKeywordInvalidDAO extends BaseDAO<PfpAdKeywordInvalid, String> implements IPfpAdKeywordInvalidDAO {
    @Override
    public int deleteMalice(Date recordDate, int recordTime) {
        String hql = "delete from PfpAdKeywordInvalid where adKeywordInvalidDate = ? and adKeywordInvalidTime = ? and adKeywordInvalidClk > 0";
        return this.getHibernateTemplate().bulkUpdate(hql, recordDate, recordTime);
    }
}
