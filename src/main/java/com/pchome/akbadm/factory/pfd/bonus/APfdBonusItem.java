package com.pchome.akbadm.factory.pfd.bonus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.factory.pfd.parse.APfdParseBonusXML;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.enumerate.pfd.bonus.EnumPfdCloseDay;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;


/**
 * 經銷商獎金計算流程
 * 
 * 
 *	
 *
 */
public abstract class APfdBonusItem {

	protected Logger log = LogManager.getRootLogger();
	
	// 獎金計算項目
	public abstract EnumPfdBonusItem getEnumPfdBonusItem();
	// 讀取 Xml 檔案類型  
	public abstract APfdParseBonusXML getPfdParseBonusXML();
	// 要讀取 Xml 檔案路徑
	public abstract String getParseFile();	
	// Pfd 廣告費用
	public abstract float getPvclkPrice(String pfdId, String startDate, String endDate, EnumPfdAccountPayType enumPfdAccountPayType);
	// Pfd 儲值廣告費用
	public abstract float getSavePvclkPrice(String pfdId, String startDate, String endDate);
	// Pfd 後付廣告費用
	public abstract float getPaidPvclkPrice(String pfdId, String startDate, String endDate);
	// 確認 Pfd 獎金記錄
	public abstract PfdBonusRecord checkPfdBonusRecord(String pfdContractId, String pfdId, int year, int month, String payType);
	// 計算更新 Pfd 獎金
	public abstract void updateBonusRecord(List<Object> parseList, String pfdId, float pvclkPrice, int year, int quarter, String startDate, String endDate, String pfdPayType, PfdBonusRecord bonusRecord);
	// 更新 Pfd 報表
	//public abstract void updateBonusDetailReport(String pfdId, int year, int month, int quarter, int closeMonth, String startDate, String endDate, String payType);
	
