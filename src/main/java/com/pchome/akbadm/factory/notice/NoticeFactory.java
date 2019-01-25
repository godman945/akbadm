package com.pchome.akbadm.factory.notice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.rmi.mailbox.EnumCategory;

public class NoticeFactory {
    private Log log = LogFactory.getLog(getClass().getName());

    private ANotice noticeAccountClosed;
    private ANotice noticeEnableReminded;
    private ANotice noticeInvalidDown;
    private ANotice noticeMoneyEntered;
    private ANotice noticeRemainNotEnough;
    private ANotice noticeRemainTooLow;
    private ANotice noticeVerifyDenied;

    public ANotice get(EnumCategory enumCategory) {
        ANotice notice = null;

        switch (enumCategory) {
        case ACCOUNT_CLOSED:
            notice = noticeAccountClosed;
            break;
        case ENABLE_REMINDED:
            notice = noticeEnableReminded;
            break;
        case INVALID_DOWN:
            notice = noticeInvalidDown;
            break;
        case MONEY_ENTERED:
            notice = noticeMoneyEntered;
            break;
        case REMAIN_NOT_ENOUGH:
            notice = noticeRemainNotEnough;
            break;
        case REMAIN_TOO_LOW:
            notice = noticeRemainTooLow;
            break;
        case VERIFY_DENIED:
            notice = noticeVerifyDenied;
            break;
        default:
            log.info(enumCategory + " == null");
        }

        return notice;
    }

    public void setNoticeAccountClosed(ANotice noticeAccountClosed) {
        this.noticeAccountClosed = noticeAccountClosed;
    }

    public void setNoticeEnableReminded(ANotice noticeEnableReminded) {
        this.noticeEnableReminded = noticeEnableReminded;
    }

    public void setNoticeInvalidDown(ANotice noticeInvalidDown) {
        this.noticeInvalidDown = noticeInvalidDown;
    }

    public void setNoticeMoneyEntered(ANotice noticeMoneyEntered) {
        this.noticeMoneyEntered = noticeMoneyEntered;
    }

    public void setNoticeRemainNotEnough(ANotice noticeRemainNotEnough) {
        this.noticeRemainNotEnough = noticeRemainNotEnough;
    }

    public void setNoticeRemainTooLow(ANotice noticeRemainTooLow) {
        this.noticeRemainTooLow = noticeRemainTooLow;
    }

    public void setNoticeVerifyDenied(ANotice noticeVerifyDenied) {
        this.noticeVerifyDenied = noticeVerifyDenied;
    }
}