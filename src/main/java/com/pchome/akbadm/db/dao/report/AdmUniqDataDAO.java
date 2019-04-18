package com.pchome.akbadm.db.dao.report;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.BaseDAO;
import com.pchome.akbadm.db.pojo.AdmUniqData;

public class AdmUniqDataDAO extends BaseDAO<AdmUniqData, String> implements IAdmUniqDataDAO {
    @Override
    @SuppressWarnings("unchecked")
    public List<AdmUniqData> select(Date recordDate, String adId) {
        String hql = "from AdmUniqData where recordDate = ? and uniqName = ?";
        return (List<AdmUniqData>) this.getHibernateTemplate().find(hql, recordDate, adId);
    }
}
