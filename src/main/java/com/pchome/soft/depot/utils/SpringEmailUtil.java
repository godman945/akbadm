package com.pchome.soft.depot.utils;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SpringEmailUtil {
	private JavaMailSender mailSender;

	private SimpleMailMessage templateMessage;

	private static Log log = LogFactory.getLog(SpringEmailUtil.class);

	public void SendEmail(String Message) {
		log.info("error mail send="+Message);
		SimpleMailMessage mailTemplateTemp = new SimpleMailMessage(templateMessage);
		templateMessage.setText(Message);
		this.mailSender.send(templateMessage);
	}

    public void SendHtmlEmail(String subject, String from, String[] to, String[] bcc, String html) throws MessagingException {
        try{
//          log.info("subject=" + subject);
//          log.info("from=" + from);
//          log.info("to=" + to);
//          log.info("html=" + html);

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

	public void SendHtmlEmail(String Message) {
		MimeMessage mailMessage = mailSender.createMimeMessage();

		MimeMessageHelper messageHelper=null;
		try {
			messageHelper = new MimeMessageHelper(mailMessage,true,"UTF-8");
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 設定收件人、寄件人、主題與內文
		try {
			messageHelper.setTo(templateMessage.getTo());
			messageHelper.setFrom(templateMessage.getFrom(),"LocalSearch");
			messageHelper.setSubject(templateMessage.getSubject());
			messageHelper.setText(Message, true);

			this.mailSender.send(mailMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 傳送郵件
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}
}