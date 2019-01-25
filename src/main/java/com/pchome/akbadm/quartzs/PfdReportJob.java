package com.pchome.akbadm.quartzs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfdAdActionReport;
import com.pchome.akbadm.db.pojo.PfdAdAgeReport;
import com.pchome.akbadm.db.pojo.PfdAdGroupReport;
import com.pchome.akbadm.db.pojo.PfdAdReport;
import com.pchome.akbadm.db.pojo.PfdAdTemplateReport;
import com.pchome.akbadm.db.pojo.PfdAdTimeReport;
import com.pchome.akbadm.db.pojo.PfdAdVideoReport;
import com.pchome.akbadm.db.pojo.PfdAdWebsiteReport;
import com.pchome.akbadm.db.pojo.PfdKeywordReport;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdActionReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdAgeReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdGroupReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdTemplateReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdTimeReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdVideoReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdWebsiteReportService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdKeywordReportService;
import com.pchome.config.TestConfig;

@Transactional
public class PfdReportJob {

	private Log log = LogFactory.getLog(this.getClass());

	private IPfdAdActionReportService pfdAdActionReportService;
	private IPfdAdGroupReportService pfdAdGroupReportService;
	private IPfdAdReportService pfdAdReportService;
	private IPfdKeywordReportService pfdKeywordReportService;
	private IPfdAdTemplateReportService pfdAdTemplateReportService;
	private IPfdAdTimeReportService pfdAdTimeReportService;
	private IPfdAdAgeReportService pfdAdAgeReportService;
	private IPfdAdWebsiteReportService pfdAdWebsiteReportService;
	private IPfdAdVideoReportService pfdAdVideoReportService;

