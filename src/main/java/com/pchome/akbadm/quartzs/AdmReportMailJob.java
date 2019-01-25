package com.pchome.akbadm.quartzs;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.service.report.IAdSourceReportService;
import com.pchome.akbadm.db.service.report.IAdmClientCountReportService;
import com.pchome.akbadm.db.vo.report.AdmClientCountForNext30DayReportVO;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.db.vo.report.AdmCountReportVO;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;
import com.pchome.akbadm.utils.EmailUtils;
import com.pchome.config.TestConfig;
import com.pchome.service.portalcms.PortalcmsUtil;
import com.pchome.service.portalcms.bean.Mail;

public class AdmReportMailJob {

	private Log log = LogFactory.getLog(getClass().getName());

	private IAdmClientCountReportService admClientCountReportService;
	private IAdSourceReportService adSourceReportService;

	private DecimalFormat df1 = new DecimalFormat("###,###,###,###");
	private DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

	//發信參數
	public static final String MAIL_API_NO = "P133";
	private String mailDir;
	private EmailUtils emailUtils;
	private String emailTitle = "PChome廣告聯播網中心";

	public void process() throws Exception {
        log.info("====AdmReportMailJob.process() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);
		String yesterday = dateFormate.format(c.getTime());

		long tim1 = System.currentTimeMillis();
		AdmClientCountReportMail(yesterday);
		long tim2 = System.currentTimeMillis();
		System.out.println("AdmClientCountReportMail cost:"+(tim2 - tim1)+"ms");
		
		tim1 = System.currentTimeMillis();
		AdmAdSourceReportMail(yesterday);
		tim2 = System.currentTimeMillis();
		System.out.println("AdmAdSourceReportMail cost:"+(tim2 - tim1)+"ms");
		
		tim1 = System.currentTimeMillis();
		AdmClientCountForNex30DayReportMail();
		tim2 = System.currentTimeMillis();
		System.out.println("AdmClientCountForNex30DayReportMail cost:"+(tim2 - tim1)+"ms");
		
		
		tim1 = System.currentTimeMillis();
		AdmCountReportMail(yesterday);
		tim2 = System.currentTimeMillis();
		System.out.println("AdmCountReportMail cost:"+(tim2 - tim1)+"ms");
		
        log.info("====AdmReportMailJob.process() end====");
	}

	public void processOldDay(String date) throws Exception {
        log.info("====AdmReportMailJob.processOldDay() start====");
        long tim1 = System.currentTimeMillis();
		AdmClientCountReportMail(date);
		long tim2 = System.currentTimeMillis();
		System.out.println("AdmClientCountReportMail cost:"+(tim2 - tim1)+"ms");
		
		tim1 = System.currentTimeMillis();
		AdmAdSourceReportMail(date);
		tim2 = System.currentTimeMillis();
		System.out.println("AdmAdSourceReportMail cost:"+(tim2 - tim1)+"ms");
		
		tim1 = System.currentTimeMillis();
		AdmClientCountForNex30DayReportMail();
		tim2 = System.currentTimeMillis();
		System.out.println("AdmClientCountForNex30DayReportMail cost:"+(tim2 - tim1)+"ms");
		
		tim1 = System.currentTimeMillis();
		AdmCountReportMail(date);
		tim2 = System.currentTimeMillis();
		System.out.println("AdmCountReportMail cost:"+(tim2 - tim1)+"ms");
		
        log.info("====AdmReportMailJob.processOldDay() end====");
	}

	//廣告聯播網客戶 統計日報表 mail
	private void AdmClientCountReportMail(String reportDate) throws Exception {

		String firstDay = getMonthFirstDay(reportDate);
		String lastDay = reportDate;

		AdmClientCountReportVO totalVO = new AdmClientCountReportVO();	//總計
		AdmClientCountReportVO avgVO = new AdmClientCountReportVO();	//平均

		List<AdmClientCountReportVO> dataList = admClientCountReportService.findReportData(firstDay, lastDay);

		Double dataSize = new Double(dataList.size());	//總比數
		Double pfpClientCountSum = new Double(0);		//直客客戶數
		Double pfdClientCountSum = new Double(0);		//經銷商客戶數
		Double pfpAdClkPriceSum = new Double(0);		//直客廣告點擊數費用
		Double pfdAdClkPriceSum = new Double(0);		//經銷商廣告點擊數費用
		Double pfpAdActionMaxPriceSum = new Double(0);	//直客廣告每日預算
		Double pfdAdActionMaxPriceSum = new Double(0);	//經銷商廣告每日預算
		Double pfpSpendRateSum = new Double(0);			//直客預算達成率
		Double pfdSpendRateSum = new Double(0);			//經銷商預算達成率
		Double pfpAdCountSum = new Double(0);			//直客廣告數
		Double pfdAdCountSum = new Double(0);			//經銷商廣告數
		Double adPvSum = new Double(0);					//廣告曝光數
		Double adClkSum = new Double(0);				//廣告點擊數
		Double adInvalidClkSum = new Double(0);			//無效點擊數
		Double adClkPriceSum = new Double(0);			//廣告點擊數總費用
		Double adInvalidClkPriceSum = new Double(0);	//無效點擊數總費用
		Double clkRateSum = new Double(0);				//廣告點擊率
		Double adClkPriceAvgSum = new Double(0);		//單次點擊費用
		Double adPvPriceSum = new Double(0);			//每千次曝光費用
		Double lossCostSum = new Double(0);				//超播金額
		Double pfpSaveSum = new Double(0);				//pchome營運儲值金
		Double pfpFreeSum = new Double(0);				//pchome營運贈送金
		Double pfpPostpaidSum = new Double(0);			//pchome營運後付費
		Double pfbSaveSum = new Double(0);				//PFB分潤儲值金
		Double pfbFreeSum = new Double(0);				//PFB分潤贈送金
		Double pfbPostpaidSum = new Double(0);			//PFB分潤後付費
		Double pfdSaveSum = new Double(0);				//PFD佣金儲值金
		Double pfdFreeSum = new Double(0);				//PFD佣金贈送金
		Double pfdPostpaidSum = new Double(0);			//PFD佣金後付費

		if(!dataList.isEmpty()){
			for(AdmClientCountReportVO vo:dataList){
				pfpClientCountSum += new Double(vo.getPfpClientCount().replaceAll(",", ""));
				pfdClientCountSum += new Double(vo.getPfdClientCount().replaceAll(",", ""));
				pfpAdClkPriceSum += new Double(vo.getPfpAdClkPrice().replaceAll(",", ""));
				pfdAdClkPriceSum += new Double(vo.getPfdAdClkPrice().replaceAll(",", ""));
				pfpAdActionMaxPriceSum += new Double(vo.getPfpAdActionMaxPrice().replaceAll(",", ""));
				pfdAdActionMaxPriceSum += new Double(vo.getPfdAdActionMaxPrice().replaceAll(",", ""));
				pfpAdCountSum += new Double(vo.getPfpAdCount().replaceAll(",", ""));
				pfdAdCountSum += new Double(vo.getPfdAdCount().replaceAll(",", ""));
				adPvSum += new Double(vo.getAdPv().replaceAll(",", ""));
				adClkSum += new Double(vo.getAdClk().replaceAll(",", ""));
				adInvalidClkSum += new Double(vo.getAdInvalidClk().replaceAll(",", ""));
				adClkPriceSum += new Double(vo.getAdClkPrice().replaceAll(",", ""));
				adInvalidClkPriceSum += new Double(vo.getAdInvalidClkPrice().replaceAll(",", ""));
				lossCostSum += new Double(vo.getLossCost().replaceAll(",", ""));
				pfpSaveSum += new Double(vo.getPfpSave().replaceAll(",", ""));
				pfpFreeSum += new Double(vo.getPfpFree().replaceAll(",", ""));
				pfpPostpaidSum += new Double(vo.getPfpPostpaid().replaceAll(",", ""));
				pfbSaveSum += new Double(vo.getPfbSave().replaceAll(",", ""));
				pfbFreeSum += new Double(vo.getPfbFree().replaceAll(",", ""));
				pfbPostpaidSum += new Double(vo.getPfbPostpaid().replaceAll(",", ""));
				pfdSaveSum += new Double(vo.getPfdSave().replaceAll(",", ""));
				pfdFreeSum += new Double(vo.getPfdFree().replaceAll(",", ""));
				pfdPostpaidSum += new Double(vo.getPfdPostpaid().replaceAll(",", ""));
			}
		}

		//直客預算達成率
		if(pfpAdClkPriceSum != 0 && pfpAdActionMaxPriceSum != 0){
			pfpSpendRateSum = (pfpAdClkPriceSum / pfpAdActionMaxPriceSum)*100;
		}

		//經銷商預算達成率
		if(pfdAdClkPriceSum != 0 && pfdAdActionMaxPriceSum != 0){
			pfdSpendRateSum = (pfdAdClkPriceSum / pfdAdActionMaxPriceSum)*100;
		}

		//點擊率
		if(adPvSum != 0 && adClkSum != 0){
			clkRateSum = (adClkSum / adPvSum)*100;
		}

		//單次點擊費用
		if(adClkSum != 0 && adClkPriceSum != 0){
			adClkPriceAvgSum = adClkPriceSum / adClkSum;
		}

		//千次曝光費用
		if(adPvSum != 0 && adClkPriceSum != 0){
			adPvPriceSum = (adClkPriceSum / adPvSum)*1000;
		}

		//總計
		totalVO.setPfpClientCount(df1.format(pfpClientCountSum));
		totalVO.setPfdClientCount(df1.format(pfdClientCountSum));
		totalVO.setPfpAdClkPrice(df1.format(pfpAdClkPriceSum));
		totalVO.setPfdAdClkPrice(df1.format(pfdAdClkPriceSum));
		totalVO.setPfpAdActionMaxPrice(df1.format(pfpAdActionMaxPriceSum));
		totalVO.setPfdAdActionMaxPrice(df1.format(pfdAdActionMaxPriceSum));
		totalVO.setPfpSpendRate(df2.format(pfpSpendRateSum));
		totalVO.setPfdSpendRate(df2.format(pfdSpendRateSum));
		totalVO.setPfpAdCount(df1.format(pfpAdCountSum));
		totalVO.setPfdAdCount(df1.format(pfdAdCountSum));
		totalVO.setAdPv(df1.format(adPvSum));
		totalVO.setAdClk(df1.format(adClkSum));
		totalVO.setAdInvalidClk(df1.format(adInvalidClkSum));
		totalVO.setAdClkPrice(df1.format(adClkPriceSum));
		totalVO.setAdInvalidClkPrice(df1.format(adInvalidClkPriceSum));
		totalVO.setClkRate(df2.format(clkRateSum));
		totalVO.setAdClkPriceAvg(df2.format(adClkPriceAvgSum));
		totalVO.setAdPvPrice(df2.format(adPvPriceSum));
		totalVO.setLossCost(df1.format(lossCostSum));
		totalVO.setPfpSave(df2.format(pfpSaveSum));
		totalVO.setPfpFree(df2.format(pfpFreeSum));
		totalVO.setPfpPostpaid(df2.format(pfpPostpaidSum));
		totalVO.setPfbSave(df2.format(pfbSaveSum));
		totalVO.setPfbFree(df2.format(pfbFreeSum));
		totalVO.setPfbPostpaid(df2.format(pfbPostpaidSum));
		totalVO.setPfdSave(df2.format(pfdSaveSum));
		totalVO.setPfdFree(df2.format(pfdFreeSum));
		totalVO.setPfdPostpaid(df2.format(pfdPostpaidSum));

		//平均
		avgVO.setPfpClientCount(df2.format(pfpClientCountSum/dataSize));
		avgVO.setPfdClientCount(df2.format(pfdClientCountSum/dataSize));
		avgVO.setPfpAdClkPrice(df2.format(pfpAdClkPriceSum/dataSize));
		avgVO.setPfdAdClkPrice(df2.format(pfdAdClkPriceSum/dataSize));
		avgVO.setPfpAdActionMaxPrice(df2.format(pfpAdActionMaxPriceSum/dataSize));
		avgVO.setPfdAdActionMaxPrice(df2.format(pfdAdActionMaxPriceSum/dataSize));
		avgVO.setPfpSpendRate(df2.format(pfpSpendRateSum));
		avgVO.setPfdSpendRate(df2.format(pfdSpendRateSum));
		avgVO.setPfpAdCount(df2.format(pfpAdCountSum/dataSize));
		avgVO.setPfdAdCount(df2.format(pfdAdCountSum/dataSize));
		avgVO.setAdPv(df2.format(adPvSum/dataSize));
		avgVO.setAdClk(df2.format(adClkSum/dataSize));
		avgVO.setAdInvalidClk(df2.format(adInvalidClkSum/dataSize));
		avgVO.setAdClkPrice(df2.format(adClkPriceSum/dataSize));
		avgVO.setAdInvalidClkPrice(df2.format(adInvalidClkPriceSum/dataSize));
		avgVO.setClkRate(df2.format(clkRateSum));
		avgVO.setAdClkPriceAvg(df2.format(adClkPriceAvgSum));
		avgVO.setAdPvPrice(df2.format(adPvPriceSum));
		avgVO.setLossCost(df2.format(lossCostSum/dataSize));
		avgVO.setPfpSave(df2.format(pfpSaveSum/dataSize));
		avgVO.setPfpFree(df2.format(pfpFreeSum/dataSize));
		avgVO.setPfpPostpaid(df2.format(pfpPostpaidSum/dataSize));
		avgVO.setPfbSave(df2.format(pfbSaveSum/dataSize));
		avgVO.setPfbFree(df2.format(pfbFreeSum/dataSize));
		avgVO.setPfbPostpaid(df2.format(pfbPostpaidSum/dataSize));
		avgVO.setPfdSave(df2.format(pfdSaveSum/dataSize));
		avgVO.setPfdFree(df2.format(pfdFreeSum/dataSize));
		avgVO.setPfdPostpaid(df2.format(pfdPostpaidSum/dataSize));

		//發信內容
		String mailContent = "";
		String[] date = reportDate.split("-");
		String titleContent = "付費刊登 廣告聯播網客戶 統計日報表 " + date[0] + "年" + date[1] + "月" + date[2] + "日";
		File mailFile = new File(mailDir + "admClientCountReportMail.html");
		log.info(mailFile.getPath());
		if (mailFile.exists()) {
		    try {
			mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
		    } catch (IOException e) {
                log.error(mailFile.getPath(), e);
		    }
		}

		mailContent = mailContent.replaceAll("@titleContent", titleContent);

		String detalContent = "";

		//每日資料
		if(!dataList.isEmpty()){
			for(AdmClientCountReportVO vo:dataList){

				String centerStyle = "td03";
				String rightStyle = "td04";

				if(StringUtils.equals(vo.getWeek(), "六") || StringUtils.equals(vo.getWeek(), "日")){
					centerStyle = "td05";
					rightStyle = "td06";
				}

				detalContent +="<tr>";
				detalContent += "<td class=" + centerStyle + "><font><span>";
				detalContent += vo.getCountDate();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + centerStyle + "><font><span>";
				detalContent += vo.getWeek();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpClientCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfdClientCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpAdClkPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfdAdClkPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpAdActionMaxPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfdAdActionMaxPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpSpendRate() + "%";
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfdSpendRate() + "%";
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpAdCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfdAdCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getAdPv();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getAdClk();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getClkRate() + "%";
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getAdClkPriceAvg();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getAdPvPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getLossCost();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getAdInvalidClk();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getAdInvalidClkPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpSave();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpFree();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpPostpaid();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfbSave();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfbFree();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfbPostpaid();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfdSave();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfdFree();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfdPostpaid();
				detalContent += "</span></font></td>";
				detalContent +="</tr>";

			}
		}

		//總計
		detalContent +="<tr>";
		detalContent += "<td colspan=2 class=td07><font><span>";
		detalContent += "加總";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfpClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfdClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfpAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfdAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfpAdActionMaxPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfdAdActionMaxPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfpSpendRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfdSpendRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfpAdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfdAdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getAdPv();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getAdClk();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getClkRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getAdClkPriceAvg();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getAdPvPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getLossCost();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getAdInvalidClk();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getAdInvalidClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfpSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfpFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfpPostpaid();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfbSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfbFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfbPostpaid();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfdSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfdFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + totalVO.getPfdPostpaid();
		detalContent += "</span></font></td>";
		detalContent +="</tr>";

		//平均
		detalContent +="<tr>";
		detalContent += "<td colspan=2 class=td07><font><span>";
		detalContent += "平均";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfdClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfdAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpAdActionMaxPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfdAdActionMaxPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpSpendRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfdSpendRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpAdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfdAdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getAdPv();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getAdClk();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getClkRate() + "%";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getAdClkPriceAvg();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getAdPvPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getLossCost();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getAdInvalidClk();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getAdInvalidClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpPostpaid();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfbSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfbFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfbPostpaid();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfdSave();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfdFree();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfdPostpaid();
		detalContent += "</span></font></td>";
		detalContent +="</tr>";

		mailContent = mailContent.replaceAll("@detalContent", detalContent);

		Mail mail = null;
		mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		if (mail == null) {
			throw new Exception("Mail Object is null.");
		}

		mail.setMsg(mailContent);

		emailUtils.sendHtmlEmail(titleContent, mail.getMailFrom(), emailTitle, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

	}

	//廣告聯播網網站 統計日報表 mail
	private void AdmAdSourceReportMail(String reportDate) throws Exception {

		Map<String, String> conditionMap = new HashMap<String, String>();

		String firstDay = getMonthFirstDay(reportDate);
		String lastDay = reportDate;

		PfpAdReportVO totalVO = new PfpAdReportVO();	//總計
		PfpAdReportVO avgVO = new PfpAdReportVO();	//平均

		conditionMap.put("startDate", firstDay);
    	conditionMap.put("endDate", lastDay);
    	conditionMap.put("emailReport", "yes");

		List<PfpAdReportVO> dataList = adSourceReportService.getAdSourceReportByCondition(conditionMap);
		List<PfpAdReportVO> sumList = adSourceReportService.getAdSourceReportSumByCondition(conditionMap);

		Double dataSize = new Double(sumList.size());	//總比數
		Double adClkPriceSum = new Double(0);			//廣告點擊費用
		Double adPvSum = new Double(0);					//廣告曝光數
		Double adClkSum = new Double(0);				//廣告點擊數
		Double clkRateSum = new Double(0);				//廣告點擊率
		Double adClkAvgPriceSum = new Double(0);		//單次點擊收益
		Double adPvRateSum = new Double(0);				//千次曝光收益
		Double invalidBonusSum = new Double(0);			//惡意點擊收益
		Double totalBonusSum = new Double(0);			//PFB分潤

		if(!sumList.isEmpty()){
			for(PfpAdReportVO vo:sumList){
				adClkPriceSum += new Double(vo.getAdClkPriceSum().replaceAll(",", ""));
				adPvSum += new Double(vo.getAdPvSum().replaceAll(",", ""));
				adClkSum += new Double(vo.getAdClkSum().replaceAll(",", ""));
				invalidBonusSum += new Double(vo.getInvalidBonus().replaceAll(",", ""));
				totalBonusSum += new Double(vo.getTotalBonus().replaceAll(",", ""));
			}
		}

		//點擊率
		if(adPvSum != 0 && adClkSum != 0){
			clkRateSum = (adClkSum / adPvSum)*100;
		}

		//單次點擊收益
		if(adClkSum != 0 && totalBonusSum != 0){
			adClkAvgPriceSum = totalBonusSum / adClkSum;
		}

		//千次曝光收益
		if(adPvSum != 0 && totalBonusSum != 0){
			adPvRateSum = (totalBonusSum / adPvSum)*1000;
		}

		//總計
		totalVO.setAdClkPriceSum(df1.format(adClkPriceSum));
		totalVO.setAdPvSum(df1.format(adPvSum));
		totalVO.setAdClkSum(df1.format(adClkSum));
		totalVO.setAdClkRate(df2.format(clkRateSum));
		totalVO.setAdClkAvgPrice(df2.format(adClkAvgPriceSum));
		totalVO.setAdPvRate(df2.format(adPvRateSum));
		totalVO.setInvalidBonus(df2.format(invalidBonusSum));
		totalVO.setTotalBonus(df2.format(totalBonusSum));

		//平均
		avgVO.setAdClkPriceSum(df1.format(adClkPriceSum/dataSize));
		avgVO.setAdPvSum(df1.format(adPvSum/dataSize));
		avgVO.setAdClkSum(df1.format(adClkSum/dataSize));
		avgVO.setAdClkRate(df2.format(clkRateSum));
		avgVO.setAdClkAvgPrice(df2.format(adClkAvgPriceSum));
		avgVO.setAdPvRate(df2.format(adPvRateSum));
		avgVO.setInvalidBonus(df2.format(invalidBonusSum/dataSize));
		avgVO.setTotalBonus(df2.format(totalBonusSum/dataSize));

		//發信內容
		String mailContent = "";
		String[] date = reportDate.split("-");
		String titleContent = "付費刊登 廣告聯播網網站 統計日報表 " + date[0] + "年" + date[1] + "月" + date[2] + "日";
		File mailFile = new File(mailDir + "AdSourceReport.html");
		log.info(mailFile.getPath());
		if (mailFile.exists()) {
		    try {
			mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
		    } catch (IOException e) {
                log.error(mailFile.getPath(), e);
		    }
		}
		
		
		mailContent = mailContent.replaceAll("@titleContent", titleContent);
		
		
//		String detalContent = "";
		StringBuffer detalContent = new StringBuffer();
		String countDay = reportDate;
		int i = 1;

		//每日資料
		if(!dataList.isEmpty()){
			for(PfpAdReportVO vo:dataList){

				String centerStyle = "td03";
				String rightStyle = "td04";
				String leftStyle = "td05";

				//判斷日期與上一筆資料是否相同，不同則更換顏色
				if(!StringUtils.equals(countDay, vo.getAdPvclkDate())){
					i++;
				}

				if(i%2 == 0){
					centerStyle = "td09";
					rightStyle = "td10";
					leftStyle = "td11";
				}
				detalContent.append("<tr>");
				detalContent.append( "<td class=" + centerStyle + "><font><span>");
				detalContent.append( vo.getAdPvclkDate());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + leftStyle + "><font><span>");
				detalContent.append( vo.getPfbCustomerInfoName());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + leftStyle + "><font><span>");
				detalContent.append( vo.getPfbWebsiteChineseName());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( vo.getPfpCustomerInfoId());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( "\\$ " + vo.getAdClkPriceSum());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( vo.getAdPvSum());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( vo.getAdClkSum());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( vo.getAdClkRate());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( "\\$ " + vo.getAdClkAvgPrice());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( "\\$ " + vo.getAdPvRate());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( "\\$ " + vo.getInvalidBonus());
				detalContent.append( "</span></font></td>");
				detalContent.append( "<td class=" + rightStyle + "><font><span>");
				detalContent.append( "\\$ " + vo.getTotalBonus());
				detalContent.append( "</span></font></td>");
				detalContent.append("</tr>");
				
//				detalContent +="<tr>";
//				detalContent += "<td class=" + centerStyle + "><font><span>";
//				detalContent += vo.getAdPvclkDate();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + leftStyle + "><font><span>";
//				detalContent += vo.getPfbCustomerInfoName();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + leftStyle + "><font><span>";
//				detalContent += vo.getPfbWebsiteChineseName();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += vo.getPfpCustomerInfoId();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += "\\$ " + vo.getAdClkPriceSum();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += vo.getAdPvSum();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += vo.getAdClkSum();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += vo.getAdClkRate();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += "\\$ " + vo.getAdClkAvgPrice();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += "\\$ " + vo.getAdPvRate();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += "\\$ " + vo.getInvalidBonus();
//				detalContent += "</span></font></td>";
//				detalContent += "<td class=" + rightStyle + "><font><span>";
//				detalContent += "\\$ " + vo.getTotalBonus();
//				detalContent += "</span></font></td>";
//				detalContent +="</tr>";
//				countDay = vo.getAdPvclkDate();
			}
		}
		mailContent = mailContent.replaceAll("@detalContent", detalContent.toString());

//		String sumContent = "";
		StringBuffer sumContent = new StringBuffer();
		//小計
		if(!sumList.isEmpty()){
			for(PfpAdReportVO vo:sumList){
				sumContent.append("<tr>");
				sumContent.append( "<td class=td05 colspan=3><font><span>");
				sumContent.append( vo.getPfbCustomerInfoName());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "");
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "\\$ " + vo.getAdClkPriceSum());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( vo.getAdPvSum());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( vo.getAdClkSum());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( vo.getAdClkRate());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "\\$ " + vo.getAdClkAvgPrice());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "\\$ " + vo.getAdPvRate());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "\\$ " + vo.getInvalidBonus());
				sumContent.append( "</span></font></td>");
				sumContent.append( "<td class=td04><font><span>");
				sumContent.append( "\\$ " + vo.getTotalBonus());
				sumContent.append( "</span></font></td>");
				sumContent.append("</tr>");
