package com.pchome.soft.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieUtil {

	private static final Log logger = LogFactory.getLog(CookieUtil.class);

	/**
	 * 取得 cookie, 有必要的話加以解碼
	 * @param request
	 * @param cookieName 要尋找的 cookie 名稱
	 * @param decodeCode 解碼用的編碼, 不需解碼的時候給 null
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String cookieName, String decodeCode) {

		String cookieValue = null;

        Cookie[] cookieArray = request.getCookies();

        if (cookieArray!=null && cookieArray.length>0) {
            for (int i=0; i<cookieArray.length; i++) {
                Cookie tmpCookie = cookieArray[i];
                if (tmpCookie.getName().equals(cookieName)) {
                	cookieValue = tmpCookie.getValue();
                	break;
                }
            }
            //logger.info(">>> cookieValue = " + cookieValue);

            if (StringUtils.isNotEmpty(cookieValue) && StringUtils.isNotEmpty(decodeCode)) {
                try {

                	cookieValue = URLDecoder.decode(cookieValue, "ISO-8859-1");
                	//logger.info(">>> decode cookieValue = " + cookieValue);

                } catch (UnsupportedEncodingException uee) {
                	logger.error(uee.getMessage(), uee);
                } catch (Exception e) {
                	logger.error(e.getMessage(), e);
                }
            }
        }

        return cookieValue;
	}

	/**
	 * 寫入 cookie, 有必要的話加以編碼
	 * @param response
	 * @param cookieName 要寫入的 cookie 名稱
	 * @param cookieValue 要寫入的 cookie 值
	 * @param domain 要寫入的 cookie domain
	 * @param maxAge 要寫入的 cookie 存活時間
	 * @param encodeCode 編碼用的編碼, 不需編碼的時候給 null
	 * @return
	 */
	public static void writeCookie(HttpServletResponse response, String cookieName, String cookieValue,
			String domain, int maxAge, String encodeCode) {

		logger.info(">>> encode cookieValue = " + cookieValue);
		if (StringUtils.isNotEmpty(encodeCode)) {
	        try {

	        	cookieValue = URLEncoder.encode(cookieValue, encodeCode);
	        	//logger.info(">>> encode cookieValue = " + cookieValue);

	        } catch (UnsupportedEncodingException uee) {
	        	logger.error(uee.getMessage(), uee);
	        } catch (Exception e) {
	        	logger.error(e.getMessage(), e);
	        }
		}

		//write cookie
        if (StringUtils.isNotEmpty(cookieValue)) {
        	//logger.info(">>> write cookie: name = " + cookieName + ", value = " + cookieValue);
            Cookie cookie = new Cookie(cookieName, cookieValue);
//            cookie.setDomain(domain);
            cookie.setMaxAge(maxAge);
            cookie.setPath("/");
    		response.addCookie(cookie);
    		logger.info(">>> write cookie end");
        }
	}

	/**
	 * 加密
	 */
    public static String encodeEmail(String email, int key1Length, int key2Length) {

    	if (StringUtils.isEmpty(email)) {
    		return null;
    	}

    	String strKey1 = keyGenerator(key1Length);

    	String strKey2 = keyGenerator(key2Length);

    	String strOriginal = strKey1 + email + strKey2;

        String strMD5 = md5(strOriginal);

        String[] tmp = email.split("@");
        String email1 = tmp[0];
        String email2 = tmp[1];

        return (strMD5 + strKey2 + email1 + strKey1 + "@" + email2);
    }

    /**
     * 解密
     * @param lowcookie
     * @return
     */
    public static String decodeEmail(String strEncoded, int key1Length, int key2Length) {

        String strMD5 = strEncoded.substring(0, 32);

        String strKey2 = strEncoded.substring(32, 32+key2Length);

        int index = strEncoded.indexOf("@");
        String email1 = strEncoded.substring(32+key2Length, index-key1Length);

        String strKey1 = strEncoded.substring(index-key1Length, index);

        String email2 = strEncoded.substring(index);

        String email = email1 + email2;

    	String strOriginal_new = strKey1 + email + strKey2;

        String strMD5_new = md5(strOriginal_new);

        if (strMD5.equals(strMD5_new)) {
            return email;
        } else {
            return null;
        }
    }

    private static String md5(String md5scr) { // MD5

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }

        md.reset();
        md.update(md5scr.getBytes());
        byte[] mdDigest = md.digest();
        String s32code = decode16to32(mdDigest);

        return s32code;
    }

    private static String decode16to32(byte[] in) {

    	if (in.length != 16)
            return "";

        String sOut = "";
        String sTmp = "";
        int ia = 0;
        for (int i = 0; i < in.length; i++) {
            ia = in[i] & 0x000000ff;
            sTmp = Integer.toHexString(ia);
            if (sTmp.length() == 1)
                sOut += ("0" + sTmp);
            else
                sOut += sTmp;
        }

        if (sOut.length() != 32)
            return "";

        return sOut;
    }

    /**
     * 亂碼產生器
     * @param length 長度
     * @return
     */
    private static String keyGenerator(int length) {

    	StringBuffer key = new StringBuffer();

    	java.util.Random r = new java.util.Random();

    	int rnd = 0;

        for (int i=0 ; i<length; i++) {
        	if(getNumChar()) {
        		rnd = r.nextInt(9);
        		key.append(rnd);
        	} else {
        		rnd = r.nextInt(52);
        		if (rnd<26) {
        			key.append((char)(rnd +65));
        		} else {
        			key.append((char)(rnd -26+ 97));
        		}
        	}
        }

        return key.toString();
    }

    private static boolean getNumChar() { //八成數字二成英文
        java.util.Random r = new java.util.Random();
        if (r.nextInt(100)>80) {
        	return true;
        } else {
        	return false;
        }
    }

    public static void main(String[] args) {
    	String strEncoded = CookieUtil.encodeEmail("richard998@hotmail.com", 20, 15);
    	System.out.println("*** strEncoded = " + strEncoded);
    	String email = CookieUtil.decodeEmail(strEncoded, 20, 15);
    	System.out.println("*** email = " + email);
    }
}