	public void process(Date recordDate, PfdContract pfdContract, int subItemId,String bonusItem){
		
		log.info(" subItemId: "+subItemId);		
		
		// 判斷合約日期
		if(this.validContractDate(recordDate, pfdContract)){
			
			String pfdId = pfdContract.getPfdCustomerInfo().getCustomerInfoId();
			
			// 獎金計算項目
			EnumPfdBonusItem enumPfdBonusItem = this.getEnumPfdBonusItem();
			
			// 專案有日期限制也需判斷
			if(this.validItemDate(recordDate, enumPfdBonusItem)){
				
				// 讀取 Xml 檔案內容
				List<Object> parseList = this.parseBonusXMLFile(subItemId);
				
				// 計算開始日期
				String startDate = this.getStartDate(recordDate, pfdContract, enumPfdBonusItem);
				// 計算結束日期
				String endDate = DateValueUtil.getInstance().dateToString(recordDate);
				
				log.info(" startDate: "+startDate);
				log.info(" endDate: "+endDate);
				
				// 獎金屬於那個0 日,1 月,2 年 
				List<Integer> dateList = this.getYearAndMonth(recordDate);
				int month = dateList.get(1);
				int year = dateList.get(2);
				int quarter = dateList.get(3);
				
				// 獎金最後結算年月份
				List<String> closeDate = this.getCloseYearAndMonth(startDate, enumPfdBonusItem);
				int closeYear = Integer.valueOf(closeDate.get(0));
				int closeMonth = Integer.valueOf(closeDate.get(1));
				
				log.info(" closeYear: "+closeYear);
				log.info(" closeMonth: "+closeMonth);
				
				//當合約為混合型(預付+後付)，先將預付與後付花費加總，再算%數後除以2，各半金額寫入預付、後付紀錄中(只有季、年獎金有花費達標問題，故月佣金不用)
				if( (StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType())&&StringUtils.equals(EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType(), bonusItem)) ||
						(StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType())&&StringUtils.equals(EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType(), bonusItem)) ){
					
					//預付花費
					float savePvclkPrice=this.getSavePvclkPrice(pfdId, startDate, endDate);
					//後付花費
					float paidPvclkPrice=this.getPaidPvclkPrice(pfdId, startDate, endDate);
					//混合型合約
					//1.先將預付和後付花費先加總，再計算達標季、年獎金(獎金除以2寫入bonus欄位)
					//2.但預付和後付的NowClkPrice花費金額為各別實際花費
					float mixPvclkPrice=savePvclkPrice+paidPvclkPrice;
					
					this.updateBonusRecord(mixPvclkPrice,EnumPfdAccountPayType.BOTH,parseList, pfdId, year, month, quarter, pfdContract, closeYear, closeMonth, enumPfdBonusItem, startDate, endDate);

					
				}else{//不為混合型合約或計算項目為月佣金
					
					//更新預付獎金
					if ( (StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.ADVANCE.getPayType())) || (StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType())&&StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), bonusItem)) ){
						float savePvclkPrice=this.getSavePvclkPrice(pfdId, startDate, endDate);
						this.updateBonusRecord(savePvclkPrice,EnumPfdAccountPayType.ADVANCE,parseList, pfdId, year, month, quarter, pfdContract, closeYear, closeMonth, enumPfdBonusItem, startDate, endDate);
					}
					 
					//更新後付獎金
					if ( (StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.LATER.getPayType())) || (StringUtils.equals(pfdContract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType())&&StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), bonusItem)) ){
						float paidPvclkPrice=this.getPaidPvclkPrice(pfdId, startDate, endDate);
						this.updateBonusRecord(paidPvclkPrice,EnumPfdAccountPayType.LATER,parseList, pfdId, year, month, quarter, pfdContract, closeYear, closeMonth, enumPfdBonusItem, startDate, endDate);
					}
					
				}
			}
		}
	}
	
   private void updateBonusRecord(float PvclkPrice,EnumPfdAccountPayType enumPfdAccountPayType,List<Object> parseList, String pfdId, int year, int month, int quarter, PfdContract pfdContract, int closeYear, int closeMonth, EnumPfdBonusItem enumPfdBonusItem, String startDate, String endDate){
		
	    //當合約為混合型(預付+後付)，先將預付與後付花費加總，再算%數後除以2，各半金額寫入預付、後付紀錄中(只有季、年獎金有花費達標問題，故月佣金不用)
 	    //當合約為混合型(預付+後付)，預付和後付花費還是寫入實際金額，不除以2
	   if ( StringUtils.equals(enumPfdAccountPayType.getPayType(), EnumPfdAccountPayType.BOTH.getPayType()) ){
		    // 預付- Pfd 獎金記錄
		    PfdBonusRecord advanceBonusRecord = new PfdBonusRecord();
			advanceBonusRecord = this.checkPfdBonusRecord(pfdContract.getPfdContractId(), pfdContract.getPfdCustomerInfo().getCustomerInfoId(), year, month,EnumPfdAccountPayType.ADVANCE.getPayType());
			if(advanceBonusRecord == null){
				advanceBonusRecord = this.createNewPfdBonusRecord(pfdId, pfdContract, year, month, quarter, closeYear, closeMonth,EnumPfdAccountPayType.ADVANCE.getPayType(), enumPfdBonusItem, startDate, endDate);
			}
			// 更新季
			advanceBonusRecord.setQuarter(quarter);
			// 計算更新預付 Pfd 獎金
			this.updateBonusRecord(parseList, pfdId, 
					PvclkPrice, year, quarter, startDate, endDate, 
					EnumPfdAccountPayType.BOTH.getPayType(), advanceBonusRecord);
			
			
			
			
			// 後付- Pfd 獎金記錄
			PfdBonusRecord laterBonusRecord = new PfdBonusRecord();
			laterBonusRecord = this.checkPfdBonusRecord(pfdContract.getPfdContractId(), pfdContract.getPfdCustomerInfo().getCustomerInfoId(), year, month,EnumPfdAccountPayType.LATER.getPayType());
			if(laterBonusRecord == null){
				laterBonusRecord = this.createNewPfdBonusRecord(pfdId, pfdContract, year, month, quarter, closeYear, closeMonth,EnumPfdAccountPayType.LATER.getPayType(), enumPfdBonusItem, startDate, endDate);
			}
			// 更新季
			laterBonusRecord.setQuarter(quarter);
			// 計算更新預付 Pfd 獎金
			this.updateBonusRecord(parseList, pfdId, 
					PvclkPrice, year, quarter, startDate, endDate, 
					EnumPfdAccountPayType.BOTH.getPayType(), laterBonusRecord);
		   
	   }else{ //預付型或後付型合約，不為混合型合約
		   
		   PfdBonusRecord advanceLaterBonusRecord = new PfdBonusRecord();
					
		   // 預付 Pfd 獎金記錄
		   advanceLaterBonusRecord = this.checkPfdBonusRecord(pfdContract.getPfdContractId(), pfdContract.getPfdCustomerInfo().getCustomerInfoId(), year, month, enumPfdAccountPayType.getPayType());
			
			if(advanceLaterBonusRecord == null){
				advanceLaterBonusRecord = this.createNewPfdBonusRecord(pfdId, pfdContract, year, month, quarter, closeYear, closeMonth, enumPfdAccountPayType.getPayType(), enumPfdBonusItem, startDate, endDate);
			}
			
			// 更新季
			advanceLaterBonusRecord.setQuarter(quarter);
			
			// 計算更新預付 Pfd 獎金
			this.updateBonusRecord(parseList, pfdId, 
					PvclkPrice, year, quarter, startDate, endDate, 
					enumPfdAccountPayType.getPayType(), advanceLaterBonusRecord);
			
			// 更新 Pfd 報表
			//this.updateBonusDetailReport(pfdId, year, month, quarter, closeMonth, startDate, endDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	   }
	}
	
   /*
	private void updateAdvanceBonusRecord(List<Object> parseList, String pfdId, int year, int month, int quarter, PfdContract pfdContract, int closeYear, int closeMonth, EnumPfdBonusItem enumPfdBonusItem, String startDate, String endDate){
		
		// 預付廣告點擊費用
		float advancePvclkPrice = this.getPvclkPrice(pfdId, startDate, endDate, EnumPfdAccountPayType.ADVANCE);
		
		// 預付 Pfd 獎金記錄
		PfdBonusRecord advanceBonusRecord = this.checkPfdBonusRecord(pfdId, year, month, EnumPfdAccountPayType.ADVANCE.getPayType());
		
		if(advanceBonusRecord == null){
			advanceBonusRecord = this.createNewPfdBonusRecord(pfdId, pfdContract, year, month, quarter, closeYear, closeMonth, EnumPfdAccountPayType.ADVANCE.getPayType(), enumPfdBonusItem, startDate, endDate);
		}
		
		// 更新季
		advanceBonusRecord.setQuarter(quarter);
		
		// 計算更新預付 Pfd 獎金
		this.updateBonusRecord(parseList, pfdId, 
				advancePvclkPrice, year, quarter, startDate, endDate, 
				EnumPfdAccountPayType.ADVANCE.getPayType(), advanceBonusRecord);
		
		// 更新 Pfd 報表
		//this.updateBonusDetailReport(pfdId, year, month, quarter, closeMonth, startDate, endDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	}
	
	private void updateLaterBonusRecord(List<Object> parseList, String pfdId, int year, int month, int quarter, PfdContract pfdContract, int closeYear, int closeMonth, EnumPfdBonusItem enumPfdBonusItem, String startDate, String endDate){
		
		// 後付廣告點擊費用
		float laterPvclkPrice = this.getPvclkPrice(pfdId, startDate, endDate, EnumPfdAccountPayType.LATER);
		// 後付 Pfd 獎金記錄
		PfdBonusRecord laterBonusRecord = this.checkPfdBonusRecord(pfdId, year, month, EnumPfdAccountPayType.LATER.getPayType());
		
		if(laterBonusRecord == null){
			laterBonusRecord = this.createNewPfdBonusRecord(pfdId, pfdContract, year, month, quarter, closeYear, closeMonth, EnumPfdAccountPayType.LATER.getPayType(), enumPfdBonusItem, startDate, endDate);
		}
		
		// 計算更新後付 enumPfdBonusItem 獎金
		this.updateBonusRecord(parseList, pfdId, 
				laterPvclkPrice, year, quarter, startDate, endDate, 
				EnumPfdAccountPayType.LATER.getPayType(), laterBonusRecord);
		
		// 更新 Pfd 報表
		//this.updateBonusDetailReport(pfdId, year, month, quarter, closeMonth, startDate, endDate, EnumPfdAccountPayType.ADVANCE.getPayType());
	}
	*/
	
	private List<Integer> getYearAndMonth(Date recordDate){
		// 獎金屬於那個日,月,年 
		List<Integer> dateList = new ArrayList<Integer>();
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(recordDate);
		
		int day = cal.get(Calendar.DATE);
		dateList.add(day);
		
		/*if(cal.get(Calendar.DATE) <= 25){
    		cal.add(Calendar.MONTH, 0);
    	}else{
    		cal.add(Calendar.MONTH, 1);
    	}*/
		
		int month = cal.get(Calendar.MONTH) + 1;
    	dateList.add(month);
    	
		int year = cal.get(Calendar.YEAR);    	
    	dateList.add(year);   
    	
    	int quarter = DateValueUtil.getInstance().getQuarterOfYear(DateValueUtil.getInstance().dateToString(recordDate));
    	dateList.add(quarter);   
		
		return dateList;
	}
	
	private List<Object> parseBonusXMLFile(int subItemId) {
		// 讀取 Xml 檔案類型 
		APfdParseBonusXML pfdParseBonusXML = this.getPfdParseBonusXML();
		
		// 要讀取 Xml 檔案路徑
		String filePath = this.getParseFile();
		
		List<Object> list = pfdParseBonusXML.parseXML(filePath, subItemId);		
		
		/*// parse 內容
		for(Object ob:list){
			BonusBean bean = (BonusBean)ob;
			log.info(bean.getTitle()+" : "+bean.getMax()+" , "+bean.getMin()+" , "+bean.getBonus());
		}
		*/
		
		return list;
	}
	
	private PfdBonusRecord createNewPfdBonusRecord(String pfdId, PfdContract pfdContract, int year, int month, int quarter, int closeYear, int closeMonth,
			String pfdPayType, EnumPfdBonusItem enumPfdBonusItem, String startDate, String endDate){
		
		PfdBonusRecord pfdBonusRecord = new PfdBonusRecord();
		Date today = new Date();
		
		pfdBonusRecord.setNowClkPrice(0);
		pfdBonusRecord.setNextClkPrice(0);
		pfdBonusRecord.setYear(year);
		pfdBonusRecord.setMonth(month);
		pfdBonusRecord.setQuarter(quarter);
		pfdBonusRecord.setCloseYear(closeYear);
		pfdBonusRecord.setCloseMonth(closeMonth);
		pfdBonusRecord.setPfdContract(pfdContract);
		pfdBonusRecord.setPayType(pfdPayType);
		pfdBonusRecord.setBonusItem(enumPfdBonusItem.getItemType());
		pfdBonusRecord.setBonusName(enumPfdBonusItem.getItemChName());
		pfdBonusRecord.setBonusNote("");		
		pfdBonusRecord.setStartDate(DateValueUtil.getInstance().stringToDate(startDate));
		pfdBonusRecord.setEndDate(DateValueUtil.getInstance().stringToDate(endDate));
		pfdBonusRecord.setDeductBonus(0);
		pfdBonusRecord.setNextAmount(0);
		pfdBonusRecord.setNextBonus(0);
		pfdBonusRecord.setNextPercent(0);
		pfdBonusRecord.setNowAmount(0);
		pfdBonusRecord.setNowBonus(0);
		pfdBonusRecord.setNowPercent(0);
		pfdBonusRecord.setReportActoin(enumPfdBonusItem.getReportAction());
		pfdBonusRecord.setUpdateDate(today);
		pfdBonusRecord.setCreateDate(today);
		
		return pfdBonusRecord;
	}
	
	private boolean validContractDate(Date recordDate, PfdContract pfdContract){
		// 確認合約有效日期
		boolean ok = false;
		String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);
		
		String contractStartDateStr = DateValueUtil.getInstance().dateToString(pfdContract.getStartDate());		
			
		long lCheckStartDate = DateValueUtil.getInstance().getDateDiffDay(contractStartDateStr, recordDateStr);	
	
		String contractEndDateStr = DateValueUtil.getInstance().dateToString(pfdContract.getEndDate());
		
		long lCheckEndDate = DateValueUtil.getInstance().getDateDiffDay(recordDateStr, contractEndDateStr);
		
		if(lCheckStartDate > 0 && lCheckEndDate > 0){
			ok = true;
		}
		else{
			log.info(" contractStartDateStr: "+contractStartDateStr);
			log.info(" contractEndDateStr: "+contractEndDateStr);
			log.info(" lCheckStartDate: "+lCheckStartDate);
			log.info(" lCheckEndDate: "+lCheckEndDate);
		}
		
		return ok;
	}
	
	private boolean validItemDate(Date recordDate, EnumPfdBonusItem enumPfdBonusItem){
		// 專案依專案設定日期為依據
		boolean ok = true;
		
		if(enumPfdBonusItem.getStartDate() != null && 
				enumPfdBonusItem.getEndDate() != null){
			
			String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);
			
			String itemStartDateStr = enumPfdBonusItem.getStartDate();		
				
			long lCheckStartDate = DateValueUtil.getInstance().getDateDiffDay(itemStartDateStr, recordDateStr);	
		
			String itemEndDateStr = enumPfdBonusItem.getEndDate();
			
			long lCheckEndDate = DateValueUtil.getInstance().getDateDiffDay(recordDateStr, itemEndDateStr);
			
			if(lCheckStartDate < 0 && lCheckEndDate < 0){
				ok = false;
			}
			else{
				log.info(" itemStartDateStr: "+itemStartDateStr);
				log.info(" itemEndDateStr: "+itemEndDateStr);
				log.info(" lCheckStartDate: "+lCheckStartDate);
				log.info(" lCheckEndDate: "+lCheckEndDate);
			}
		}
		
		return ok;
	}
	
	
	private String getStartDate(Date recordDate, PfdContract pfdContract, EnumPfdBonusItem pfdBonusItem){
		// 取開始記錄日期
		String startDate = null;
		String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);
		String itemStartDate = DateValueUtil.getInstance().dateToString(pfdContract.getStartDate());
		String rangeStartDate = null;
		
		//2018後拿掉	
		String[] dateArray = recordDateStr.split("-");
		Integer year = Integer.parseInt(dateArray[0]);
		Integer month = Integer.parseInt(dateArray[1]);
		//2018後拿掉	
		
		if(EnumPfdBonusItem.EVERY_MONTH_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_MONTH_DEVELOP_BONUS.equals(pfdBonusItem)){	
			
			String[] rangeDate = DateValueUtil.getInstance().getMonthRangeDate(recordDateStr).split(",");
			rangeStartDate = rangeDate[1];
					
		}
		else if(EnumPfdBonusItem.EVERY_QUARTER_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_QUARTER_DEVELOP_BONUS.equals(pfdBonusItem)){
			
			String[] rangeDate = getQuarterRangeDate(recordDateStr);
			rangeStartDate = rangeDate[1];
			
		}
		else if(EnumPfdBonusItem.EVERY_YEAR_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_YEAR_DEVELOP_BONUS.equals(pfdBonusItem)){
			
			if(year <= 2017){ //2018年後只保留 else 內容
				String[] rangeDatetest = DateValueUtil.getInstance().getYearRangeDate(recordDateStr).split(",");
				
				String[] yearLastDate = DateValueUtil.getInstance().getYearBonusEndDate(rangeDatetest[1], EnumPfdCloseDay.DAY_25.getDay()).split(",");		
				
				Calendar cal = Calendar.getInstance();
				
				cal.setTime(DateValueUtil.getInstance().stringToDate(yearLastDate[0]));
				
				cal.add(Calendar.YEAR, -1);
				cal.add(Calendar.DATE, 1);
				
				rangeStartDate = DateValueUtil.getInstance().dateToString(cal.getTime());	
			} else {
				String[] rangeDate = DateValueUtil.getInstance().getYearRangeDate(recordDateStr).split(",");
				rangeStartDate = rangeDate[1];
			}
			
		}
		else{			
			// 專案是依專案設定日期為依據
			String[] rangeDate = DateValueUtil.getInstance().getMonthBonusRangeDate(recordDateStr, EnumPfdCloseDay.DAY_25.getDay()).split(",");

			rangeStartDate = rangeDate[1];						
			
			itemStartDate = pfdBonusItem.getStartDate();			
		}
		
		//合約開始日期若大於該月開始日期，則開始計算日期為合約開始日期
		long lCheckStartDate = DateValueUtil.getInstance().getDateDiffDay(itemStartDate, rangeStartDate);
		
		if(lCheckStartDate > 0){
			startDate = rangeStartDate; 
		}else{
			startDate = itemStartDate;
		}
		
		return startDate;
	}
	
	private List<String> getCloseYearAndMonth(String startDate, EnumPfdBonusItem pfdBonusItem){
		// 取結算最後一天的年月份
		List<String> dateList = new ArrayList<String>();
		String endDate = "";
		
		if(EnumPfdBonusItem.EVERY_MONTH_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_MONTH_DEVELOP_BONUS.equals(pfdBonusItem)){	
			
			String[] rangeDate = DateValueUtil.getInstance().getMonthRangeDate(startDate).split(",");

			endDate = rangeDate[0];			
		}
		else if(EnumPfdBonusItem.EVERY_QUARTER_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_QUARTER_DEVELOP_BONUS.equals(pfdBonusItem)){
			
			String[] rangeDate = getQuarterRangeDate(startDate);
			
			endDate = rangeDate[0];
		}
		else if(EnumPfdBonusItem.EVERY_YEAR_BONUS.equals(pfdBonusItem) || 
				EnumPfdBonusItem.EVERY_YEAR_DEVELOP_BONUS.equals(pfdBonusItem)){
			
			String[] rangeDate = DateValueUtil.getInstance().getYearRangeDate(startDate).split(",");		
			
			endDate = rangeDate[0];	
		}
		else{			
			// 專案是依專案設定日期為依據
			endDate = pfdBonusItem.getEndDate();				
		}
		
		String closeYear = endDate.substring(0, 4);
		String closeMonth = endDate.substring(5, 7);
		
		dateList.add(closeYear);
		dateList.add(closeMonth);
		
		return dateList;
	}
	
	private String[] getQuarterRangeDate(String recordDateStr){
		//取得季初日期[1]與季末日期[0]
		String[] recordDateAry=recordDateStr.split("-");
		int month = Integer.parseInt(recordDateAry[1])-1;//月份從0開始
		
		Calendar calStart = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();
		SimpleDateFormat tempformatter = new SimpleDateFormat("yyyy-MM-dd");
		
		if (month >=0 && month<=2){
			calStart.set(Calendar.MONTH,0);
			calEnd.set(Calendar.MONTH,2);
		}else if(month >=3 && month<=5){
			calStart.set(Calendar.MONTH,3);
			calEnd.set(Calendar.MONTH,5);
		}else if(month >=6 && month<=8){
			calStart.set(Calendar.MONTH,6);
			calEnd.set(Calendar.MONTH,8);
		}else if(month >=9 && month<=11){
			calStart.set(Calendar.MONTH,9);
			calEnd.set(Calendar.MONTH,11);
		}
		
		calStart.set(Calendar.YEAR,Integer.parseInt(recordDateAry[0]));
		calStart.set(Calendar.DAY_OF_MONTH,calStart.getActualMinimum(Calendar.DAY_OF_MONTH));
		String startDate = tempformatter.format(calStart.getTime());
		
		calEnd.set(Calendar.YEAR,Integer.parseInt(recordDateAry[0]));
		calEnd.set(Calendar.DAY_OF_MONTH,calEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
		String endDate = tempformatter.format(calEnd.getTime());
		
		return new String [] {endDate,startDate};
	}

}
