package com.pchome.akbadm.db.service.dmp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pchome.akbadm.db.dao.dmp.IDmpAdclassReportDAO;
import com.pchome.akbadm.db.pojo.DmpAdclassReport;
import com.pchome.akbadm.db.service.BaseService;

public class DmpAdclassReportService extends BaseService<DmpAdclassReport, String> implements IDmpAdclassReportService {
    @Override
    public Map<String, List<DmpAdclassReport>> getByFullDate(Date recordDate, int firstResult, int maxResults) {
        Map<String, List<DmpAdclassReport>> map = new HashMap<String, List<DmpAdclassReport>>();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(recordDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = null;

        List<DmpAdclassReport> dmpAdclassReportList = null;
        for (int i = 0; i < day; i++) {
            date = df.format(calendar.getTime());
            dmpAdclassReportList = ((IDmpAdclassReportDAO) dao).getByRecordDate(date, firstResult, maxResults);
            map.put(date, dmpAdclassReportList);

            log.info(date + " " + dmpAdclassReportList.size());

            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        return map;
    }

    @Override
    public int deleteByRecordDate(String recordDate) {
        return ((IDmpAdclassReportDAO) dao).deleteByRecordDate(recordDate);
    }
}
