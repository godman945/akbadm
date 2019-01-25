package com.pchome.akbadm.db.dao.report;

import java.util.Date;

import com.pchome.akbadm.db.dao.IBaseDAO;
import com.pchome.akbadm.db.pojo.PfpReport;

public interface IPfpReportDAO extends IBaseDAO<PfpReport, Integer> {
    public int deletePfpReport(Date reportDate);
}
