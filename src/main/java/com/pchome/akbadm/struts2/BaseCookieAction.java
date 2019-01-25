package com.pchome.akbadm.struts2;

public class BaseCookieAction extends BaseAction {
    private static final long serialVersionUID = -6178583495765526849L;

    protected static final String CHARSET = "UTF-8";

    protected String pchome_billingadm_cookie;
    protected String Pchome_membervip_email;

    public String getPchome_billingadm_cookie() {
        return pchome_billingadm_cookie;
    }

    public void setPchome_billingadm_cookie(String pchome_billingadm_cookie) {
        this.pchome_billingadm_cookie = pchome_billingadm_cookie;
    }

    public String getPchome_membervip_email() {
        return Pchome_membervip_email;
    }

    public void setPchome_membervip_email(String pchome_membervip_email) {
        Pchome_membervip_email = pchome_membervip_email;
    }
}