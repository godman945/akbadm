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

public class PfdBonusYearEndSaleB extends APfdBonusItem{

	private final EnumPfdBonusItem pfdBonusItem = EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_A;
	
	private IPfdAdActionReportService pfdAdActionReportService;
	private IPfdBonusDayReportService pfdBonusDayReportService;
	private IPfdBonusRecordService pfdBonusRecordService;
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
	public void updateBonusRecord(List<Object> parseList, String pfdId,
			float pvclkPrice, int year, int quarter, String startDate,
			String endDate, String pfdPayType, PfdBonusRecord bonusRecord) {

		for(int i=0; i<parseList.size();i++){
			
			BonusBean bean = (BonusBean) parseList.get(i);

			if(bean.getMax() >= pvclkPrice && pvclkPrice >= bean.getMin()){
				
				log.info(bean.getMax()+" , "+bean.getMin()+" , "+bean.getBonus());
				
				// 達成獎金百分比
				float reachPercent = bean.getBonus();
				log.info(" reachPercent "+reachPercent);
				// 應得獎金 = 季獎金 - 月獎金
				float bonusMoney = (float)(pvclkPrice * reachPercent) ;
				
				if(bonusMoney < 0){
					bonusMoney = 0;
				}
				bonusRecord.setNowClkPrice(pvclkPrice);
				bonusRecord.setEndDate(DateValueUtil.getInstance().stringToDate(endDate));
				bonusRecord.setNowBonus(bonusMoney);
				bonusRecord.setNowPercent((int)(reachPercent*100));
				bonusRecord.setDeductBonus(0);
				
				// 下個達到奬金條件
				if((++i)<parseList.size()){
					
					bean = (BonusBean) parseList.get(i);
					
					bonusRecord.setNextClkPrice(bean.getMin());
					bonusRecord.setNextPercent((bean.getBonus()*100));
				}

				bonusRecord.setUpdateDate(new Date());
				
				pfdBonusRecordService.saveOrUpdate(bonusRecord);
				
				break;
			}
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
