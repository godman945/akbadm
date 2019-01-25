package com.pchome.soft.util;

import  java.io.IOException;
import  java.security.Key;
import  java.security.KeyPair;
import  java.security.KeyPairGenerator;
import  java.security.MessageDigest;
import  java.security.SecureRandom;
import  java.security.Signature;
import  java.security.SignatureException;

import  javax.crypto.Cipher;
import  javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

public  class  CipherUtil {
    /**
     * @param args
     * @throws Exception
     */
    public  static  void  main(String[] args)  throws  Exception {
        StringBuffer strb =  new StringBuffer("20120105:8542154:125478:235417:14578") ;

        //strb.append("無線電手機使用UHF頻道，搭配鋰電池供電，使用時間可達11.5小時，");
        //strb.append("使用環境則支援IP 54，具有基本的防雨水功能。螢幕採用強固型的彩色顯示器，");
      	//strb.append("為適合戶外陽光下使用，手機內建光感測器，自動依環境光源調整螢幕亮度。");
      	//strb.append("此外，還具有智慧型音訊功能，可抑制通話時的背景噪音。");
        //strb.append("雖然話機體積縮小，但SL1K整合藍牙功能，除了可以連接條碼掃描器回報傳輸資料之外，");
        //strb.append("可連接無線耳機，提昇工作時的行動性、提昇通話的隱密性。為方便整合不同行業應用，");
        //strb.append("SL1K在軟體功能上內建派工管理、簡訊、派工、電話互連等功能。");

        String str=strb.toString();

        BASE64(str.getBytes());
        MessageDigest(str.getBytes());
        MD5MessageDigest(str.getBytes());
        AESCipher(str.getBytes());
        DESCipher(str.getBytes(), "pchome27000898" );
        RSACipher(str.getBytes());
        DigitalSignature(str.getBytes());
    }

    /**
     * Base64就是用來將非ASCII字符的數據轉換成ASCII字符的一種方法
     *
     * @param plainText
     * @throws IOException
     */
    public  static  void  BASE64( byte [] plainText)  throws  IOException {
        Base64 base64 = new Base64();
        String str = new String(base64.encode(plainText));
        System.out.println( "BASE64 encode-----"  + str);
        // str轉換BASE64
        byte [] bys = base64.decode(str);
        System.out.println( "BASE64 decode-----"  +  new String(bys));
    }

    /**
     * SHA消息摘要 不可逆(不可解密)
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  MessageDigest( byte [] plainText)  throws  Exception {
        MessageDigest messageDigest = MessageDigest.getInstance( "SHA-1" );

        System.out.println( "SHA消息摘要-----"  + bytesToHexString(messageDigest.digest(plainText)));
        //System.out.println( "SHA消息摘要-----"  + messageDigest.digest(plainText).toString());
    }

    /**
     * MD5消息摘要 不可逆(不可解密)
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  MD5MessageDigest( byte [] plainText)  throws  Exception {
        MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );

        System.out.println( "MD5消息摘要-----"  + bytesToHexString(messageDigest.digest(plainText)));
        //System.out.println( "SHA消息摘要-----"  + HexUtils.convert(messageDigest.digest(plainText)));
    }

    /**
     * AES對稱加密
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  AESCipher( byte [] plainText)  throws  Exception {
        // 初始化AES密鑰
        KeyGenerator keyGen = KeyGenerator.getInstance( "AES" );
        keyGen.init( 128 );
        Key key = keyGen.generateKey();

        // 初始化Cipher
        Cipher cipher = Cipher.getInstance( "AES/ECB/PKCS5Padding" );

        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] cipherText = cipher.doFinal(plainText);
        System.out.println( "AESCipher Encoder-----"  + bytesToHexString(cipherText));

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte [] bys = cipher.doFinal(cipherText);
        System.out.println( "AESCipher----"  +  new  String(bys));
    }

    /**
     * DES對稱加密
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  DESCipher( byte [] plainText,String strKey)  throws  Exception {
        // 初始化DES密鑰
        KeyGenerator keyGen = KeyGenerator.getInstance( "DES" );
        keyGen.init( new  SecureRandom(strKey.getBytes()));
        Key key = keyGen.generateKey();

        // 初始化Cipher
        Cipher cipher = Cipher.getInstance( "DES" );

        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] cipherText = cipher.doFinal(plainText);
        System.out.println( "DESCipher Encoder-----"  + bytesToHexString(cipherText));

        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte [] bys = cipher.doFinal(cipherText);
        System.out.println( "DESCipher----"  +  new  String(bys));
    }

    /**
     * RSA非對稱加密
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  RSACipher( byte [] plainText)  throws  Exception {
        // 初始化RSA密鑰
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
        keyGen.initialize( 1024 );
        KeyPair key = keyGen.generateKeyPair();

        // 初始化Cipher
        Cipher cipher = Cipher.getInstance( "RSA/ECB/PKCS1Padding" );

        // 加密
        cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
        byte [] cipherText = cipher.doFinal(plainText);

        System.out.println( "RSACipher Encoder-----"  + bytesToHexString(cipherText));
        // 解密
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        byte [] bys = cipher.doFinal(cipherText);
        System.out.println( "RSACipher----"  +  new  String(bys));
    }

    /**
     * RSA數字簽名
     *
     * @param plainText
     * @throws Exception
     */
    public  static  void  DigitalSignature( byte [] plainText)  throws  Exception {
        // 初始化對稱密鑰
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance( "RSA" );
        keyGen.initialize( 1024 );
        KeyPair key = keyGen.generateKeyPair();

        // 初始化Signature
        Signature sig = Signature.getInstance( "SHA1WithRSA" );

        // 簽名
        sig.initSign(key.getPrivate());
        sig.update(plainText);
        byte [] signature = sig.sign();

        // 解簽
        sig.initVerify(key.getPublic());
        sig.update(plainText);
        try  {
            if  (sig.verify(signature)) {
                System.out.println( "Signature verified" );
            }  else
                System.out.println( "Signature failed" );
        }  catch  (SignatureException e) {
            System.out.println( "Signature failed" );
        }
    }

    public  static  byte [] hexString2Bytes(String src) {
        byte [] ret =  new  byte [ 8 ];
        byte [] tmp = src.getBytes();
        for  ( int  i =  0 ; i <  8 ; i++) {
            ret[i] = uniteBytes(tmp[i *  2 ], tmp[i *  2  +  1 ]);
        }
        return  ret;
    }

    public  static  String bytesToHexString( byte [] src) {
        StringBuilder stringBuilder =  new  StringBuilder( "" );
        if  (src ==  null  || src.length <=  0 ) {
            return  null ;
        }
        for  ( int  i =  0 ; i < src.length; i++) {
            int  v = src[i] &  0xFF ;
            String hv = Integer.toHexString(v);
            if  (hv.length() <  2 ) {
                stringBuilder.append( 0 );
            }
            stringBuilder.append(hv);
        }
        return  stringBuilder.toString();
    }

    public  static  byte  uniteBytes( byte  src0,  byte  src1) {
        byte  _b0 = Byte.decode( "0x"  +  new  String( new  byte [] { src0 })).byteValue();
        _b0 = ( byte ) (_b0 <<  4 );
        byte  _b1 = Byte.decode( "0x"  +  new  String( new  byte [] { src1 })).byteValue();
        byte  ret = ( byte ) (_b0 ^ _b1);
        return  ret;
    }
}