package com.pchome.akbadm.db.dao.dmp;

import java.util.List;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.DmpHitReport;

public interface IDmpHitReportDAO extends IBaseDAO<DmpHitReport, String> {
    public List<DmpHitReport> getByRecordDate(String startDate, String endDate);

    public int deleteByRecordDate(String recordDate);
}
