package com.pchome.soft.util;

import java.math.BigDecimal;
import java.util.Date;

public class ObjectTransUtil {
    private static final ObjectTransUtil objectTransUtil=new ObjectTransUtil();

    private ObjectTransUtil(){};

    public static ObjectTransUtil getInstance(){
        return objectTransUtil;
    }

    public String getObjectToString(Object od){
        String objectStr="";

        int scale = 2;//小數點位數

        int roundingMode = 4;//表示四射五入

        if(od instanceof Date ){
            objectStr=DateValueUtil.getInstance().dateToString((Date)od);
        }
        if(od instanceof Integer ){
            objectStr=String.valueOf(od);
        }
        if(od instanceof Long ){
            objectStr=String.valueOf(od);
        }
        if(od instanceof Float ){
            objectStr=String.valueOf(getFloatNumP2((Float)od));
        }
        if(od instanceof Double ){
            objectStr=String.valueOf(getDoubleNumP2((Double)od));
        }
        if(od instanceof String ){
            objectStr=String.valueOf(od);
        }

        return objectStr;
    }

    public String getFloatNumP2String(float num){
        return String.valueOf(getFloatNumP2(num));
    }

    public String getFloatNumP0String(float num){
        return String.valueOf(getFloatNumP0(num));
    }

    public String getDoubleumP2String(Double num){
        return String.valueOf(getDoubleNumP2(num));
    }

    public float getFloatNumP2(float num){
        int scale = 2;//小數點位數

        int roundingMode = 4;//表示四射五入

        BigDecimal bd;
        bd = new BigDecimal(num);

        bd = bd.setScale(scale,roundingMode);

        return bd.floatValue();
    }

   public float getFloatNumP0(float num){
        int scale = 0;//小數點位數

        int roundingMode = 4;//表示四射五入

        BigDecimal bd;
        bd = new BigDecimal(num);

        bd = bd.setScale(scale,roundingMode);

        return bd.floatValue();
    }

    public Double getDoubleNumP2(Double num){
        int scale = 2;//小數點位數
        int roundingMode = 4;//表示四射五入
        BigDecimal bd;
        bd = new BigDecimal(num);

        bd = bd.setScale(scale,roundingMode);

        return bd.doubleValue();
    }
}