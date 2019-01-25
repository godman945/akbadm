package com.pchome.akbadm.quartzs;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfpAdPvclk;
import com.pchome.akbadm.db.service.ad.IPfpAdKeywordPvclkService;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.db.vo.report.CheckBillReportVo;
import com.pchome.config.TestConfig;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;
import com.pchome.soft.util.SpringEmailUtil;

@Transactional
public class CheckPvclkJob {
    private static int LIMIT_OF_HOUR = -3;
    private static String nextLine = "<br />\r\n";

    private Log log = LogFactory.getLog(getClass());

    private String mailSubject;
    private String mailFrom;
    private String[] mailTo;
    private IPfpAdPvclkService pfpAdPvclkService;
    private IPfpAdKeywordPvclkService pfpAdKeywordPvclkService;
    private IAdReportService adReportService;
    private SpringEmailUtil springEmailUtil;

    private String checkAdPriceFile;
    public static final String MAIL_API_NO = "P159";

    public boolean process() throws Exception {
        log.info("====CheckPvclkJob.process() start====");

        boolean flag = false;
        boolean adPvclkFlag = false;
//        boolean adKeywordPvclkFlag = false;

        List<PfpAdPvclk> adPvclkList = pfpAdPvclkService.selectPfpAdPvclk(0, 1);
//        List<PfpAdKeywordPvclk> adKeywordPvclkList = pfpAdKeywordPvclkService.selectPfpAdKeywordPvclk(0, 1);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, LIMIT_OF_HOUR);
        calendar.setTime(df.parse(df.format(calendar.getTime())));

        log.info("checkTime: " + sdf.format(calendar.getTime()));

        Calendar pvclkCalendar = Calendar.getInstance();

        StringBuffer content = new StringBuffer();

        // ad pvclk
        if (adPvclkList.size() > 0) {
            PfpAdPvclk pfpAdPvclk = adPvclkList.get(0);

            pvclkCalendar.setTime(df.parse(pfpAdPvclk.getAdPvclkDate() + " " + pfpAdPvclk.getAdPvclkTime()));
            log.info("adPvclkTime: " + sdf.format(pvclkCalendar.getTime()));

            if (pvclkCalendar.getTime().getTime() < calendar.getTimeInMillis()) {
                log.info("checkTime: " + calendar.getTimeInMillis());
                log.info("adPvclkTime: " + pvclkCalendar.getTime().getTime());

                content.append("ad pvclk").append(nextLine);
                content.append("check = ").append(sdf.format(calendar.getTime())).append(nextLine);
                content.append("update = ").append(sdf.format(pfpAdPvclk.getAdPvclkUpdateTime().getTime())).append(nextLine);
                content.append("date = ").append(pfpAdPvclk.getAdPvclkDate()).append(nextLine);
                content.append("time = ").append(pfpAdPvclk.getAdPvclkTime()).append(nextLine);
                content.append(nextLine);
            }
            else {
                adPvclkFlag = true;
            }
        }
        log.info("adPvclkFlag: " + adPvclkFlag);

//        // ad keyword pvclk
//        if (adKeywordPvclkList.size() > 0) {
//            PfpAdKeywordPvclk pfpAdKeywordPvclk = adKeywordPvclkList.get(0);
//
//            pvclkCalendar.setTime(df.parse(pfpAdKeywordPvclk.getAdKeywordPvclkDate() + " " + pfpAdKeywordPvclk.getAdKeywordPvclkTime()));
//            log.info("adKeywordPvclkTime: " + sdf.format(pvclkCalendar.getTime()));
//
//            if (pvclkCalendar.getTime().getTime() < calendar.getTimeInMillis()) {
//                content.append("ad keyword pvclk").append(nextLine);
//                content.append("check = ").append(sdf.format(calendar.getTime())).append(nextLine);
//                content.append("update = ").append(sdf.format(pfpAdKeywordPvclk.getAdKeywordPvclkUpdateTime().getTime())).append(nextLine);
//                content.append("date = ").append(pfpAdKeywordPvclk.getAdKeywordPvclkDate()).append(nextLine);
//                content.append("time = ").append(pfpAdKeywordPvclk.getAdKeywordPvclkTime()).append(nextLine);
//                content.append(nextLine);
//            }
//            else {
//                adKeywordPvclkFlag = true;
//            }
//        }
//        log.info("adKeywordPvclkFlag: " + adKeywordPvclkFlag);
//
//        flag = adPvclkFlag & adKeywordPvclkFlag;
//        log.info("flag: " + flag);

        flag = adPvclkFlag;

        if (StringUtils.isNotBlank(content.toString())) {
            springEmailUtil.sendHtmlEmail(mailSubject, mailFrom, mailTo, null, content.toString());

            log.info("subject = " + mailSubject);
            if (mailTo != null) {
                for (String to: mailTo) {
                    log.info("mail to = " + to);
                }
            }
        }

        checkAdPrice();

        log.info("====CheckPvclkJob.process() end====");

        return flag;
    }

