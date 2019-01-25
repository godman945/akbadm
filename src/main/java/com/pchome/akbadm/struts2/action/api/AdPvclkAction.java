package com.pchome.akbadm.struts2.action.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfpAd;
import com.pchome.akbadm.db.pojo.PfpAdAction;
import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.pojo.PfpCustomerInfo;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.soft.util.HttpConnectionClient;
import com.pchome.soft.util.SpringECcoderUtil;
import com.pchome.soft.util.UAgentInfo;

@Deprecated
public class AdPvclkAction extends BaseCookieAction {
    private static int LOG_LENGTH = 39;
    private static String SYMBOL = String.valueOf(new char[]{9, 31});

    private SpringECcoderUtil springECcoderUtil;
    private IPfpAdActionService pfpAdActionService;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpCustomerInfoService pfpCustomerInfoService;
    private IPfpAdService pfpAdService;
    private String[] kdclOfflineUrl;

    private String info;

    private InputStream inputStream = new ByteArrayInputStream("".getBytes());

    @Override
    public String execute() throws Exception {
        String decodeInfo = null;

        try {
            decodeInfo = springECcoderUtil.decodeString(info);
            log.info(decodeInfo);

            // validate
            if (StringUtils.isBlank(decodeInfo)) {
                log.info("info is blank");
                return SUCCESS;
            }

            String[] infos = decodeInfo.split(SYMBOL);
            if (infos.length < LOG_LENGTH) {
                log.info("info length = " + infos.length);
                return SUCCESS;
            }

            // check hour
            boolean hourFlag = checkDatehour(infos);
            if (!hourFlag) {
                return SUCCESS;
            }

            // save
            boolean saveFlag = save(infos);
            if (!saveFlag) {
                return SUCCESS;
            }

            // notice
            boolean result = notice(infos);
            inputStream = new ByteArrayInputStream(String.valueOf(result).getBytes());
        }
        catch (Exception e) {
            log.error(decodeInfo, e);
        }

        return SUCCESS;
    }

    private boolean checkDatehour(String[] infos) throws Exception {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat hdf = new SimpleDateFormat("yyyy-MM-dd HH");

        // now date hour
        Calendar calendar = Calendar.getInstance();
        String datehourNow = hdf.format(calendar.getTime());

        // log date hour
        calendar.setTime(sdf.parse(infos[0]));
        String datehourLog = hdf.format(calendar.getTime());

        if (!datehourNow.equals(datehourLog)) {
            log.info("datehour " + datehourNow + " " + datehourLog);
            return false;
        }

        return true;
    }

