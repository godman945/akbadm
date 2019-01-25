package com.pchome.service.portalcms.bean;

/**
 * mail bean
 * @author weich
 * @since 1.0
 * @version 1.0
 */
public class Mail {
    private boolean status;
    private String msg;
    private String rname;
    private String mailFrom;
    private String[] mailTo;
    private String[] mailBcc;
    private String[] phone;

    /**
     * 資料是否有效
     * @return true 有效, false 無效
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * 設定資料是否有效
     * @param status true 有效, false 無效
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * 取得資料說明
     * @return 說明
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 設定資料說明
     * @param msg 說明
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 取得信件主旨
     * @return 信件主旨
     */
    public String getRname() {
        return rname;
    }

    /**
     * 設定信件主旨
     * @param rname 信件主旨
     */
    public void setRname(String rname) {
        this.rname = rname;
    }

    /**
     * 取得寄件者
     * @return 寄件者
     */
    public String getMailFrom() {
        return mailFrom;
    }

    /**
     * 設定寄件者
     * @param mailFrom 寄件者
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /**
     * 取得收件者
     * @return 收件者
     */
    public String[] getMailTo() {
        return mailTo;
    }

    /**
     * 設定收件者
     * @param mailTo 收件者
     */
    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    /**
     * 取得密件副本
     * @return 密件副本
     */
    public String[] getMailBcc() {
        return mailBcc;
    }

    /**
     * 設定密件副本
     * @param mailBcc 密件副本
     */
    public void setMailBcc(String[] mailBcc) {
        this.mailBcc = mailBcc;
    }

    /**
     * 取得手機號碼
     * @return 手機號碼
     */
    public String[] getPhone() {
        return phone;
    }

    /**
     * 設定手機號碼
     * @param phone 手機號碼
     */
    public void setPhone(String[] phone) {
        this.phone = phone;
    }
}