    //檢查每是花費餘額是否不足，若不足則發送簡訊通知
    private void checkAdPrice(){
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
    	
    	String today = "2016-01-01";
    	long controlPriceSum = 0;		//調控金額
		double priceSum = 0;			//費用
		double spendRateSum = 0;		//消耗率

		today = df.format(new Date());

		try {
			List<AdActionReportVO> dataList = new ArrayList<AdActionReportVO>();
			dataList = adReportService.getAdSpendReport(today, today, null);

			if (dataList.size()!=0) {
				for (int i=0; i<dataList.size(); i++) {
					AdActionReportVO vo = new AdActionReportVO();
					vo = dataList.get(i);
					if(vo.getAdActionControlPrice() != null){
						controlPriceSum += Long.parseLong(vo.getAdActionControlPrice().replaceAll(",", ""));
	    			}

					if(vo.getPriceSum() != null){
	    				priceSum += Double.parseDouble(vo.getPriceSum().replaceAll(",", ""));
	    			}
				}

				//消耗率
				if(controlPriceSum != 0 && priceSum != 0){
					spendRateSum = priceSum / controlPriceSum * 100;
				}

				File checkPath = new File(checkAdPriceFile);
				File checkFile = new File(checkAdPriceFile+ "checkPrice." + today + ".txt");
				
				//檢查資料夾是否存在
				if(!checkPath.exists()){
					checkPath.mkdirs();
				}
				
				//消耗率大於95%則發簡訊通知
				if(spendRateSum >= 95){

					//檢查檔案是否存在(存在表示當天已經發過簡訊，若發過簡訊則不再發)
					if (!checkFile.exists()) {
						log.info("==== new file ====");
						checkFile.createNewFile();
						log.info("==== new file success ====");

						Mail mail = null;
						mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
						String [] phone = mail.getPhone();
						String smBody = URLEncoder.encode("每日廣告花費餘額不足!!(消耗率已達95%)", "BIG5");

						//發送簡訊通知
						for(String p : phone){
							 if(StringUtils.isNotEmpty(p)){//檢查電話號碼是否存在
								 HttpClient client = new DefaultHttpClient();
								 HttpGet get = new HttpGet("http://smexpress.mitake.com.tw:9600/SmSendGet.asp?username=16606102&password=pchomeportal&dstaddr="+ p +"&smbody=" + smBody);
								 // execute http get method
								 client.execute(get);
							 }
						 }

					}
				} else {
					//檢查檔案是否存在，存在則刪除檔案
					if (checkFile.exists()) {
						checkFile.delete();
						
						Mail mail = null;
						mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
						String [] phone = mail.getPhone();
						String smBody = URLEncoder.encode("每日廣告花費餘額已補足!!(目前消耗率為" + df2.format(spendRateSum) + "%)", "BIG5");

						//發送簡訊通知
						for(String p : phone){
							 if(StringUtils.isNotEmpty(p)){//檢查電話號碼是否存在
								 HttpClient client = new DefaultHttpClient();
								 HttpGet get = new HttpGet("http://smexpress.mitake.com.tw:9600/SmSendGet.asp?username=16606102&password=pchomeportal&dstaddr="+ p +"&smbody=" + smBody);
								 // execute http get method
								 client.execute(get);
							 }
						 }
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

    }

    //檢查pfp_ad_pvclk、pfp_ad_action_report、pfd_ad_action_report、 pfbx_ad_time_report的pv及clk是否一樣
	public void checkReportProcess(){
        log.info("====CheckPvclkJob.checkReportProcess() start====");

    	try {
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	String today = "2016-01-01";
	    	String mailMsg = "";

	    	today = df.format(new Date());

	    	Map<String,Boolean> checkMap = new HashMap<String,Boolean>();
	    	checkMap = pfpAdPvclkService.checkPvClk(today);

	    	if(checkMap.get("pfpPv") != null){
	    		if(!checkMap.get("pfpPv")){
	    			mailMsg += "pfp報表pv資料不正確<br>";
	    		}
	    	}

	    	if(checkMap.get("pfpClk") != null){
	    		if(!checkMap.get("pfpClk")){
	    			mailMsg += "pfp報表clk資料不正確<br>";
	    		}
	    	}

	    	if(checkMap.get("pfbPv") != null){
	    		if(!checkMap.get("pfbPv")){
	    			mailMsg += "pfb報表pv資料不正確<br>";
	    		}
	    	}

	    	if(checkMap.get("pfbClk") != null){
	    		if(!checkMap.get("pfbClk")){
	    			mailMsg += "pfb報表clk資料不正確<br>";
	    		}
	    	}

	    	if(checkMap.get("pfdPv") != null){
	    		if(!checkMap.get("pfdPv")){
	    			mailMsg += "pfd報表pv資料不正確<br>";
	    		}
	    	}

	    	if(checkMap.get("pfdClk") != null){
	    		if(!checkMap.get("pfdClk")){
	    			mailMsg += "pfd報表clk資料不正確<br>";
	    		}
	    	}

    		if(StringUtils.isNotEmpty(mailMsg)){
    			Mail mail = null;
    			mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

    			if (mail == null) {
    				throw new Exception("Mail Object is null.");
    			}

    			mail.setMsg("<html><body>" + mailMsg + "</body></html>");

    			springEmailUtil.sendHtmlEmail("pfp、pfb、pfd報表數據錯誤通知(" + today + ")", mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
    		}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}

        log.info("====CheckPvclkJob.checkReportProcess() end====");
    }

	
	//每小時整理pfp_ad_video_report時檢查影音廣告IDC播出，大於10寄信通知
	public void checkPfpIdcProcess(Map<String,Integer> idcMap){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Mail mail = null;
		try {
			mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
			if (mail == null) {
				throw new Exception("Mail Object is null.");
			}
			StringBuffer mailMsg = new StringBuffer();
			mailMsg.append("<html><body>");
			for (Entry<String, Integer> entry : idcMap.entrySet()) {
				mailMsg.append(entry.getKey() + " : " +entry.getValue() +"次" + "<br>");
			}
			mailMsg.append("</body></html>");
			mail.setMsg("<html><body>" + mailMsg.toString() + "</body></html>");
//			String test [] = {"nicolee@staff.pchome.com.tw","alexchen@staff.pchome.com.tw"};
			springEmailUtil.sendHtmlEmail("pfp影音廣告IDC數量警告(" + df.format(date) + ")", mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//每日9:15檢查前一日的帳戶與攤提餘額是否相同，不同則發mail通知
	public void checkPfpRemainProcess(){
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);
		String yesterday = dateFormate.format(c.getTime());

		List<CheckBillReportVo> vos = pfpAdPvclkService.findCheckBillReportToVo(yesterday);

		String msg = "";
		for(CheckBillReportVo vo:vos){
			if(vo.getTransRemain() != vo.getRecognizeRemain()){
				msg = msg + vo.getPfpId() + "、" ;
			}
		}

		if(StringUtils.isNotEmpty(msg)){
			Mail mail = null;
			try {
				mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);
				if (mail == null) {
					throw new Exception("Mail Object is null.");
				}

				mail.setMsg("<html><body>pfp攤提餘額錯誤帳戶：" + msg.substring(0, msg.length()-1) + "</body></html>");

				springEmailUtil.sendHtmlEmail("pfp攤提餘額錯誤通知(" + yesterday + ")", mail.getMailFrom(), mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

    public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

        CheckPvclkJob job = context.getBean(CheckPvclkJob.class);
        job.process();
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailTo(String[] mailTo) {
        this.mailTo = mailTo;
    }

    public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
        this.pfpAdPvclkService = pfpAdPvclkService;
    }

    public void setPfpAdKeywordPvclkService(IPfpAdKeywordPvclkService pfpAdKeywordPvclkService) {
        this.pfpAdKeywordPvclkService = pfpAdKeywordPvclkService;
    }

    public void setSpringEmailUtil(SpringEmailUtil springEmailUtil) {
        this.springEmailUtil = springEmailUtil;
    }

	public void setCheckAdPriceFile(String checkAdPriceFile) {
		this.checkAdPriceFile = checkAdPriceFile;
	}

	public void setAdReportService(IAdReportService adReportService) {
		this.adReportService = adReportService;
	}

}
