package com.pchome.service.portalcms;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.HttpConnectionClient;
//import com.pchome.soft.util.HttpUtil;

/**
 * process portalcms util
 * @author weich
 * @see <a href="http://portalcms.mypchome.com.tw/list.php">portalcms</a>
 * @see "commons-lang-2.6.jar"
 * @see "httpclient-4.1.2.jar"
 * @see "httpcore-4.1.3.jar"
 * @see "json.jar"
 * @since 1.0
 * @version 1.0
 */
public class PortalcmsUtil {
//    private static final Log log = LogFactory.getLog(PortalcmsUtil.class);
    private static PortalcmsUtil instance = new PortalcmsUtil();

    private PortalcmsUtil(){}

    /**
     * get singleton instance
     * @return instance
     */
    public synchronized static PortalcmsUtil getInstance() {
        return instance;
    }

    /**
     * get mail data from portalcms
     * @param rcode report code
     * @return mail data
     * @throws JSONException
     */
    public Mail getMail(String rcode) throws JSONException {
        StringBuffer url = new StringBuffer()
            .append("http://portalcms.mypchome.com.tw/test.php")
            .append("?RCode=").append(rcode)
            .append("&RType=json");
//        String result = HttpUtil.getInstance().getResult(url.toString(), "UTF-8");
        String result = HttpConnectionClient.getInstance().doGet(url.toString(), "UTF-8");

        Mail mail = new Mail();

        JSONObject jsonobject = new JSONObject(result);

        mail.setStatus(jsonobject.getBoolean("Status"));
        mail.setMsg(jsonobject.getString("Msg"));

        if (mail.isStatus()) {
            String[] mailToArr = new String[0];
            String[] mailBccArr = new String[0];
            String[] phoneArr = new String[0];

            try {
                JSONArray mailTo = jsonobject.getJSONArray("MailTo");
                mailToArr = new String[mailTo.length()];
                for (int i = 0; i < mailTo.length(); i++) {
                    mailToArr[i] = (String) mailTo.get(i);
                }
            } catch (JSONException e) {
//                log.error(e.getMessage(), e);
            }

            try {
                JSONArray mailBcc = jsonobject.getJSONArray("MailBcc");
                mailBccArr = new String[mailBcc.length()];
                for (int i = 0; i < mailBcc.length(); i++) {
                    mailBccArr[i] = (String) mailBcc.get(i);
                }
            } catch (JSONException e) {
//                log.error(e.getMessage(), e);
            }

            try {
                JSONArray phone = jsonobject.getJSONArray("phone");
                phoneArr = new String[phone.length()];
                for (int i = 0; i < phone.length(); i++) {
                    phoneArr[i] = (String) phone.get(i);
                }
            } catch (JSONException e) {
//                log.error(e.getMessage(), e);
            }

            mail.setRname(jsonobject.getString("RName"));
            mail.setMailFrom(jsonobject.getString("MailFrom"));
            mail.setMailTo(mailToArr);
            mail.setMailBcc(mailBccArr);
            mail.setPhone(phoneArr);
        }

        return mail;
    }

//    public static void main(String[] args) throws JSONException {
//        Mail mail = PortalcmsUtil.getInstance().getMail("P094");
//        System.out.println("Status:\t" + mail.isStatus());
//        System.out.println("Msg:\t" + mail.getMsg());
//        System.out.println("RName:\t" + mail.getRname());
//        System.out.println("MailFrom:\t" + mail.getMailFrom());
//        System.out.println("MailTo:\t" + mail.getMailTo());
//        if (mail.getMailTo() != null) {
//            for (String str: mail.getMailTo()) {
//                System.out.println("  " + str);
//            }
//        }
//        System.out.println("MailBcc:\t" + mail.getMailBcc());
//        if (mail.getMailBcc() != null) {
//            for (String str: mail.getMailBcc()) {
//                System.out.println("  " + str);
//            }
//        }
//    }
}