    private boolean save(String[] infos) throws Exception {
        // get pfp_ad
        PfpAd pfpAd = pfpAdService.get(infos[11]);
        if (pfpAd == null) {
            log.info(infos[11] + " is null");
            return false;
        }

        // now
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        // datetime
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.setTime(sdf.parse(infos[0]));

        // host
        String host = this.getHost(infos[4]);

        // device
        UAgentInfo uAgentInfo = new UAgentInfo(infos[5], null);
        String device = uAgentInfo.detectSmartphone() ? "mobile" : "PC";

        // os
        String os = "";
        if (uAgentInfo.detectAndroid()) {
            os = "Android";
        }
        else if (uAgentInfo.detectIphoneOrIpod()) {
            os = "IOS";
        }
        else if (uAgentInfo.detectWindowsMobile()) {
            os = "Windows";
        }
        else if (uAgentInfo.detectWindows()) {
            os = "Windows";
        }

        // age_code
        int age = 0;
        if (StringUtils.isNotBlank(infos[32]) && StringUtils.isNumeric(infos[32])) {
            age = Integer.parseInt(infos[32]);
        }
        String ageCode = "";
        if ((age >= 1) && (age <= 17)) {
            ageCode = "A";
        }
        else if ((age >= 18) && (age <= 24)) {
            ageCode = "B";
        }
        else if ((age >= 25) && (age <= 34)) {
            ageCode = "C";
        }
        else if ((age >= 35) && (age <= 44)) {
            ageCode = "D";
        }
        else if ((age >= 45) && (age <= 54)) {
            ageCode = "E";
        }
        else if ((age >= 55) && (age <= 64)) {
            ageCode = "F";
        }
        else if ((age >= 65) && (age <= 74)) {
            ageCode = "G";
        }
        else if (age >= 75) {
            ageCode = "H";
        }

        // time_code
        String timeCode = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if ((hour >= 0) && (hour <= 3)) {
            timeCode = "A";
        }
        else if ((hour >= 4) && (hour <= 7)) {
            timeCode = "B";
        }
        else if ((hour >= 8) && (hour <= 11)) {
            timeCode = "C";
        }
        else if ((hour >= 12) && (hour <= 15)) {
            timeCode = "D";
        }
        else if ((hour >= 16) && (hour <= 19)) {
            timeCode = "E";
        }
        else if ((hour >= 20) && (hour <= 23)) {
            timeCode = "F";
        }

        PfpAdPvclk pfpAdPvclk = new PfpAdPvclk();
        pfpAdPvclk.setCustomerInfoId(infos[6]);
        pfpAdPvclk.setPfbxCustomerInfoId(infos[25]);
        pfpAdPvclk.setPfbxPositionId(infos[26]);
        pfpAdPvclk.setPfdCustomerInfoId(infos[23]);
        pfpAdPvclk.setPfdUserId(infos[24]);
        pfpAdPvclk.setPayType(Integer.valueOf(infos[29]));
        pfpAdPvclk.setPfpAd(pfpAd);
        pfpAdPvclk.setAdGroupSeq(infos[22]);
        pfpAdPvclk.setAdActionSeq(infos[21]);
        pfpAdPvclk.setAdType(Integer.parseInt(infos[14]));
        pfpAdPvclk.setAdPvclkDate(calendar.getTime());
        pfpAdPvclk.setAdPvclkTime(calendar.get(Calendar.HOUR_OF_DAY));
        pfpAdPvclk.setSex(infos[31]);
        pfpAdPvclk.setAgeCode(ageCode);
        pfpAdPvclk.setTimeCode(timeCode);
        pfpAdPvclk.setTemplateProductSeq(infos[8]);
        pfpAdPvclk.setTemplateAdSeq(infos[9]);
        pfpAdPvclk.setAdPvclkPropClassify(infos[15]);
        pfpAdPvclk.setAdUrl(host);
        pfpAdPvclk.setStyleNo(infos[7]);
        pfpAdPvclk.setAdPv(10); // special rule
        pfpAdPvclk.setAdClk(1);
        pfpAdPvclk.setAdInvalidClk(0);
        pfpAdPvclk.setAdPvPrice(0f);
        pfpAdPvclk.setAdClkPrice(Float.parseFloat(infos[17]));
        pfpAdPvclk.setAdInvalidClkPrice(0f);
        pfpAdPvclk.setAdPvclkDevice(device);
        pfpAdPvclk.setAdPvclkOs(os);
        pfpAdPvclk.setAdPvclkBrand("");
        pfpAdPvclk.setAdPvclkArea("");
        pfpAdPvclk.setAdActionControlPrice(Float.parseFloat(infos[18]));
        pfpAdPvclk.setAdActionMaxPrice(Float.parseFloat(infos[19]));
        pfpAdPvclk.setAdPvclkUpdateTime(now);
        pfpAdPvclk.setAdPvclkCreateTime(now);
        pfpAdPvclkService.save(pfpAdPvclk);

        return true;
    }

    private boolean notice(String[] infos) throws Exception {
        // date
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(infos[0]));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date pvclkDate = calendar.getTime();

        // pfp_customer_info
        String pfpCustomerId = infos[6];
        PfpCustomerInfo pfpCustomerInfo = pfpCustomerInfoService.get(pfpCustomerId);
        if (pfpCustomerInfo == null) {
            return false;
        }

        // customer cost
        float customerCost = pfpAdPvclkService.customerCost(pfpCustomerId, pvclkDate);
        if (customerCost >= pfpCustomerInfo.getRemain()) {
            StringBuffer url = null;
            for (String offlineUrl: kdclOfflineUrl) {
                url = new StringBuffer();
                url.append(offlineUrl);
                url.append("?pfpCustomerId=").append(pfpCustomerId);
                url.append("&offline=true");
                log.info(url);

                HttpConnectionClient.getInstance().doGet(url.toString());
            }

            return true;
        }

        // ad_action
        String adActionId = infos[21];
        PfpAdAction pfpAdAction = pfpAdActionService.get(adActionId);
        if (pfpAdAction == null) {
            return false;
        }

        // action cost
        float actionCost = pfpAdPvclkService.actionCost(pfpAdAction.getAdActionSeq(), pvclkDate);
        log.info("action cost" + pfpAdAction.getAdActionSeq() + " " + actionCost + "/" + pfpAdAction.getAdActionMax());

        if (actionCost >= pfpAdAction.getAdActionMax()) {
            StringBuffer url = null;
            for (String offlineUrl: kdclOfflineUrl) {
                url = new StringBuffer();
                url.append(offlineUrl);
                url.append("?actionId=").append(adActionId);
                url.append("&offline=true");
                log.info(url);

                HttpConnectionClient.getInstance().doGet(url.toString());
            }

            return true;
        }

        return false;
    }

    public void setSpringECcoderUtil(SpringECcoderUtil springECcoderUtil) {
        this.springECcoderUtil = springECcoderUtil;
    }

    public void setPfpAdActionService(IPfpAdActionService pfpAdActionService) {
        this.pfpAdActionService = pfpAdActionService;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }

    public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
        this.pfpCustomerInfoService = pfpCustomerInfoService;
    }

    public void setPfpAdService(IPfpAdService pfpAdService) {
        this.pfpAdService = pfpAdService;
    }

    public void setKdclOfflineUrl(String[] kdclOfflineUrl) {
        this.kdclOfflineUrl = kdclOfflineUrl;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
