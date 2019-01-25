package com.pchome.akbadm.quartzs;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.AdmAccesslog;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.config.TestConfig;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;
import com.pchome.soft.util.SpringEmailUtil;

public class AccesslogJob {
	protected final static String CHARSET = "UTF-8";
	protected Log log = LogFactory.getLog(this.getClass());

	private IAdmAccesslogService accesslogService;

	private String mailDir;
	private String mailService;
	private String mailSubject;
	private String mailFrom;
	private String[] mailTo;

	public void process() {
		log.info("====AccesslogJob.process() start====");

        File mailFile = new File(mailDir + "accesslog.html");

		try {
			List<AdmAccesslog> accesslogs = accesslogService.findSendMail();

            log.info("accesslogs = " + accesslogs.size());

			for (AdmAccesslog accesslog: accesslogs) {
				String mailContent = FileUtils.readFileToString(mailFile, CHARSET);
				sendMail(accesslog, mailContent);
			}
		}
		catch (Exception e) {
			log.error(mailFile.getPath(), e);
		}

        log.info("====AccesslogJob.process() end====");
	}

	private void sendMail(AdmAccesslog accesslog, String mailContent) {
		String memberId = StringUtils.defaultString(accesslog.getMemberId(), "");
		String orderId = StringUtils.defaultString(accesslog.getOrderId(), "");
		String customerInfoId = StringUtils.defaultString(accesslog.getCustomerInfoId(), "");
		String userId = StringUtils.defaultString(accesslog.getUserId(), "");
		String clientIp = StringUtils.defaultString(accesslog.getClientIp(), "");

		mailContent = mailContent.replace("@accesslog_id", accesslog.getAccesslogId().toString());
		mailContent = mailContent.replace("@channel", accesslog.getChannel());
		mailContent = mailContent.replace("@message", accesslog.getMessage());
		mailContent = mailContent.replace("@member_id", memberId);
		mailContent = mailContent.replace("@order_id", orderId);
		mailContent = mailContent.replace("@customerInfo_id", customerInfoId);
		mailContent = mailContent.replace("@user_id", userId);
		mailContent = mailContent.replace("@ip", clientIp);

		try {
			SpringEmailUtil.getInstance().setHost(mailService);
			SpringEmailUtil.getInstance().sendHtmlEmail(mailSubject,
														mailFrom,
														mailTo,
														null,
														mailContent);
		}
		catch (MessagingException e) {
			log.error("send mail error", e);
		}

		accesslog.setMailSend(EnumAccesslogEmailStatus.DONE.getStatus());
		accesslogService.saveOrUpdate(accesslog);
	}

	public void setAccesslogService(IAdmAccesslogService accesslogService) {
		this.accesslogService = accesslogService;
	}

	public void setMailDir(String mailDir) {
		this.mailDir = mailDir;
	}

	public void setMailService(String mailService) {
		this.mailService = mailService;
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

	public static void main(String[] args) throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        AccesslogJob job = context.getBean(AccesslogJob.class);
        job.process();
    }
}
