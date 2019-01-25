package com.pchome.akbadm.quartzs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmPfpdAdPvclkReport;
import com.pchome.akbadm.db.pojo.PfpAdActionReport;
import com.pchome.akbadm.db.pojo.PfpAdAgeReport;
import com.pchome.akbadm.db.pojo.PfpAdGroupReport;
import com.pchome.akbadm.db.pojo.PfpAdKeywordReport;
import com.pchome.akbadm.db.pojo.PfpAdOsReport;
import com.pchome.akbadm.db.pojo.PfpAdReport;
import com.pchome.akbadm.db.pojo.PfpAdTimeReport;
import com.pchome.akbadm.db.pojo.PfpAdWebsiteReport;
import com.pchome.akbadm.db.service.report.quartzs.IAdmPfpAdVideoReportService;
import com.pchome.akbadm.db.service.report.quartzs.IAdmPfpdAdPvclkReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdActionReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdAgeReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdGroupReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdKeywordReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdOsReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdTimeReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfpAdWebsiteReportService;
import com.pchome.akbadm.db.vo.report.PfpAdVideoReportVO;
import com.pchome.config.TestConfig;

@Transactional
public class PfpAdActionReportJob {
	protected Log log = LogFactory.getLog(this.getClass().getName());
	private IPfpAdActionReportService pfpAdActionReportService;
	private IPfpAdGroupReportService pfpAdGroupReportService;
	private IPfpAdReportService pfpAdReportService;
	private IPfpAdKeywordReportService pfpAdKeywordReportService;
	private IPfpAdOsReportService pfpAdOsReportService;
	private IAdmPfpdAdPvclkReportService admPfpdAdPvclkReportService;
	private IPfpAdTimeReportService pfpAdTimeReportService;
	private IPfpAdAgeReportService pfpAdAgeReportService;
	private IPfpAdWebsiteReportService pfpAdWebsiteReportService;
	private IAdmPfpAdVideoReportService admPfpAdVideoReportService;
	private CheckPvclkJob checkPvclkJob;
	/**
	 * 1.手動執行全部report job
	 * */
	public void processAllPfpReport(String reportDate) throws Exception {
        log.info("====PfpAdActionReportJob.processAllPfpReport() start====");
		/*每日花費成效，廣告成效*/
        processPfpAdActionReport(reportDate);
		/*分類成效*/
		processPfpAdGroupReport(reportDate);
		/*廣告明細成效*/
        processPfpAdReport(reportDate);
		processPfpAdKeywordReport(reportDate);
		processPfpAdOsReport(reportDate);
		processAdmPfpdAdPvclkReport(reportDate);
        /*廣告播放時段成效*/
        processPfpAdTimeReport(reportDate);
		processPfpAdAgeReport(reportDate);
		processPfpAdWebsiteReport(reportDate);
		processVideoToVideoReportByDay(reportDate);
        log.info("====PfpAdActionReportJob.processAllPfpReport() end====");
	}
	
	
	
