package com.pchome.akbadm.quartzs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpReport;
import com.pchome.akbadm.db.service.ad.IPfpAdActionService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.ad.IPfpAdService;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.report.IPfpReportService;
import com.pchome.akbadm.db.service.trans.IAdmTransLossService;
import com.pchome.akbadm.db.service.trans.IPfpTransDetailService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.report.EnumPageName;
import com.pchome.enumerate.trans.EnumTransType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.HttpUtil;

@Transactional
public class ReportJob {
    private Log log = LogFactory.getLog(getClass().getName());

    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private String rmiserviceServer;
    private String charset;
    private int timeout;
    private String proxyHost;
    private int proxyPort;

    private IAdmTransLossService admTransLossService;
    private IPfpAdService pfpAdService;
    private IPfpAdActionService pfpAdActionService;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpCustomerInfoService pfpCustomerInfoService;
    private IPfpTransDetailService pfpTransDetailService;
    private IPfpReportService pfpReportService;

    public void process(Date startDate, Date endDate) throws Exception {
        log.info("==== ReportJob process start ====");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        try {
            while (calendar.getTimeInMillis() <= endDate.getTime()) {
                calculate(calendar.getTime());
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (Exception e) {
            log.error(calendar.getTime(), e);
        }

        log.info("==== ReportJob process end ====");
    }

    private void calculate(Date date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String nowStr = df.format(now.getTime());

        Calendar reportCalendar = Calendar.getInstance();
        reportCalendar.setTime(df.parse(df.format(date)));  // clear HH:mm:ss
        Date reportDate = reportCalendar.getTime();
        String reportDateStr = df.format(reportDate);

        Calendar dueCalendar = Calendar.getInstance();
        dueCalendar.setTimeInMillis(reportCalendar.getTimeInMillis());
        dueCalendar.add(Calendar.DAY_OF_YEAR, 7);
        String dueDateStr = df.format(dueCalendar.getTime());

        PfpReport pfpReport = new PfpReport();
        StringBuffer url = null;
        String result = null;

        /* activateNum start */
        // activateNum
        pfpReport.setActivateNum(pfpCustomerInfoService.selectCustomerInfoIdCountByActivateDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ACTIVATE_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getActivateNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // laterActivateNum
        pfpReport.setLaterActivateNum(pfpCustomerInfoService.selectCustomerInfoIdCountByActivateDate(EnumPfdAccountPayType.LATER, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_LATER_ACTIVATE_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getLaterActivateNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // advanceActivateNum
        pfpReport.setAdvanceActivateNum(pfpReport.getActivateNum() - pfpReport.getLaterActivateNum());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ADVANCE_ACTIVATE_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdvanceActivateNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);
        /* activateNum end */

        /* activatePrice start */
        // advanceActivatePrice
        pfpReport.setAdvanceActivatePrice(pfpTransDetailService.selectActivatePriceSumByActivateDate(EnumTransType.ADD_MONEY, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ADVANCE_ACTIVATE_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdvanceActivatePrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // laterActivatePrice
        pfpReport.setLaterActivatePrice(pfpTransDetailService.selectActivatePriceSumByActivateDate(EnumTransType.LATER_SAVE, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_LATER_ACTIVATE_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getLaterActivatePrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // activatePrice
        pfpReport.setActivatePrice(pfpReport.getAdvanceActivatePrice() + pfpReport.getLaterActivatePrice());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ACTIVATE_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getActivatePrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);
        /* activatePrice end */

        /* customerNum start */
        // advanceCustomerNum
        pfpReport.setAdvanceCustomerNum(pfpTransDetailService.selectCustomerInfoCountByTransDate(EnumTransType.ADD_MONEY, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ADVANCE_CUSTOMER_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdvanceCustomerNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // laterCustomerNum
        pfpReport.setLaterCustomerNum(pfpTransDetailService.selectCustomerInfoCountByTransDate(EnumTransType.LATER_SAVE, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_LATER_CUSTOMER_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getLaterCustomerNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // customerNum
        pfpReport.setCustomerNum(pfpReport.getAdvanceCustomerNum() + pfpReport.getLaterCustomerNum());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_CUSTOMER_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getCustomerNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);
        /* customerNum end */

        /* customerPrice start */
        // advanceCustomerPrice
        pfpReport.setAdvanceCustomerPrice(pfpTransDetailService.selectTransPriceSumByTransDate(EnumTransType.ADD_MONEY, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_ADVANCE_CUSTOMER_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdvanceCustomerPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // laterCustomerPrice
        pfpReport.setLaterCustomerPrice(pfpTransDetailService.selectTransPriceSumByTransDate(EnumTransType.LATER_SAVE, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_LATER_CUSTOMER_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getLaterCustomerPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // customerPrice
        pfpReport.setCustomerPrice(pfpReport.getAdvanceCustomerPrice() + pfpReport.getLaterCustomerPrice());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_CUSTOMER_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getCustomerPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);
        /* customerPrice end */

        // giftPrice
        float giftPrice = pfpTransDetailService.selectTransPriceSumByTransDate(EnumTransType.GIFT, reportDateStr);
        float feedbackPrice = pfpTransDetailService.selectTransPriceSumByTransDate(EnumTransType.FEEDBACK_MONEY, reportDateStr);
        pfpReport.setGiftPrice(giftPrice + feedbackPrice);
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_GIFT_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getGiftPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // refundPrice
        pfpReport.setRefundPrice(pfpTransDetailService.selectTransPriceSumByTransDate(EnumTransType.REFUND, reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_REFUND_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getRefundPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // remainPrice
        pfpReport.setRemainPrice(pfpCustomerInfoService.selectCustomerInfoRemain());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_REMAIN_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getRemainPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adNew
        pfpReport.setAdNew(pfpAdService.selectAdNewByUserVerifyDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_NEW.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdNew()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adNum
        pfpReport.setAdNum(pfpAdService.selectAdNumByActionDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_NUM.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdNum()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adReady
        pfpReport.setAdReady(pfpAdService.selectAdReadyByActionDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_READY.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdReady()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adDue
        pfpReport.setAdDue(pfpAdService.selectAdDueByActionDate(nowStr, dueDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_DUE.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdDue()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adPv
        Float[] pvclks = pfpAdPvclkService.selectAdPvclkSumByPvclkDate(reportDateStr, null, null, null, null);
        pfpReport.setAdPv(pvclks[0].intValue());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_PV.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdPv()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adClk - adInvalidClk
        pfpReport.setAdClk(pvclks[1].intValue() - pvclks[3].intValue());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_CLK.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdClk()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adClkPrice - adInvalidClkPrice
        pfpReport.setAdClkPrice(pvclks[2] - pvclks[4].intValue());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_CLK_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdClkPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // cpc
        if (pfpReport.getAdClk() > 0) {
            pfpReport.setCpc(pfpReport.getAdClkPrice() / pfpReport.getAdClk());
        }
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_CPC.toString().toLowerCase()).append("&counter=").append(pfpReport.getCpc()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        if (pfpReport.getAdPv() > 0) {
            // cpm
            pfpReport.setCpm(pfpReport.getAdClkPrice() * 1000 / pfpReport.getAdPv());

            // ctr
            pfpReport.setCtr(pfpReport.getAdClk() * 100 / (float) pfpReport.getAdPv());
        }
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_CPM.toString().toLowerCase()).append("&counter=").append(pfpReport.getCpm()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_CTR.toString().toLowerCase()).append("&counter=").append(pfpReport.getCtr()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adUnderMax
        pfpReport.setAdUnderMax(pfpAdActionService.selectAdUnderMaxByPvclkDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_UNDER_MAX.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdUnderMax()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // reachRate
        pfpReport.setReachRate(pfpAdActionService.selectReachRate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_REACH_RATE.toString().toLowerCase()).append("&counter=").append(pfpReport.getReachRate()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // overPrice
        pfpReport.setOverPrice(admTransLossService.selectTransLossSumByTransDate(reportDateStr));
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_OVER_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getOverPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adInvalidClk
        pfpReport.setAdInvalidClk(pvclks[3].intValue());
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_INVALID_CLK.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdInvalidClk()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // adInvalidClkPrice
        pfpReport.setAdInvalidClkPrice(pvclks[4]);
        url = new StringBuffer(rmiserviceServer).append("/pvsetup.html?channelName=keyword&pageName=").append(EnumPageName.KEYWORD_AD_INVALID_CLK_PRICE.toString().toLowerCase()).append("&counter=").append(pfpReport.getAdInvalidClkPrice()).append("&createDate=").append(reportDateStr);
        result = HttpUtil.getInstance().getResult(url.toString(), charset, timeout, null, proxyHost, proxyPort);

        // reportTime
        pfpReport.setReportTime(reportDate);

        now = new Date();
        pfpReport.setCreateTime(now);
        pfpReport.setUpdateTime(now);

        // delete
        pfpReportService.deletePfpReport(reportDate);

        // insert
        pfpReportService.save(pfpReport);

        log.info("calculate " + reportDateStr + " " + result);
    }

    public void setRmiserviceServer(String rmiserviceServer) {
        this.rmiserviceServer = rmiserviceServer;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setAdmTransLossService(IAdmTransLossService admTransLossService) {
        this.admTransLossService = admTransLossService;
    }

    public void setPfpAdService(IPfpAdService pfpAdService) {
        this.pfpAdService = pfpAdService;
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

    public void setPfpTransDetailService(IPfpTransDetailService pfpTransDetailService) {
        this.pfpTransDetailService = pfpTransDetailService;
    }

    public void setPfpReportService(IPfpReportService pfpReportService) {
        this.pfpReportService = pfpReportService;
    }

    public static void printUsage() {
        System.out.println("Usage: [DATE]...");
        System.out.println();
        System.out.println("[DATE] format: yyyy-MM-dd");
        System.out.println("  startDate");
        System.out.println("  endDate");
    }

    public static void main(String[] args) throws Exception {
        Date startDate = null;
        Date endDate = null;

    	System.out.println(">>> args.length = " + args.length);

        if (args.length == 0) {
        	System.out.println(">>> args[0] must be prd/stg");
        	return;
        } else if (args.length == 1) {
        	Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            startDate = calendar.getTime();
            endDate = startDate;
        } else if (args.length == 2) {
        	startDate = df.parse(args[1]);
            endDate = df.parse(args[1]);
        } else if (args.length == 3) {
        	startDate = df.parse(args[1]);
            endDate = df.parse(args[2]);
        } else {
        	System.out.println(">>> args too much");
        	return;
        }

        ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
        ReportJob job = context.getBean(ReportJob.class);
        job.process(startDate, endDate);
    }
}
