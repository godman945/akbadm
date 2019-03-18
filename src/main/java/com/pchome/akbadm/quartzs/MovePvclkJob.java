package com.pchome.akbadm.quartzs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.service.ad.IPfpAdPvclkRefererService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.config.TestConfig;
import com.pchome.soft.util.SpringEmailUtil;

public class MovePvclkJob {
    private Logger log = LogManager.getRootLogger();

    private String mailSubject;
    private String mailFrom;
    private String[] mailTo;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpAdPvclkRefererService pfpAdPvclkRefererService;
    private SpringEmailUtil springEmailUtil;

    public void process(Date date) throws Exception {
        log.info("====MovePvclkJob.process() start====");

        // pvclk
        StringBuffer content = new StringBuffer();
        boolean mailFlag = false;
        try {
            mailFlag = pfpAdPvclkService.insertSelect(date);
            if (!mailFlag) {
                content.append("move pvclk warning<br>\r\n");
            }
        }
        catch (Exception e) {
            log.error(date, e);
            content.append(e.getMessage());
        }

        // pvclk_referer
        mailFlag = false;
        try {
            mailFlag = pfpAdPvclkRefererService.insertSelect(date);
            if (!mailFlag) {
                content.append("move pvclk_referer warning<br>\r\n");
            }
        }
        catch (Exception e) {
            log.error(date, e);
            content.append(e.getMessage());
        }

        if (StringUtils.isNotBlank(content.toString())) {
            springEmailUtil.sendHtmlEmail(mailSubject, mailFrom, mailTo, null, content.toString());
        }

        log.info("====MovePvclkJob.process() end====");
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }

    public void setPfpAdPvclkRefererService(IPfpAdPvclkRefererService pfpAdPvclkRefererService) {
        this.pfpAdPvclkRefererService = pfpAdPvclkRefererService;
    }

    public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
        this.springEmailUtil = springEmailUtil;
    }

    public static void printUsage() {
        System.out.println("Usage: [prd|stg] [DATE]");
        System.out.println("DATE (option, format: yyyy-MM-dd)");
        System.out.println("If no date, use earliest date before 6 months ago");
    }

    public static void main(String[] args) throws Exception {
        Date date = null;

        if (args == null) {
            return;
        }

        if (args.length == 1) {}
        else if (args.length == 2) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.parse(args[1]);
        }
        else {
            printUsage();
            return;
        }

        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        MovePvclkJob job = context.getBean(MovePvclkJob.class);
        job.process(date);
    }
}
