package com.pchome.akbadm.factory.pfd.bonus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pchome.akbadm.bean.bonus.BonusBean;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.service.customerInfo.IPfdUserAdAccountRefService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.akbadm.factory.pfd.parse.PfdParseFactory;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class PfdYearDevelopBonus extends APfdBonusItem{

	private final EnumPfdBonusItem pfdBonusItem = EnumPfdBonusItem.EVERY_YEAR_DEVELOP_BONUS;
	
	private IPfdUserAdAccountRefService pfdUserAdAccountRefService;
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
		return 0;
	}
	
	@Override
	public float getSavePvclkPrice(String pfdId, String startDate,String endDate) {
		return 0;
	}

	@Override
	public float getPaidPvclkPrice(String pfdId, String startDate,String endDate) {
		return 0;
	}

	@Override
	public PfdBonusRecord checkPfdBonusRecord(String pfdContractId, String pfdId, int year, int month, String payType){		
		return pfdBonusRecordService.findPfdBonusRecordByContractId(pfdContractId, pfdId, payType, year, month, pfdBonusItem.getItemType());
	}

	@Override
	public void updateBonusRecord(List<Object> parseList, String pfdId, float pvclkPrice, int year, int quarter, 
			String startDate, String endDate, String pfdPayType, PfdBonusRecord bonusRecord) {
		
		int developAmount = pfdUserAdAccountRefService.findPfpCustomerInfoAmount(pfdId, startDate, endDate);
		
		if(bonusRecord.getNextAmount() == 0 || bonusRecord.getNextBonus() == 0){
			// 先塞入條件值
			BonusBean bean = (BonusBean) parseList.get(0);	
			bonusRecord.setNextAmount((int)bean.getMin());				
			bonusRecord.setNextBonus(bean.getBonus());
		}
				
		if(developAmount > 0){
			
			bonusRecord.setNowAmount(developAmount);
			
			for(int i=0; i<parseList.size();i++){
				
				BonusBean bean = (BonusBean) parseList.get(i);

				// 計算開發獎金
				if(bean.getMax() >= developAmount && developAmount >= bean.getMin()){				
				
					bonusRecord.setNowBonus(bean.getBonus());					
					
					// 下個達到奬金條件
					if((++i)<parseList.size()){							
						bean = (BonusBean) parseList.get(i);			
					}
					
					bonusRecord.setNextAmount((int)bean.getMin());	
					bonusRecord.setNextBonus(bean.getBonus());
					
					break;					
				}		
			
			}			
		}
				
		bonusRecord.setEndDate(DateValueUtil.getInstance().stringToDate(endDate));
		bonusRecord.setUpdateDate(new Date());
		
		pfdBonusRecordService.saveOrUpdate(bonusRecord);		
	}
	
	

	
	public void setPfdUserAdAccountRefService(
			IPfdUserAdAccountRefService pfdUserAdAccountRefService) {
		this.pfdUserAdAccountRefService = pfdUserAdAccountRefService;
	}

	public void setPfdParseFactory(PfdParseFactory pfdParseFactory) {
		this.pfdParseFactory = pfdParseFactory;
	}

	public void setPfdBonusRecordService(
			IPfdBonusRecordService pfdBonusRecordService) {
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setParsePath(String parsePath) {
		this.parsePath = parsePath;
	}
}
