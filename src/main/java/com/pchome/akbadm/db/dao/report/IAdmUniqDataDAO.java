package com.pchome.akbadm.db.dao.report;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.AdmUniqData;

public interface IAdmUniqDataDAO extends IBaseDAO<AdmUniqData, String> {
    public List<AdmUniqData> select(Date recordDate, String adId);
}