	/**
	 * 每天AM 1:00 執行，更新前一天資料
	 */
	public void processPerDay() throws Exception {
        log.info("====PfpAdActionReportJob.processPerDay() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);

		String reportDate = dateFormate.format(c.getTime());

		this.processPfpAdActionReport(reportDate);
		this.processPfpAdGroupReport(reportDate);
		this.processPfpAdReport(reportDate);
		this.processPfpAdKeywordReport(reportDate);
		this.processPfpAdOsReport(reportDate);
		this.processAdmPfpdAdPvclkReport(reportDate);
		this.processPfpAdTimeReport(reportDate);
		this.processPfpAdAgeReport(reportDate);
		this.processPfpAdWebsiteReport(reportDate);
		this.processVideoToVideoReportByDay(reportDate);
		
        log.info("====PfpAdActionReportJob.processPerDay() end====");
	}

	/**
	 * 每小時執行一次，更新當天資料
	 */
	public void processPerHour() throws Exception {
		try{
			log.info("====PfpAdActionReportJob.processPerHour() start====");
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
			Date now = new Date();
			String reportDate = dateFormate.format(now);
			this.processPfpAdActionReport(reportDate);
			this.processPfpAdGroupReport(reportDate);
			this.processPfpAdReport(reportDate);
			this.processPfpAdKeywordReport(reportDate);
			this.processPfpAdOsReport(reportDate);
			this.processAdmPfpdAdPvclkReport(reportDate);
			this.processPfpAdTimeReport(reportDate);
			this.processPfpAdAgeReport(reportDate);
			this.processPfpAdWebsiteReport(reportDate);
			this.processVideoToVideoReportByDay(reportDate);
	        log.info("====PfpAdActionReportJob.processPerHour() end====");
		}catch(Exception e){
			log.error("====PfpAdActionReportJob.processPerHour() error====");
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 往前補舊資料
	 */
	public void processOldData() throws Exception {
        log.info("====PfpAdActionReportJob.processOldData() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date startDate = dateFormate.parse("2017-10-01");

		Date now = new Date();

		Date reportDate = startDate;
		while (reportDate.getTime() <= now.getTime()) {

//			this.processPfpAdActionReport(dateFormate.format(reportDate));
//			this.processPfpAdGroupReport(dateFormate.format(reportDate));
//			this.processPfpAdReport(dateFormate.format(reportDate));
//			this.processPfpAdKeywordReport(dateFormate.format(reportDate));
			this.processPfpAdOsReport(dateFormate.format(reportDate));
//			this.processAdmPfpdAdPvclkReport(dateFormate.format(reportDate));
//			this.processPfpAdTimeReport(dateFormate.format(reportDate));
//			this.processPfpAdAgeReport(dateFormate.format(reportDate));
//			this.processPfpAdWebsiteReport(dateFormate.format(reportDate));

			Calendar c = Calendar.getInstance();
			c.setTime(reportDate);
			c.add(Calendar.DATE, 1);

			reportDate = c.getTime();
		}

        log.info("====PfpAdActionReportJob.processOldData() end====");
	}

	/**
	 * 把 pfp_ad_pvclk 的資料整理到 pfp_ad_action_report
	 * @param reportDate 報表日期
	 */
	private void processPfpAdActionReport(String reportDate) throws Exception {
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdActionReportService.prepareReportData(reportDate);

		List<PfpAdActionReport> pojoList = new ArrayList<PfpAdActionReport>();

		for (int i=0; i<dataList.size(); i++) {
			double pv = 0;
			int click = 0;
			int adView = 0;
			int vpv = 0;
			double cost = 0;
			double invClick = 0;
			double invPrice = 0;
			double adActionMaxPrice = 0;
			int count = 0;
			double adActionMaxPriceAvg = 0; //每日花費上限
			String customerInfoId;
			int adType;
			String adActionSeq;
			String adDevice;
			int payType;
			String adClkPriceType;
			String adOperatingRule;
			Date now = new Date();
			
			Object[] objArray = (Object[]) dataList.get(i);
			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = (((BigDecimal) objArray[1])).intValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			adActionMaxPrice = ((Double) objArray[5]).doubleValue();
			count = ((BigInteger) objArray[6]).intValue();
			customerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adActionSeq = (String) objArray[9];
			adDevice = (String) objArray[10];
			payType = ((Integer) objArray[11]).intValue();
			adClkPriceType = (String) objArray[12];
			adView = (((BigDecimal) objArray[13])).intValue();
			adOperatingRule = (String) objArray[14];
			vpv = (((BigDecimal) objArray[15])).intValue();
			//計算平均每日花費上限
			if (adActionMaxPrice>0 && count>0) {
				adActionMaxPriceAvg = adActionMaxPrice/count;
			}

			PfpAdActionReport pojo = new PfpAdActionReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk(click);
			pojo.setAdView(adView);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setAdActionMaxPrice((float) adActionMaxPriceAvg);
			pojo.setAdActionCount(count);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setPayType("" + payType);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojo.setAdVpv(vpv);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdActionReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdActionReportService.insertReportData(pojoList);
	}

	private void processPfpAdGroupReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdGroupReportService.prepareReportData(reportDate);

		List<PfpAdGroupReport> pojoList = new ArrayList<PfpAdGroupReport>();
		int vpv = 0;
		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String customerInfoId;
		int adType;
		String adActionSeq;
		String adGroupSeq;
		String adDevice;
		String adOs;
		String adClkPriceType;
		String adOperatingRule;
		int adView = 0;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			adType = ((Integer) objArray[6]).intValue();
			adActionSeq = (String) objArray[7];
			adGroupSeq = (String) objArray[8];
			adDevice = (String) objArray[9];
			adOs = (String) objArray[10];
			adClkPriceType = (String) objArray[11];
			adView = (((BigDecimal) objArray[12])).intValue();
			adOperatingRule = objArray[13].toString();
			vpv = (((BigDecimal) objArray[14])).intValue();
			
			PfpAdGroupReport pojo = new PfpAdGroupReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setAdPvclkOs(adOs);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdGroupReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdGroupReportService.insertReportData(pojoList);
	}

	/**
	 * 整理當日 pfp_ad_pvclk 資料至pfp_ad_report
	 * */
	private void processPfpAdReport(String reportDate) throws Exception {
		log.info(" PfpAdReport: "+reportDate);

		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Object> dataList = pfpAdReportService.prepareReportData(reportDate);

		List<PfpAdReport> pojoList = new ArrayList<PfpAdReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String customerInfoId;
		int adType;
		String adActionSeq;
		String adGroupSeq;
		String adSeq;
		String templateProductSeq;
		String adDevice;
		String adOs;
		String adClkPriceType;
		String adOperatingRule;
		int adView = 0;
		int vpv = 0;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			adActionSeq = (String) objArray[6];
			adGroupSeq = (String) objArray[7];
			adSeq = (String) objArray[8];
			templateProductSeq = (String) objArray[9];
			adType = ((Integer) objArray[10]).intValue();
			adDevice = (String) objArray[11];
			adOs = (String) objArray[12];
			adClkPriceType = (String) objArray[13];
			adView = (((BigDecimal) objArray[14])).intValue();
			adOperatingRule = (String) objArray[15];
			vpv = vpv + (((BigDecimal) objArray[16])).intValue();
			
			PfpAdReport pojo = new PfpAdReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdSeq(adSeq);
			pojo.setTemplateProductSeq(templateProductSeq);
			pojo.setAdType(adType);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setAdPvclkOs(adOs);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		int delSize = pfpAdReportService.deleteReportDataByReportDate(reportDate);

		log.info(" delSize: "+delSize);

		//step3. 重寫當日資料
		pfpAdReportService.insertReportData(pojoList);
	}

	private void processPfpAdKeywordReport(String reportDate) throws Exception {
		log.info(" processPfpAdKeywordReport: "+reportDate);

		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdKeywordReportService.prepareReportData(reportDate);

		List<PfpAdKeywordReport> pojoList = new ArrayList<PfpAdKeywordReport>();

		//廣泛比對
		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;

		//詞組比對
		double phrPv = 0;
		double phrClick = 0;
		double phrCost = 0;
		double phrInvClick = 0;
		double phrInvPrice = 0;

		//精準比對
		double prePv = 0;
		double preClick = 0;
		double preCost = 0;
		double preInvClick = 0;
		double preInvPrice = 0;

		String customerInfoId;
		int adKeywordType;
		String adActionSeq;
		String adGroupSeq;
		String adKeywordSeq;
		String adKeyword;
		String adKeywordDevice;
		String adKeywordOs;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			phrPv = ((BigDecimal) objArray[5]).doubleValue();
			phrClick = ((BigDecimal) objArray[6]).doubleValue();
			phrCost = ((Double) objArray[7]).doubleValue();
			phrInvClick = ((BigDecimal) objArray[8]).doubleValue();
			phrInvPrice = ((Double) objArray[9]).doubleValue();
			prePv = ((BigDecimal) objArray[10]).doubleValue();
			preClick = ((BigDecimal) objArray[11]).doubleValue();
			preCost = ((Double) objArray[12]).doubleValue();
			preInvClick = ((BigDecimal) objArray[13]).doubleValue();
			preInvPrice = ((Double) objArray[14]).doubleValue();
			customerInfoId = (String) objArray[15];
			adKeywordType = ((Integer) objArray[16]).intValue();
			adActionSeq = (String) objArray[17];
			adGroupSeq = (String) objArray[18];
			adKeywordSeq = (String) objArray[19];
			adKeyword = (String) objArray[20];
			adKeywordDevice = (String) objArray[21];
			adKeywordOs = (String) objArray[22];

			PfpAdKeywordReport pojo = new PfpAdKeywordReport();
			pojo.setAdKeywordPv((int) pv);
			pojo.setAdKeywordClk((int) click);
			pojo.setAdKeywordClkPrice((float) cost);
			pojo.setAdKeywordInvalidClk((int) invClick);
			pojo.setAdKeywordInvalidClkPrice((float) invPrice);
			pojo.setAdKeywordPhrasePv((int) phrPv);
			pojo.setAdKeywordPhraseClk((int) phrClick);
			pojo.setAdKeywordPhraseClkPrice((float) phrCost);
			pojo.setAdKeywordPhraseInvalidClk((int) phrInvClick);
			pojo.setAdKeywordPhraseInvalidClkPrice((float) phrInvPrice);
			pojo.setAdKeywordPrecisionPv((int) prePv);
			pojo.setAdKeywordPrecisionClk((int) preClick);
			pojo.setAdKeywordPrecisionClkPrice((float) preCost);
			pojo.setAdKeywordPrecisionInvalidClk((int) preInvClick);
			pojo.setAdKeywordPrecisionInvalidClkPrice((float) preInvPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdKeywordType(adKeywordType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdKeywordSeq(adKeywordSeq);
			pojo.setAdKeyword(adKeyword);
			pojo.setAdKeywordPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdKeywordPvclkDevice(adKeywordDevice);
			pojo.setAdKeywordPvclkOs(adKeywordOs);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdKeywordReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdKeywordReportService.insertReportData(pojoList);
	}

	/**
	 * 整理當日 pfp_ad_pvclk 資料至 pfp_ad_os_report
	 * */
	private void processPfpAdOsReport(String reportDate) throws Exception {
		log.info(" processPfpAdOsReport: "+reportDate);
		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdOsReportService.prepareReportData(reportDate);
		
		
		log.info(" processPfpAdOsReport dataList: "+dataList.size());
		
		
		List<PfpAdOsReport> pojoList = new ArrayList<PfpAdOsReport>();

		Date adPvclkDate = new Date();
		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String customerInfoId;
		String adPvclkDevice;
		String adPvclkOs;
		String adSeq;
		String adGroupSeq;
		String adActionSeq;
		String templateProductSeq;
		String adClkPriceType;
		int adView = 0;
		int vpv = 0;
		
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			adPvclkDate = (Date) objArray[0];
			pv = ((BigDecimal) objArray[1]).doubleValue();
			click = ((BigDecimal) objArray[2]).doubleValue();
			cost = ((Double) objArray[3]).doubleValue();
			invClick = ((BigDecimal) objArray[4]).doubleValue();
			invPrice = ((Double) objArray[5]).doubleValue();
			customerInfoId = (String) objArray[6];
			adPvclkDevice = (String) objArray[7];
			adPvclkOs = (String) objArray[8];
			adSeq = (String) objArray[9];
			adGroupSeq = (String) objArray[10];
			adActionSeq = (String) objArray[11];
			templateProductSeq = (String) objArray[12];
			adClkPriceType = (String) objArray[13];
			adView = (((BigDecimal) objArray[14])).intValue();
			vpv = (((BigDecimal) objArray[16])).intValue();
			
			PfpAdOsReport pojo = new PfpAdOsReport();
			pojo.setAdPvclkDate(adPvclkDate);
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdPvclkDevice(adPvclkDevice);
			pojo.setAdPvclkOs(adPvclkOs);
			pojo.setAdSeq(adSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setTemplateProductSeq(templateProductSeq);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdOsReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdOsReportService.insertReportData(pojoList);
	}

	/**
	 * 把 pfp_ad_pvclk 的資料整理到 adm_pfpd_ad_pvclk_report
	 * @param reportDate 報表日期
	 */
	private void processAdmPfpdAdPvclkReport(String reportDate) throws Exception {

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = admPfpdAdPvclkReportService.prepareReportData(reportDate);
		List<Object> dataList2 = admPfpdAdPvclkReportService.prepareReportData2(reportDate);
		List<Object> dataList3 = admPfpdAdPvclkReportService.prepareReportData3(reportDate);

		List<AdmPfpdAdPvclkReport> pojoList = new ArrayList<AdmPfpdAdPvclkReport>();

		pojoList = getAddAdmPfpdAdPvclkReportList(pojoList,dataList,reportDate);
		pojoList = getAddAdmPfpdAdPvclkReportList(pojoList,dataList2,reportDate);
		pojoList = getAddAdmPfpdAdPvclkReportList(pojoList,dataList3,reportDate);
		
		
		//step2. 刪除當日資料
		admPfpdAdPvclkReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		admPfpdAdPvclkReportService.insertReportData(pojoList);
	}

	private List<AdmPfpdAdPvclkReport> getAddAdmPfpdAdPvclkReportList(List<AdmPfpdAdPvclkReport> pojoList, List<Object> dataList, String reportDate) throws Exception{
		
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		
		Date now = new Date();
		int adView = 0;
		int vpv = 0;
		double pv = 0;
		double click = 0;
		double invClick = 0;
		double clkPrice = 0;
		double invalidClkPrice = 0;
		String customerInfoId;
		String pfbxCustomerInfoId;
		String pfdCustomerInfoId;
		String urlName = "";
		String url = "";
		String adPvclkDevice = "";
		String timeCode = "";
		String adClkPriceType = "";
		
		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			invClick = ((BigDecimal) objArray[2]).doubleValue();
			clkPrice = ((Double) objArray[3]).doubleValue();
			invalidClkPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			pfbxCustomerInfoId = (String) objArray[6];
			pfdCustomerInfoId = (String) objArray[7];
			if(objArray[9] != null){
				urlName = (String) objArray[9];
			} else {
				urlName = "";
			}
			
			if(objArray[10] != null){
				url = (String) objArray[10];
			} else {
				url = "";
			}
			
			if(objArray[11] != null){
				adPvclkDevice = (String) objArray[11];
			} else {
				adPvclkDevice = "";
			}
			
			if(objArray[12] != null){
				timeCode = (String) objArray[12];
			} else {
				timeCode = "";
			}
			adClkPriceType = StringUtils.isNotBlank((String) objArray[13]) ? (String) objArray[13] : "";
			adView = (((BigDecimal) objArray[14])).intValue();
			vpv = (((BigDecimal) objArray[15])).intValue();
			
			AdmPfpdAdPvclkReport pojo = new AdmPfpdAdPvclkReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdClkPrice((float) clkPrice);
			pojo.setAdInvalidClkPrice((float) invalidClkPrice);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setPfbxCustomerInfoId(pfbxCustomerInfoId);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setAdUrl(url);
			pojo.setAdUrlName(urlName);
			pojo.setAdPvclkDevice(adPvclkDevice);
			pojo.setTimeCode(timeCode);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setCreateDate(now);
			pojo.setUpdateTime(now);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojoList.add(pojo);
		}
		
		return pojoList;
	}
	
	/**
	 * 整理pfp_ad_pvclk:--> pfp_ad_website_report
	 * */
	private void processPfpAdTimeReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdTimeReportService.prepareReportData(reportDate);

		List<PfpAdTimeReport> pojoList = new ArrayList<PfpAdTimeReport>();

		
	
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			double pv = 0;
			double click = 0;
			double cost = 0;
			double invClick = 0;
			double invPrice = 0;
			String customerInfoId;
			int adType;
			String adActionSeq;
			String adGroupSeq;
			String adDevice;
			String adClkPriceType;
			String adOperatingRule;
			int adView = 0;
			int vpv = 0;
			Object[] objArray = (Object[]) dataList.get(i);
			
			String timeCode = "A";

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			adType = ((Integer) objArray[6]).intValue();
			adActionSeq = (String) objArray[7];
			adGroupSeq = (String) objArray[8];
			adDevice = (String) objArray[9];
			if(objArray[10] != null){
				if(StringUtils.isNotEmpty((String) objArray[10])){
					timeCode = (String) objArray[10];
				}
			}
			adClkPriceType = (String) objArray[11];
			adView = (((BigDecimal) objArray[12])).intValue();
			adOperatingRule = (String) objArray[13];
			vpv =  (((BigDecimal) objArray[14])).intValue();
			PfpAdTimeReport pojo = new PfpAdTimeReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setTimeCode(timeCode);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdTimeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdTimeReportService.insertReportData(pojoList);
	}
	
	private void processPfpAdAgeReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdAgeReportService.prepareReportData(reportDate);

		List<PfpAdAgeReport> pojoList = new ArrayList<PfpAdAgeReport>();

		
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			double pv = 0;
			double click = 0;
			double cost = 0;
			double invClick = 0;
			double invPrice = 0;
			String customerInfoId;
			int adType;
			String adActionSeq;
			String adGroupSeq;
			String adDevice;
			String adClkPriceType;
			String adOperatingRule;
			int adView = 0;
			int vpv = 0;
			String sex = "";
			String ageCode = "I";
			
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			adType = ((Integer) objArray[6]).intValue();
			adActionSeq = (String) objArray[7];
			adGroupSeq = (String) objArray[8];
			adDevice = (String) objArray[9];
			if(objArray[10] != null){
				if(StringUtils.isNotEmpty((String) objArray[10])){
					sex = (String) objArray[10];
				}
			}
			if(objArray[11] != null){
				if(StringUtils.isNotEmpty((String) objArray[11])){
					ageCode = (String) objArray[11];
				}
			}
			adClkPriceType = (String) objArray[12];
			adView = (((BigDecimal) objArray[13])).intValue();
			adOperatingRule = (String) objArray[14];
			vpv = (((BigDecimal) objArray[15])).intValue();
					
			PfpAdAgeReport pojo = new PfpAdAgeReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			if(StringUtils.isNotEmpty(sex)){
				pojo.setSex(sex);
			}
			pojo.setAgeCode(ageCode);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(vpv);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdAgeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdAgeReportService.insertReportData(pojoList);
	}
	
	private void processPfpAdWebsiteReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfpAdWebsiteReportService.prepareReportData(reportDate);

		List<PfpAdWebsiteReport> pojoList = new ArrayList<PfpAdWebsiteReport>();

		
		
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {	
			
			double pv = 0;
			double click = 0;
			double cost = 0;
			double invClick = 0;
			double invPrice = 0;
			String customerInfoId;
			int adType;
			String adActionSeq;
			String adGroupSeq;
			String adDevice;
			String websiteCategoryCode;
			String adClkPriceType;
			String adOperatingRule;
			int adView = 0;
			int vpv = 0;
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			customerInfoId = (String) objArray[5];
			adType = ((Integer) objArray[6]).intValue();
			adActionSeq = (String) objArray[7];
			adGroupSeq = (String) objArray[8];
			adDevice = (String) objArray[9];
			websiteCategoryCode = "";
			if(objArray[10] != null){
				if(StringUtils.isNotEmpty((String) objArray[10])){
					websiteCategoryCode = (String) objArray[10];
				}
			}
			adClkPriceType = (String) objArray[12];
			adView = (((BigDecimal) objArray[13])).intValue();
			adOperatingRule = (String) objArray[14];
			vpv =  (((BigDecimal) objArray[15])).intValue();
			
			PfpAdWebsiteReport pojo = new PfpAdWebsiteReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(customerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setWebsiteCategoryCode(websiteCategoryCode);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdClkPriceType(adClkPriceType);
			pojo.setAdView((int)adView);
			if(objArray[11] != null){
				pojo.setTimeCode(objArray[11].toString());
			}
			pojo.setAdVpv(vpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfpAdWebsiteReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfpAdWebsiteReportService.insertReportData(pojoList);
	}
	
	/*
	 * 1.日排程
	 * 2.每小時執行
	 * 3.處理pfp_ad_video至pfp_ad_video_report
	 * */
	public void processVideoToVideoReportByDay(String reportDate)  {
		try{
			log.info("====PfpAdActionReportJob.processVideoToVideoReportByDay() start ====");
			//1.每小時刪除pfp_ad_video_report當日資料
			admPfpAdVideoReportService.deleteVideoReportDataBytDate(reportDate);
			//2.取得寫入資料
			List<PfpAdVideoReportVO> pfpAdVideoReportVOList = admPfpAdVideoReportService.findVideoInfoByDate(reportDate);
			//3.重新寫入資料(批次)
			admPfpAdVideoReportService.addVideoReportDataBytDate(pfpAdVideoReportVOList);
			//4.監聽idc是否超過10次
			Map<String,Integer> idcMap = new HashMap<>();
			for (PfpAdVideoReportVO pfpAdVideoReportVO : pfpAdVideoReportVOList) {
				if(idcMap.containsKey(pfpAdVideoReportVO.getAdSeq())){
					int totalIdc = idcMap.get(pfpAdVideoReportVO.getAdSeq());
					totalIdc = totalIdc + pfpAdVideoReportVO.getAdVideoIdc();
					idcMap.put(pfpAdVideoReportVO.getAdSeq(), totalIdc);
				}else{
					idcMap.put(pfpAdVideoReportVO.getAdSeq(), pfpAdVideoReportVO.getAdVideoIdc());
				}
			}
			//移除idc小於10的廣告
			for(Iterator<Map.Entry<String, Integer>> iteratorIdcMap = idcMap.entrySet().iterator(); iteratorIdcMap.hasNext();) {
				Entry<String, Integer> entry = iteratorIdcMap.next();
				if(entry.getValue() < 10){
					iteratorIdcMap.remove();
			     }
			}
			//寄送email
			if(idcMap.size() > 0){
				checkPvclkJob.checkPfpIdcProcess(idcMap);
			}
			 
			log.info("====PfpAdActionReportJob.processVideoToVideoReportByDay() end ====");
		}catch(Exception e){
			e.printStackTrace();
			log.error(">>>>>>"+e.getMessage());
		}
	}
	
	public void alexTest() throws Exception{
		System.out.println("AAAAAAAAAA");
		
		
//		System.out.println(jredisUtil.getJedisTemplate() == null);
//		Jedis jedis = (Jedis) jredisUtil.getJedisTemplate();
//		System.out.println(jedis.get("CCCCC"));
		
//		System.out.println(jredisUtil.getKey("ALEX"));
		
//		jredisUtil.setKeyAndExpire("ALEX", "CCCCCAAAA", 80);
		
		
	}
	
	public void setPfpAdActionReportService(IPfpAdActionReportService pfpAdActionReportService) {
		this.pfpAdActionReportService = pfpAdActionReportService;
	}

	public void setPfpAdGroupReportService(IPfpAdGroupReportService pfpAdGroupReportService) {
		this.pfpAdGroupReportService = pfpAdGroupReportService;
	}

	public void setPfpAdReportService(IPfpAdReportService pfpAdReportService) {
		this.pfpAdReportService = pfpAdReportService;
	}

	public void setPfpAdKeywordReportService(IPfpAdKeywordReportService pfpAdKeywordReportService) {
		this.pfpAdKeywordReportService = pfpAdKeywordReportService;
	}

	public void setPfpAdOsReportService(IPfpAdOsReportService pfpAdOsReportService) {
		this.pfpAdOsReportService = pfpAdOsReportService;
	}

	public void setAdmPfpdAdPvclkReportService(IAdmPfpdAdPvclkReportService admPfpdAdPvclkReportService) {
		this.admPfpdAdPvclkReportService = admPfpdAdPvclkReportService;
	}

	public void setPfpAdTimeReportService(IPfpAdTimeReportService pfpAdTimeReportService) {
		this.pfpAdTimeReportService = pfpAdTimeReportService;
	}

	public void setPfpAdAgeReportService(IPfpAdAgeReportService pfpAdAgeReportService) {
		this.pfpAdAgeReportService = pfpAdAgeReportService;
	}

	public void setPfpAdWebsiteReportService(IPfpAdWebsiteReportService pfpAdWebsiteReportService) {
		this.pfpAdWebsiteReportService = pfpAdWebsiteReportService;
	}

	public IAdmPfpAdVideoReportService getAdmPfpAdVideoReportService() {
		return admPfpAdVideoReportService;
	}

	public void setAdmPfpAdVideoReportService(IAdmPfpAdVideoReportService admPfpAdVideoReportService) {
		this.admPfpAdVideoReportService = admPfpAdVideoReportService;
	}
	
	public void processAdGroupReport(String reportDate) throws Exception {
		processPfpAdGroupReport(reportDate);
	}

	public void processAdReport(String reportDate) throws Exception {
		processPfpAdReport(reportDate);
	}

	public void processAdKeywordReport(String reportDate) throws Exception {
		processPfpAdKeywordReport(reportDate);
	}

	public void processPfpdAdPvclkReport(String reportDate) throws Exception {
		processAdmPfpdAdPvclkReport(reportDate);
	}

	public void processAdTimeReport(String reportDate) throws Exception {
		processPfpAdTimeReport(reportDate);
	}
	
	public void processAdAgeReport(String reportDate) throws Exception {
		processPfpAdAgeReport(reportDate);
	}
	
	public void processAdWebsiteReport(String reportDate) throws Exception {
		processPfpAdWebsiteReport(reportDate);
	}
	
	public void processAdOsReport(String reportDate) throws Exception {
		processPfpAdOsReport(reportDate);
	}
	
	public CheckPvclkJob getCheckPvclkJob() {
		return checkPvclkJob;
	}

	public void setCheckPvclkJob(CheckPvclkJob checkPvclkJob) {
		this.checkPvclkJob = checkPvclkJob;
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		System.out.println(">>> start");
		PfpAdActionReportJob job = context.getBean(PfpAdActionReportJob.class);
		if (args.length == 2 || args.length == 3) {
			if (args[1].equals("day")) { //補指定日期
				if (args.length == 3) {
					job.processAllPfpReport(args[2]);
				} else {
					System.out.println("Plz input args[1]: date(yyyy-MM-dd)");
				}
			} else if (args[1].equals("all")) { //補全部日期
				job.processOldData();
			} else {
				System.out.println("args[1]: must be day or all");
			}

		} else {
			System.out.println("Plz input parameters, args[0]: stg/prd, args[1]: day/all, args[2]: yyyy-MM-dd");
		}

		System.out.println(">>> end");
	}
		
//		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
//		PfpAdActionReportJob job = context.getBean(PfpAdActionReportJob.class);
//		job.processVideoToVideoReportByDay();
//		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
//
//		System.out.println(">>> start");
//
//		PfpAdActionReportJob job = context.getBean(PfpAdActionReportJob.class);
//
//		if (args.length == 2 || args.length == 3) {
//			if (args[1].equals("day")) { //補指定日期
//				if (args.length == 3) {
//					job.processAllPfpReport(args[2]);
//				} else {
//					System.out.println("Plz input args[1]: date(yyyy-MM-dd)");
//				}
//			} else if (args[1].equals("all")) { //補全部日期
//				job.processOldData();
//			} else {
//				System.out.println("args[1]: must be day or all");
//			}
//
//		} else {
//			System.out.println("Plz input parameters, args[0]: stg/prd, args[1]: day/all, args[2]: yyyy-MM-dd");
//		}
//
//		System.out.println(">>> end");
//	}
	
}
