package com.pchome.akbadm.factory.pfd.bonus;

import java.util.Date;
import java.util.List;

import com.pchome.akbadm.bean.bonus.BonusBean;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdActionReportService;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.akbadm.factory.pfd.parse.PfdParseFactory;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class PfdMonthBonus extends APfdBonusItem{
	
	private final EnumPfdBonusItem pfdBonusItem = EnumPfdBonusItem.EVERY_MONTH_BONUS;
	
	private IPfdAdActionReportService pfdAdActionReportService;
	private IPfdBonusRecordService pfdBonusRecordService;
	private IPfdBonusDayReportService pfdBonusDayReportService;
	private PfdParseFactory pfdParseFactory;	
	private String parsePath;
	
	@Override
	public EnumPfdBonusItem getEnumPfdBonusItem() {
		return pfdBonusItem;
	}

	@Override
	public APfdParseBonusXML getPfdParseBonusXML() {
		return pfdParseFactory.getPfdParseBonusXML(pfdBonusItem.getEnumPfdXmlParse());
	}

	@Override
	public String getParseFile() {
		StringBuffer filePath = new StringBuffer();		
		filePath.append(parsePath).append(pfdBonusItem.getFileName());
		return filePath.toString();
	}
	
	@Override
	public float getPvclkPrice(String pfdId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType) {
		return pfdAdActionReportService.findPfdAdActionClickCost(pfdId, startDate, endDate, enumPfdAccountPayType);
	}
	
	@Override
	public float getSavePvclkPrice(String pfdId, String startDate,String endDate) {
		return pfdBonusDayReportService.getPfdSavePrice(pfdId, startDate, endDate);
	}

	@Override
	public float getPaidPvclkPrice(String pfdId, String startDate,String endDate) {
		return pfdBonusDayReportService.getPfdPaidPrice(pfdId, startDate, endDate);
	}
	
	@Override
	public PfdBonusRecord checkPfdBonusRecord(String pfdContractId, String pfdId, int year, int month, String payType){		
		return pfdBonusRecordService.findPfdBonusRecordByContractId(pfdContractId, pfdId, payType, year, month, pfdBonusItem.getItemType());
	}
	
	@Override
	public void updateBonusRecord(List<Object> parseList, String pfdId, float pvclkPrice, int year, int quarter, 
			String startDate, String endDate, String pfdPayType, PfdBonusRecord bonusRecord) {
		
		for(Object list:parseList){
			
			BonusBean bean = (BonusBean) list;
			
			// 達成獎金百分比
			float reachPercent = bean.getBonus();			
			// 應得獎金
			float bonusMoney = (float) (pvclkPrice * reachPercent); 
			
			log.info(" bonusMoney: "+bonusMoney);
			
			bonusRecord.setNowClkPrice(pvclkPrice);
			bonusRecord.setEndDate(DateValueUtil.getInstance().stringToDate(endDate));
			bonusRecord.setNowBonus(bonusMoney);
			bonusRecord.setNowPercent(reachPercent*100);
			bonusRecord.setUpdateDate(new Date());
			
			pfdBonusRecordService.saveOrUpdate(bonusRecord);
		}
		
	}

	

	public void setPfdParseFactory(PfdParseFactory pfdParseFactory) {
		this.pfdParseFactory = pfdParseFactory;
	}

	public void setPfdAdActionReportService(
			IPfdAdActionReportService pfdAdActionReportService) {
		this.pfdAdActionReportService = pfdAdActionReportService;
	}

	public void setPfdBonusRecordService(
			IPfdBonusRecordService pfdBonusRecordService) {
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setParsePath(String parsePath) {
		this.parsePath = parsePath;
	}

	public void setPfdBonusDayReportService(
			IPfdBonusDayReportService pfdBonusDayReportService) {
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}

	

	

	

	

}
