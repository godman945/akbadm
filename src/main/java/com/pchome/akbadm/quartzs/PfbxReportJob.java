package com.pchome.akbadm.quartzs;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.AdmTemplateAd;
import com.pchome.akbadm.db.pojo.AdmTemplateProduct;
import com.pchome.akbadm.db.pojo.PfbxAdCustomerReport;
import com.pchome.akbadm.db.pojo.PfbxAdDeviceReport;
import com.pchome.akbadm.db.pojo.PfbxAdGroupReport;
import com.pchome.akbadm.db.pojo.PfbxAdSizeReport;
import com.pchome.akbadm.db.pojo.PfbxAdStyleReport;
import com.pchome.akbadm.db.pojo.PfbxAdTimeReport;
import com.pchome.akbadm.db.pojo.PfbxAdUnitReport;
import com.pchome.akbadm.db.pojo.PfbxAdUrlReport;
import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.pojo.PfbxPosition;
import com.pchome.akbadm.db.pojo.PfbxSize;
import com.pchome.akbadm.db.pojo.PfbxUserGroup;
import com.pchome.akbadm.db.service.ad.IPfpAdPvclkService;
import com.pchome.akbadm.db.service.pfbx.IPfbSizeService;
import com.pchome.akbadm.db.service.pfbx.IPfbxPositionService;
import com.pchome.akbadm.db.service.pfbx.IPfbxUserGroupService;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdCustomerReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdDeviceReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdGroupReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdSizeReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdStyleReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdTimeReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdUnitReportService;
import com.pchome.akbadm.db.service.pfbx.quartzs.IPfbxAdUrlReportService;
import com.pchome.akbadm.db.service.template.ITemplateAdService;
import com.pchome.akbadm.db.service.template.ITemplateProductService;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.pfbx.report.EnumTemplateProduct;

@Transactional
public class PfbxReportJob {

	private Log log = LogFactory.getLog(this.getClass());

	private IPfbxAdTimeReportService pfbxAdTimeReportService;
	private IPfbxAdCustomerReportService pfbxAdCustomerReportService;
	private IPfbxAdUrlReportService pfbxAdUrlReportService;
	private IPfbxAdDeviceReportService pfbxAdDeviceReportService;
	private IPfbxAdUnitReportService pfbxAdUnitReportService;
	private IPfbxAdSizeReportService pfbxAdSizeReportService;
	private IPfbxAdStyleReportService pfbxAdStyleReportService;
	private IPfbxAdGroupReportService pfbxAdGroupReportService;

	private IPfbxPositionService pfbxPositionService;
	private IPfbSizeService pfbSizeService;
	private ITemplateProductService templateProductService;
	private IPfbxUserGroupService pfbxUserGroupService;

	private IPfbxCustomerInfoService pfbxCustomerInfoService;

	private IPfpAdPvclkService pfpAdPvclkService;
	private ITemplateAdService templateAdService;

	
	private String pfbxEmptyCustomerInfoId;
	private String pfbxEmptyPositionId;


