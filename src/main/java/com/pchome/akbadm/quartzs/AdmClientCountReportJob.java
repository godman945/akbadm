package com.pchome.akbadm.quartzs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.pchome.akbadm.db.pojo.AdmClientCountReport;
import com.pchome.akbadm.db.pojo.AdmPortalBonusReport;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.service.report.IAdmClientCountReportService;
import com.pchome.akbadm.db.service.report.IAdmPortalBonusReportService;
import com.pchome.config.TestConfig;

public class AdmClientCountReportJob {

	private Log log = LogFactory.getLog(getClass().getName());

	private IAdmClientCountReportService admClientCountReportService;
	private IAdmPortalBonusReportService admPortalBonusReportService;
	private IAdmRecognizeDetailService admRecognizeDetailService;

	/**
	 * 每天AM 6:00 執行，更新前一天資料
	 */
	public void processPerDay() throws Exception {
		log.info("====AdmClientCountReportJob.processPerDay() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		Date now = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		c.add(Calendar.DATE, -1);

		String reportDate = dateFormate.format(c.getTime());

		this.processAdmClientCountReport(reportDate);
		this.processAdmPortalBonusReport(reportDate);

        log.info("====AdmClientCountReportJob.processPerDay() end====");
	}

	/**
	 * 往前補舊資料
	 */
	public void processOldData() throws Exception {
        log.info("====AdmClientCountReportJob.processOldData() start====");

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");

		//Date startDate = dateFormate.parse("2013-09-01");
		Date startDate = dateFormate.parse("2014-04-01");
		//Date startDate = dateFormate.parse("2016-11-21");

		Date now = new Date();

		Calendar c2 = Calendar.getInstance();
		c2.setTime(now);
		c2.add(Calendar.DATE, -1);
		Date yesterday = c2.getTime();

		Date reportDate = startDate;
		while (reportDate.getTime() <= yesterday.getTime()) {
			log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>reportDate=" + reportDate);
			String _reportDate = dateFormate.format(reportDate);

			this.processAdmClientCountReport(_reportDate);
			this.processAdmPortalBonusReport(_reportDate);

			Calendar c = Calendar.getInstance();
			c.setTime(reportDate);
			c.add(Calendar.DATE, 1);

			reportDate = c.getTime();
		}

        log.info("====AdmClientCountReportJob.processOldData() end====");
	}

	public void processAllAdmReport(String reportDate) throws Exception{
        log.info("====AdmClientCountReportJob.processAllAdmReport() start====");

		processAdmClientCountReport(reportDate);
		processAdmPortalBonusReport(reportDate);

		log.info("====AdmClientCountReportJob.processAllAdmReport() end====");
	}

	private void processAdmClientCountReport(String reportDate) throws Exception {

		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();

		String countDate = reportDate;	//日期
		int pfpClientCount = 0;			//直客客戶數
		int pfdClientCount = 0;			//經銷商客戶數
		int salesClientCount = 0;		//業務客戶數
		
		float pfpAdClkPrice = 0;		//直客廣告點擊數費用
		float pfdAdClkPrice = 0;		//經銷商廣告點擊數費用
		float salesAdClkPrice = 0;		//廣告點擊數費用廣告點擊數費用
		
		
		float pfpAdActionMaxPrice = 0;	//直客廣告每日預算
		float pfdAdActionMaxPrice = 0;	//經銷商廣告每日預算
		float salesAdActionMaxPrice = 0;//業務廣告每日預算
		
		
		int pfpAdCount = 0;				//直客廣告數
		int pfdAdCount = 0;				//經銷商廣告數
		int salesAdCount = 0;			//業務廣告數
		
		int adPv = 0;					//廣告曝光數
		int adClk = 0;					//廣告點擊數
		int adInvalidClk = 0;			//無效點擊數
		float adClkPrice = 0;			//廣告點擊數總費用
		float adInvalidClkPrice = 0;	//無效點擊數總費用
		float lossCost = 0;				//超播金額
		float pfpSave = 0;				//pchome營運儲值金
		float pfpFree = 0;				//pchome營運贈送金
		float pfpPostpaid = 0;			//pchome營運後付費
		float pfbSave = 0;				//PFB分潤儲值金
		float pfbFree = 0;				//PFB分潤贈送金
		float pfbPostpaid = 0;			//PFB分潤後付費
		float pfdSave = 0;				//PFD佣金儲值金
		float pfdFree = 0;				//PFD佣金贈送金
		float pfdPostpaid = 0;			//PFD佣金後付費
		float totalSavePrice = 0;		//訂單成功總金額
		int totalSaveCount = 0;		//訂單成功總筆數

		//step1. 整理當日 pfp_ad_pvclk 資料
		List<Object> dataList = admClientCountReportService.prepareReportData(reportDate);

		if(!dataList.isEmpty()){
			Object[] objArray = (Object[]) dataList.get(0);

			countDate = dateFormate.format(objArray[0]);
			pfpClientCount = Integer.parseInt(objArray[1].toString());
			pfdClientCount = Integer.parseInt(objArray[2].toString());
			salesClientCount = Integer.parseInt(objArray[3].toString());
			pfpAdClkPrice = ((Double) objArray[4]).floatValue();
			pfdAdClkPrice = ((Double) objArray[5]).floatValue();
			salesAdClkPrice = ((Double) objArray[6]).floatValue();
			pfpAdActionMaxPrice = ((Double) objArray[7]).floatValue();
			pfdAdActionMaxPrice = ((Double) objArray[8]).floatValue();
			salesAdActionMaxPrice = ((Double) objArray[9]).floatValue();
			pfpAdCount = Integer.parseInt(objArray[10].toString());
			pfdAdCount = Integer.parseInt(objArray[11].toString());
			salesAdCount = Integer.parseInt(objArray[12].toString());
			adPv = Integer.parseInt(objArray[13].toString());
			adClk = Integer.parseInt(objArray[14].toString());
			adInvalidClk = Integer.parseInt(objArray[15].toString());
			adClkPrice = ((Double) objArray[16]).floatValue();
			adInvalidClkPrice = ((Double) objArray[17]).floatValue();
			totalSavePrice = ((Double) objArray[18]).floatValue();
			totalSaveCount =  Integer.parseInt(objArray[19].toString());
		}

		//step2. 整理當日 adm_trans_loss 超播金額
		lossCost = admClientCountReportService.findLossCost(reportDate);

		//step3. 整理當日 adm_bonus_detail_report 資料
		List<Object> bonusDetaildataList = admClientCountReportService.findAdmBonusDetailReport(reportDate);

		if(!bonusDetaildataList.isEmpty()){
			Object[] objArray2 = (Object[]) bonusDetaildataList.get(0);

			pfpSave = ((Double) objArray2[0]).floatValue();
			pfpFree = ((Double) objArray2[1]).floatValue();
			pfpPostpaid = ((Double) objArray2[2]).floatValue();
			pfbSave = ((Double) objArray2[3]).floatValue();
			pfbFree = ((Double) objArray2[4]).floatValue();
			pfbPostpaid = ((Double) objArray2[5]).floatValue();
			pfdSave = ((Double) objArray2[6]).floatValue();
			pfdFree = ((Double) objArray2[7]).floatValue();
			pfdPostpaid = ((Double) objArray2[8]).floatValue();
		}

		AdmClientCountReport admClientCountReport = new AdmClientCountReport();
		admClientCountReport.setCountDate(dateFormate.parse(countDate));
		admClientCountReport.setPfpClientCount(pfpClientCount);
		admClientCountReport.setPfdClientCount(pfdClientCount);
		admClientCountReport.setSalesClientCount(salesClientCount);
		admClientCountReport.setPfpAdClkPrice(pfpAdClkPrice);
		admClientCountReport.setPfdAdClkPrice(pfdAdClkPrice);
		admClientCountReport.setSalesAdClkPrice(salesAdClkPrice);
		admClientCountReport.setPfpAdActionMaxPrice(pfpAdActionMaxPrice);
		admClientCountReport.setPfdAdActionMaxPrice(pfdAdActionMaxPrice);
		admClientCountReport.setSalesAdActionMaxPrice(salesAdActionMaxPrice);
		admClientCountReport.setPfpAdCount(pfpAdCount);
		admClientCountReport.setPfdAdCount(pfdAdCount);
		admClientCountReport.setSalesAdCount(salesAdCount);
		admClientCountReport.setAdPv(adPv);
		admClientCountReport.setAdClk(adClk);
		admClientCountReport.setAdInvalidClk(adInvalidClk);
		admClientCountReport.setAdClkPrice(adClkPrice);
		admClientCountReport.setAdInvalidClkPrice(adInvalidClkPrice);
		admClientCountReport.setLossCost(lossCost);
		admClientCountReport.setPfpSave(pfpSave);
		admClientCountReport.setPfpFree(pfpFree);
		admClientCountReport.setPfpPostpaid(pfpPostpaid);
		admClientCountReport.setPfbSave(pfbSave);
		admClientCountReport.setPfbFree(pfbFree);
		admClientCountReport.setPfbPostpaid(pfbPostpaid);
		admClientCountReport.setPfdSave(pfdSave);
		admClientCountReport.setPfdFree(pfdFree);
		admClientCountReport.setPfdPostpaid(pfdPostpaid);
		admClientCountReport.setTotalSaveCount(totalSaveCount);
		admClientCountReport.setTotalSavePrice(totalSavePrice);
		admClientCountReport.setCreateDate(now);

		//step4. 刪除當日資料
		admClientCountReportService.deleteReportDataByReportDate(reportDate);

		//step5. 重寫當日資料
		admClientCountReportService.saveOrUpdate(admClientCountReport);
	}

	private void processAdmPortalBonusReport(String reportDate) throws Exception {
		
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		//						直客經銷商			  DD測試經銷商			PORTAL業務用
		String[] pfdIdArray = {"PFDC20140520001","PFDC20141024003","PFDC20150422001"};
		
		Map<String,Object> pfdBonusMap = new HashMap<String,Object>();
		Map<String,Integer> pfpClientCountMap = new HashMap<String,Integer>();
		float portalPfbBonus = 0;
		float portalOperatingIncome = 0;
		
		pfdBonusMap = admPortalBonusReportService.getPfdBonusDayReport(reportDate);
		pfpClientCountMap = admPortalBonusReportService.getPfpClientCountMap(reportDate);
		portalPfbBonus = admPortalBonusReportService.findPortalPfbBonus(reportDate);
		portalOperatingIncome = admPortalBonusReportService.findPortalOperatingIncome(reportDate);
		
		//刪除當日資料
		admPortalBonusReportService.deleteReportDataByReportDate(reportDate);
		
		//重寫當日資料
		for(String pfdId: pfdIdArray){
			float adValidPrice = 0;
			Integer pfpClientCount = 0;
			float pfdBonus = 0;
			
			if(pfdBonusMap.get(pfdId) != null){
				Object[] objArray = (Object[]) pfdBonusMap.get(pfdId);
				adValidPrice = ((Double) (objArray[2])).floatValue();
				pfdBonus = ((Double) (objArray[3])).floatValue();
			} else {
				adValidPrice = getPfdFreeClkPrice(pfdId,dateFormate.parse(reportDate));
			}
			
			if(pfpClientCountMap.get(pfdId) != null){
				pfpClientCount = pfpClientCountMap.get(pfdId);
			}
			
			AdmPortalBonusReport data = new AdmPortalBonusReport();
			
			data.setPfdCustomerInfoId(pfdId);
			data.setAdPvclkDate(dateFormate.parse(reportDate));
			data.setAdValidPrice(adValidPrice);
			data.setPfpClientCount(pfpClientCount);
			data.setPfdBonus(pfdBonus);
			data.setPortalPfbBonus(0);
			data.setPortalOperatingIncome(0);
			data.setUpdateDate(now);
			data.setCreateDate(now);
			
			admPortalBonusReportService.saveOrUpdate(data);
		}
		
		//寫入PChome營收資料
		AdmPortalBonusReport admPortalBonusReport = new AdmPortalBonusReport();
		
		admPortalBonusReport.setPfdCustomerInfoId("PORTAL");
		admPortalBonusReport.setAdPvclkDate(dateFormate.parse(reportDate));
		admPortalBonusReport.setAdValidPrice(0);
		admPortalBonusReport.setPfpClientCount(0);
		admPortalBonusReport.setPfdBonus(0);
		admPortalBonusReport.setPortalPfbBonus(portalPfbBonus);
		admPortalBonusReport.setPortalOperatingIncome(portalOperatingIncome);
		admPortalBonusReport.setUpdateDate(now);
		admPortalBonusReport.setCreateDate(now);
		
		admPortalBonusReportService.saveOrUpdate(admPortalBonusReport);
	}
	
	
	private float getPfdFreeClkPrice(String pfdId, Date costDate){
		
		float postFreeClkPrice = 0.0f;
				
		postFreeClkPrice = admRecognizeDetailService.findPfdAdPvClkPriceOrderTypeForFree(pfdId,  costDate, costDate);
				
		return postFreeClkPrice;
	}
	
	public void setAdmClientCountReportService(
			IAdmClientCountReportService admClientCountReportService) {
		this.admClientCountReportService = admClientCountReportService;
	}

	public void setAdmPortalBonusReportService(
			IAdmPortalBonusReportService admPortalBonusReportService) {
		this.admPortalBonusReportService = admPortalBonusReportService;
	}

	public void setAdmRecognizeDetailService(
			IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));

		System.out.println(">>> start");

		AdmClientCountReportJob job = context.getBean(AdmClientCountReportJob.class);

		if (args.length == 2 || args.length == 3) {

			if (args[1].equals("day")) { //補指定日期
				if (args.length == 3) {
					job.processAllAdmReport(args[2]);
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

}