//				sumContent +="<tr>";
//				sumContent += "<td class=td05 colspan=3><font><span>";
//				sumContent += vo.getPfbCustomerInfoName();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "";
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "\\$ " + vo.getAdClkPriceSum();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += vo.getAdPvSum();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += vo.getAdClkSum();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += vo.getAdClkRate();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "\\$ " + vo.getAdClkAvgPrice();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "\\$ " + vo.getAdPvRate();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "\\$ " + vo.getInvalidBonus();
//				sumContent += "</span></font></td>";
//				sumContent += "<td class=td04><font><span>";
//				sumContent += "\\$ " + vo.getTotalBonus();
//				sumContent += "</span></font></td>";
//				sumContent +="</tr>";
			}
		}

		//加總
		sumContent.append("<tr>");
		sumContent.append( "<td class=td07 colspan=3><font><span>");
		sumContent.append( "加總");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + totalVO.getAdClkPriceSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( totalVO.getAdPvSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( totalVO.getAdClkSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( totalVO.getAdClkRate() + "%");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + totalVO.getAdClkAvgPrice());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + totalVO.getAdPvRate());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + totalVO.getInvalidBonus());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + totalVO.getTotalBonus());
		sumContent.append( "</span></font></td>");
		sumContent.append("</tr>");
