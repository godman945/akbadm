package com.pchome.akbadm.factory.pfd.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.bean.bonus.BonusBean;
import com.pchome.akbadm.db.pojo.PfdBonusDayReport;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUserAdAccountRef;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.db.service.order.IPfpRefundOrderService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusItemSetService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.akbadm.factory.pfd.parse.PfdParseFactory;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.order.EnumPfpRefundOrderStatus;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.enumerate.recognize.EnumOrderType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class EveryDayPfdBonus {
	
	protected Logger log = LogManager.getRootLogger();
	
	private IPfdContractService pfdContractService;
	private IPfdBonusItemSetService pfdBonusItemSetService;
	private IAdmRecognizeDetailService admRecognizeDetailService;
	private IPfdBonusDayReportService pfdBonusDayReportService;
	private IPfpRefundOrderService pfpRefundOrderService;
	private PfdParseFactory pfdParseFactory;	
	private String parsePath;
	private float publicPfdSaveBonusMoney=0.0f;
	private float publicPfdPaidBonusMoney=0.0f;

	public void pfdBonusProcess(Date startDate ,PfdContract contract){
		
	
				
				log.info(" pfdCustomerInfo: "+contract.getPfdCustomerInfo().getCustomerInfoId());
				
				// 檢查合約(生效中的，過期的如果是綜合經銷商也要拆帳;合約中止或過期當天要算錢給PFD)
				if(this.checkPfdContract(contract,startDate)){	
					
					log.info(" ok pfdCustomerInfo: "+contract.getPfdCustomerInfo().getCustomerInfoId());
					
					log.info(" ok pfdCustomerInfo: "+contract.getPfdContractId());
					
					List<PfdBonusItemSet> pfdBonusItemSets = pfdBonusItemSetService.findPfdBonusItemSets(contract.getPfdContractId());

					for(PfdBonusItemSet set:pfdBonusItemSets){
						
						log.info(" set.getBonusItem() "+set.getBonusItem());
						
						if(set.getBonusItem().equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType())){							
							// 讀取 xml 取出 percent 
							float percent = this.getPercent(set.getSubItemId());
							// 讀取 廣告花費(儲值+後付的金額)
							float pfdSaveClkPrice = this.getPfdSaveClkPrice(set.getPfdContract().getPfdCustomerInfo(), startDate);
							
							float pfdFreeClkPrice = this.getPfdFreeClkPrice(set.getPfdContract().getPfdCustomerInfo().getCustomerInfoId(), startDate);
							
							float pfdPaidClkPrice = this.getPfdPaidClkPrice(set.getPfdContract().getPfdCustomerInfo(), startDate);
							// 更新 pfd_bonus_day_report
							this.updatePfdBonusDayReport(startDate, set.getPfdContract().getPfdCustomerInfo(), percent, pfdSaveClkPrice,pfdFreeClkPrice,pfdPaidClkPrice);
						}
												
					}
					
				}
				
			}
	
	
	private boolean checkPfdContract(PfdContract contract, Date recordDate){
		// 檢查合約
		boolean ok = false;
		
		if(contract.getStatus().equals(EnumContractStatus.USE.getStatusId())){
			ok = true;
		}
		
		if(contract.getStatus().equals(EnumContractStatus.OVERTIME.getStatusId())){
			// 混合型經銷商
			if(contract.getPfdCustomerInfo().getMixFlag().equals("y")){
				ok = true;
			} else {
				//過期合約結束後不跑
				String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);
				String endDateStr = DateValueUtil.getInstance().dateToString(contract.getEndDate());
				
				long checkEndDate = DateValueUtil.getInstance().getDateDiffDay(endDateStr, recordDateStr);
				
				if(checkEndDate <= 1){
					ok = true;
					log.info("true id="+contract.getPfdCustomerInfo().getCompanyName());
				}
			}
		}
		
		//合約中止當天要算錢給PFD
		if(contract.getStatus().equals(EnumContractStatus.CLOSE.getStatusId())){
			if(contract.getCloseDate() != null){
				String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);
				String closeDateStr = DateValueUtil.getInstance().dateToString(contract.getCloseDate());
				
				long checkCloseDate = DateValueUtil.getInstance().getDateDiffDay(closeDateStr, recordDateStr);
				
				if(checkCloseDate <= 1){
					ok = true;
					log.info("true id="+contract.getPfdCustomerInfo().getCompanyName());
				}
			}
		}
		
		return ok;
	}
	
	private List<Object> parseBonusXMLFile(int subItemId) {
		// 讀取 Xml 檔案類型 
		APfdParseBonusXML pfdParseBonusXML = pfdParseFactory.getPfdParseBonusXML(EnumPfdBonusItem.EVERY_MONTH_BONUS.getEnumPfdXmlParse());
		
		// 要讀取 Xml 檔案路徑
		StringBuffer filePath = new StringBuffer();		
		filePath.append(parsePath).append(EnumPfdBonusItem.EVERY_MONTH_BONUS.getFileName());
		
		String xmlPath = filePath.toString();
		
		List<Object> list = pfdParseBonusXML.parseXML(xmlPath, subItemId);		
		
		/*// parse 內容
		for(Object ob:list){
			BonusBean bean = (BonusBean)ob;
			log.info(bean.getTitle()+" : "+bean.getMax()+" , "+bean.getMin()+" , "+bean.getBonus());
		}
		*/
		
		return list;
	}
	
	private float getPercent(int subItemId){
		
		float reachPercent = 0;
		// 讀取 Xml 檔案內容
		List<Object> parseList = this.parseBonusXMLFile(subItemId);

		for(Object list:parseList){
			
			BonusBean bean = (BonusBean) list;
			
			// 達成獎金百分比
			reachPercent = bean.getBonus();
			
		}
		
		return reachPercent;
	}
	
	private float getPfdSaveClkPrice(PfdCustomerInfo pfdCustomerInfo, Date costDate){
		
		float saveClkPrice = 0.0f;
		float refundPrice = 0.0f;
		
		
		//申請中PFD 跳過
		if (!pfdCustomerInfo.getStatus().equals(EnumPfdAccountStatus.APPLY.getStatus())) {

			List<String> pfpIdList = new ArrayList<String>();

			List<PfdUserAdAccountRef> pfdUserAdAccountRefList = new ArrayList<PfdUserAdAccountRef>(
					pfdCustomerInfo.getPfdUserAdAccountRefs());
			for (PfdUserAdAccountRef pfdUserAdAccountRef : pfdUserAdAccountRefList) {
				pfpIdList.add(pfdUserAdAccountRef.getPfpCustomerInfo().getCustomerInfoId());
			}

			saveClkPrice = admRecognizeDetailService.findPfdAdPvClkPrice(pfdCustomerInfo.getCustomerInfoId(), EnumOrderType.SAVE.getTypeTag(),costDate, costDate);

			if (!pfpIdList.isEmpty()) {
				refundPrice = pfpRefundOrderService.findRefundPrice(pfpIdList, costDate, costDate,
						EnumPfdAccountPayType.ADVANCE.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
			} else {
				log.info("pfd_id=" + pfdCustomerInfo.getCustomerInfoId() + ",pfp list is empty");
			}

			saveClkPrice = saveClkPrice - refundPrice;

		} else {

			log.info("pfd_id=" + pfdCustomerInfo.getCustomerInfoId() + "sataus_id=" + pfdCustomerInfo.getStatus());
		}
		
		return saveClkPrice;
	}
	
private float getPfdPaidClkPrice(PfdCustomerInfo pfdCustomerInfo, Date costDate){
		
		float postpaidClkPrice = 0.0f;
		float refundPrice = 0.0f;
		
		//申請中PFD 跳過
		if(!pfdCustomerInfo.getStatus().equals(EnumPfdAccountStatus.APPLY.getStatus())){
			
		
		
			List<String> pfpIdList = new ArrayList<String>();
			
			List<PfdUserAdAccountRef> pfdUserAdAccountRefList = new ArrayList<PfdUserAdAccountRef>(pfdCustomerInfo.getPfdUserAdAccountRefs());
			for(PfdUserAdAccountRef pfdUserAdAccountRef:pfdUserAdAccountRefList){
				pfpIdList.add(pfdUserAdAccountRef.getPfpCustomerInfo().getCustomerInfoId());
			}
					
			postpaidClkPrice = admRecognizeDetailService.findPfdAdPvClkPrice(pfdCustomerInfo.getCustomerInfoId(), EnumOrderType.VIRTUAL.getTypeTag(), costDate, costDate);
			
			if(!pfpIdList.isEmpty()){
				refundPrice = pfpRefundOrderService.findRefundPrice(pfpIdList, costDate, costDate, EnumPfdAccountPayType.LATER.getPayType(), EnumPfpRefundOrderStatus.SUCCESS.getStatus());
			}else{
				log.info("pfd_id="+pfdCustomerInfo.getCustomerInfoId()+",pfp list is empty");
			}
			
			postpaidClkPrice = postpaidClkPrice - refundPrice;
		
		}else{
			
			log.info("pfd_id="+pfdCustomerInfo.getCustomerInfoId()+"sataus_id="+pfdCustomerInfo.getStatus());
		}
		
		return postpaidClkPrice;
	}
	
private float getPfdFreeClkPrice(String pfdId, Date costDate){
	
	float postFreeClkPrice = 0.0f;
	
			
	postFreeClkPrice = admRecognizeDetailService.findPfdAdPvClkPriceOrderTypeForFree(pfdId,  costDate, costDate);
			
	return postFreeClkPrice;
}
	
	private void updatePfdBonusDayReport(Date reportDate, PfdCustomerInfo pfdCustomerInfo, float percent, float padSavePrice,float padFreePrice,float padPaidPrice){
		
		log.info(" pfdCustomerInfo: "+pfdCustomerInfo.getCustomerInfoId());
		log.info(" percent: "+percent);
		log.info(" padSavePrice: "+padSavePrice);
		log.info(" padFreePrice: "+padFreePrice);
		log.info(" padPaidPrice: "+padPaidPrice);
		
		if(percent > 0 && (padSavePrice > 0 || padPaidPrice >0)){
			
			Date today = new Date();
			PfdBonusDayReport report = new PfdBonusDayReport();
			float clkPrice = padSavePrice + padPaidPrice;
			float pfdSaveBonusMoney = percent * padSavePrice;
			float pfdFreeBonusMoney = percent * padFreePrice;
			float pfdPaidBonusMoney = percent * padPaidPrice;
			
			float pfdBonusMoney = pfdSaveBonusMoney +  pfdPaidBonusMoney;
			report.setReportDate(reportDate);
			report.setPfdCustomerInfo(pfdCustomerInfo);
			report.setPfdMonthPercent(percent);
			report.setPfdClkPrice(clkPrice);
			report.setSaveClkPrice(padSavePrice);
			report.setFreeClkPrice(padFreePrice);
			report.setPostpaidClkPrice(padPaidPrice);
			report.setSaveBouns(pfdSaveBonusMoney);
			report.setFreeBonus(pfdFreeBonusMoney);
			report.setPostpaidBonus(pfdPaidBonusMoney);
			report.setPfdBonusMoney(pfdBonusMoney);
			report.setCreateDate(today);
			
			pfdBonusDayReportService.saveOrUpdate(report);
		}
		
	}

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setPfdBonusItemSetService(
			IPfdBonusItemSetService pfdBonusItemSetService) {
		this.pfdBonusItemSetService = pfdBonusItemSetService;
	}

	public void setAdmRecognizeDetailService(
			IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}

	public void setPfdBonusDayReportService(
			IPfdBonusDayReportService pfdBonusDayReportService) {
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}

	public void setPfdParseFactory(PfdParseFactory pfdParseFactory) {
		this.pfdParseFactory = pfdParseFactory;
	}

	public void setParsePath(String parsePath) {
		this.parsePath = parsePath;
	}

	public float getPublicPfdSaveBonusMoney() {
		return publicPfdSaveBonusMoney;
	}

	public void setPublicPfdSaveBonusMoney(float publicPfdSaveBonusMoney) {
		this.publicPfdSaveBonusMoney = publicPfdSaveBonusMoney;
	}

	public float getPublicPfdPaidBonusMoney() {
		return publicPfdPaidBonusMoney;
	}

	public void setPublicPfdPaidBonusMoney(float publicPfdPaidBonusMoney) {
		this.publicPfdPaidBonusMoney = publicPfdPaidBonusMoney;
	}

	public void setPfpRefundOrderService(IPfpRefundOrderService pfpRefundOrderService) {
		this.pfpRefundOrderService = pfpRefundOrderService;
	}
	
	
}
