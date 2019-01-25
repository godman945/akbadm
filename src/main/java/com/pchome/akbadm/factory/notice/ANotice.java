package com.pchome.akbadm.factory.notice;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.pojo.PfpUser;
import com.pchome.akbadm.db.service.board.IPfpBoardService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.mailbox.IPfpMailboxService;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.privilege.EnumPrivilegeModel;
import com.pchome.enumerate.user.EnumUserStatus;
import com.pchome.rmi.board.EnumBoardType;
import com.pchome.rmi.board.IBoardProvider;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.rmi.mailbox.EnumCategory;
import com.pchome.rmi.mailbox.IMailboxProvider;

public abstract class ANotice {
    protected final static String CHARSET = "UTF-8";

    protected Log log = LogFactory.getLog(getClass().getName());

    protected int boardCount = 0;
    protected int mailboxCount = 0;
    protected String mailContent;

    protected String mailDir;
    protected IPfpBoardService pfpBoardService;
    protected IPfpCustomerInfoService pfpCustomerInfoService;
    protected IPfpMailboxService pfpMailboxService;
    protected IBoardProvider boardProvider;
    protected IMailboxProvider mailboxProvider;

    public abstract void notice();

    public abstract String getMailContent();

    protected String getMailContent(EnumCategory enumCategory) {
        if (StringUtils.isBlank(mailContent)) {
            File mailFile = new File(mailDir + enumCategory.getCategory() + ".html");
            log.info(mailFile.getPath());
            if (mailFile.exists()) {
                try {
                    mailContent = FileUtils.readFileToString(mailFile, CHARSET);
                } catch (IOException e) {
                    log.error(mailFile.getPath(), e);
                }
            }
        }

        return mailContent;
    }

    protected Integer addBoard(PfpCustomerInfo pfpCustomerInfo, EnumBoardType enumBoardType, EnumCategory enumCategory) throws Exception {
        return addBoard(pfpCustomerInfo, enumBoardType, enumCategory, null);
    }

    protected Integer addBoard(PfpCustomerInfo pfpCustomerInfo, EnumBoardType enumBoardType, EnumCategory enumCategory, String deleteId) throws Exception {
        return addBoard(pfpCustomerInfo, null, enumBoardType, enumCategory, deleteId);
    }

    protected Integer addBoard(PfpCustomerInfo pfpCustomerInfo, String urlAddress, EnumBoardType enumBoardType, EnumCategory enumCategory, String deleteId) throws Exception {
        String customerInfoId = pfpCustomerInfo.getCustomerInfoId();
        Integer pk = null;

        if (enumCategory.isBoard() &&
                pfpBoardService.selectPfpBoard(customerInfoId, enumCategory.getCategory(), "Y").isEmpty()) {

            boardCount++;
            urlAddress = StringUtils.isBlank(urlAddress) ? enumCategory.getUrlAddress() : urlAddress;

            //公告為 餘額不足 or 餘額偏低 時，若為後付使用者，不用超連結 (2014-11-10)
            if (enumCategory.getCategory().equals(EnumCategory.REMAIN_NOT_ENOUGH.getCategory()) ||
            		enumCategory.getCategory().equals(EnumCategory.REMAIN_TOO_LOW.getCategory())) {

            	if (pfpCustomerInfo.getPfdUserAdAccountRefs().size() > 0) {
            		Iterator<PfdUserAdAccountRef> itRef = pfpCustomerInfo.getPfdUserAdAccountRefs().iterator();
            		while (itRef.hasNext()) {
            			if (itRef.next().getPfpPayType().equals(EnumPfdAccountPayType.LATER.getPayType())) {
            				urlAddress = "";
            			}
            		}
            	}
            }

            pk = boardProvider.add(customerInfoId, enumCategory.getBoardContent(), urlAddress, enumBoardType, enumCategory, deleteId);
        }

        return pk;
    }

    protected Integer deleteBoard(PfpCustomerInfo pfpCustomerInfo, EnumCategory enumCategory) throws Exception {
        return boardProvider.delete(pfpCustomerInfo.getCustomerInfoId(), enumCategory);
    }

    protected Integer deleteBoard(PfpCustomerInfo pfpCustomerInfo, EnumCategory enumCategory, String deleteId) throws Exception {
        return boardProvider.delete(pfpCustomerInfo.getCustomerInfoId(), enumCategory, deleteId);
    }

    protected void addMailbox(PfpCustomerInfo pfpCustomerInfo, EnumCategory enumCategory) throws Exception {
        String customerInfoId = pfpCustomerInfo.getCustomerInfoId();

        if (enumCategory.isMailbox() &&
                pfpMailboxService.selectPfpMailbox(customerInfoId, enumCategory.getCategory()).isEmpty()) {

            for (PfpUser pfpUser: pfpCustomerInfo.getPfpUsers()) {
//                log.info("userId = " + pfpUser.getUserId());

                if (!EnumUserStatus.START.getStatus().equals(pfpUser.getStatus())) {
//                    log.info("user status " + pfpUser.getStatus());
                    continue;
                }

                if (!EnumPrivilegeModel.ROOT_USER.getPrivilegeId().equals(pfpUser.getPrivilegeId())) {
//                    log.info("privilege " + pfpUser.getPrivilegeId());
                    continue;
                }

                mailboxProvider.add(customerInfoId, pfpUser.getUserEmail(), enumCategory);

                mailboxCount++;
            }
        }
    }

    protected void deleteMailbox(PfpCustomerInfo pfpCustomerInfo, EnumCategory enumCategory) throws Exception {
        mailboxProvider.delete(pfpCustomerInfo.getCustomerInfoId(), enumCategory);
    }

    public void setMailDir(String mailDir) {
        this.mailDir = mailDir;
    }

    public void setPfpBoardService(IPfpBoardService pfpBoardService) {
        this.pfpBoardService = pfpBoardService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public void setPfpMailboxService(IPfpMailboxService pfpMailboxService) {
        this.pfpMailboxService = pfpMailboxService;
    }

    public void setBoardProvider(IBoardProvider boardProvider) {
        this.boardProvider = boardProvider;
    }

    public void setMailboxProvider(IMailboxProvider mailboxProvider) {
        this.mailboxProvider = mailboxProvider;
    }
}