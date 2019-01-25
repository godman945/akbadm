package com.pchome.rmi.email;

public interface IEmailProvider {
    /**
     * send mail by ord.ordSno
     * @param enumEmail
     * @param ordSno
     * @return
     * 1 send mail success<br />
     * 0 config send mail = false<br />
     * -1 send mail fail<br />
     * -2 exception<br />
     */
    public abstract int send(EnumEmail enumEmail, String ordSno);
}