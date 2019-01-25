package com.pchome.soft.depot.utils;

import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.codec.binary.Base64;

public class RSAUtils {

	private static final Log log = LogFactory.getLog(RSAUtils.class);

	private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgAkCremk6gUuWfnA/pphMNTWChmfm8oBZn8CoPYCjilRvpHCdcCGOWJM7faTWs1qYfuNmMtB5VfYsL+CjCubVt+2cEbUaWZ5wiu30Kmwl+BqcV7a9IRJK7k7pEVcuOB0CZbF/gM57qyCumffnSl9+p7eEi9HxNPaj762286673QIDAQAB";

	private static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKACQKt6aTqBS5Z+cD+mmEw1NYKGZ+bygFmfwKg9gKOKVG+kcJ1wIY5Ykzt9pNazWph+42Yy0HlV9iwv4KMK5tW37ZwRtRpZnnCK7fQqbCX4GpxXtr0hEkruTukRVy44HQJlsX+AznurIK6Z9+dKX36nt4SL0fE09qPvrbbzrrvdAgMBAAECgYBwouVPUO5+ZsIFAKCXzfhrvREb/u1pX9AGRzThudmyRhBGPuWfjm2wvJ14RWwiT0M5EimVqbOjlmWimVlyrJvtPWMsop+erH7bYTqFuAkyXhfvwoqj1e+tlmT9rYUIUNwCd6y70TfgN0CAXi0isi35h6ohHBvGBdnsTQ/fR4nPKQJBAND1OnRJUhHugRGDKfTT/cprg1yTUlkVkSMwIbtERtzTYpjwxLutF5ed8CmfsO5RlTD+scQ38rqZaQjuX8USNa8CQQDEB/aCG2+bA7GL37QCkkAh0YN8o9HyLH6hCHLo/JNEUA3C+AA+lCYCZLnDRbqp9ZYm++Vg+PLfF0RLxcOz3xYzAkBaYnjSbBSv5Pa3WIEBgeE5eZ/sn1zzY7zP97Xfhv0P7++qMBspAwo9bve/SXAC5g8ejkzej0TTKiCg2Ftcpy/JAkEArelamub4RpAqTatzKezSFK6pdkRoF++9j+PM/kJF7I8RBm262cnZRrpRy5nrFqmYQpGrWDLEVYNwxcB39PXv3QJAEkangCuzjQGGZzmQIn1jbsNLcBq7lIY0/KH4HJ8sJRdq+tdNYKrk8Nq41QuhgX6jpCf3t1l57JmEUQd9G4K6pA==";

	private static PublicKey getPublicKey(String key) throws Exception {
          byte[] keyBytes = Base64.decodeBase64(key.getBytes());
          X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          PublicKey publicKey = keyFactory.generatePublic(keySpec);
          return publicKey;
    }

	private static PrivateKey getPrivateKey(String key) throws Exception {
          byte[] keyBytes = Base64.decodeBase64(key.getBytes());
          PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
          KeyFactory keyFactory = KeyFactory.getInstance("RSA");
          PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
          return privateKey;
    }

    /**
     * 加密
     */
    public static String encode(String str) {
    	String result = null;

    	try {

    		PublicKey pubKey = getPublicKey(PUBLIC_KEY);

    		//分段加密 -> 1024 編碼的 RSA key 一次最多只能壓 118 bytes 的長度, 所以長度超過要分段加密, 解密亦然
    		Cipher cipher = Cipher.getInstance("RSA");
        	cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        	byte[] bytes = str.getBytes("UTF-8");
        	byte[] encodedByteArray = new byte[] {};
        	for (int i = 0; i < bytes.length; i += 100) {
        		byte[] subarray = ArrayUtils.subarray(bytes, i, i + 100);
        		byte[] doFinal = cipher.doFinal(subarray);
        		encodedByteArray = ArrayUtils.addAll(encodedByteArray, doFinal);
        	}

        	//一定要強制 utf-8 編碼, 則中文亂碼, 解碼亦然
        	result = new String(Base64.encodeBase64(encodedByteArray), "UTF-8");

    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		e.printStackTrace();
    	}

    	return result;
    }

    /**
     * 解密
     */
    public static String decode(String strEncode) {
    	String result = null;

    	try {

    		PrivateKey priKey = getPrivateKey(PRIVATE_KEY);

    		//分段解密
    		Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] decodeByteArray = Base64.decodeBase64(strEncode.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<decodeByteArray.length; i += 128) {
            	byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(decodeByteArray, i, i + 128));
            	sb.append(new String(doFinal, "UTF-8"));
            }

            result = sb.toString();

    	} catch (Exception e) {
    		log.error(e.getMessage(), e);
    		e.printStackTrace();
    	}

    	return result;
    }

    public static void main(String[] args) throws Exception {

    	//String strTest = "<member><id>richard0120</id><pwd>nissan370z</pwd></member>";
    	//String strTest = "<member><email>test9965@pchome.com.tw</email></member>";
    	String strTest = "{\"id\":\"barthes\",\"user_name\":\"趴趴\",\"sexuality\":\"F\",\"birthday\":\"2012-12-21\",\"phone\":\"02-22668855\",\"cellphone\":\"0922555666\",\"zipcode\":\"106\",\"address\":\"中華民國萬萬歲17號\"}";
       	System.out.println(">>> strTest = " + strTest);

    	//test encode
    	String strEncode = encode(strTest);
    	System.out.println(">>> strEncode = " + strEncode);
    	//System.out.println(">>> strEncode length = " + strEncode.getBytes().length);

    	//test decode
    	//strEncode = "RhEBhGjDKmSkwpNLt2m4uC1AvM9Zu6W738lMiZM2L+oUpZVOF/cF3FT1IRolSb5wDhHKFpSDvUe55ABJSVVa6yEJaJTII7hRhdTrZJkkkAibZ0RgbwRGCOilvcWalIhYVR054aV1YZAHEbcAX/2NjF1pBnaiJrcz92LIN6I1qN4Iyx5IQ07Cigjx3/GAWnUEjyl/E+yX/yY6EQlHck5tRjpKaADJXNcbT80PL4S/xu8+n7lTcAt+sZ5fBovcGgDhZXDIeZGofyrWQ+y6I96+WMgn3Nu2/wV50WdVkAdf50ANGreRpTDnR2leG5/c3JESbF+B8RvJuLZaQBb5vNgkKA==";
    	//System.out.println(">>> strEncode 2 length = " + strEncode.getBytes().length);

        String strDecode = decode(strEncode);
        System.out.println(">>> strDecode = " + strDecode);
        
    }
}
