package com.pchome.soft.util;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * send mail util by spring<br />
 * usage example:<br />
 * SpringEmailUtil springEmailUtil = SpringEmailUtil.getInstance();<br />
 * springEmailUtil.setHost("staff.pchome.com.tw");<br />
 * springEmailUtil.sendHtmlEmail("test", "softdepot&lt;softdepot@msx.pchome.com.tw&gt;", new String[]{"user&lt;user@staff.pchome.com.tw&gt;"}, null, "test");
 * @author weich
 * @see "commons-logging-1.1.1.jar"
 * @see "mail.jar"
 * @see "org.springframework.beans-3.0.5.RELEASE.jar"
 * @see "org.springframework.context.support-3.0.5.RELEASE.jar"
 * @see "org.springframework.core-3.0.5.RELEASE.jar"
 * @since 1.0
 * @version 1.0
 */
public class SpringEmailUtil {
    private static final Log log = LogFactory.getLog(SpringEmailUtil.class);
    private static SpringEmailUtil instance = new SpringEmailUtil();

    private JavaMailSender mailSender;
    private SimpleMailMessage templateMessage;

    private SpringEmailUtil(){}

    public synchronized static SpringEmailUtil getInstance() {
        return instance;
    }

    public void setHost(String host) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        this.mailSender = mailSender;
    }

    public void sendEmail(String Message) {
//        SimpleMailMessage mailTemplateTemp = new SimpleMailMessage(templateMessage);
        templateMessage.setText(Message);
        this.mailSender.send(templateMessage);
    }

    public void sendHtmlEmail(String subject, String from, String[] to, String[] bcc, String html) throws MessagingException {
        try{
//            log.info("subject=" + subject);
//            log.info("from=" + from);
//            log.info("to=" + to);
//            log.info("html=" + html);

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mime, "utf-8");
            messageHelper.setSubject(subject);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            if (bcc != null) {
                messageHelper.setBcc(bcc);
            }
            messageHelper.setText(html, true);

            this.mailSender.send(mime);
        } catch (Exception e) {
            log.error("SendHtmlEmail error", e);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

//    public static void main(String[] args) throws MessagingException {
//        SpringEmailUtil springEmailUtil = SpringEmailUtil.getInstance();
//        springEmailUtil.setHost("staff.pchome.com.tw");
//        springEmailUtil.sendHtmlEmail("test", "softdepot<softdepot@msx.pchome.com.tw>", new String[]{"weich<weich@staff.pchome.com.tw>"}, null, "test");
//        System.out.println("send mail done");
//    }
}