	/**
	 * 每天AM 1:00 執行，更新前一天資料
	 */
	public void processPerDay() throws Exception {
        log.info("====PfbxReportJob.processPerDay() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);

		String reportDate = dateFormate.format(c.getTime());

		this.processAllPfbReport(reportDate);

        log.info("====PfbxReportJob.processPerDay() end====");
	}

	/**
	 * 每小時執行一次，更新當天資料
	 */
	public void processPerHour() throws Exception {
        log.info("====PfbxReportJob.processPerHour() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		String reportDate = dateFormate.format(now);

		this.processAllPfbReport(reportDate);

        log.info("====PfbxReportJob.processPerHour() end====");
	}

	/**
	 * 往前補舊資料
	 */
	public void processOldData() throws Exception {
        log.info("====PfbxReportJob.processOldData() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//Date startDate = dateFormate.parse("2013-09-01");
		Date startDate = dateFormate.parse("2014-04-01");

		Date now = new Date();

		Date reportDate = startDate;
		while (reportDate.getTime() <= now.getTime()) {

			String _reportDate = dateFormate.format(reportDate);

			this.processAllPfbReport(_reportDate);

			Calendar c = Calendar.getInstance();
			c.setTime(reportDate);
			c.add(Calendar.DATE, 1);

			reportDate = c.getTime();
		}

        log.info("====PfbxReportJob.processOldData() end====");
	}

	private void processPfbxEmptyDbColum(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		pfpAdPvclkService.updateEmptyPfbxCustomerPostion(dateFormate.parse(reportDate), pfbxEmptyCustomerInfoId, pfbxEmptyPositionId);


	}


    private void processEmptyUrlDbColum(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		pfpAdPvclkService.updateEmptyUrl(dateFormate.parse(reportDate));


	}

	private void processPfbAdTimeReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdTimeReportService.prepareReportData(reportDate);

		List<PfbxAdTimeReport> pojoList = new ArrayList<PfbxAdTimeReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbxCustomerInfoId;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbxCustomerInfoId =(String)objArray[5];
			float bonusRecord=0;


			PfbxAdTimeReport pojo = new PfbxAdTimeReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(pfbxCustomerInfoId);
			pojo.setAdPvPrice(bonusRecord);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfbxAdTimeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdTimeReportService.insertReportData(pojoList);

	}

	private void processPfbAdCustomerReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdCustomerReportService.prepareReportData(reportDate);

		List<PfbxAdCustomerReport> pojoList = new ArrayList<PfbxAdCustomerReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbxCustomerInfoId;
		String custName;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbxCustomerInfoId = (String) objArray[5];
			if(StringUtils.isBlank(pfbxCustomerInfoId)){
			    continue;
			}
			log.info("pfbxCustomerInfoId: "+pfbxCustomerInfoId);
//			pfdUserId = (String) objArray[6];

			PfbxCustomerInfo pfbxCustomerInfo = pfbxCustomerInfoService.get(pfbxCustomerInfoId);

			log.info(">>>pfbxCustomerInfo.getCategory(): "+pfbxCustomerInfo.getCategory());
			//category: 1:個人戶 2:公司戶
			if(pfbxCustomerInfo.getCategory().equals("1")){
				custName = pfbxCustomerInfo.getContactName();
			}else{
				custName = pfbxCustomerInfo.getCompanyName();
			}

			PfbxAdCustomerReport pojo = new PfbxAdCustomerReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(pfbxCustomerInfoId);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setAdPvclkCustomer(custName);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfbxAdCustomerReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdCustomerReportService.insertReportData(pojoList);

	}

	private void processPfbAdUrlReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdUrlReportService.prepareReportData(reportDate);

		List<PfbxAdUrlReport> pojoList = new ArrayList<PfbxAdUrlReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbxCustomerInfoId;
		String adUrl;
//		String pfdUserId;
//		String pfpCustomerInfoId;
//		int adType;
//		String adSeq;
//		String adGroupSeq;
//		String adActionSeq;
//		String adDevice;
//		String templateProductSeq;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbxCustomerInfoId = (String) objArray[5];
			adUrl = (String) objArray[6];
//			pfdUserId = (String) objArray[6];
//			pfpCustomerInfoId = (String) objArray[7];
//			adType = ((Integer) objArray[8]).intValue();
//			adSeq = (String) objArray[9];
//			adActionSeq = (String) objArray[10];
//			adGroupSeq = (String) objArray[11];
//			adDevice = (String) objArray[12];
//			templateProductSeq = (String) objArray[13];


			if(StringUtils.isBlank(pfbxCustomerInfoId)){
			    continue;
			}

			if(adUrl.length()>50){
				adUrl=adUrl.substring(0,49);
			}

			//PfbxCustomerInfo pfbxCustomerInfo = pfbxCustomerInfoService.get(pfbxCustomerInfoId);

			PfbxAdUrlReport pojo = new PfbxAdUrlReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(pfbxCustomerInfoId);
			pojo.setAdPvclkUrl(adUrl);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfbxAdUrlReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdUrlReportService.insertReportData(pojoList);

	}