//		sumContent +="<tr>";
//		sumContent += "<td class=td07 colspan=3><font><span>";
//		sumContent += "加總";
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "";
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "\\$ " + totalVO.getAdClkPriceSum();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += totalVO.getAdPvSum();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += totalVO.getAdClkSum();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += totalVO.getAdClkRate() + "%";
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "\\$ " + totalVO.getAdClkAvgPrice();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "\\$ " + totalVO.getAdPvRate();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "\\$ " + totalVO.getInvalidBonus();
//		sumContent += "</span></font></td>";
//		sumContent += "<td class=td08><font><span>";
//		sumContent += "\\$ " + totalVO.getTotalBonus();
//		sumContent += "</span></font></td>";
//		sumContent +="</tr>";

		//平均
		sumContent.append("<tr>");
		sumContent.append( "<td class=td07 colspan=3><font><span>");
		sumContent.append( "平均");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + avgVO.getAdClkPriceSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( avgVO.getAdPvSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( avgVO.getAdClkSum());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( avgVO.getAdClkRate() + "%");
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + avgVO.getAdClkAvgPrice());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + avgVO.getAdPvRate());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + avgVO.getInvalidBonus());
		sumContent.append( "</span></font></td>");
		sumContent.append( "<td class=td08><font><span>");
		sumContent.append( "\\$ " + avgVO.getTotalBonus());
		sumContent.append( "</span></font></td>");
		sumContent.append("</tr>");

		mailContent = mailContent.replaceAll("@sumContent", sumContent.toString());
		
		Mail mail = null;
		mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		if (mail == null) {
			throw new Exception("Mail Object is null.");
		}

		mail.setMsg(mailContent);

		emailUtils.sendHtmlEmail(titleContent, mail.getMailFrom(), emailTitle, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

	}

	//關鍵字廣告 未來30天統計日報表 mail
	private void AdmClientCountForNex30DayReportMail() throws Exception {


		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		List<AdmClientCountForNext30DayReportVO> dataList = new ArrayList<AdmClientCountForNext30DayReportVO>();
		AdmClientCountForNext30DayReportVO avgVO = new AdmClientCountForNext30DayReportVO();	//平均

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		for(int i=0;i<=29;i++){
			if(i>0){
				c.add(Calendar.DATE, 1);
			}
			String searchDay = dateFormate.format(c.getTime());
			List<AdmClientCountForNext30DayReportVO> data = admClientCountReportService.findReportDataFpr30Day(searchDay);
			dataList.addAll(data);
		}

		Double dataSize = new Double(30);				//總比數
		Double pfpCountSum = new Double(0);				//廣告客戶數
		Double pfpAdActionCountSum = new Double(0);		//廣告活動數
		Double pfpAdGroupCountSum = new Double(0);		//廣告分類數
		Double pfpAdCountSum = new Double(0);			//廣告明細數
		Double pfpAdActionMaxPriceSum = new Double(0);	//廣告每日預算

		if(!dataList.isEmpty()){
			for(AdmClientCountForNext30DayReportVO vo:dataList){
				pfpCountSum += new Double(vo.getPfpCount().replaceAll(",", ""));
				pfpAdActionCountSum += new Double(vo.getPfpAdActionCount().replaceAll(",", ""));
				pfpAdGroupCountSum += new Double(vo.getPfpAdGroupCount().replaceAll(",", ""));
				pfpAdCountSum += new Double(vo.getPfpAdCount().replaceAll(",", ""));
				pfpAdActionMaxPriceSum += new Double(vo.getPfpAdActionMaxPrice().replaceAll(",", ""));
			}
		}

		//平均
		avgVO.setPfpCount(df2.format(pfpCountSum/dataSize));
		avgVO.setPfpAdActionCount(df2.format(pfpAdActionCountSum/dataSize));
		avgVO.setPfpAdGroupCount(df2.format(pfpAdGroupCountSum/dataSize));
		avgVO.setPfpAdCount(df2.format(pfpAdCountSum/dataSize));
		avgVO.setPfpAdActionMaxPrice(df2.format(pfpAdActionMaxPriceSum/dataSize));

		//發信內容
		String reportDate = dateFormate.format(now);
		String mailContent = "";
		String[] date = reportDate.split("-");
		String titleContent = "付費刊登 未來30天統計日報表 " + date[0] + "年" + date[1] + "月" + date[2] + "日";
		File mailFile = new File(mailDir + "admClientCountFor30DayReportMail.html");
		log.info(mailFile.getPath());
		if (mailFile.exists()) {
		    try {
			mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
		    } catch (IOException e) {
                log.error(mailFile.getPath(), e);
		    }
		}

		mailContent = mailContent.replaceAll("@titleContent", titleContent);

		String detalContent = "";

		//每日資料
		if(!dataList.isEmpty()){
			for(AdmClientCountForNext30DayReportVO vo:dataList){

				String centerStyle = "td03";
				String rightStyle = "td04";

				if(StringUtils.equals(vo.getWeek(), "六") || StringUtils.equals(vo.getWeek(), "日")){
					centerStyle = "td05";
					rightStyle = "td06";
				}

				detalContent +="<tr>";
				detalContent += "<td class=" + centerStyle + "><font><span>";
				detalContent += vo.getCountDate() + "(" + vo.getWeek() + ")";
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpAdActionCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpAdCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += "\\$ " + vo.getPfpAdActionMaxPrice();
				detalContent += "</span></font></td>";
				detalContent +="</tr>";
			}
		}

		//平均
		detalContent +="<tr>";
		detalContent += "<td class=td07><font><span>";
		detalContent += "平均";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpCount();;
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpAdActionCount();;
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpAdCount();;
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += "\\$ " + avgVO.getPfpAdActionMaxPrice();;
		detalContent += "</span></font></td>";
		detalContent +="</tr>";

		mailContent = mailContent.replaceAll("@detalContent", detalContent);

		Mail mail = null;
		mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		if (mail == null) {
			throw new Exception("Mail Object is null.");
		}

		mail.setMsg(mailContent);

		emailUtils.sendHtmlEmail(titleContent, mail.getMailFrom(), emailTitle, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());

	}

	//聯播網 統計日報表 mail
	private void AdmCountReportMail(String reportDate) throws Exception {
		
		String firstDay = getMonthFirstDay(reportDate);
		String lastDay = reportDate;

		AdmCountReportVO totalVO = new AdmCountReportVO();	//總計
		AdmCountReportVO avgVO = new AdmCountReportVO();	//平均

		List<AdmCountReportVO> dataList = admClientCountReportService.findCountReportData(firstDay, lastDay);

		Double dataSize = new Double(dataList.size());	//總比數
		Double adClkPriceSum = new Double(0);			//廣告點級費用數
		Double clientCountSum = new Double(0);			//廣告客戶數
		Double pfdCountSum = new Double(0);				//經銷商數
		Double pfdBonusSum = new Double(0);				//PFD獎金
		Double pfbBonusSum = new Double(0);				//PFB分潤
		Double pfpBonusSum = new Double(0);				//PFP收入
		
		if(!dataList.isEmpty()){
			for(AdmCountReportVO vo:dataList){
				adClkPriceSum += new Double(vo.getAdClkPrice().replaceAll(",", ""));
				clientCountSum += new Double(vo.getClientCount().replaceAll(",", ""));
				pfdCountSum += new Double(vo.getPfdCount().replaceAll(",", ""));
				pfdBonusSum += new Double(vo.getPfdBonus().replaceAll(",", ""));
				pfbBonusSum += new Double(vo.getPfbBonus().replaceAll(",", ""));
				pfpBonusSum += new Double(vo.getPfpBonus().replaceAll(",", ""));
			}
		}
		
		//總計
		totalVO.setAdClkPrice(df1.format(adClkPriceSum));
		totalVO.setClientCount(df1.format(clientCountSum));
		totalVO.setPfdCount(df1.format(pfdCountSum));
		totalVO.setPfdBonus(df2.format(pfdBonusSum));
		totalVO.setPfbBonus(df2.format(pfbBonusSum));
		totalVO.setPfpBonus(df2.format(pfpBonusSum));
		
		//平均
		avgVO.setAdClkPrice(df2.format(adClkPriceSum/dataSize));
		avgVO.setClientCount(df2.format(clientCountSum/dataSize));
		avgVO.setPfdCount(df2.format(pfdCountSum/dataSize));
		avgVO.setPfdBonus(df2.format(pfdBonusSum/dataSize));
		avgVO.setPfbBonus(df2.format(pfbBonusSum/dataSize));
		avgVO.setPfpBonus(df2.format(pfpBonusSum/dataSize));
		
		//發信內容
		String mailContent = "";
		String[] date = reportDate.split("-");
		String titleContent = "付費刊登 聯播網 統計日報表 " + date[0] + "年" + date[1] + "月" + date[2] + "日";
		File mailFile = new File(mailDir + "admCountReportMail.html");
		log.info(mailFile.getPath());
		if (mailFile.exists()) {
		    try {
			mailContent = FileUtils.readFileToString(mailFile, "UTF-8");
		    } catch (IOException e) {
                log.error(mailFile.getPath(), e);
		    }
		}

		mailContent = mailContent.replaceAll("@titleContent", titleContent);

		String detalContent = "";

		//每日資料
		if(!dataList.isEmpty()){
			for(AdmCountReportVO vo:dataList){
				String centerStyle = "td03";
				String rightStyle = "td04";

				if(StringUtils.equals(vo.getWeek(), "六") || StringUtils.equals(vo.getWeek(), "日")){
					centerStyle = "td05";
					rightStyle = "td06";
				}
				
				detalContent +="<tr>";
				detalContent += "<td class=" + centerStyle + "><font><span>";
				detalContent += vo.getCountDate();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + centerStyle + "><font><span>";
				detalContent += vo.getWeek();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getAdClkPrice();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getClientCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfdCount();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfdBonus();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfbBonus();
				detalContent += "</span></font></td>";
				detalContent += "<td class=" + rightStyle + "><font><span>";
				detalContent += vo.getPfpBonus();
				detalContent += "</span></font></td>";	
				detalContent +="</tr>";
			}
		}
		
		//總計
		detalContent +="<tr>";
		detalContent += "<td colspan=2 class=td07><font><span>";
		detalContent += "加總";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfdBonus();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfbBonus();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += totalVO.getPfpBonus();
		detalContent += "</span></font></td>";
		detalContent +="</tr>";
		
		//平均
		detalContent +="<tr>";
		detalContent += "<td colspan=2 class=td07><font><span>";
		detalContent += "平均";
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getAdClkPrice();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getClientCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfdCount();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfdBonus();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfbBonus();
		detalContent += "</span></font></td>";
		detalContent += "<td class=td08><font><span>";
		detalContent += avgVO.getPfpBonus();
		detalContent += "</span></font></td>";
		detalContent +="</tr>";
		
		mailContent = mailContent.replaceAll("@detalContent", detalContent);

		Mail mail = null;
		mail = PortalcmsUtil.getInstance().getMail(MAIL_API_NO);

		if (mail == null) {
			throw new Exception("Mail Object is null.");
		}

		mail.setMsg(mailContent);

		emailUtils.sendHtmlEmail(titleContent, mail.getMailFrom(), emailTitle, mail.getMailTo(), mail.getMailBcc(), mail.getMsg());
		
	}
	
	//取指定日期當月的第一天
	private String getMonthFirstDay(String searchDate) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
        c.setTime(format.parse(searchDate));
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//
        String firstDay = format.format(c.getTime());

        return firstDay;
	}

	//取指定日期當月的最後一天
	private String getMonthLastDay(String searchDate) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
        c.setTime(format.parse(searchDate));
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDay = format.format(c.getTime());

        return lastDay;
	}

	public void setAdmClientCountReportService(
			IAdmClientCountReportService admClientCountReportService) {
		this.admClientCountReportService = admClientCountReportService;
	}

	public void setAdSourceReportService(
			IAdSourceReportService adSourceReportService) {
		this.adSourceReportService = adSourceReportService;
	}

	public void setMailDir(String mailDir) {
		this.mailDir = mailDir;
	}

	public void setEmailUtils(EmailUtils emailUtils) {
		this.emailUtils = emailUtils;
	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		System.out.println(">>> start");

		AdmReportMailJob job = context.getBean(AdmReportMailJob.class);

		if (args.length == 2) {
			job.processOldDay(args[1]);
		} else {
			System.out.println("Plz input parameters, args[0]: stg/prd, args[1]: yyyy-MM-dd");
		}

		System.out.println(">>> end");
	}

}
