package com.pchome.akbadm.quartzs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.service.ad.AMaliceService;
import com.pchome.akbadm.factory.malice.EnumMaliceType;
import com.pchome.akbadm.factory.malice.MaliceFactory;
import com.pchome.config.TestConfig;

@Deprecated
@Transactional
public class MaliceJob {
    private Logger log = LogManager.getRootLogger();

    private MaliceFactory maliceFactory;
    private AMaliceService maliceService;

    // last date & hour
    public void execute() throws Exception {
        PfpAdPvclk pfpAdPvclk = this.maliceService.getPfpAdPvclkService().findLastPfpAdPvclk();
        if (pfpAdPvclk == null) {
            log.info("pfpAdPvclk = null");
            return;
        }

        this.execute(pfpAdPvclk.getAdPvclkDate(), pfpAdPvclk.getAdPvclkTime());
    }

    // 24 hours of the date
    public void execute(Date date) throws Exception {
        for (int i = 0; i < 24; i++) {
            execute(date, i);
        }
    }

    // specify date & hour
    public void execute(Date date, int hour) throws Exception {
        log.info("==== MaliceJob process start ====");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        log.info(df.format(date) + " " + hour);

        // get ad_click
        List<PfpAdClick> pfpAdClickList = this.maliceService.getPfpAdClickService().findPfpAdClick(date, hour, 0);
        log.info("pfpAdClickList.size() = " + pfpAdClickList.size());
        if (pfpAdClickList.size() == 0) {
            return;
        }

        // delete old data
        maliceService.delete(date, hour);

        // malice 5
        pfpAdClickList = maliceFactory.getInstance(EnumMaliceType.OVER_HOUR_LIMIT).execute(pfpAdClickList);

        // malice 6
        pfpAdClickList = maliceFactory.getInstance(EnumMaliceType.OVER_PADDING).execute(pfpAdClickList);

        // malice 7
        pfpAdClickList = maliceFactory.getInstance(EnumMaliceType.INSIDE_RECTANGLE).execute(pfpAdClickList);

        // malice 8
        pfpAdClickList = maliceFactory.getInstance(EnumMaliceType.OVER_PRICE).execute(pfpAdClickList);

        log.info("==== MaliceJob process end ====");
    }

    public void setMaliceFactory(MaliceFactory maliceFactory) {
        this.maliceFactory = maliceFactory;
    }

    public void setMaliceService(AMaliceService maliceService) {
        this.maliceService = maliceService;
    }

    public static void printUsage() {
        System.out.println("Usage: [DATE] [HOUR]");
        System.out.println();
        System.out.println("[DATE] format: yyyy-MM-dd");
    }

    public static void main(String[] args) throws Exception {
        Date date = null;
        int hour = 0;
        switch(args.length) {
        case 3:
            hour  = Integer.parseInt(args[2]);
        case 2:
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
            date = sdf.parse(args[1]);
        case 1:
            break;
        default:
            printUsage();
            return;
        }

        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        MaliceJob job = context.getBean(MaliceJob.class);
        switch(args.length) {
        case 3:
            job.execute(date, hour);
            break;
        case 2:
            job.execute(date);
            break;
        case 1:
            job.execute();
            break;
        }
    }
}
