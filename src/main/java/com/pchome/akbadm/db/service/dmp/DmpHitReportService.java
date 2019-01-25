package com.pchome.akbadm.db.service.dmp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.db.dao.dmp.IDmpHitReportDAO;
import com.pchome.akbadm.db.pojo.DmpHitReport;
import com.pchome.akbadm.db.service.BaseService;

public class DmpHitReportService extends BaseService<DmpHitReport, String> implements IDmpHitReportService {
    @Override
    public List<DmpHitReport> getByFullDate(Date date) {
        List<DmpHitReport> list = new ArrayList<DmpHitReport>();

        // end date
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String endDate = df.format(date);

        // start date
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = df.format(calendar.getTime());

        List<DmpHitReport> dmpHitReportList = ((IDmpHitReportDAO) dao).getByRecordDate(startDate, endDate);

        DmpHitReport dmpHitReport = null;
        String recordDate = null;

        calendar.setTime(date);
        for (int i = 0; i < day; i++) {
            dmpHitReport = null;
            recordDate = df.format(calendar.getTime());

            for (DmpHitReport report: dmpHitReportList) {
                if (report.getRecordDate().equals(recordDate)) {
                    dmpHitReport = report;
                    break;
                }
            }

            if (dmpHitReport == null) {
                dmpHitReport = new DmpHitReport();
                dmpHitReport.setRecordDate(recordDate);
            }
            list.add(dmpHitReport);

            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return list;
    }

    @Override
    public int deleteByRecordDate(String recordDate) {
        return ((IDmpHitReportDAO) dao).deleteByRecordDate(recordDate);
    }
}
