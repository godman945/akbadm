package com.pchome.akbadm.db.dao.dmp;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.DmpAdclassReport;

public interface IDmpAdclassReportDAO extends IBaseDAO<DmpAdclassReport, String> {
    public List<DmpAdclassReport> getByRecordDate(String recordDate, int firstResult, int maxResults);

    public int deleteByRecordDate(String recordDate);
}
