package com.pchome.akbadm.factory.pfd.bonus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.bean.bonus.BonusBean;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.db.service.report.quartzs.IPfdAdActionReportService;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.akbadm.factory.pfd.parse.PfdParseFactory;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class PfdQuarterBonus extends APfdBonusItem{

	private final EnumPfdBonusItem pfdBonusItem = EnumPfdBonusItem.EVERY_QUARTER_BONUS;
	
	private IPfdAdActionReportService pfdAdActionReportService;
	private IPfdBonusRecordService pfdBonusRecordService;
	private IPfdBonusDayReportService pfdBonusDayReportService;
	private PfdParseFactory pfdParseFactory;	
	private String parsePath;
	
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
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
		
		for(int i=0; i<parseList.size();i++){
			
			BonusBean bean = (BonusBean) parseList.get(i);
			
			if(bean.getMax() >= pvclkPrice && pvclkPrice >= bean.getMin()){
				log.info(bean.getMax()+" , "+bean.getMin()+" , "+bean.getBonus());
				
				// 達成獎金百分比
				float reachPercent = bean.getBonus();
				log.info(" reachPercent "+reachPercent);
				// 應得獎金 = 季獎金
				float bonusMoney = (float)(pvclkPrice * reachPercent);
				
				if(bonusMoney < 0){
					bonusMoney = 0;
				}
				
				//如果為混合型合約
				if ( StringUtils.equals(pfdPayType, EnumPfdAccountPayType.BOTH.getPayType()) ){
					pvclkPrice = 0;
					if ( StringUtils.equals(bonusRecord.getPayType(),EnumPfdAccountPayType.ADVANCE.getPayType()) ){
						pvclkPrice = this.getSavePvclkPrice(pfdId, startDate, endDate);
					}else if( StringUtils.equals(bonusRecord.getPayType(),EnumPfdAccountPayType.LATER.getPayType()) ){
						pvclkPrice = this.getPaidPvclkPrice(pfdId, startDate, endDate);
					}
					//為混合型合約的獎金=付預+後付除以2，但預付和後付的NowClkPrice花費金額為各別實際花費
					bonusMoney = bonusMoney/2;
				}
				
				bonusRecord.setNowClkPrice(pvclkPrice);
				bonusRecord.setEndDate(DateValueUtil.getInstance().stringToDate(endDate));
				bonusRecord.setNowBonus(bonusMoney);
				bonusRecord.setNowPercent(reachPercent*100);
				
				// 下個達到奬金條件
				if((++i)<parseList.size()){
					
					bean = (BonusBean) parseList.get(i);
					
					bonusRecord.setNextClkPrice(bean.getMin());
					bonusRecord.setNextPercent((bean.getBonus()*100));
				}

				//檢查合約是否為作廢或過期
				PfdContract pfdContract = bonusRecord.getPfdContract();
				String pfdContractStatus = pfdContract.getStatus();
				
				//合約過期時，檢查合約結束日期與獎金記錄結束日期是否相同，若相同則季獎金於當月結算
				if(StringUtils.equals(pfdContractStatus, EnumContractStatus.OVERTIME.getStatusId())){
					if(pfdContract.getEndDate() != null && StringUtils.equals(dateFormate.format(pfdContract.getEndDate()),endDate)){
						bonusRecord.setCloseMonth(bonusRecord.getMonth());
					}
				}
				
				//合約中止時，檢查合約中止日期與獎金記錄結束日期是否相同，若相同則季獎金於當月結算
				if(StringUtils.equals(pfdContractStatus, EnumContractStatus.CLOSE.getStatusId())){
					if(pfdContract.getCloseDate() != null && StringUtils.equals(dateFormate.format(pfdContract.getCloseDate()),endDate)){
						bonusRecord.setCloseMonth(bonusRecord.getMonth());
					}
				}
				
				bonusRecord.setUpdateDate(new Date());
				
				pfdBonusRecordService.saveOrUpdate(bonusRecord);
				
				break;
			}
		}
		
	}
	
	
	
	private float getPayBonus(String pfdId, String pfdPayType, int year, int quarter) {
		// 取已領取月獎金
		float payMonthBonus = pfdBonusRecordService.findPayBonus(pfdId, pfdPayType, year, quarter, EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType());
		
		return payMonthBonus;
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
