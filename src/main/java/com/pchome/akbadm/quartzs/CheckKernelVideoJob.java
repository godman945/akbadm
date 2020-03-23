package com.pchome.akbadm.quartzs;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.config.TestConfig;
import com.pchome.soft.util.SpringEmailUtil;

public class CheckKernelVideoJob {
    private Logger log = LogManager.getRootLogger();

    private String nagiosPathKernelVideo;
    private String mailSubject;
    private String mailFrom;
    private String[] mailTo;
    private SpringEmailUtil springEmailUtil;

    public void process() {
        log.info("====CheckKernelVideoJob.process() start====");

        File file = new File(nagiosPathKernelVideo);
        if (file.exists()) {
            try {
                String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

                if (!"ok".equals(content)) {
                    springEmailUtil.sendHtmlEmail(mailSubject, mailFrom, mailTo, null, content);
                }

                log.info("subject = " + mailSubject);
                if (mailTo != null) {
                    for (String to: mailTo) {
                        log.info("mail to = " + to);
                    }
                }
            } catch (IOException e) {
                log.error(file);
            } catch (MessagingException me) {
                log.error(mailSubject, me);
            }
        }

        log.info("====CheckKernelVideoJob.process() end====");
    }

    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        CheckKernelVideoJob job = context.getBean(CheckKernelVideoJob.class);
        job.process();
        ((AnnotationConfigApplicationContext) context).close();
    }

    public void setNagiosPathKernelVideo(String nagiosPathKernelVideo) {
        this.nagiosPathKernelVideo = nagiosPathKernelVideo;
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