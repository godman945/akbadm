package com.pchome.rmi.bonus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.enumerate.bonus.EnumCloseDay;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.soft.util.DateValueUtil;

@Transactional
public class PfdBonusProvider implements IPfdBonusProvider{

	private Log log = LogFactory.getLog(getClass().getName());
	DecimalFormat df3 = new DecimalFormat("###,###,###,###.##");
	private final String checkPerson = "謝元豪";
	private final String checkPersonTel = "02-27000898#2258";
	private final String fox = "02-27097828";
	private final String sentAddress = "106台北市大安區敦化南路二段105號12樓  Portal產品部-PChome聯播網經銷商小組收";
	private final String invoiceTitle = "網路家庭國際資訊股份有限公司";
	private final String invoiceTaxId = "16606102";

	private IPfdBonusRecordService pfdBonusRecordService;
	private IPfdBonusInvoiceService pfdBonusInvoiceService;
	private IPfdAccountService pfdAccountService;

	
	public Map<String,Object> pfdBonusBillMap(String pfdId, int year, int month) throws Exception{
		
		log.info(">>> pfdId = " + pfdId);
		log.info(">>> year = " + year);
		log.info(">>> month = " + month);
		
		Map<String,Object> map = new HashMap<String,Object>(); //報表資料
		
		//本期廣告費用 ：當月的廣告花費總和(後付)
		float totalAdClick = 0 ;
		//本期廣告費用：只算後付廣告費用，故要用原始沒合併的花費計算
		List<PfdBonusRecord> adClickBonusRecord= pfdBonusRecordService.findPfdTotalAdClick(pfdId, year, month);
		if(!adClickBonusRecord.isEmpty()){
			for (PfdBonusRecord pfdBonusRecord : adClickBonusRecord) { 
				//本期廣告費用 ：當月的廣告花費總和(後付)
				if ( (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), pfdBonusRecord.getBonusItem())) && (StringUtils.equals(EnumPfdAccountPayType.LATER.getPayType(), pfdBonusRecord.getPayType())) ){
					totalAdClick = totalAdClick + pfdBonusRecord.getNowClkPrice();
				}
			}
		}
		//本期廣告費用(後付)
		map.put("totalAdClick", totalAdClick);

		
		
		//前期未結廣告費用 ：前期未結的廣告花費總和(後付)
		float nonCloseTotalAdClick = 0 ;
		//前期未結廣告費用：只算後付廣告費用，故要用原始沒合併的花費計算
		List<PfdBonusRecord> nonCloseAdClickBonusRecord= pfdBonusRecordService.findPfdNonCloseTotalAdClick(pfdId, year, month);
		if(!nonCloseAdClickBonusRecord.isEmpty()){
			for (PfdBonusRecord nonClosePfdBonusRecord : nonCloseAdClickBonusRecord) { 
				//前期未結廣告費用 ：當月的廣告花費總和(後付)
				if ( (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), nonClosePfdBonusRecord.getBonusItem())) && (StringUtils.equals(EnumPfdAccountPayType.LATER.getPayType(), nonClosePfdBonusRecord.getPayType())) ){
					nonCloseTotalAdClick = nonCloseTotalAdClick + nonClosePfdBonusRecord.getNowClkPrice();
				}
			}
		}
		//前期未結廣告費用
		map.put("nonCloseTotalAdClick",nonCloseTotalAdClick);
		
		
		
		//本期佣金(只算月佣金)
		double monthBonusMoney = 0 ;
		//本期應付獎金(含季、年獎金)
		double quarterAndYearBonusMoney = 0 ;
		
		//本期佣金與本期應付獎金
		List<PfdBonusRecord> bonusRecordsOriginal = pfdBonusRecordService.findPfdBonusRecordsByPayment(pfdId, year, month);
		List<PfdBonusRecord> pfdBonusRecords = bonusRecordMergePayType(bonusRecordsOriginal);//當合約為混合付款方式(預付+後付)，將廣告花費合併再計算佣金獎金
		
		if(!pfdBonusRecords.isEmpty()){
			for (PfdBonusRecord pfdBonusRecord : pfdBonusRecords) { 
				//本期佣金(只算月佣金)
				if ( (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(),pfdBonusRecord.getBonusItem())) ){
					monthBonusMoney = monthBonusMoney + Math.round(pfdBonusRecord.getNowBonus());
				}
				//本期應付獎金(含季、年獎金)
				if ( (StringUtils.equals(EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType(),pfdBonusRecord.getBonusItem())) || (StringUtils.equals(EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType(),pfdBonusRecord.getBonusItem())) ){
					quarterAndYearBonusMoney = quarterAndYearBonusMoney + Math.round(pfdBonusRecord.getNowBonus());
				}
			}
		}
		
		//本期佣金(只算月佣金)
		map.put("bonusMoney", monthBonusMoney);
		//本期應付獎金(含季、年獎金)
		map.put("quarterAndYearBonusMoney", quarterAndYearBonusMoney);
		
		
		//取得經銷商資料
		Map<String,String> CustomerInfoMap = new HashMap<String,String>(); 
		CustomerInfoMap.put("customerInfoId", pfdId);
		List<PfdCustomerInfo> customerList = pfdAccountService.getPfdCustomerInfoByCondition(CustomerInfoMap);
		PfdCustomerInfo pfd = customerList.get(0);
		
		// 每月請款區間
		StringBuffer bonusCloseDate = new StringBuffer();
		bonusCloseDate.append(year).append("-");
		bonusCloseDate.append(month).append("-");
		bonusCloseDate.append(EnumCloseDay.CLOSE_DAY.getDay());		
		String[] rangeDate = DateValueUtil.getInstance().getMonthRangeDate(bonusCloseDate.toString()).split(",");
		StringBuffer checkRangDate = new StringBuffer();
		if(rangeDate.length > 1){
			checkRangDate.append(rangeDate[1]).append(" ~ ");	
			checkRangDate.append(rangeDate[0]);
		}else{
			log.error(" check rang date error!!");
		}
		
		// 請款日期
		String checkDate = DateValueUtil.getInstance().dateToString(DateValueUtil.getInstance().getDateForStartDateAddDay(bonusCloseDate.toString(), 1));
		map.put("checkDate", checkDate);
		
		// 請款區間
		map.put("checkRangDate", checkRangDate.toString());
		// 印表日期
		map.put("printDate", DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH));
		// 經銷商名稱
		map.put("pfdAccountTitle", pfd.getCompanyName());
		// 發票抬頭
		map.put("invoiceTitle", invoiceTitle);
		// Pchome發票統編
		map.put("invoiceTaxId", invoiceTaxId);
		// PFD統一編號
		map.put("companyTaxId", pfd.getCompanyTaxId());
		
		
		
		//前期未結佣(獎)金
		double nonCloseBonusMoney = 0 ;		
		List<PfdBonusRecord> nonCloseListOriginal = pfdBonusRecordService.findnonClosePfdBonusRecords(pfdId, year, month);
		List<PfdBonusRecord> nonCloseList = bonusRecordMergePayType(nonCloseListOriginal); //當合約為混合付款方式(預付+後付)，將廣告花費合併再計算佣金獎金
		
		if(!nonCloseList.isEmpty()){
			for (PfdBonusRecord nonCloseLists : nonCloseList) {
				//前期未結(含前期未請款之月佣金+季獎金+年獎金)
				nonCloseBonusMoney = nonCloseBonusMoney +  Math.round(nonCloseLists.getNowBonus());
			}
		}
		//前期未結佣(獎)金
		map.put("nonCloseBonusMoney", nonCloseBonusMoney);
		
		//應收廣告費用
		double totalAdNonCloseAdClick = 0;
		totalAdNonCloseAdClick = totalAdClick + nonCloseTotalAdClick;
		map.put("totalAdNonCloseAdClick", totalAdNonCloseAdClick);
		//總廣告花費營業稅
		double totalAdClickTax = (double) Math.round((totalAdNonCloseAdClick*0.05));
		map.put("totalAdClickTax", totalAdClickTax);
		// 總廣告花費總金額(含稅)
		double totalAdClickSum =  (double) (totalAdNonCloseAdClick + totalAdClickTax);
		map.put("totalAdClickSum", totalAdClickSum);
		
		
		//應付佣金( 本期佣金 + 本期應付獎金  + 前期未結 )
		double totalPayMoney =  monthBonusMoney + quarterAndYearBonusMoney + nonCloseBonusMoney ;
		map.put("totalPayMoney", totalPayMoney);
		// 應付佣金稅額
		double totalPayTax = (double) Math.round((totalPayMoney * 0.05));
		map.put("totalPayTax", totalPayTax);
		// 應付佣金總金額(含稅)
		double totalPayMoneySum = (double) (totalPayMoney + totalPayTax);
		map.put("totalPayMoneySum", totalPayMoneySum);		
		
		
		
		//本期+前期未結之明細
		List<PfdBonusRecord> bonusRecordsInvoiceDetailOriginal = pfdBonusRecordService.findPfdBonusRecordsInvoiceDetail(pfdId, year, month);
		List<PfdBonusRecord> bonusRecordsInvoiceDetail = bonusRecordMergePayType(bonusRecordsInvoiceDetailOriginal); //當合約為混合付款方式(預付+後付)，將廣告花費合併再計算佣金獎金

		//本期+前期未結之明細
		List<PfdBonusDetailVo> vos = pfdBonusRecordToVo(bonusRecordsInvoiceDetail);
		map.put("pfdBonusDetailVos", vos);
		

		//		double allAdClick = 0;
