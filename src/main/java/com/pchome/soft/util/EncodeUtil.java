package com.pchome.soft.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncodeUtil {

	private static final EncodeUtil instance = new EncodeUtil();

    private EncodeUtil() {
    }

    public static EncodeUtil getInstance() {
        return instance;
    }

    private Key initKeyForAES(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
        SecretKeySpec key2 = null;
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(key.getBytes("utf-8"));
            kgen.init(secureRandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            key2 = new SecretKeySpec(enCodeFormat, "AES");
        } catch (NoSuchAlgorithmException ex) {
            throw new NoSuchAlgorithmException();
        }
        return key2;
    }

    public String encryptAES(String content, String key){
        try{
            SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(byteContent);
            return asHex(result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String decryptAES(String content, String key){
        try{
            SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] result = cipher.doFinal(asBytes(content));
            return new String(result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String asHex(byte buf[]){
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++){
            if ((buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString(buf[i] & 0xff, 16));
        }
        return strbuf.toString();
    }

    public byte[] asBytes(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++){
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void main(String[] args) {
    	String test = "richard@pchome.com.tw$$$9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30";
    	String key = "pchomeakbadm2012";

    	String testEncode = EncodeUtil.getInstance().encryptAES(test, key);
    	System.out.println(">>> testEncode = " + testEncode);
    	String testDecode = EncodeUtil.getInstance().decryptAES(testEncode, key);
    	System.out.println(">>> testDecode = " + testDecode);
    }
}
