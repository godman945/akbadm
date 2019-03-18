package com.pchome.akbadm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DateTimeUtils {

    private static Logger logger = LogManager.getRootLogger();


    private static final String LOG_NAME = DateTimeUtils.class.getName();
    private static final int LOG_ERROR = 30121;

    private static Calendar calendar = Calendar.getInstance(Locale.TAIWAN);
    public static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yMdHms16 = new SimpleDateFormat("yyyyMMddHHmmssss");
    public static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    public static SimpleDateFormat yyyyMMddHHmm = new SimpleDateFormat("yyyyMMddHHmm");
    public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat yyyyMM = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat mmddHHmm = new SimpleDateFormat("MMddHHmm");

    /**
     * 將日期轉換成需要的格式
     * @param parseDate 欲轉換的日期，若為null 則為現在時間
     * @param sdf 轉換的格式
     * @return
     */
    public static String getDateStr(Date parseDate , SimpleDateFormat sdf){
        if(parseDate == null){
            parseDate = getDate();
        }
        return sdf.format(parseDate);
    }

    /**
     * 將字串格式轉換成時間格式
     * @param parseStr 欲轉換的時間字串
     * @param sdf 欲轉換的格式
     * @return
     */
    public static Date parseDate(String parseStr , SimpleDateFormat sdf){
        Date tmpDate = null;
        try {
            tmpDate = sdf.parse(parseStr);
        } catch (ParseException e) {
            logger.error("Parse String to Date Fail !!\n" + e);
        }
        return tmpDate;
    }

    /**
     * 取得現在時間
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 取得當月第一天日期
     */
    public static String getFirstDayOfMonth(Date dateParam, SimpleDateFormat sdf) {
        if(sdf == null){
            sdf = yyyy_MM_dd;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateParam);
        calendar.set(Calendar.DATE, 1);
        return sdf.format(calendar.getTime());
    }

    /**
     * 取得當月最後一天日期
     */
    public static String getLastDayOfMonth(Date dateParam , SimpleDateFormat sdf) {
        if(sdf == null){
            sdf = yyyy_MM_dd;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(dateParam);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        return sdf.format(calendar.getTime());
    }

    /**
     * 移動月份
     * @param dateParam 基準日期
     * @param shiftParam 移動參數
     * @return
     */
    public static Date shiftMontth(Date dateParam , Integer shiftParam){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( dateParam );
        calendar.set( Calendar.MONTH, calendar.get( Calendar.MONTH ) + shiftParam );
        return calendar.getTime();
    }

    /**
     * 移動日期
     * @param dateParam 基準日期
     * @param shiftParam 移動參數
     * @return
     */
    public static Date shiftDay(Date dateParam , Integer shiftParam){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( dateParam );
        calendar.set( Calendar.DATE, calendar.get( Calendar.DATE ) + shiftParam );
        return calendar.getTime();
    }
}
