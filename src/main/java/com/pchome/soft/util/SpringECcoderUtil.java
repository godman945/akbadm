package com.pchome.soft.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pchome.soft.util.PHPSerializer.AssocArray;
import com.pchome.soft.util.PHPSerializer.PHPSerializer;

public class SpringECcoderUtil {
    private static final Log log = LogFactory.getLog(SpringECcoderUtil.class);

    /**
     * ec encode
     * @param value
     * @return encode string
     */
    public String encodeString(String value) {
        String result="";

        int x=11;
        int y=99;
        int key1=(int)(Math.random() * (y-x+1)) + x;
        int key2=(int)(Math.random() * (y-x+1)) + x;

        StringBuffer resultBuffer=new StringBuffer();

        int assciiWord=0;

        resultBuffer.append(key1);
        resultBuffer.append(key2);

        for(int i=0;i<value.length();i++){
            assciiWord=value.charAt(i);
            if(i%2==0){
                resultBuffer.append( new Character((char)(assciiWord+key1)).toString());
            }else{
                resultBuffer.append( new Character((char)(assciiWord+key2)).toString());
            }
        }

//        String os=resultBuffer.toString();
//        log.info("os="+os);

        byte[] isostr=null;

        try {
            isostr = resultBuffer.toString().getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        Base64 base64 = new Base64();
        result = new String(base64.encode(isostr));

        return result;
    }

    /**
     * ec urlencode
     * @param value
     * @return encode string
     * @throws UnsupportedEncodingException
     */
    public String encoderEcString(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(encodeString(value), "UTF-8");
    }

    /**
     * ec encode
     * @param value
     * @return decode string
     */
    public String decodeString(String value) {
        String result="";

        Base64 base64 = new Base64();
        byte[] g=base64.decode(value.getBytes());

        try {
            value=new String(g,"ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        int key1=Integer.valueOf(value.substring(0,2));
        int key2=Integer.valueOf(value.substring(2,4));

        value=value.substring(4,value.length());

        StringBuffer resultBuffer=new StringBuffer();

        int assciiWord=0;

        for(int i=0;i<value.length();i++){
            assciiWord=value.charAt(i);
            //log.info( assciiWord );
            if(i%2==0){
                resultBuffer.append((char)(assciiWord-key1));
            }else{
                resultBuffer.append((char)(assciiWord-key2));
            }
        }

        result=resultBuffer.toString();

        return result;
    }

    /**
     * ec urldecode
     * @param value
     * @return decode string
     * @throws UnsupportedEncodingException
     */
    public String decoderEcString(String value) throws UnsupportedEncodingException {
        return decodeString(URLDecoder.decode(value, "UTF-8"));
    }

    public String MD5CheckString(String str, String timeStamp){
        String result ="";
        String sig ="";

        sig = str +"&timestamp="+timeStamp+"&key=DkezgfraoBlo4oX7koB7xBEKaYcdh6DUOayopso0t1leRrlpcn";
        result = DigestUtils.md5Hex(sig);

        return result;
    }

    public String getPHPSerialzerStr(LinkedHashMap<String,Object> dataMap){
        String result="";
        PHPSerializer ps = new PHPSerializer();
        ps.setCharset("UTF-8");

        byte[] data=null;

        try {
            data = ps.serialize(dataMap);
            result = new String(data, "UTF-8");
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }

        return result;
    }

    public LinkedList<LinkedHashMap<String,String>> getPHPunSerialzer(String phpString) throws UnsupportedEncodingException{
        PHPSerializer ps = new PHPSerializer();

        LinkedList<LinkedHashMap<String,String>> resultList=null;

        AssocArray a;
        try {
            a = (AssocArray)ps.unserialize(phpString.getBytes("UTF-8"));

            resultList=new LinkedList<LinkedHashMap<String,String>>();
            HashMap bbb=a.toHashMap();
            Object valueO;
            for(Object key:bbb.keySet()){
                valueO=bbb.get(key);
                if(valueO instanceof AssocArray ){
                    ps.assocArrayToMap((AssocArray)valueO,resultList);
                }else{
                    ps.objectToMap(String.valueOf(key), valueO, resultList);
                }
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }

        return resultList;
    }

    public static void main(String avg[]) throws UnsupportedEncodingException{
        SpringECcoderUtil eCcoderUtil=new SpringECcoderUtil();
        //ec encoder test
        String encoderStr=eCcoderUtil.encodeString("pchome8044");
        System.out.println(encoderStr);
        //ec decoder test
        String decoderStr=eCcoderUtil.decodeString(encoderStr);
        System.out.println(decoderStr);

        // PHPSerialzer test
//        LinkedHashMap<String, Object> mapTest = new LinkedHashMap<String, Object>();
//        mapTest.put("HD_NETAMT", "3000");
//        mapTest.put("MEM_NAME", "王大明");
//        mapTest.put("MEM_MAIL", "test.lee@gmail.com");
//        mapTest.put("MEM_SEX", "0");
//        mapTest.put("MEM_TEL", "0911525412");
//        mapTest.put("MEM_ZIP", "231");
//        mapTest.put("MEM_ADDR", "臺北市敦化南路一段152號");
//        mapTest.put("IND_BTN", "回關鍵字登入");
//        mapTest.put("IND_URL", "http://www.pchome.com.tw");
//        mapTest.put("PD_ORDSEQ", "01");
//        mapTest.put("PD_PRDNME", "關鍵字廣告");
//        mapTest.put("PD_ATTB", "M");
//        mapTest.put("PD_QTY", "1");
//        mapTest.put("PD_UNTPRI", "3000");
//        mapTest.put("PD_TOTPRI", "3000");

        // 將map轉成 PHPSerialzer String 格式
//        String phStr=eCcoderUtil.getPHPSerialzerStr(mapTest);
//        phStr=phStr.replace("\"", "\\\"");
//        System.out.println(phStr);

        // 將PHPSerialzer String 轉回 java map 格式 包在 list 內
//        String phStr="a:2:{i:0;i:0;i:1;a:17:{s:6:\"INV_NO\";s:10:\"AA00005074\";s:10:\"CHECK_CODE\";s:3:\"032\";s:8:\"ORDER_ID\";s:13:\"8100400000093\";s:10:\"ISSUE_DATE\";s:10:\"2010/04/29\";s:6:\"TAX_ID\";s:8:\"28863560\";s:9:\"INV_TITLE\";s:36:\"樂屋國際資訊股份有限公司\";s:8:\"INV_ADDR\";N;s:9:\"IS_DONATE\";s:1:\"N\";s:8:\"TAX_TYPE\";s:1:\"1\";s:7:\"TAX_AMT\";s:1:\"0\";s:8:\"SALE_AMT\";s:2:\"10\";s:9:\"TOTAL_AMT\";s:2:\"10\";s:9:\"VALID_AMT\";s:2:\"10\";s:10:\"ISSUE_USER\";s:15:\"AutoOrderCommit\";s:9:\"IS_DEMAND\";s:1:\"Y\";s:7:\"IS_VOID\";s:1:\"N\";s:10:\"INV_DETAIL\";a:1:{s:2:\"01\";a:8:{s:10:\"DISP_LABEL\";s:30:\"熱門-[售]祺祺測試中~~!\";s:7:\"TAX_AMT\";s:4:\"0.48\";s:9:\"DSALE_AMT\";s:4:\"9.52\";s:10:\"SUB_TOTAMT\";s:2:\"10\";s:8:\"QUANTITY\";s:1:\"1\";s:10:\"UNIT_PRICE\";s:4:\"9.52\";s:9:\"VALID_AMT\";s:2:\"10\";s:11:\"DOMAIN_NAME\";N;}}}}";
//        String phStr="a:2:{i:0;i:0;i:1;a:2:{s:6:\"RET_ID\";s:4:\"FAIL\";s:6:\"RET_CT\";s:46:\"該發票[AA00005074]已有索取列印記錄!\";}}";
//        String phStr = "a:15:{s:9:\"HD_NETAMT\";s:4:\"3000\";s:8:\"MEM_NAME\";s:9:\"王大明\";s:8:\"MEM_MAIL\";s:18:\"test.lee@gmail.com\";s:7:\"MEM_SEX\";s:1:\"0\";s:7:\"MEM_TEL\";s:10:\"0911525412\";s:7:\"MEM_ZIP\";s:3:\"231\";s:8:\"MEM_ADDR\";s:33:\"臺北市敦化南路一段152號\";s:7:\"IND_BTN\";s:18:\"回關鍵字登入\";s:7:\"IND_URL\";s:24:\"http://www.pchome.com.tw\";s:9:\"PD_ORDSEQ\";s:2:\"01\";s:9:\"PD_PRDNME\";s:15:\"關鍵字廣告\";s:7:\"PD_ATTB\";s:1:\"M\";s:6:\"PD_QTY\";s:1:\"1\";s:9:\"PD_UNTPRI\";r:2;s:9:\"PD_TOTPRI\";r:2;}";
//        String phStr = "a:15:{s:9:\"HD_NETAMT\";s:4:\"3000\";s:8:\"MEM_NAME\";s:9:\"王大明\";s:8:\"MEM_MAIL\";s:18:\"test.lee@gmail.com\";s:7:\"MEM_SEX\";s:1:\"0\";s:7:\"MEM_TEL\";s:10:\"0911525412\";s:7:\"MEM_ZIP\";s:3:\"231\";s:8:\"MEM_ADDR\";s:33:\"臺北市敦化南路一段152號\";s:7:\"IND_BTN\";s:18:\"回關鍵字登入\";s:7:\"IND_URL\";s:24:\"http://www.pchome.com.tw\";s:9:\"PD_ORDSEQ\";s:2:\"01\";s:9:\"PD_PRDNME\";s:15:\"關鍵字廣告\";s:7:\"PD_ATTB\";s:1:\"M\";s:6:\"PD_QTY\";s:1:\"1\";s:9:\"PD_UNTPRI\";r:2;s:9:\"PD_TOTPRI\";r:2;}";
//        String phStr = "a:2:{i:0;i:0;i:1;a:23:{s:6:\"ORD_ID\";s:13:\"8101000000041\";s:8:\"BUY_DATE\";s:19:\"2010/10/22 10:35:58\";s:8:\"PAY_TYPE\";s:3:\"ATM\";s:10:\"BUYER_NAME\";s:9:\"楊秀秀\";s:10:\"BUYER_MAIL\";s:20:\"rdtest@pchome.com.tw\";s:9:\"INV_TITLE\";N;s:6:\"TAX_ID\";N;s:8:\"REC_NAME\";s:9:\"楊秀秀\";s:7:\"REC_TEL\";s:7:\"6625874\";s:8:\"REC_ADDR\";s:18:\"台北市大安區\";s:7:\"REC_ZIP\";s:3:\"106\";s:9:\"BANK_CODE\";s:29:\"013(國泰世華銀行代碼)\";s:7:\"ACCOUNT\";s:16:\"2220102300000413\";s:6:\"AMOUNT\";s:4:\"5000\";s:8:\"DUE_DATE\";s:10:\"2010/10/23\";s:8:\"RED_DESC\";N;s:6:\"MEM_ID\";s:20:\"rdtest@pchome.com.tw\";s:10:\"ORD_STATUS\";s:2:\"BW\";s:9:\"ORD_COMDT\";N;s:7:\"ORD_AMT\";s:4:\"5000\";s:8:\"FAK_DATE\";N;s:8:\"AUTH_MSG\";N;s:11:\"PROD_DETAIL\";a:1:{s:2:\"01\";a:14:{s:7:\"PROD_ID\";s:8:\"M0000992\";s:9:\"PROD_NAME\";s:22:\"PChome 關鍵字廣告\";s:9:\"PROD_SPEC\";N;s:8:\"QUANTITY\";s:1:\"1\";s:9:\"UNITPRICE\";s:4:\"5000\";s:10:\"TOTALPRICE\";s:4:\"5000\";s:8:\"OUT_DATE\";N;s:7:\"WEB_KEY\";s:10:\"P000109-57\";s:11:\"PROD_CHNLNO\";s:7:\"P000109\";s:10:\"MAJOR_CASE\";s:14:\"20101000000833\";s:10:\"SALES_CASE\";s:14:\"20101000000833\";s:5:\"PHASE\";s:1:\"1\";s:10:\"START_DATE\";N;s:8:\"END_DATE\";N;}}}}";
//        String phStr="a:2:{i:0;i:0;i:1;a:23:{s:6:"ORD_ID";s:13:"8101000000041";s:8:"BUY_DATE";s:19:"2010/10/22 10:35:58";s:8:"PAY_TYPE";s:3:"ATM";s:10:"BUYER_NAME";s:9:"aaa";s:10:"BUYER_MAIL";s:20:"rdtest@pchome.com.tw";s:9:"INV_TITLE";N;s:6:"TAX_ID";N;s:8:"REC_NAME";s:9:"aaa";s:7:"REC_TEL";s:7:"6625874";s:8:"REC_ADDR";s:18:"aaa";s:7:"REC_ZIP";s:3:"106";s:9:"BANK_CODE";s:29:"013(aaa)";s:7:"ACCOUNT";s:16:"2220102300000413";s:6:"AMOUNT";s:4:"5000";s:8:"DUE_DATE";s:10:"2010/10/23";s:8:"RED_DESC";N;s:6:"MEM_ID";s:20:"rdtest@pchome.com.tw";s:10:"ORD_STATUS";s:2:"BW";s:9:"ORD_COMDT";N;s:7:"ORD_AMT";s:4:"5000";s:8:"FAK_DATE";N;s:8:"AUTH_MSG";N;s:11:"PROD_DETAIL";a:1:{s:2:"01";a:14:{s:7:"PROD_ID";s:8:"M0000992";s:9:"PROD_NAME";s:22:"PChome aaa";s:9:"PROD_SPEC";N;s:8:"QUANTITY";s:1:"1";s:9:"UNITPRICE";s:4:"5000";s:10:"TOTALPRICE";s:4:"5000";s:8:"OUT_DATE";N;s:7:"WEB_KEY";s:10:"P000109-57";s:11:"PROD_CHNLNO";s:7:"P000109";s:10:"MAJOR_CASE";s:14:"20101000000833";s:10:"SALES_CASE";s:14:"20101000000833";s:5:"PHASE";s:1:"1";s:10:"START_DATE";N;s:8:"END_DATE";N;}}}}";
//        String ur="https://value-dev.pchome.com.tw/adm/sup_order_data_dev.htm?order_id=8101000000041&store_id=V000007&timestamp=1288076821871&sig=8468ac9602f5d762a8461b7956af558b";

//        String phStr = HttpUtil.getInstance().doGet(ur, "UTF-8");
//        System.out.println("data2: "+phStr);
//        LinkedList<LinkedHashMap<String,String>> resultList=eCcoderUtil.getPHPunSerialzer(phStr);
//        for(LinkedHashMap<String,String> resultMap:resultList){
//            for(String k:resultMap.keySet()){
//                System.out.println("--------------------------");
//                System.out.println(k+"="+resultMap.get(k));
//            }
//        }
    }
}