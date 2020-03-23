package com.pchome.akbadm.quartzs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.mail.MessagingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.config.TestConfig;
import com.pchome.soft.util.SpringEmailUtil;

public class CheckKernelJob {
    private Logger log = LogManager.getRootLogger();

    private String kernelAddata;
    private String mailSubject;
    private String mailFrom;
    private String[] mailTo;
    private SpringEmailUtil springEmailUtil;

    public void process() {
        log.info("====CheckKernelJob.process() start====");

        File lockFile = new File(kernelAddata + File.separator + "lock.txt");
        if (lockFile.exists()) {
            try {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                StringBuffer content = new StringBuffer();
                content.append(sdf.format(calendar.getTime())).append("<br />\r\n");

                File dir = new File(kernelAddata);
                if (dir.isDirectory()) {
                    for (File file: dir.listFiles()) {
                        content.append(file.getPath()).append("<br />\r\n");
                    }
                }

                springEmailUtil.sendHtmlEmail(mailSubject, mailFrom, mailTo, null, content.toString());

                log.info("subject = " + mailSubject);
                if (mailTo != null) {
                    for (String to: mailTo) {
                        log.info("mail to = " + to);
                    }
                }
            } catch (MessagingException me) {
                log.error(mailSubject, me);
            }
        }

        log.info("====CheckKernelJob.process() end====");
    }

    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        CheckKernelJob job = context.getBean(CheckKernelJob.class);
        job.process();
        ((FileSystemXmlApplicationContext) context).close();
    }

    public void setKernelAddata(String kernelAddata) {
        this.kernelAddata = kernelAddata;
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

    public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
        this.springEmailUtil = springEmailUtil;
    }
}