	/**
	 * 每天AM 1:00 執行，更新前一天資料
	 */
	public void processPerDay() throws Exception {
        log.info("====PfdReportJob.processPerDay() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);

		String reportDate = dateFormate.format(c.getTime());

		this.processAllPfdReport(reportDate);

        log.info("====PfdReportJob.processPerDay() end====");
	}

	/**
	 * 每小時執行一次，更新當天資料
	 */
	public void processPerHour() throws Exception {
        log.info("====PfdReportJob.processPerHour() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		String reportDate = dateFormate.format(now);

		this.processAllPfdReport(reportDate);

        log.info("====PfdReportJob.processPerHour() end====");
	}

	/**
	 * 往前補舊資料
	 */
	public void processOldData() throws Exception {
        log.info("====PfdReportJob.processOldData() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//Date startDate = dateFormate.parse("2013-09-01");
		Date startDate = dateFormate.parse("2014-04-01");

		Date now = new Date();

		Date reportDate = startDate;
		while (reportDate.getTime() <= now.getTime()) {

			String _reportDate = dateFormate.format(reportDate);

			this.processAllPfdReport(_reportDate);
			log.info("reportDate:" + reportDate);
			Calendar c = Calendar.getInstance();
			c.setTime(reportDate);
			c.add(Calendar.DATE, 1);

			reportDate = c.getTime();
		}

        log.info("====PfdReportJob.processOldData() end====");
	}

	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_action_report)
	 * 總覽-成效總覽表格資料
	 * 總廣告成效-廣告
	 * 每日花費成效
	 * 廣告成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdActionReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdActionReportService.prepareReportData(reportDate);

		List<PfdAdActionReport> pojoList = new ArrayList<PfdAdActionReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		double adActionMaxPrice = 0;
		double adActionControlPrice = 0;
		int count = 0;
		double adActionMaxPriceAvg = 0; //每日花費上限
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adActionSeq;
		String adDevice;
		String payType;
		String adPriceType;
		int adView;
		int adVpv;
		String adOperatingRule;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);
			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			adActionMaxPrice = ((Double) objArray[5]).doubleValue();
			adActionControlPrice = ((Double) objArray[6]).doubleValue();
			count = ((BigInteger) objArray[7]).intValue();
			pfdCustomerInfoId = (String) objArray[8];
			pfdUserId = (String) objArray[9];
			pfpCustomerInfoId = (String) objArray[10];
			adType = ((Integer) objArray[11]).intValue();
			adActionSeq = (String) objArray[12];
			adDevice = (String) objArray[13];
			payType = ((Integer) objArray[14]).toString();
			adPriceType = (String) objArray[15];
			adView = ((BigDecimal) objArray[16]).intValue();
			adVpv = ((BigDecimal) objArray[17]).intValue();
			adOperatingRule = (String) objArray[18];
			
			//計算平均每日花費上限
			if (adActionMaxPrice>0 && count>0) {
				adActionMaxPriceAvg = adActionMaxPrice/count;
			}

			PfdAdActionReport pojo = new PfdAdActionReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setAdActionMaxPrice((float) adActionMaxPriceAvg);
			pojo.setAdActionControlPrice((float) adActionControlPrice);
			pojo.setAdPvlckCount(count);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setPfpPayType(payType);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdActionReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdActionReportService.insertReportData(pojoList);
	}

	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_group_report)
	 * 總廣告成效-分類
	 * 分類成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdGroupReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdGroupReportService.prepareReportData(reportDate);

		List<PfdAdGroupReport> pojoList = new ArrayList<PfdAdGroupReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adGroupSeq;
		String adActionSeq;
		String adDevice;
		String adPriceType;
		String adOperatingRule;
		int adVpv = 0;
		int adView = 0;
		
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);
			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			pfdUserId = (String) objArray[6];
			pfpCustomerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adActionSeq = (String) objArray[9];
			adGroupSeq = (String) objArray[10];
			adDevice = (String) objArray[11];
			adPriceType = (String) objArray[12];
			adOperatingRule = (String) objArray[13];
			adVpv = ((BigDecimal) objArray[14]).intValue();
			adView = ((BigDecimal) objArray[15]).intValue();
					
			PfdAdGroupReport pojo = new PfdAdGroupReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdVpv(adVpv);
			pojo.setAdView(adView);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdGroupReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdGroupReportService.insertReportData(pojoList);
	}

	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_report)
	 * 總廣告成效-廣告明細
	 * 廣告明細成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdDetailReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdReportService.prepareReportData(reportDate);

		List<PfdAdReport> pojoList = new ArrayList<PfdAdReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adSeq;
		String adGroupSeq;
		String adActionSeq;
		String adDevice;
		String templateProductSeq;

		String adPriceType;
		String adOperatingRule;
		int adVpv;
		int adView;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			pfdUserId = (String) objArray[6];
			pfpCustomerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adSeq = (String) objArray[9];
			adActionSeq = (String) objArray[10];
			adGroupSeq = (String) objArray[11];
			adDevice = (String) objArray[12];
			templateProductSeq = (String) objArray[13];
			adPriceType = (String) objArray[14];
			adOperatingRule = (String) objArray[15];
			adVpv = ((BigDecimal) objArray[16]).intValue();
			adView = ((BigDecimal) objArray[17]).intValue();
			
			PfdAdReport pojo = new PfdAdReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdSeq(adSeq);
			pojo.setTemplateProductSeq(templateProductSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdReportService.insertReportData(pojoList);
	}

	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_keyword_report)
	 * 總廣告成效-關鍵字
	 * 關鍵字成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdKeywordReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdKeywordReportService.prepareReportData(reportDate);

		List<PfdKeywordReport> pojoList = new ArrayList<PfdKeywordReport>();

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

		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String keywordSeq;
		String adGroupSeq;
		String adActionSeq;
		String adDevice;

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
			pfdCustomerInfoId = (String) objArray[15];
			pfdUserId = (String) objArray[16];
			pfpCustomerInfoId = (String) objArray[17];
			adType = ((Integer) objArray[18]).intValue();
			keywordSeq = (String) objArray[19];
			adActionSeq = (String) objArray[20];
			adGroupSeq = (String) objArray[21];
			adDevice = (String) objArray[22];

			PfdKeywordReport pojo = new PfdKeywordReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setAdPhrasePv((int) phrPv);
			pojo.setAdPhraseClk((int) phrClick);
			pojo.setAdPhraseClkPrice((float) phrCost);
			pojo.setAdPhraseInvalidClk((int) phrInvClick);
			pojo.setAdPhraseInvalidClkPrice((float) phrInvPrice);
			pojo.setAdPrecisionPv((int) prePv);
			pojo.setAdPrecisionClk((int) preClick);
			pojo.setAdPrecisionClkPrice((float) preCost);
			pojo.setAdPrecisionInvalidClk((int) preInvClick);
			pojo.setAdPrecisionInvalidClkPrice((float) preInvPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setKeywordSeq(keywordSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdKeywordReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdKeywordReportService.insertReportData(pojoList);
	}

	private void processPfdAdTemplateReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdTemplateReportService.prepareReportData(reportDate);

		List<PfdAdTemplateReport> pojoList = new ArrayList<PfdAdTemplateReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String templateProductSeq;
		String adPriceType;
		String adOperatingRule;
		int adVpv;
		int adView;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);
			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			templateProductSeq = (String) objArray[6];
			adPriceType = (String) objArray[7];
			adOperatingRule = (String) objArray[8];
			adVpv = ((BigDecimal) objArray[9]).intValue();
			adView = ((BigDecimal) objArray[10]).intValue();
			
			PfdAdTemplateReport pojo = new PfdAdTemplateReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setTemplateProductSeq(templateProductSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);
			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdTemplateReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdTemplateReportService.insertReportData(pojoList);
	}

	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_time_report)
	 * 廣告播放時段成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdTimeReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdTimeReportService.prepareReportData(reportDate);

		List<PfdAdTimeReport> pojoList = new ArrayList<PfdAdTimeReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adActionSeq;
		String adGroupSeq;
		String adDevice;
		String adPriceType;
		String adOperatingRule;
		int adVpv;
		int adView;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);
			
			String timeCode = "A";

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			pfdUserId = (String) objArray[6];
			pfpCustomerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adActionSeq = (String) objArray[9];
			adGroupSeq = (String) objArray[10];
			adDevice = (String) objArray[11];
			if(objArray[12] != null){
				if(StringUtils.isNotEmpty((String) objArray[12])){
					timeCode = (String) objArray[12];
				}
			}
			adPriceType = (String) objArray[13];
			adOperatingRule = (String) objArray[14];
			adVpv = ((BigDecimal) objArray[15]).intValue();
			adView = ((BigDecimal) objArray[16]).intValue();

			PfdAdTimeReport pojo = new PfdAdTimeReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setTimeCode(timeCode);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdTimeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdTimeReportService.insertReportData(pojoList);
	}
	
	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_age_report)
	 * 廣告族群成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdAgeReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdAgeReportService.prepareReportData(reportDate);

		List<PfdAdAgeReport> pojoList = new ArrayList<PfdAdAgeReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adActionSeq;
		String adGroupSeq;
		String adDevice;
		String adPriceType;
		String adOperatingRule;
		int adVpv;
		int adView;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			String sex = "";
			String ageCode = "I";
			
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			pfdUserId = (String) objArray[6];
			pfpCustomerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adActionSeq = (String) objArray[9];
			adGroupSeq = (String) objArray[10];
			adDevice = (String) objArray[11];
			if(objArray[12] != null){
				if(StringUtils.isNotEmpty((String) objArray[12])){
					sex = (String) objArray[12];
				}
			}
			if(objArray[13] != null){
				if(StringUtils.isNotEmpty((String) objArray[13])){
					ageCode = (String) objArray[13];
				}
			}
			adPriceType = (String) objArray[14];
			adOperatingRule = (String) objArray[15];
			adVpv = ((BigDecimal) objArray[16]).intValue();
			adView = ((BigDecimal) objArray[17]).intValue();
			
			PfdAdAgeReport pojo = new PfdAdAgeReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			if(StringUtils.isNotEmpty(sex)){
				pojo.setSex(sex);
			}
			pojo.setAgeCode(ageCode);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdAgeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdAgeReportService.insertReportData(pojoList);
	}
	
	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_website_report)
	 * 網站類型成效
	 * @param reportDate
	 * @throws Exception
	 */
	private void processPfdAdWebsiteReport(String reportDate) throws Exception {
		// 設定日期的文字格式
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfdAdWebsiteReportService.prepareReportData(reportDate);

		List<PfdAdWebsiteReport> pojoList = new ArrayList<PfdAdWebsiteReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfdCustomerInfoId;
		String pfdUserId;
		String pfpCustomerInfoId;
		int adType;
		String adActionSeq;
		String adGroupSeq;
		String adDevice;
		String adPriceType;
		String adOperatingRule;
		int adVpv;
		int adView;
		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);
			
			String websiteCategoryCode = "";

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfdCustomerInfoId = (String) objArray[5];
			pfdUserId = (String) objArray[6];
			pfpCustomerInfoId = (String) objArray[7];
			adType = ((Integer) objArray[8]).intValue();
			adActionSeq = (String) objArray[9];
			adGroupSeq = (String) objArray[10];
			adDevice = (String) objArray[11];
			if(objArray[12] != null){
				if(StringUtils.isNotEmpty((String) objArray[12])){
					websiteCategoryCode = (String) objArray[12];
				}
			}
			adPriceType = (String) objArray[13];
			adOperatingRule = (String) objArray[14];
			adVpv = ((BigDecimal) objArray[15]).intValue();
			adView = ((BigDecimal) objArray[16]).intValue();
			
			PfdAdWebsiteReport pojo = new PfdAdWebsiteReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setPfdCustomerInfoId(pfdCustomerInfoId);
			pojo.setPfdUserId(pfdUserId);
			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
			pojo.setAdType(adType);
			pojo.setAdActionSeq(adActionSeq);
			pojo.setAdGroupSeq(adGroupSeq);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkDevice(adDevice);
			pojo.setWebsiteCategoryCode(websiteCategoryCode);
			pojo.setAdClkPriceType(adPriceType);
			pojo.setAdOperatingRule(adOperatingRule);
			pojo.setAdView(adView);
			pojo.setAdVpv(adVpv);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfdAdWebsiteReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfdAdWebsiteReportService.insertReportData(pojoList);
	}
	
	/**
	 * 將總資料表(pfp_ad_pvclk)資料，寫入到下列功能會用到的table(pfd_ad_video_report)
	 * 影音廣告成效
	 * @param reportDate
	 */
	private void processPfdAdVideoReport(String reportDate) {
		try{
			//step1. 整理當日 pfp_ad_pvclk 資料
			List<PfdAdVideoReport> pfdAdVideoReportList = pfdAdVideoReportService.findVideoInfoByDate(reportDate);

			//step2. 刪除當日資料
			pfdAdVideoReportService.deleteVideoReportDataBytDate(reportDate);
			
			//step3. 重寫當日資料
			pfdAdVideoReportService.addVideoReportDataBytDate(pfdAdVideoReportList);
		}catch(Exception e){
			e.printStackTrace();
			log.error(">>>>>>"+e.getMessage());
		}
	}
	
	/**
	 * 執行全部報表排程
	 * @param reportDate
	 * @throws Exception
	 */
	public void processAllPfdReport(String reportDate) throws Exception {
        log.info("====PfdReportJob.processAllPfdReport() start====");

    	processPfdAdActionReport(reportDate);
		processPfdAdGroupReport(reportDate);
		processPfdAdDetailReport(reportDate);
		processPfdAdKeywordReport(reportDate);
		processPfdAdTemplateReport(reportDate);
		processPfdAdTimeReport(reportDate);
		processPfdAdAgeReport(reportDate);
		processPfdAdWebsiteReport(reportDate);
		processPfdAdVideoReport(reportDate);

        log.info("====PfdReportJob.processAllPfdReport() end====");
	}

	/**
	 * 本機測試排程時使用
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		System.out.println(">>> start");

		PfdReportJob job = context.getBean(PfdReportJob.class);

		if (args.length == 2 || args.length == 3) {

			if (args[1].equals("day")) { //補指定日期
				if (args.length == 3) {
					job.processAllPfdReport(args[2]);
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
	
	public void setPfdAdActionReportService(IPfdAdActionReportService pfdAdActionReportService) {
		this.pfdAdActionReportService = pfdAdActionReportService;
	}

	public void setPfdAdGroupReportService(IPfdAdGroupReportService pfdAdGroupReportService) {
		this.pfdAdGroupReportService = pfdAdGroupReportService;
	}

	public void setPfdAdReportService(IPfdAdReportService pfdAdReportService) {
		this.pfdAdReportService = pfdAdReportService;
	}

	public void setPfdKeywordReportService(IPfdKeywordReportService pfdKeywordReportService) {
		this.pfdKeywordReportService = pfdKeywordReportService;
	}

	public void setPfdAdTemplateReportService(IPfdAdTemplateReportService pfdAdTemplateReportService) {
		this.pfdAdTemplateReportService = pfdAdTemplateReportService;
	}

	public void setPfdAdTimeReportService(IPfdAdTimeReportService pfdAdTimeReportService) {
		this.pfdAdTimeReportService = pfdAdTimeReportService;
	}

	public void setPfdAdAgeReportService(IPfdAdAgeReportService pfdAdAgeReportService) {
		this.pfdAdAgeReportService = pfdAdAgeReportService;
	}

	public void setPfdAdWebsiteReportService(IPfdAdWebsiteReportService pfdAdWebsiteReportService) {
		this.pfdAdWebsiteReportService = pfdAdWebsiteReportService;
	}

	public void setPfdAdVideoReportService(IPfdAdVideoReportService pfdAdVideoReportService) {
		this.pfdAdVideoReportService = pfdAdVideoReportService;
	}
}
