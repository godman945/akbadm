package com.pchome.akbadm.quartzs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpMailbox;
import com.pchome.akbadm.db.service.board.IPfpBoardService;
import com.pchome.akbadm.db.service.mailbox.IPfpMailboxService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.factory.notice.ANotice;
import com.pchome.akbadm.factory.notice.NoticeFactory;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.config.TestConfig;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.service.portalcms.bean.Mail;

@Transactional
public class NoticeJob {
    private Logger log = LogManager.getRootLogger();

    private String mailFrom;
    private IPfpBoardService pfpBoardService;
    private IPfdBoardService pfdBoardService;
    private IPfpMailboxService pfpMailboxService;
    private EmailUtils emailUtils;
    private NoticeFactory noticeFactory;
    private String mailUserName;

    public void process() {
        log.info("====NoticeJob.process() start====");

        notice();
        send();
        delete();

        log.info("====NoticeJob.process() end====");
    }

    private void notice() {
        ANotice notice = null;
        for (EnumCategory enumCategory: EnumCategory.values()) {
            log.info(enumCategory.ordinal() + "." + enumCategory + " start");

            notice = noticeFactory.get(enumCategory);
            if (notice != null) {
                notice.notice();
            }

            log.info(enumCategory.ordinal() + "." + enumCategory + " end");
        }

        log.info("notice ok");
    }

    private void send() {
        int count = 0;

        List<PfpMailbox> list = pfpMailboxService.selectPfpMailboxBySend("N");
        ANotice notice = null;
        Mail mail = null;

        for (PfpMailbox pfpMailbox: list) {
            try {
                mail = new Mail();
                mail.setMailFrom(mailFrom);
                mail.setMailTo(new String[]{pfpMailbox.getReceiver()});

                for (EnumCategory enumCategory: EnumCategory.values()) {
                    if (enumCategory.getCategory().equals(pfpMailbox.getCategory())) {
                        mail.setRname(enumCategory.getMailTitle());

                        notice = noticeFactory.get(enumCategory);
                        if (notice != null) {
                            mail.setMsg(notice.getMailContent());
                        }

                        break;
                    }
                }

                if (StringUtils.isBlank(mail.getMsg())) {
                    log.info(pfpMailbox.getMailboxId() + " mail content is blank");
                    continue;
                }

                emailUtils.sendHtmlEmail(mail.getRname(), mail.getMailFrom(), mailUserName, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

                pfpMailbox.setSend("Y");
                pfpMailbox.setUpdateDate(Calendar.getInstance().getTime());

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

                count++;
            } catch (Exception e) {
                log.error(pfpMailbox.getMailboxId(), e);
            }
        }

        log.info("send " + count);
    }

    /**
     * 砍掉 PFP & PFD 已過期的公告
     */
    private void delete() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String deleteDate = df.format(Calendar.getInstance().getTime());
        int count = pfpBoardService.deletePfpBoardOvertime(deleteDate);
        log.info("delete " + count);
        try {
            int count2 = pfdBoardService.deletePfdBoardOvertime(df.parse(deleteDate));
            log.info("delete2 " + count2);
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setPfpBoardService(IPfpBoardService pfpBoardService) {
        this.pfpBoardService = pfpBoardService;
    }

    public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setPfpMailboxService(IPfpMailboxService pfpMailboxService) {
        this.pfpMailboxService = pfpMailboxService;
    }

    public void setNoticeFactory(NoticeFactory noticeFactory) {
        this.noticeFactory = noticeFactory;
    }

    public void setEmailUtils(EmailUtils emailUtils) {
		this.emailUtils = emailUtils;
	}

	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}

	public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        NoticeJob job = context.getBean(NoticeJob.class);
        job.process();
    }
}