package com.pchome.akbadm.db.service.dmp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.pojo.DmpAdclassReport;
import com.pchome.akbadm.db.service.IBaseService;

public interface IDmpAdclassReportService extends IBaseService<DmpAdclassReport, String> {
    public Map<String, List<DmpAdclassReport>> getByFullDate(Date recordDate, int firstResult, int maxResults);

    public int deleteByRecordDate(String recordDate);
}