	private void processPfbAdDeviceReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdDeviceReportService.prepareReportData(reportDate);

		List<PfbxAdDeviceReport> pojoList = new ArrayList<PfbxAdDeviceReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbxCustomerInfoId;
		String device;
//		String pfdUserId;
//		String pfpCustomerInfoId;
//		int adType;
//		String keywordSeq;
//		String adGroupSeq;
//		String adActionSeq;
//		String adDevice;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbxCustomerInfoId = (String) objArray[5];
			device = (String) objArray[6];
//			pfdUserId = (String) objArray[6];
//			pfpCustomerInfoId = (String) objArray[7];
//			adType = ((Integer) objArray[8]).intValue();
//			keywordSeq = (String) objArray[9];
//			adActionSeq = (String) objArray[10];
//			adGroupSeq = (String) objArray[11];
//			adDevice = (String) objArray[12];
			if(StringUtils.isBlank(pfbxCustomerInfoId)){
			    continue;
			}
			PfbxAdDeviceReport pojo = new PfbxAdDeviceReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setCustomerInfoId(pfbxCustomerInfoId);
			pojo.setAdPvclkDevice(device);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
//			pojo.setPfdUserId(pfdUserId);
//			pojo.setPfpCustomerInfoId(pfpCustomerInfoId);
//			pojo.setAdType(adType);
//			pojo.setAdActionSeq(adActionSeq);
//			pojo.setAdGroupSeq(adGroupSeq);
//			pojo.setKeywordSeq(keywordSeq);
//			pojo.setAdPvclkDevice(adDevice);
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfbxAdDeviceReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdDeviceReportService.insertReportData(pojoList);

	}

	private void processPfbAdUnitReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdUnitReportService.prepareReportData(reportDate);

		List<PfbxAdUnitReport> pojoList = new ArrayList<PfbxAdUnitReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbCustomerInfoId;
		String unit;
		String pfbxPositionId;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbCustomerInfoId = (String) objArray[5];

			if(StringUtils.isBlank(pfbCustomerInfoId)){
			    continue;
			}

			pfbxPositionId = (String) objArray[6];
			PfbxPosition position = null;
			position = pfbxPositionService.get(pfbxPositionId);

			if(position!=null){
				unit = position.getPName();
			}else{
				unit="版位已刪除";
			}

			PfbxAdUnitReport pojo = new PfbxAdUnitReport();
			pojo.setAdPv((int) pv);
			pojo.setAdClk((int) click);
			pojo.setAdClkPrice((float) cost);
			pojo.setAdInvalidClk((int) invClick);
			pojo.setAdInvalidClkPrice((float) invPrice);
			pojo.setAdPvclkUnit(unit);
			pojo.setCustomerInfoId(pfbCustomerInfoId);
			pojo.setAdPvclkDate(dateFormate.parse(reportDate));
			pojo.setCreateDate(now);
			pojo.setUpdateDate(now);

			pojoList.add(pojo);
		}

		//step2. 刪除當日資料
		pfbxAdUnitReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdUnitReportService.insertReportData(pojoList);

	}

	private void processPfbAdSizeReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdSizeReportService.prepareReportData(reportDate);

		List<PfbxAdSizeReport> pojoList = new ArrayList<PfbxAdSizeReport>();

		Map<String,PfbxAdSizeReport> pojoMap = new HashMap<String,PfbxAdSizeReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbCustomerInfoId;
		int sid;
		String size;
		String pfbxPositionId;

		Date now = new Date();

        String mapKey="";

        PfbxAdSizeReport pojo=null;

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbCustomerInfoId = (String) objArray[5];
			if(StringUtils.isBlank(pfbCustomerInfoId)){
			    continue;
			}
			pfbxPositionId = (String) objArray[6];
			PfbxPosition position = null;

			position = pfbxPositionService.get(pfbxPositionId);

			if(position != null){
				sid = position.getSId();
				PfbxSize  pfbxSize = pfbSizeService.get(sid);
				size = String.valueOf(pfbxSize.getWidth())+" x "+String.valueOf(pfbxSize.getHeight());
			}else{

				size = "已刪除";
			}


            mapKey=size+"_"+pfbCustomerInfoId;

			pojo=pojoMap.get(mapKey);

			if(pojo==null){

				pojo = new PfbxAdSizeReport();
				pojo.setAdPv((int) pv);
				pojo.setAdClk((int) click);
				pojo.setAdClkPrice((float) cost);
				pojo.setAdInvalidClk((int) invClick);
				pojo.setAdInvalidClkPrice((float) invPrice);
				pojo.setAdPvclkSize(size);
				pojo.setCustomerInfoId(pfbCustomerInfoId);
				pojo.setAdPvclkDate(dateFormate.parse(reportDate));
				pojo.setCreateDate(now);
				pojo.setUpdateDate(now);

			}else{

				pojo.setAdPv(pojo.getAdPv()+(int) pv);
				pojo.setAdClk(pojo.getAdClk()+(int) click);
				pojo.setAdClkPrice(pojo.getAdClkPrice()+(float) cost);
				pojo.setAdInvalidClk(pojo.getAdInvalidClk()+(int) invClick);
				pojo.setAdInvalidClkPrice(pojo.getAdInvalidClkPrice()+(float) invPrice);


			}


			pojoMap.put(mapKey, pojo);


		}


		for (String key : pojoMap.keySet())
		{
			pojoList.add(pojoMap.get(key));
		}


		//step2. 刪除當日資料
		pfbxAdSizeReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdSizeReportService.insertReportData(pojoList);

	}

	private void processPfbAdStyleReport(String reportDate) throws Exception {
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = pfbxAdStyleReportService.prepareReportData(reportDate);

		List<PfbxAdStyleReport> pojoList = new ArrayList<PfbxAdStyleReport>();

		Map<String,PfbxAdStyleReport> pojoMap = new HashMap<String,PfbxAdStyleReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbCustomerInfoId;
		int styleCode;
		String style = null;
		String admTemplateAdSeq="";

		Date now = new Date();

		String mapKey="";

		PfbxAdStyleReport pojo=null;

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbCustomerInfoId = (String) objArray[5];
			if(StringUtils.isBlank(pfbCustomerInfoId)){
			    continue;
			}

			admTemplateAdSeq = (String) objArray[7];
			AdmTemplateAd admTemplateAd = templateAdService.getTemplateAdBySeq(admTemplateAdSeq);
			styleCode = admTemplateAd.getAdmTemplateAdType();
			if(styleCode >0){
				if(EnumTemplateProduct.WORD.getTemplateId().equals(styleCode)){
					style = EnumTemplateProduct.WORD.getTemplate();
				}else if(EnumTemplateProduct.PIC.getTemplateId().equals(styleCode)){
					style = EnumTemplateProduct.PIC.getTemplate();
				}else if(EnumTemplateProduct.FLASH.getTemplateId().equals(styleCode)){
					style = EnumTemplateProduct.FLASH.getTemplate();
				}else if(EnumTemplateProduct.WORDPIC.getTemplateId().equals(styleCode)){
					style = EnumTemplateProduct.WORDPIC.getTemplate();
				}
			}
			

			mapKey=style+"_"+pfbCustomerInfoId;

			pojo = pojoMap.get(mapKey);

			if(pojo==null){
				pojo = new PfbxAdStyleReport();
				pojo.setAdPv((int) pv);
				pojo.setAdClk((int) click);
				pojo.setAdClkPrice((float) cost);
				pojo.setAdInvalidClk((int) invClick);
				pojo.setAdInvalidClkPrice((float) invPrice);
				pojo.setAdPvclkStyle(style);
				pojo.setCustomerInfoId(pfbCustomerInfoId);
				pojo.setAdPvclkDate(dateFormate.parse(reportDate));
				pojo.setCreateDate(now);
				pojo.setUpdateDate(now);
			}else{
				pojo.setAdPv(pojo.getAdPv()+(int) pv);
				pojo.setAdClk(pojo.getAdClk()+(int) click);
				pojo.setAdClkPrice(pojo.getAdClkPrice()+(float) cost);
				pojo.setAdInvalidClk(pojo.getAdInvalidClk()+(int) invClick);
				pojo.setAdInvalidClkPrice(pojo.getAdInvalidClkPrice()+(float) invPrice);
			}
			pojoMap.put(mapKey, pojo);
		}


		for (String key : pojoMap.keySet()){
			pojoList.add(pojoMap.get(key));
		}


		//step2. 刪除當日資料
		pfbxAdStyleReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdStyleReportService.insertReportData(pojoList);

	}


	private void processPfbAdGroupReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//step1. 整理當日 pfp_ad_pvclk_user_group 資料
		List<Object> dataList = pfbxAdGroupReportService.prepareReportData(reportDate);

		List<PfbxAdGroupReport> pojoList = new ArrayList<PfbxAdGroupReport>();

		double pv = 0;
		double click = 0;
		double cost = 0;
		double invClick = 0;
		double invPrice = 0;
		String pfbCustomerInfoId;
		String pfbxUserGroupId;
		String adPvclkGroup;

		Date now = new Date();

		for (int i=0; i<dataList.size(); i++) {
			Object[] objArray = (Object[]) dataList.get(i);

			pv = ((BigDecimal) objArray[0]).doubleValue();
			click = ((BigDecimal) objArray[1]).doubleValue();
			cost = ((Double) objArray[2]).doubleValue();
			invClick = ((BigDecimal) objArray[3]).doubleValue();
			invPrice = ((Double) objArray[4]).doubleValue();
			pfbCustomerInfoId = (String) objArray[5];
			if(StringUtils.isBlank(pfbCustomerInfoId)){
			    continue;
			}
			pfbxUserGroupId = (String) objArray[6];



			PfbxUserGroup pfbxUserGroup = pfbxUserGroupService.getPfbxUserGroupById(pfbxUserGroupId);
//				PfbxUserGroup pfbxUserGroup = pfbxUserGroupService.get(pfbxUserGroupId);
			if(pfbxUserGroup!=null){
				adPvclkGroup = pfbxUserGroup.getGroupName();

			}else{
				adPvclkGroup = "";
			}

			int deleteFlag = 1;
			deleteFlag = pfbxUserGroup.getDeleteFlag();

			//判斷group是否刪除，若為未刪除才將資料寫入report
			if(deleteFlag == 0){
				PfbxAdGroupReport pojo = new PfbxAdGroupReport();
				pojo.setAdPv((int) pv);
				pojo.setAdClk((int) click);
				pojo.setAdClkPrice((float) cost);
				pojo.setAdInvalidClk((int) invClick);
				pojo.setAdInvalidClkPrice((float) invPrice);
				pojo.setCustomerInfoId(pfbCustomerInfoId);
				pojo.setAdPvclkGroup(adPvclkGroup);
				pojo.setAdPvclkDate(dateFormate.parse(reportDate));
				pojo.setCreateDate(now);
				pojo.setUpdateDate(now);

				pojoList.add(pojo);
			}

		}

		//step2. 刪除當日資料
		pfbxAdGroupReportService.deleteReportDataByReportDate(reportDate);

		//step3. 重寫當日資料
		pfbxAdGroupReportService.insertReportData(pojoList);

	}

	public void processAllPfbReport(String reportDate) throws Exception {
	    log.info("====PfbxReportJob.processAllPfbReport() start====");
	    
		//pfp_ad_pvclk pfbxcustomerid pfbxpostionid 空的填入幽靈版位的ID
		processPfbxEmptyDbColum(reportDate);
		//pfp_ad_pvclk ad_url 空的填入 none referer
		processEmptyUrlDbColum(reportDate);

		processPfbAdTimeReport(reportDate);
		processPfbAdCustomerReport(reportDate);
		processPfbAdUrlReport(reportDate);
		processPfbAdDeviceReport(reportDate);

		processPfbAdUnitReport(reportDate);
		processPfbAdSizeReport(reportDate);
		processPfbAdStyleReport(reportDate);
		processPfbAdGroupReport(reportDate);
		
		log.info("====PfbxReportJob.processAllPfbReport() end====");
	}


	public void setPfbxAdTimeReportService(
			IPfbxAdTimeReportService pfbxAdTimeReportService) {
		this.pfbxAdTimeReportService = pfbxAdTimeReportService;
	}

	public void setPfbxAdCustomerReportService(
			IPfbxAdCustomerReportService pfbxAdCustomerReportService) {
		this.pfbxAdCustomerReportService = pfbxAdCustomerReportService;
	}

	public void setPfbxAdUrlReportService(
			IPfbxAdUrlReportService pfbxAdUrlReportService) {
		this.pfbxAdUrlReportService = pfbxAdUrlReportService;
	}

	public void setPfbxCustomerInfoService(
			IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
	}

	public void setPfbxAdDeviceReportService(
			IPfbxAdDeviceReportService pfbxAdDeviceReportService) {
		this.pfbxAdDeviceReportService = pfbxAdDeviceReportService;
	}

	public static void main(String[] args) throws Exception {
//		String[]test ={"local", "day", "2015-05-27"};
//		args = test;

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
		System.out.println(">>> start");

		PfbxReportJob job = context.getBean( PfbxReportJob.class);

		if (args.length == 2 || args.length == 3) {

			if (args[1].equals("day")) { //補指定日期
				if (args.length == 3) {
					job.processAllPfbReport(args[2]);
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



	public void setPfbxAdUnitReportService(
			IPfbxAdUnitReportService pfbxAdUnitReportService) {
		this.pfbxAdUnitReportService = pfbxAdUnitReportService;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public void setPfbxAdSizeReportService(
			IPfbxAdSizeReportService pfbxAdSizeReportService) {
		this.pfbxAdSizeReportService = pfbxAdSizeReportService;
	}

	public void setPfbxAdStyleReportService(
			IPfbxAdStyleReportService pfbxAdStyleReportService) {
		this.pfbxAdStyleReportService = pfbxAdStyleReportService;
	}

	public void setPfbxAdGroupReportService(
			IPfbxAdGroupReportService pfbxAdGroupReportService) {
		this.pfbxAdGroupReportService = pfbxAdGroupReportService;
	}

	public void setPfbxPositionService(IPfbxPositionService pfbxPositionService) {
		this.pfbxPositionService = pfbxPositionService;
	}

	public void setPfbSizeService(IPfbSizeService pfbSizeService) {
		this.pfbSizeService = pfbSizeService;
	}

	public void setTemplateProductService(
			ITemplateProductService templateProductService) {
		this.templateProductService = templateProductService;
	}

	public void setPfbxUserGroupService(IPfbxUserGroupService pfbxUserGroupService) {
		this.pfbxUserGroupService = pfbxUserGroupService;
	}

	public void setPfbxEmptyCustomerInfoId(String pfbxEmptyCustomerInfoId) {
		this.pfbxEmptyCustomerInfoId = pfbxEmptyCustomerInfoId;
	}

	public void setPfbxEmptyPositionId(String pfbxEmptyPositionId) {
		this.pfbxEmptyPositionId = pfbxEmptyPositionId;
	}

	public void setPfpAdPvclkService(IPfpAdPvclkService pfpAdPvclkService) {
		this.pfpAdPvclkService = pfpAdPvclkService;
	}

	public ITemplateAdService getTemplateAdService() {
		return templateAdService;
	}

	public void setTemplateAdService(ITemplateAdService templateAdService) {
		this.templateAdService = templateAdService;
	}
	
}