//		double allPayMoney = 0;
//		if(vos != null){
//			for(PfdBonusDetailVo vo:vos){
//				allAdClick += vo.getNowClkPriceSum();
//				allPayMoney += vo.getNowBonusSum();
//			}
//		}
//		map.put("allAdClick", allAdClick);
//		map.put("allPayMoney", allPayMoney);
		
		
		// 經銷商帳戶編號
		map.put("pfdCustomerInfoId", pfd.getCustomerInfoId());
		// 寄送地址
		map.put("sentAddress", sentAddress);
		// 請款人員
		map.put("checkPerson", checkPerson);
		// 請款人員聯絡方式
		map.put("checkPersonTel", checkPersonTel);
		// 傳真
		map.put("fox", fox);
		// 統一編號
		map.put("companyTaxId", pfd.getCompanyTaxId());	
				
		return map;
	}
	
	//當合約為混合付款方式(預付+後付)，將廣告花費合併再計算佣金獎金
	public List<PfdBonusRecord> bonusRecordMergePayType(List<PfdBonusRecord> pfdBonusRecords)throws Exception{
		
		Map<String,PfdBonusRecord> processedBonusRecordsLinkmap = new LinkedHashMap<String,PfdBonusRecord>();
		List<PfdBonusRecord> mergedBonusRecords = new ArrayList<PfdBonusRecord>();
			
			for (PfdBonusRecord pfdBonusRecord : pfdBonusRecords) {
				String contractIdByBonusItem = "";
				contractIdByBonusItem = pfdBonusRecord.getPfdContract().getPfdContractId()+"_"+pfdBonusRecord.getBonusItem()+"_"+pfdBonusRecord.getCloseYear()+"_"+pfdBonusRecord.getCloseMonth();
				PfdBonusRecord pfdBonusRecordsBean = new PfdBonusRecord();

				if (processedBonusRecordsLinkmap.get(contractIdByBonusItem) == null){
					
					pfdBonusRecordsBean.setPfdContract(pfdBonusRecord.getPfdContract());
					pfdBonusRecordsBean.setPayType(pfdBonusRecord.getPayType());
					pfdBonusRecordsBean.setYear(pfdBonusRecord.getYear());
					pfdBonusRecordsBean.setMonth(pfdBonusRecord.getMonth());
					pfdBonusRecordsBean.setQuarter(pfdBonusRecord.getQuarter());
					pfdBonusRecordsBean.setCloseYear(pfdBonusRecord.getCloseYear());
					pfdBonusRecordsBean.setCloseMonth(pfdBonusRecord.getCloseMonth());
					pfdBonusRecordsBean.setBonusItem(pfdBonusRecord.getBonusItem());
					pfdBonusRecordsBean.setBonusName(pfdBonusRecord.getBonusName());
					pfdBonusRecordsBean.setStartDate(pfdBonusRecord.getStartDate());
					pfdBonusRecordsBean.setEndDate(pfdBonusRecord.getEndDate());
					pfdBonusRecordsBean.setNowClkPrice(pfdBonusRecord.getNowClkPrice());
					pfdBonusRecordsBean.setNextClkPrice(pfdBonusRecord.getNextClkPrice());
					pfdBonusRecordsBean.setNowPercent(pfdBonusRecord.getNowPercent());
					pfdBonusRecordsBean.setNextPercent(pfdBonusRecord.getNextPercent());
					pfdBonusRecordsBean.setDeductBonus(pfdBonusRecord.getDeductBonus());
					pfdBonusRecordsBean.setNowAmount(pfdBonusRecord.getNowAmount());
					pfdBonusRecordsBean.setNextAmount(pfdBonusRecord.getNextAmount());
					pfdBonusRecordsBean.setNowBonus(pfdBonusRecord.getNowBonus());
					pfdBonusRecordsBean.setNextBonus(pfdBonusRecord.getNextBonus());
					pfdBonusRecordsBean.setReportActoin(pfdBonusRecord.getReportActoin());
					pfdBonusRecordsBean.setBonusNote(pfdBonusRecord.getBonusNote());
					pfdBonusRecordsBean.setCreateDate(pfdBonusRecord.getCreateDate());
					pfdBonusRecordsBean.setUpdateDate(pfdBonusRecord.getUpdateDate());
					
					processedBonusRecordsLinkmap.put(contractIdByBonusItem, pfdBonusRecordsBean);
					
				}else{
					pfdBonusRecordsBean = processedBonusRecordsLinkmap.get(contractIdByBonusItem);
					pfdBonusRecordsBean.setNowClkPrice(pfdBonusRecordsBean.getNowClkPrice() + pfdBonusRecord.getNowClkPrice());
					pfdBonusRecordsBean.setNowBonus(pfdBonusRecordsBean.getNowBonus() + pfdBonusRecord.getNowBonus());
					
					processedBonusRecordsLinkmap.put(contractIdByBonusItem, pfdBonusRecordsBean);
				}
			}
			
		  pfdBonusRecords = null;
		  
		  for(Entry<String,PfdBonusRecord> entry:processedBonusRecordsLinkmap.entrySet()){
			  mergedBonusRecords.add(entry.getValue());
		  }
		  
		return mergedBonusRecords;
	}
	
	
	
	public List<PfdBonusDetailVo> pfdBonusRecordToVo(List<PfdBonusRecord> pfdBonusRecordsInvoiceDetail){
	
	List<PfdBonusDetailVo> vos = null;
	
	if(!pfdBonusRecordsInvoiceDetail.isEmpty()){
		
		vos = new ArrayList<PfdBonusDetailVo>();
		
		for(PfdBonusRecord record:pfdBonusRecordsInvoiceDetail){
			
			PfdBonusDetailVo vo = new PfdBonusDetailVo();
			
			DecimalFormat df = new DecimalFormat("###,###");
			
			StringBuffer desc = new StringBuffer();
			
			vo.setContractId(record.getPfdContract().getPfdContractId());
			vo.setBonusItemName(record.getBonusName().replace("-", "-<br>"));
			vo.setCount(1);
			double nowClkPrice = record.getNowClkPrice();
			double nowClkTax = Math.round((double) (nowClkPrice*0.05));
			double nowBonus = Math.round(record.getNowBonus());
			double nowBonusTax = Math.round((double) (nowBonus*0.05));
			vo.setNowClkPrice(nowClkPrice);
			vo.setNowClkTax(nowClkTax);
			vo.setNowClkPriceSum(nowClkPrice + nowClkTax);
			vo.setNowBonus(nowBonus);
			vo.setNowBonusTax(nowBonusTax);
			vo.setNowBonusSum(nowBonus + nowBonusTax);
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType())){
				desc.append(record.getYear() + "年度" + record.getMonth() + "月<br>廣告費用：$");
				desc.append(df.format(vo.getNowClkPrice()));
				desc.append("，獎金 ");
				desc.append(df3.format(record.getNowPercent()));
				desc.append("%");
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType())){
				
				double balance = record.getNextClkPrice() - record.getNowClkPrice();
				
				desc.append(record.getYear() + "年度第" + record.getQuarter() + "季<br>廣告費用：$");
				desc.append(df.format(vo.getNowClkPrice()));
				desc.append("，獎金 ");
				desc.append(df3.format(record.getNowPercent()));
				desc.append("%");
				/*desc.append("；還差＄ ");
				desc.append(df.format(balance));
				desc.append("，獎金＄ ");
				desc.append(doubleFormat.format(record.getNextPercent()));
				desc.append(" ％");*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType())){
				
				double balance = record.getNextClkPrice() - record.getNowClkPrice();
				
				desc.append(record.getYear() + "年度<br>廣告費用：$");
				desc.append(df.format(vo.getNowClkPrice()));
				desc.append("，獎金");
				desc.append(df3.format(record.getNowPercent()));
				desc.append("%");
				/*desc.append("；還差＄ ");
				desc.append(df.format(balance));
				desc.append("，獎金＄ ");
				desc.append(doubleFormat.format(record.getNextPercent()));
				desc.append(" ％");*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_MONTH_DEVELOP_BONUS.getItemType())){
				
				int balance = record.getNextAmount() - record.getNowAmount();
				
				desc.append(record.getYear() + "年度" + record.getMonth() + "月累計開發數： ");
				desc.append(record.getNowAmount());
				desc.append(" 間 ");
				desc.append("，獎金$");
				desc.append(df.format(vo.getNowBonus()));
				/*desc.append("；還差 ");
				desc.append(df.format(balance));
				desc.append(" 間");
				desc.append("，獎金＄ ");
				desc.append(df.format(record.getNextBonus()));*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_QUARTER_DEVELOP_BONUS.getItemType())){
				
				int balance = record.getNextAmount() - record.getNowAmount();
				
				desc.append(record.getYear() + "年度第" + record.getQuarter() + "季累計開發數： ");
				desc.append(record.getNowAmount());
				desc.append(" 間 ");
				desc.append("，獎金$");
				desc.append(df.format(vo.getNowBonus()));
				/*desc.append("；還差 ");
				desc.append(df.format(balance));
				desc.append(" 間");
				desc.append("，獎金＄ ");
				desc.append(df.format(record.getNextBonus()));*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.EVERY_YEAR_DEVELOP_BONUS.getItemType())){
				
				int balance = record.getNextAmount() - record.getNowAmount();
				
				desc.append(record.getYear() + "年度累計開發數： ");
				desc.append(record.getNowAmount());
				desc.append(" 間 ");
				desc.append("，獎金$");
				desc.append(df.format(vo.getNowBonus()));
				/*desc.append("；還差 ");
				desc.append(df.format(balance));
				desc.append(" 間");
				desc.append("，獎金＄ ");
				desc.append(df.format(record.getNextBonus()));*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			if(record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_A.getItemType()) ||
					record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_B.getItemType()) ||
					record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_C.getItemType()) ||
					record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_D.getItemType()) ||
					record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_E.getItemType()) ||
					record.getBonusItem().equals(EnumPfdBonusItem.CASE_BONUS_YEAR_END_SALE_F.getItemType())){
				
				double balance = record.getNextClkPrice() - record.getNowClkPrice();
				
				desc.append("專案獎金<br>廣告費用：$");
				desc.append(df.format(vo.getNowClkPrice()));
				desc.append("，獎金$");
				desc.append(record);
				desc.append("%");
				/*desc.append("；還差＄ ");
				desc.append(df.format(balance));
				desc.append("，獎金＄ ");
				desc.append(doubleFormat.format(record.getNextPercent()));
				desc.append(" ％");*/
				
				vo.setBonusDetailDesc(desc.toString());
			}
			
			vos.add(vo);
		}
	}
	return vos;
}
	
