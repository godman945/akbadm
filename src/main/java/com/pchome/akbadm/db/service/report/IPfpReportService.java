package com.pchome.akbadm.db.service.report;

import java.util.Date;

import com.pchome.akbadm.db.pojo.PfpReport;
import com.pchome.akbadm.db.service.IBaseService;

public interface IPfpReportService extends IBaseService<PfpReport, Integer> {
    public int deletePfpReport(Date reportDate);
}
