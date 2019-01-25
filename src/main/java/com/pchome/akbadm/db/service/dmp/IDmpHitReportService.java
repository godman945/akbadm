package com.pchome.akbadm.db.service.dmp;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.pojo.DmpHitReport;
import com.pchome.akbadm.db.service.IBaseService;

public interface IDmpHitReportService extends IBaseService<DmpHitReport, String> {
    public List<DmpHitReport> getByFullDate(Date date);

    public int deleteByRecordDate(String recordDate);
}
