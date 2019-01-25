package com.pchome.akbadm.quartzs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.DmpAdclassReport;
import com.pchome.akbadm.db.pojo.DmpHitReport;
import com.pchome.akbadm.db.service.ad.IPfpAdCategoryNewService;
import com.pchome.akbadm.db.service.dmp.IDmpAdclassReportService;
import com.pchome.akbadm.db.service.dmp.IDmpHitReportService;
import com.pchome.config.TestConfig;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

public class DmpHitReportJob {
    private Log log = LogFactory.getLog(getClass());

    private IDmpAdclassReportService dmpAdclassReportService;
    private IDmpHitReportService dmpHitReportService;
    private IPfpAdCategoryNewService pfpAdCategoryNewService;
    private SpringEmailUtil springEmailUtil;
    private String subject;
    private String mailTestFrom;
    private String[] mailTestTo;
    private String savePath;

    private String rcode = "P158";

    @Transactional
    private void calculate(Date date) {
        date = yesterdayIfNull(date);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String recordDate = df.format(date);
        Calendar calendar = Calendar.getInstance();

        log.info(recordDate);

        File dir = new File(savePath + File.separator + recordDate);
        if (!dir.exists()) {
            log.info(dir.getPath() + " not exist");
            return;
        }

        String line = null;
        String[] lines = null;
        String idType = null;
//        String memidOrUuid = null;
        String behavior = null;
        String adClass = null;
        int count = 0;

        int totalPv = 0;
        int hitDmpMemid = 0;
        int hitDmpUuid = 0;
        int hitAdMemid = 0;
        int hitAdUuid = 0;
        int hitBehavior1 = 0;
        int hitBehavior2 = 0;
        Map<String, Integer> adclassMap = new HashMap<String, Integer>();
        Integer adclassCount = null;

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        for (File hostDir: dir.listFiles()) {
            for (File file: hostDir.listFiles()) {
                try {
                    fileReader = new FileReader(file);
                    bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        lines = line.split(",");
                        if (lines.length < 5) {
                            continue;
                        }

                        idType = lines[0];
//                        memidOrUuid = lines[1];
                        behavior = lines[2];
                        adClass = lines[3];
                        count = Integer.parseInt(lines[4]);

                        // DmpHitReport
                        totalPv += count;

                        if ("memid".equals(idType)) {
                            hitDmpMemid += count;

                            if (StringUtils.isNotBlank(behavior)) {
                                hitAdMemid += count;
                            }
                        }
                        else if ("uuid".equals(idType)) {
                            hitDmpUuid += count;

                            if (StringUtils.isNotBlank(behavior)) {
                                hitAdUuid += count;
                            }
                        }

                        if ("ad_click".equals(behavior)) {
                            hitBehavior1 += count;
                        }
                        else if (StringUtils.isNotBlank(behavior)) {
                            hitBehavior2 += count;
                        }

                        // DmpAdclassReport
                        if (StringUtils.isNotBlank(adClass)) {
                            adclassCount = adclassMap.get(adClass);
                            if (adclassCount == null) {
                                adclassCount = 0;
                            }
                            adclassCount += count;
                            adclassMap.put(adClass, adclassCount);
                        }
                    }
                }
                catch (Exception e) {
                    log.error(file.getPath() + "\n" + line, e);
                }
                finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        }
                        catch (Exception e) {
                            log.error(file.getPath(), e);
                        }
                    }

                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        }
                        catch (Exception e) {
                            log.error(file.getPath(), e);
                        }
                    }
                }
            }
        }

        // DmpHitReport
        DmpHitReport dmpHitReport = new DmpHitReport();
        dmpHitReport.setTotalPv(totalPv);
        dmpHitReport.setHitDmpMemid(hitDmpMemid);
        dmpHitReport.setHitDmpUuid(hitDmpUuid);
        dmpHitReport.setHitDmpUuid(hitDmpUuid);
        dmpHitReport.setHitAdMemid(hitAdMemid);
        dmpHitReport.setHitAdUuid(hitAdUuid);
        dmpHitReport.setHitBehavior1(hitBehavior1);
        dmpHitReport.setHitBehavior2(hitBehavior2);
        dmpHitReport.setRecordDate(recordDate);
        dmpHitReport.setUpdateDate(calendar.getTime());
        dmpHitReport.setCreateDate(calendar.getTime());

        int num = dmpHitReportService.deleteByRecordDate(recordDate);
        dmpHitReportService.save(dmpHitReport);

        log.info("DmpHitReport delete " + num);

        // DmpAdclassReport
        List<DmpAdclassReport> dmpAdclassReportList = new ArrayList<DmpAdclassReport>();
        DmpAdclassReport dmpAdclassReport = null;
        for (String key: adclassMap.keySet()) {
            dmpAdclassReport = new DmpAdclassReport();
            dmpAdclassReport.setAdclass(key);
            dmpAdclassReport.setCounter(adclassMap.get(key));
            dmpAdclassReport.setRecordDate(recordDate);
            dmpAdclassReport.setUpdateDate(calendar.getTime());
            dmpAdclassReport.setCreateDate(calendar.getTime());
            dmpAdclassReportList.add(dmpAdclassReport);
        }

        num = dmpAdclassReportService.deleteByRecordDate(recordDate);
        log.info("DmpAdclassReport delete " + num);

        dmpAdclassReportService.saveOrUpdateAll(dmpAdclassReportList);
        log.info("DmpAdclassReport update " + dmpAdclassReportList.size());
    }

    private void send(Date date, boolean testFlag) throws Exception {
        date = yesterdayIfNull(date);

        SimpleDateFormat cdf = new SimpleDateFormat("yyyy年MM月dd日");

        Mail mail = null;
        if (testFlag) {
            mail = new Mail();
            mail.setMailFrom(mailTestFrom);
            mail.setMailTo(mailTestTo);
            mail.setRname(subject + " " + cdf.format(date));
        }
        else {
            mail = PortalcmsUtil.getInstance().getMail(rcode);
            mail.setRname(mail.getRname() + " " + cdf.format(date));
        }

        springEmailUtil.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), getContent(date));

        log.info("rname: " + mail.getRname());
        if (mail.getMailTo() != null) {
            for (String to: mail.getMailTo()) {
                log.info("mail to: " + to);
            }
        }
        if (mail.getMailBcc() != null) {
            for (String bcc: mail.getMailBcc()) {
                log.info("mail bcc: " + bcc);
            }
        }
    }

    private void execute(Date date, boolean testFlag) throws Exception {
        date = yesterdayIfNull(date);

        calculate(date);
        send(date, testFlag);
    }

    private Date yesterdayIfNull(Date date) {
        if (date == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            date = calendar.getTime();
        }

        return date;
    }

    private String getContent(Date date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat cdf = new SimpleDateFormat("yyyy年MM月dd日");

        log.info(df.format(date));

        Calendar calendar = Calendar.getInstance();
        String[] table1ths = {
            "廣告總曝光數",
            "命中DMP-UUID 廣告曝光數",
            "命中DMP-UUID 廣告命中率",
            "命中DMP-PCID 廣告曝光數",
            "命中DMP-PCID 廣告命中率",
            "命中DMP收集類型一 廣告曝光數",
            "命中DMP收集類型一 廣告命中率",
            "命中DMP收集類型二 廣告曝光數",
            "命中DMP收集類型二 廣告命中率",
            "命中DMP-UUID 廣告撥出數",
            "命中DMP-UUID 廣告命中率",
            "命中DMP-PCID 廣告撥出數",
            "命中DMP-PCID 廣告命中率",
            "廣告命中分類 第一名",
            "分類第一名廣告撥出數",
            "廣告命中分類 第二名",
            "分類第二名廣告撥出數",
            "廣告命中分類 第三名",
            "分類第三名廣告撥出數",
            "廣告命中分類 第四名",
            "分類第四名廣告撥出數",
            "廣告命中分類 第五名",
            "分類第五名廣告撥出數",
            "廣告命中分類 第六名",
            "分類第六名廣告撥出數",
            "廣告命中分類 第七名",
            "分類第七名廣告撥出數",
            "廣告命中分類 第八名",
            "分類第八名廣告撥出數",
            "廣告命中分類 第九名",
            "分類第九名廣告撥出數",
            "廣告命中分類 第十名",
            "分類第十名廣告撥出數"
        };

        int tdWidth = 100;
        int table1Width = (2 + table1ths.length) * tdWidth;  // table1 width
        int table2Width = 0;  // table2 width
        int tableWidth = Math.max(table1Width, table2Width);     // table width

        // html start
        StringBuffer content = new StringBuffer();
        content.append("<html><head>\n");
        content.append("<title>").append(subject).append(" ").append(cdf.format(calendar.getTime())).append("</title>\n");
        content.append("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>\n");
        content.append("<style type='text/css'>\n");
        content.append("<!--\n");
        content.append("tr {font-size: 13px; font-family: Verdana, Arial, Helvetica, sans-serif};\n");
        content.append("-->\n");
        content.append("</style>\n");
        content.append("</head>\n");
        content.append("<body bgcolor='#ffffff'>\n");
        content.append("<table width='").append(tableWidth).append("'>\n");
        content.append("<tr><td>\n");
        content.append("<table width='").append(table1Width).append("'><tr><td align='left' bgcolor='#6f6f6f'><font color='#ffffff'><b>").append(subject).append(" ").append(cdf.format(date)).append("</font></b></td></tr></table><br>\n");

        // table1 start
        content.append("<table width='").append(table1Width).append("' border='0'>\n");
        content.append("<tr bgcolor='#6f6f6f'>\n");
        content.append("<td rowspan='2' align='center' width='").append(tdWidth).append("'><font color='#ffffff'>日期</font></td>\n");
        content.append("<td rowspan='2' align='center' width='").append(tdWidth).append("'><font color='#ffffff'>星期</font></td>\n");
        for (String th: table1ths) {
            content.append("<td rowspan='2' align='center' width='").append(tdWidth).append("'><font color='#ffffff'>").append(th).append("</font></td>\n");
        }
        content.append("</tr>\n");
        content.append("<tr bgcolor='#6f6f6f'>\n");
        content.append("</tr>\n");

        // day value
        int limit = 10;
        List<DmpHitReport> dmpHitReportList = dmpHitReportService.getByFullDate(date);
        Map<String, List<DmpAdclassReport>> dmpAdclassReportFullMap = dmpAdclassReportService.getByFullDate(date, 0, limit);
        Map<String,String> adCategoryNewMap = pfpAdCategoryNewService.getPfpAdCategoryNewNameByCodeMap();
        List<DmpAdclassReport> dmpAdclassReportList = null;
        String categoryName = null;
        Map<String, Integer> dateMap = new HashMap<String, Integer>();
        Map<String, Integer> monthMap = new HashMap<String, Integer>();
        Calendar dateCalendar = Calendar.getInstance();
        NumberFormat numberFormat = new DecimalFormat("#,##0");
        NumberFormat decimalFormat = new DecimalFormat("#,##0.00%");
        String sDayOfWeek = "";
        String sTrColor = "";
        int count = dmpHitReportList.size() > 0 ? dmpHitReportList.size() : 1;
        double hitDmpMemidRate = 0d;
        double hitDmpUuidRate = 0d;
        double behavior1Rate = 0d;
        double behavior2Rate = 0d;
        double hitAdMemidRate = 0d;
        double hitAdUuidRate = 0d;

        // init
        for (String th: table1ths) {
            dateMap.put(th, 0);
            monthMap.put(th, 0);
        }

        // day
        for (DmpHitReport dmpHitReport: dmpHitReportList) {
            dateCalendar.setTime(df.parse(dmpHitReport.getRecordDate()));

            switch(dateCalendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                sDayOfWeek = "日";
                sTrColor = "#ffcccc";
                break;
            case 2:
                sDayOfWeek = "一";
                sTrColor = "#E1FCFF";
                break;
            case 3:
                sDayOfWeek = "二";
                sTrColor = "#E1FCFF";
                break;
            case 4:
                sDayOfWeek = "三";
                sTrColor = "#E1FCFF";
                break;
            case 5:
                sDayOfWeek = "四";
                sTrColor = "#E1FCFF";
                break;
            case 6:
                sDayOfWeek = "五";
                sTrColor = "#E1FCFF";
                break;
            case 7:
                sDayOfWeek = "六";
                sTrColor = "#ffcccc";
                break;
            }

            // DmpHitReport
            hitDmpMemidRate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitDmpMemid() / (double) dmpHitReport.getTotalPv() : 0;
            hitDmpUuidRate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitDmpUuid() / (double) dmpHitReport.getTotalPv() : 0;
            behavior1Rate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitBehavior1() / (double) dmpHitReport.getTotalPv() : 0;
            behavior2Rate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitBehavior2() / (double) dmpHitReport.getTotalPv() : 0;
            hitAdMemidRate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitAdMemid() / (double) dmpHitReport.getTotalPv() : 0;
            hitAdUuidRate = dmpHitReport.getTotalPv() > 0 ? dmpHitReport.getHitAdUuid() / (double) dmpHitReport.getTotalPv() : 0;

            content.append("<tr bgcolor='").append(sTrColor).append("'>\n");
            content.append("<td align='center'><nobr>").append(dmpHitReport.getRecordDate()).append("</nobr></td>\n");
            content.append("<td align='center'>").append(sDayOfWeek).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getTotalPv())).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitDmpMemid())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(hitDmpMemidRate)).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitDmpUuid())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(hitDmpUuidRate)).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitBehavior1())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(behavior1Rate)).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitBehavior2())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(behavior2Rate)).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitAdMemid())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(hitAdMemidRate)).append("</td>\n");
            content.append("<td align='right'>").append(numberFormat.format(dmpHitReport.getHitAdUuid())).append("</td>\n");
            content.append("<td align='right'>").append(decimalFormat.format(hitAdUuidRate)).append("</td>\n");

            dateMap.put(table1ths[0], dateMap.get(table1ths[0]) + dmpHitReport.getTotalPv());
            dateMap.put(table1ths[1], dateMap.get(table1ths[1]) + dmpHitReport.getHitDmpMemid());
            dateMap.put(table1ths[3], dateMap.get(table1ths[3]) + dmpHitReport.getHitDmpUuid());
            dateMap.put(table1ths[5], dateMap.get(table1ths[5]) + dmpHitReport.getHitBehavior1());
            dateMap.put(table1ths[7], dateMap.get(table1ths[7]) + dmpHitReport.getHitBehavior2());
            dateMap.put(table1ths[9], dateMap.get(table1ths[9]) + dmpHitReport.getHitAdMemid());
            dateMap.put(table1ths[11], dateMap.get(table1ths[11]) + dmpHitReport.getHitAdUuid());

            // DmpAdclassReport
            dmpAdclassReportList = dmpAdclassReportFullMap.get(dmpHitReport.getRecordDate());
            if (dmpAdclassReportList == null) {
                dmpAdclassReportList = new ArrayList<DmpAdclassReport>();
            }
            for (DmpAdclassReport dmpAdclassReport: dmpAdclassReportList) {
                categoryName = adCategoryNewMap.get(dmpAdclassReport.getAdclass());
                content.append("<td align='right'>").append(StringUtils.defaultIfEmpty(categoryName, dmpAdclassReport.getAdclass())).append("</td>\n");
                content.append("<td align='right'>").append(dmpAdclassReport.getCounter()).append("</td>\n");
            }
            for (int i = dmpAdclassReportList.size(); i < limit; i++) {
                content.append("<td align='right'></td>\n");
                content.append("<td align='right'></td>\n");
            }
            content.append("</tr>\n");
        }

        // sum
        hitDmpMemidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[1]) / (double) dateMap.get(table1ths[0]) : 0;
        hitDmpUuidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[3]) / (double) dateMap.get(table1ths[0]) : 0;
        behavior1Rate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[5]) / (double) dateMap.get(table1ths[0]) : 0;
        behavior2Rate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[7]) / (double) dateMap.get(table1ths[0]) : 0;
        hitAdMemidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[9]) / (double) dateMap.get(table1ths[0]) : 0;
        hitAdUuidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[11]) / (double) dateMap.get(table1ths[0]) : 0;

        content.append("<tr bgcolor='#66ccff'>\n");
        content.append("<td colspan='2' align='center'>加 總</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[0]))).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[1]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitDmpMemidRate)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[3]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitDmpUuidRate)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[5]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(behavior1Rate)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[7]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(behavior2Rate)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[9]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitAdMemidRate)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[11]))).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitAdUuidRate)).append("</td>\n");
        for (int i = 0; i < limit; i++) {
            content.append("<td align='right'></td>\n");
            content.append("<td align='right'></td>\n");
        }
        content.append("</tr>\n");

        // average
        hitDmpMemidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[1]) / (double) dateMap.get(table1ths[0]) : 0;
        hitDmpUuidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[3]) / (double) dateMap.get(table1ths[0]) : 0;
        behavior1Rate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[5]) / (double) dateMap.get(table1ths[0]) : 0;
        behavior2Rate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[7]) / (double) dateMap.get(table1ths[0]) : 0;
        hitAdMemidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[9]) / (double) dateMap.get(table1ths[0]) : 0;
        hitAdUuidRate = dateMap.get(table1ths[0]) > 0 ? dateMap.get(table1ths[11]) / (double) dateMap.get(table1ths[0]) : 0;

        content.append("<tr bgcolor='#66ccff'>\n");
        content.append("<td colspan='2' align='center'>平 均</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[0]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[1]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitDmpMemidRate / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[3]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitDmpUuidRate / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[5]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(behavior1Rate / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[7]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(behavior2Rate / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[9]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitAdMemidRate / count)).append("</td>\n");
        content.append("<td align='right'>").append(numberFormat.format(dateMap.get(table1ths[11]) / count)).append("</td>\n");
        content.append("<td align='right'>").append(decimalFormat.format(hitAdUuidRate / count)).append("</td>\n");
        for (int i = 0; i < limit; i++) {
            content.append("<td align='right'></td>\n");
            content.append("<td align='right'></td>\n");
        }
        content.append("</tr>\n");

        // table1 end
        content.append("</table><br>\n");

        // html end
        content.append("</tr></table>\n");
        content.append("</body></html>");

        return content.toString();
    }

    public void setDmpAdclassReportService(IDmpAdclassReportService dmpAdclassReportService) {
        this.dmpAdclassReportService = dmpAdclassReportService;
    }

    public void setDmpHitReportService(IDmpHitReportService dmpHitReportService) {
        this.dmpHitReportService = dmpHitReportService;
    }

    public void setPfpAdCategoryNewService(IPfpAdCategoryNewService pfpAdCategoryNewService) {
        this.pfpAdCategoryNewService = pfpAdCategoryNewService;
    }

    public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
        this.springEmailUtil = springEmailUtil;
    }

    public void setMailTestFrom(String mailTestFrom) {
        this.mailTestFrom = mailTestFrom;
    }

    public void setMailTestTo(String[] mailTestTo) {
        this.mailTestTo = mailTestTo;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static void printUsage() {
        System.out.println("Usage: [ENV] [DATE] [MOTION]");
        System.out.println();
        System.out.println("ENV=[local | stg | prd]");
        System.out.println("DATE (option, format: yyyy-MM-dd)");
        System.out.println("MOTION=[calculate | send | sendtest | execute] (option, if null, calculate and send)");
    }

    public static void main(String[] args) throws Exception {
        String motion = null;
        Date date = null;

        if (args != null) {
            switch (args.length) {
            case 3:
                motion = args[2];
            case 2:
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                date = df.parse(args[1]);
            }
        }
        else {
            printUsage();
            return;
        }

        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        DmpHitReportJob job = context.getBean(DmpHitReportJob.class);

        if ("calculate".equals(motion)) {
            job.calculate(date);
        }
        else if ("send".equals(motion)) {
            job.send(date, false);
        }
        else if ("sendtest".equals(motion)) {
            job.send(date, true);
        }
        else if ("execute".equals(motion) || (motion == null)) {
            job.execute(date, false);
        }
        else {
            printUsage();
        }
    }
}