//	private Map<String,Object> getInitMap(){
//		
//		// 月獎金
//		//this.monthBonusDetailVo();
//		
//		// 季獎金
//		//this.quarterBonusDetailVo();
//		
//		// 年獎金
//		//this.yearBonusDetailVo();
//		
//		// 開發獎金
//		//this.developBonusDetailVo();
//		
//		// 專案獎金
//		//this.caseBonusDetailVo();
//		
//		return map;
//	}
	

	/*
	public void monthBonusDetailVo(){
		// 月獎金
		float totalMonthBonus = 0;
		List<BonusDetailVo> vos = pfdBonusReportService.findBonusReportToVo(pfdId, year, month, EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), payType);
		
		if(!vos.isEmpty()){
			
		}
		map.put("monthBonusDetailVo", vos);
		for(BonusDetailVo vo:vos){
			totalMonthBonus += vo.getBonusMoney();
		}
		map.put("totalMonthBonus", totalMonthBonus);
	}
	
	public void quarterBonusDetailVo(){
		// 季獎金
		
		List<BonusDetailVo> vos = pfdBonusReportService.findBonusReportToVo(pfdId, year, month, EnumPfdBonusItem.EVERY_QUARTER_BONUS.getItemType(), payType);
		
		if(!vos.isEmpty()){
			float totalQuarterBonus = 0;
			map.put("quarterBonusDetailVo", vos);
			for(BonusDetailVo vo:vos){
				totalQuarterBonus += vo.getBonusMoney();
			}
			map.put("totalQuarterBonus", totalQuarterBonus);
		}
	}
	
	public void yearBonusDetailVo(){
		// 年獎金
		
		List<BonusDetailVo> vos = pfdBonusReportService.findBonusReportToVo(pfdId, year, month, EnumPfdBonusItem.EVERY_YEAR_BONUS.getItemType(), payType);
		
		if(!vos.isEmpty()){
			float totalYearBonus = 0;
			map.put("yearBonusDetailVo", vos);
			for(BonusDetailVo vo:vos){
				totalYearBonus += vo.getBonusMoney();
			}
			map.put("totalYearBonus", totalYearBonus);
		}
	}
	
	public void developBonusDetailVo(){
		// 開發獎金
		
		List<DevelopBonusDetailVo> vos = pfdDevelopBonusReportService.findDevelopBonusReportToVo(pfdId, year, month, payType);				
		if(!vos.isEmpty()){
			float totalDevelopBonus = 0;
			map.put("developBonusDetailVo", vos);
			for(DevelopBonusDetailVo vo:vos){
				totalDevelopBonus += vo.getBonusMoney();
			}
			map.put("totalDevelopBonus", totalDevelopBonus);
		}
	}
	
	public void caseBonusDetailVo(){
		// 專案獎金
		
		List<CaseBonusDetailVo> vos = pfdCaseBonusReportService.findCaseBonusReportToVo(pfdId, year, month, payType);	
		
		if(!vos.isEmpty()){
			float totalCaseBonus = 0;
			map.put("caseBonusDetailVo", vos);
			for(CaseBonusDetailVo vo:vos){
				totalCaseBonus += vo.getBonusMoney();
			}
			map.put("totalCaseBonus", totalCaseBonus);		
		}
	}
	*/
	public void setPfdBonusRecordService(
			IPfdBonusRecordService pfdBonusRecordService) {
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setPfdBonusInvoiceService(
			IPfdBonusInvoiceService pfdBonusInvoiceService) {
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}
	
}
