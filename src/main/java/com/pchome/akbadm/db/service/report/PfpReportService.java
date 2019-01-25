package com.pchome.akbadm.db.service.report;

import java.util.Date;

import com.pchome.akbadm.db.dao.report.IPfpReportDAO;
import com.pchome.akbadm.db.pojo.PfpReport;
import com.pchome.akbadm.db.service.BaseService;

public class PfpReportService extends BaseService<PfpReport, Integer> implements IPfpReportService {
    public int deletePfpReport(Date reportDate) {
        return ((IPfpReportDAO) dao).deletePfpReport(reportDate);
    }
}
