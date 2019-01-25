package com.pchome.akbadm.struts2.ajax.pfd.bonus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
//import org.apache.jasper.tagplugins.jstl.core.ForEach;

import com.pchome.akbadm.db.pojo.PfdBoard;
import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.pojo.PfdBonusRecord;
import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.pojo.PfdUser;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.pfd.board.IPfdBoardService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.db.service.pfd.user.IPfdUserService;
import com.pchome.akbadm.db.service.recognize.IAdmRecognizeDetailService;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.bonus.EnumInvoicPayType;
import com.pchome.enumerate.pfd.EnumPfdPrivilege;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusBill;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.enumerate.privilege.EnumPrivilegeModel;
import com.pchome.rmi.board.EnumPfdBoardType;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class BonusInvoiceAjax extends BaseCookieAction{

	private IPfdBonusInvoiceService pfdBonusInvoiceService;
	private IPfdCustomerInfoService pfdCustomerInfoService;
	private IPfdBoardService pfdBoardService;
	private IPfdBonusRecordService pfdBonusRecordService;
	private IPfdUserService pfdUserService;
	private IAdmRecognizeDetailService admRecognizeDetailService;
	
	
	private String invoiceStrDate;
	private String invoiceEndDate;
	private String pfdCustomerInfoId;
	private String bonusInvoiceId;
	private String balancePfdYearMonth;	//pfd沖帳年月
	private int closeYear;
	private int closeMonth;
	
	
	private List<PfdBonusInvoiceVO> pfdBonusInvoices;
	private PfdBonusInvoice pfdBonusInvoice;
	private List<PfdBonusRecord> pfdBonusRecord;
	
	private EnumPfdAccountPayType[] enumPfdAccountPayType = EnumPfdAccountPayType.values();				// 帳戶付款方式
	private EnumInvoicPayType[] enumInvoicPayType = EnumInvoicPayType.values();							// 發票付款方式
	
	private String financeInvoiceSno;					// 財務發票號碼
	private String financeInvoiceDate;					// 財務發票日期
	private float financeInvoiceMoney;					// 財務發票金額
	private String financePayDate;						// 財務付款日期
	private String pfdInvoiceSno;						// 廣告商發票號碼
	private String pfdInvoiceDate;						// 廣告商發票日期
	private float pfdInvoiceMoney;						// 廣告商發票金額
	private String pfdPayCategory;						// 廣告商付款方式
	private String pfdCheckSno;							// 廣告商支票號碼
	private String pfdCheckCloseDate;					// 廣告商支票到期日期
	
	private String bonusType;							// 獎金付款方式
	private float debitMoney;							// 廣告商折讓金額
	private String debitDate;							// 廣告商折讓日期
	private String billStatus;							// 請款進度
	private String billNote;							// 付款失敗原因
	
	private double totalBonus;							//當月的月、季、年應付獎金(不含前期未結金額)
	private double totalAdClkPrice;						//當月的後付廣告花費
	
	private Map<String,String> pfdBnousBillMap;
	
	public String searchBonusInvoiceAjax() throws Exception{
		
		try{
			log.info(">>> invoiceStrDate = "+invoiceStrDate);
			log.info(">>> invoiceEndDate = "+invoiceEndDate);
			log.info(">>> pfdCustomerInfoId = "+pfdCustomerInfoId);
			
			
			String strYear = invoiceStrDate.split("-")[0];
			String strMonth = invoiceStrDate.split("-")[1];
			String endYear = invoiceEndDate.split("-")[0];
			String endMonth = invoiceEndDate.split("-")[1];
			
			String strDate = strYear+strMonth;
			String endDate = endYear+endMonth;
			
			log.info(">>> strDate = "+strDate);
			log.info(">>> endDate = "+endDate);
			
			pfdBonusInvoices = new ArrayList<PfdBonusInvoiceVO>();
			Map<String,PfdBonusInvoiceVO> closeMap = new HashMap<String,PfdBonusInvoiceVO>();
	
			//撈出查詢區間資料
			List<PfdBonusRecordInvoiceVO> dataList = pfdBonusInvoiceService.findPfdBonusInvoicesByPayment(pfdCustomerInfoId, strDate, endDate);
			
			//如為預付+後付合約，先合併資料
			List<PfdBonusRecordInvoiceVO>mergeDataList = bonusRecordInvoiceMergePayType(dataList);
			
			//計算經銷商每月的佣獎金
			Map<String,PfdBonusInvoiceVO> mergeBonusRecordInvoiceByMonth = calculateBonusByMonth(mergeDataList);
			
			//取得合約作廢或到期最後一筆invoice的年度月份(該pfd帳戶目前無任何生效中的合約)
			closeMap = pfdBonusInvoiceService.getCloseTheLastInvoice();
			
			//依據closeMap塞入對帳單狀態
			if(!mergeBonusRecordInvoiceByMonth.isEmpty()){
				  for(Entry<String, PfdBonusInvoiceVO> data : mergeBonusRecordInvoiceByMonth.entrySet()){
					
					PfdBonusInvoiceVO vo = new PfdBonusInvoiceVO();
					
					vo.setId(data.getValue().getId());
					vo.setPayType(data.getValue().getPayType());
					vo.setRecordDate(data.getValue().getRecordDate());
					vo.setCloseYear(data.getValue().getCloseYear());
					vo.setCloseMonth(data.getValue().getCloseMonth());
					vo.setTotalAdClkPrice(data.getValue().getTotalAdClkPrice());
					vo.setTotalBonus(data.getValue().getTotalBonus());
					vo.setBonusType(data.getValue().getBonusType());
					vo.setDownload(data.getValue().getDownload());
					vo.setBillStatus(data.getValue().getBillStatus());
					vo.setBillNote(data.getValue().getBillNote());
					vo.setFinanceInvoiceSno(data.getValue().getFinanceInvoiceSno());
					vo.setFinanceInvoiceDate(data.getValue().getFinanceInvoiceDate());
					vo.setFinanceInvoiceMoney(data.getValue().getFinanceInvoiceMoney());
					vo.setFinancePayDate(data.getValue().getFinancePayDate());
					vo.setPfdInvoiceSno(data.getValue().getPfdInvoiceSno());
					vo.setPfdInvoiceDate(data.getValue().getPfdInvoiceDate());
					vo.setPfdInvoiceMoney(data.getValue().getPfdInvoiceMoney());
					vo.setPfdPayCategory(data.getValue().getPfdPayCategory());
					vo.setPfdCheckSno(data.getValue().getPfdCheckSno());
					vo.setPfdCheckCloseDate(data.getValue().getPfdCheckCloseDate());
					vo.setDebitMoney(data.getValue().getDebitMoney());
					vo.setDebitDate(data.getValue().getDebitDate());
					vo.setBalanceDate(data.getValue().getBalanceDate());
					vo.setPayId(data.getValue().getPayId());
					vo.setCreateDate(data.getValue().getCreateDate());
					vo.setUpdateDate(data.getValue().getUpdateDate());					
					vo.setPfdCustomerInfoId(data.getValue().getPfdCustomerInfoId());
					vo.setCompanyName(data.getValue().getCompanyName());
					vo.setCompanyTaxId(data.getValue().getCompanyTaxId());
					vo.setLaterAdClkPrice(data.getValue().getLaterAdClkPrice()); //後付應付廣告金
					vo.setBalanceFlag(data.getValue().getLaterAdClkPrice() <= 0 ? "1" : "2");	//如果應付廣告金為0元則為預付，超過0元為後付要沖帳
					
					String thisMonth = "N";
					
					//當下日期減1個月，前1個月才能請款
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.add( Calendar.MONTH, -1 );
					calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
					//塞入資料月份
					Date recordDate = data.getValue().getRecordDate();
					Calendar invoiceCal = Calendar.getInstance();
					invoiceCal.setTime(recordDate);
					
					//如果資料年月為當下前1個月，即可請款
					if(calendar.get(Calendar.YEAR) == invoiceCal.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == invoiceCal.get(Calendar.MONTH)){
						thisMonth = "Y";
					}						
					
					
					//如果已無生效合約，即打開該PFD最後一筆可請款的年月資料
					if(closeMap.get(data.getValue().getPfdCustomerInfoId()) != null){
						PfdBonusInvoiceVO closeVo = new PfdBonusInvoiceVO();
						closeVo = closeMap.get(data.getValue().getPfdCustomerInfoId());
						//如果該帳戶沒有生效中的合約，則最後一筆明細設為可列印
						if(closeVo.getCloseYear() == vo.getCloseYear() && closeVo.getCloseMonth() == vo.getCloseMonth()){
							//最後一筆明細也要下個月到才可列印
							if( (invoiceCal.get(Calendar.YEAR)<=calendar.get(Calendar.YEAR)) && (invoiceCal.get(Calendar.MONTH)<=calendar.get(Calendar.MONTH)) ){
								thisMonth = "Y";
							}
						}
					}
					
					//如果為上個月或是無生效合約且該筆為最後一筆資料，即可下載PDF
					vo.setThisMonth(thisMonth);
					
					
					//是否到請款時間
					Calendar currentDate = Calendar.getInstance();
					currentDate.setTime(new Date());
					//java.util.Calendar之get MONTH會少1個月(Calendar的0月等於1月)
					if (vo.getCloseYear() > currentDate.get(Calendar.YEAR) || (vo.getCloseYear() == currentDate.get(Calendar.YEAR)&&vo.getCloseMonth()-1 >=currentDate.get(Calendar.MONTH)) ){
						vo.setDownload("N");	//尚未到請款時間
					}else{
						vo.setDownload("Y");	//已過請款時間
					}
					
					//加總該月的禮金+回饋金花費
					float pfdFreeMoney = 0;
					pfdFreeMoney = pfdFreeMoneyByMonth(vo.getPfdCustomerInfoId(),vo.getCloseYear(),vo.getCloseMonth()-1);
					vo.setPfdFreeMoney(pfdFreeMoney);
					
					
					pfdBonusInvoices.add(vo);
				}
			}
			
			log.info(" pfdBonusInvoices: "+pfdBonusInvoices);
		
		}catch (Exception e){
			log.error("Error: "+e);
		}
		
		return SUCCESS;
	}
	
	
	//使用merge後的資料做每月獎金計算
	public Map<String,PfdBonusInvoiceVO> calculateBonusByMonth(List<PfdBonusRecordInvoiceVO>mergeDataList)throws Exception{
		
		Map<String,PfdBonusInvoiceVO> mergeBonusRecordInvoiceByMonth = new LinkedHashMap<String,PfdBonusInvoiceVO>();
		
		if(!mergeDataList.isEmpty()){
			for(PfdBonusRecordInvoiceVO data:mergeDataList){
				
				data.setNowBonus(Math.round(data.getNowBonus()));//bonus的原始資料先四捨五入
				
				//如果不是月佣金，花費歸0
				if (!StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), data.getBonusItem())){
					data.setNowClkPrice(0);
				}
				
				String bonusKey="";
				bonusKey = data.getPfdCustomerInfoId() +"_"+ data.getCloseYear()+"_"+data.getCloseMonth();
				
				PfdBonusInvoiceVO bonusInvoice = new PfdBonusInvoiceVO();
				
				if (mergeBonusRecordInvoiceByMonth.get(bonusKey)==null){
					
					//判斷為月佣金且為後付，計算其廣費花費，供沖帳使用(balanceFlag)，有後付廣告金才做沖帳
					if (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), data.getBonusItem())){
						bonusInvoice.setLaterAdClkPrice(data.getLaterAdClkPrice());
					}
					
					bonusInvoice.setId(data.getId());
					bonusInvoice.setPayType(data.getPfpPayType()); //塞入最後合約類型(預付、後付、預付+後付)
					bonusInvoice.setRecordDate(data.getRecordDate());
					bonusInvoice.setCloseYear(data.getCloseYear());
					bonusInvoice.setCloseMonth(data.getCloseMonth());
					bonusInvoice.setBonusType(data.getBonusType());
					bonusInvoice.setDownload(data.getDownload());
					bonusInvoice.setBillStatus(data.getBillStatus());
					bonusInvoice.setBillNote(data.getBillNote());
					bonusInvoice.setFinanceInvoiceSno(data.getFinanceInvoiceSno());
					bonusInvoice.setFinanceInvoiceDate(data.getFinanceInvoiceDate());
					bonusInvoice.setFinanceInvoiceMoney(data.getFinanceInvoiceMoney());
					bonusInvoice.setFinancePayDate(data.getFinancePayDate());
					bonusInvoice.setPfdInvoiceSno(data.getPfdInvoiceSno());
					bonusInvoice.setPfdInvoiceDate(data.getPfdInvoiceDate());
					bonusInvoice.setPfdInvoiceMoney(data.getPfdInvoiceMoney());
					bonusInvoice.setPfdPayCategory(data.getPfdPayCategory());
					bonusInvoice.setPfdCheckSno(data.getPfdCheckSno());
					bonusInvoice.setPfdCheckCloseDate(data.getPfdCheckCloseDate());
					bonusInvoice.setDebitMoney(data.getDebitMoney());
					bonusInvoice.setDebitDate(data.getDebitDate());
					bonusInvoice.setBalanceDate(data.getBalanceDate());
					bonusInvoice.setPayId(data.getPayId());
					bonusInvoice.setCreateDate(data.getCreateDate());
					bonusInvoice.setUpdateDate(data.getUpdateDate());
					bonusInvoice.setTotalAdClkPrice(data.getNowClkPrice());
					bonusInvoice.setTotalBonus(data.getNowBonus());
					bonusInvoice.setPfdCustomerInfoId(data.getPfdCustomerInfoId());
					bonusInvoice.setCompanyName(data.getCompanyName());
					bonusInvoice.setCompanyTaxId(data.getCompanyTaxId());
					
					mergeBonusRecordInvoiceByMonth.put(bonusKey, bonusInvoice);
					
				}else{
					
					bonusInvoice = mergeBonusRecordInvoiceByMonth.get(bonusKey);
					bonusInvoice.setTotalAdClkPrice(bonusInvoice.getTotalAdClkPrice() + data.getNowClkPrice());
					bonusInvoice.setTotalBonus(bonusInvoice.getTotalBonus() + data.getNowBonus());
					
					//判斷為月佣金且為後付，計算其廣費花費，供沖帳使用(balanceFlag)，有後付廣告金才做沖帳
					if (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), data.getBonusItem())){
						bonusInvoice.setLaterAdClkPrice(bonusInvoice.getLaterAdClkPrice()+data.getLaterAdClkPrice());
					}
					
					mergeBonusRecordInvoiceByMonth.put(bonusKey, bonusInvoice);
				}
			}
		}
		
		return mergeBonusRecordInvoiceByMonth;
	}
	
	//當合約為混合付款方式(預付+後付)，將廣告花費合併再計算佣金獎金
	public List<PfdBonusRecordInvoiceVO> bonusRecordInvoiceMergePayType(List<PfdBonusRecordInvoiceVO> pfdBonusRecords)throws Exception{
		
		Map<String,PfdBonusRecordInvoiceVO> processedBonusRecordsLinkmap = new LinkedHashMap<String,PfdBonusRecordInvoiceVO>();
		List<PfdBonusRecordInvoiceVO> mergedBonusRecords = new ArrayList<PfdBonusRecordInvoiceVO>();
			
			for (PfdBonusRecordInvoiceVO data : pfdBonusRecords) {
				String contractIdByBonusItem = "";
				contractIdByBonusItem = data.getPfdContractId()+"_"+data.getBonusItem()+"_"+data.getCloseYear()+"_"+data.getCloseMonth();
				PfdBonusRecordInvoiceVO pfdBonusRecordsBean = new PfdBonusRecordInvoiceVO();

				if (processedBonusRecordsLinkmap.get(contractIdByBonusItem) == null){
					
					//判斷為月佣金且為後付，計算其廣費花費，供沖帳使用(balanceFlag)，有後付廣告金才做沖帳
					if (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), data.getBonusItem()) && StringUtils.equals(EnumPfdAccountPayType.LATER.getPayType(), data.getPayType())){
						pfdBonusRecordsBean.setLaterAdClkPrice(data.getNowClkPrice());
					}
					
					pfdBonusRecordsBean.setId(data.getId());
					pfdBonusRecordsBean.setPayType(data.getPayType());
					pfdBonusRecordsBean.setBonusItem(data.getBonusItem());
					pfdBonusRecordsBean.setQuarter(data.getQuarter());
					pfdBonusRecordsBean.setNowClkPrice(data.getNowClkPrice());
					pfdBonusRecordsBean.setNowBonus(data.getNowBonus());
					pfdBonusRecordsBean.setRecordDate(data.getRecordDate());
					pfdBonusRecordsBean.setCloseYear(data.getCloseYear());
					pfdBonusRecordsBean.setCloseMonth(data.getCloseMonth());
					pfdBonusRecordsBean.setBonusType(data.getBonusType());
					pfdBonusRecordsBean.setDownload(data.getDownload());
					pfdBonusRecordsBean.setBillStatus(data.getBillStatus());
					pfdBonusRecordsBean.setBillNote(data.getBillNote());
					pfdBonusRecordsBean.setFinanceInvoiceSno(data.getFinanceInvoiceSno());
					pfdBonusRecordsBean.setFinanceInvoiceDate(data.getFinanceInvoiceDate());
					pfdBonusRecordsBean.setFinanceInvoiceMoney(data.getFinanceInvoiceMoney());
					pfdBonusRecordsBean.setFinancePayDate(data.getFinancePayDate());
					pfdBonusRecordsBean.setPfdInvoiceSno(data.getPfdInvoiceSno());
					pfdBonusRecordsBean.setPfdInvoiceDate(data.getPfdInvoiceDate());
					pfdBonusRecordsBean.setPfdInvoiceMoney(data.getPfdInvoiceMoney());
					pfdBonusRecordsBean.setPfdPayCategory(data.getPfdPayCategory());
					pfdBonusRecordsBean.setPfdCheckSno(data.getPfdCheckSno());
					pfdBonusRecordsBean.setPfdCheckCloseDate(data.getPfdCheckCloseDate());
					pfdBonusRecordsBean.setDebitMoney(data.getDebitMoney());
					pfdBonusRecordsBean.setDebitDate(data.getDebitDate());
					pfdBonusRecordsBean.setBalanceDate(data.getBalanceDate());
					pfdBonusRecordsBean.setPayId(data.getPayId());
					pfdBonusRecordsBean.setCreateDate(data.getCreateDate());
					pfdBonusRecordsBean.setUpdateDate(data.getUpdateDate());
					pfdBonusRecordsBean.setPfdContractId(data.getPfdContractId());
					pfdBonusRecordsBean.setPfpPayType(data.getPfpPayType());
					pfdBonusRecordsBean.setPfdCustomerInfoId(data.getPfdCustomerInfoId());
					pfdBonusRecordsBean.setCompanyName(data.getCompanyName());
					pfdBonusRecordsBean.setCompanyTaxId(data.getCompanyTaxId());
					
					processedBonusRecordsLinkmap.put(contractIdByBonusItem, pfdBonusRecordsBean);
					
				}else{
					pfdBonusRecordsBean = processedBonusRecordsLinkmap.get(contractIdByBonusItem);
					
					//判斷為月佣金且為後付，計算其廣費花費，供沖帳使用(balanceFlag)，有後付廣告金才做沖帳
					if (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), data.getBonusItem()) && StringUtils.equals(EnumPfdAccountPayType.LATER.getPayType(), data.getPayType())){
						pfdBonusRecordsBean.setLaterAdClkPrice(pfdBonusRecordsBean.getLaterAdClkPrice() + data.getNowClkPrice());
					}
					
					pfdBonusRecordsBean.setNowClkPrice(pfdBonusRecordsBean.getNowClkPrice() + data.getNowClkPrice());
					pfdBonusRecordsBean.setNowBonus(pfdBonusRecordsBean.getNowBonus() + data.getNowBonus());
					
					processedBonusRecordsLinkmap.put(contractIdByBonusItem, pfdBonusRecordsBean);
				}
			}
			
		  pfdBonusRecords = null;
		  
		  for(Entry<String,PfdBonusRecordInvoiceVO> entry:processedBonusRecordsLinkmap.entrySet()){
			  mergedBonusRecords.add(entry.getValue());
		  }
		  
		return mergedBonusRecords;
	}
	
	
	public String modifyBonusInvoiceAjax() {
		try{
			log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
			log.info(">>> closeYear = " + closeYear);
			log.info(">>> closeMonth = " + closeMonth);
			
			//本期廣告費用 ：當月的廣告花費總和(後付)
			float totalAdClick = 0 ;
			//本期廣告費用：只算後付廣告費用，故要用原始沒合併的花費計算
			List<PfdBonusRecord> adClickBonusRecord= pfdBonusRecordService.findPfdInvoiceTotalAdClick(pfdCustomerInfoId, closeYear, closeMonth);
			if(!adClickBonusRecord.isEmpty()){
				for (PfdBonusRecord pfdBonusRecord : adClickBonusRecord) { 
					//本期廣告費用 ：當月的廣告花費總和(後付)
					if ( (StringUtils.equals(EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), pfdBonusRecord.getBonusItem())) && (StringUtils.equals(EnumPfdAccountPayType.LATER.getPayType(), pfdBonusRecord.getPayType())) ){
						totalAdClick = totalAdClick + pfdBonusRecord.getNowClkPrice();
					}
				}
			}
			//本期後付廣告費用
			totalAdClkPrice = totalAdClick;
			
			
			//本期佣金(只算月佣金)
			double monthBonusMoney = 0 ;
			//本期應付獎金(含季、年獎金)
			double quarterAndYearBonusMoney = 0 ;
			//本期佣金與本期應付獎金
			List<PfdBonusRecord> bonusRecordsOriginal = pfdBonusRecordService.findPfdInvoiceBonusRecords(pfdCustomerInfoId, closeYear, closeMonth);
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
			//本期應付獎金(含月、季、年獎金)			
			totalBonus = monthBonusMoney + quarterAndYearBonusMoney;
			
			
			//撈出該月最後一筆發票資料
			pfdBonusInvoice = pfdBonusInvoiceService.findPfdLastInvoiceByMonthPayment(pfdCustomerInfoId, closeYear, closeMonth);
			
			//畫面自動帶發票金額(含稅)*1.05後四捨五入的金額
			//應收廣告費用發票金額(含稅)
			pfdBonusInvoice.setPfdInvoiceMoney((float)Math.round((totalAdClkPrice*1.05)));
			//應付獎金發票金額(含稅)
			pfdBonusInvoice.setFinanceInvoiceMoney((float)Math.round((totalBonus*1.05)));
			
			//塞入付款狀態
			pfdBnousBillMap = new LinkedHashMap<String,String>();
			for(EnumPfdBonusBill enumPfdBonusBill:EnumPfdBonusBill.values()){
				pfdBnousBillMap.put(enumPfdBonusBill.getStatus(), enumPfdBonusBill.getAdvanceChName());			
			}
			
		}catch (Exception e){
			log.error("Error: "+e);
		}
		
		return SUCCESS;
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
	
	public String updateBonusInvoiceAjax() throws Exception {
		try {
			log.info(">>> pfdCustomerInfoId = "+pfdCustomerInfoId);
			log.info(">>> closeYear = "+closeYear);
			log.info(">>> closeMonth = "+closeMonth);
			log.info(">>> financeInvoiceSno = "+financeInvoiceSno);
			log.info(">>> financeInvoiceDate = "+financeInvoiceDate);
			log.info(">>> financeInvoiceMoney = "+financeInvoiceMoney);
			log.info(">>> financePayDate = "+financePayDate);
			log.info(">>> pfdInvoiceSno = "+pfdInvoiceSno);
			log.info(">>> pfdInvoiceDate = "+pfdInvoiceDate);
			log.info(">>> pfdInvoiceMoney = "+pfdInvoiceMoney);
			log.info(">>> pfdCheckSno = "+pfdCheckSno);
			log.info(">>> pfdCheckCloseDate = "+pfdCheckCloseDate);
			log.info(">>> bonusInvoiceId = "+bonusInvoiceId);
			log.info(">>> billStatus = "+billStatus);
			log.info(">>> billNote = "+billNote);
			log.info(">>> debitMoney = "+debitMoney);
			log.info(">>> debitDate = "+debitDate);
			log.info(">>> pfdPayCategory = "+pfdPayCategory);
			log.info(">>> bonusType = "+bonusType);
			
			//依據付款狀態還原請款欄位內容
			//狀態更變為：已付PChome廣告費/需寄回佣金發票與請款確認單
			if(StringUtils.equals(EnumPfdBonusBill.APPLY.getStatus(), billStatus)){
				//應付獎金
				bonusType = null;			//付款憑證(1:發票，2:折讓單)
				financeInvoiceSno = null;	//財務發票號碼
				financeInvoiceDate = null;	//財務發票日期
				financeInvoiceMoney = 0;	//財務發票金額(含稅)	
				financePayDate = null;		//財務付款日期
				debitMoney = 0;				//折讓金額(含稅)
				debitDate = null;			//折讓日期
				
				//付款失敗原因
				billNote = null;
			}
			//狀態更變為：尚未請款
			if(StringUtils.equals(EnumPfdBonusBill.NOT_APPLY.getStatus(), billStatus)){
				//應收廣告費用
				pfdInvoiceSno = null;		//經銷商發票號碼
				pfdInvoiceDate = null;		//經銷商發票日期
				pfdInvoiceMoney = 0;		//經銷商發票金額(含稅)
				pfdPayCategory = null;		//經銷商付款方式 1支票 2現金 3電匯
				pfdCheckSno = null;			//經銷商支票號碼
				pfdCheckCloseDate = null;	//經銷商支票到期日期
				
				
				//應付獎金
				bonusType = null;			//付款憑證(1:發票，2:折讓單)
				financeInvoiceSno = null;	//財務發票號碼
				financeInvoiceDate = null;	//財務發票日期
				financeInvoiceMoney = 0;	//財務發票金額(含稅)	
				financePayDate = null;		//財務付款日期
				debitMoney = 0;				//折讓金額(含稅)
				debitDate = null;			//折讓日期
				
				//付款失敗原因
				billNote = null;
			}
			//狀態更變為：佣金請款失敗
			if(StringUtils.equals(EnumPfdBonusBill.PAY_FAIL.getStatus(), billStatus)){
				//應付獎金
				bonusType = null;			//付款憑證(1:發票，2:折讓單)
				financeInvoiceSno = null;	//財務發票號碼
				financeInvoiceDate = null;	//財務發票日期
				financeInvoiceMoney = 0;	//財務發票金額(含稅)	
				financePayDate = null;		//財務付款日期
				debitMoney = 0;				//折讓金額(含稅)
				debitDate = null;			//折讓日期
			}
			//狀態更變為：佣金請款成功
			if(StringUtils.equals(EnumPfdBonusBill.PAY_MONEY.getStatus(), billStatus)){
				//付款失敗原因
				billNote = null;
			}
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			
			// by月份全部更新
			List<PfdBonusInvoice> pfdBonusInvoiceList = pfdBonusInvoiceService.findPfdBonusInvoice(pfdCustomerInfoId,closeYear,closeMonth);
			
			if(!pfdBonusInvoiceList.isEmpty()){
				for (PfdBonusInvoice pfdBonusInvoice : pfdBonusInvoiceList) {
	
					//更新發票內容
					pfdBonusInvoice.setFinanceInvoiceSno(financeInvoiceSno);
					pfdBonusInvoice.setFinanceInvoiceMoney(financeInvoiceMoney);
					
					if(StringUtils.isNotEmpty(financeInvoiceDate)){
						pfdBonusInvoice.setFinanceInvoiceDate(DateValueUtil.getInstance().stringToDate(financeInvoiceDate));
					} else{
						pfdBonusInvoice.setFinanceInvoiceDate(null);
					}
					
					if(StringUtils.isNotEmpty(financePayDate)){
						pfdBonusInvoice.setFinancePayDate(DateValueUtil.getInstance().stringToDate(financePayDate));		
					} else{
						pfdBonusInvoice.setFinancePayDate(null);
					}
					
					pfdBonusInvoice.setBillStatus(billStatus);
					pfdBonusInvoice.setPfdInvoiceSno(pfdInvoiceSno);
					
					if(StringUtils.isNotEmpty(pfdInvoiceDate)){
						pfdBonusInvoice.setPfdInvoiceDate(DateValueUtil.getInstance().stringToDate(pfdInvoiceDate));	
					} else{
						pfdBonusInvoice.setPfdInvoiceDate(null);
					}
					
					pfdBonusInvoice.setPfdInvoiceMoney(pfdInvoiceMoney);
					pfdBonusInvoice.setPfdPayCategory(pfdPayCategory);
					pfdBonusInvoice.setPfdCheckSno(pfdCheckSno);
					
					if(StringUtils.isNotEmpty(pfdCheckCloseDate)){
						pfdBonusInvoice.setPfdCheckCloseDate(DateValueUtil.getInstance().stringToDate(pfdCheckCloseDate));		
					} else{
						pfdBonusInvoice.setPfdCheckCloseDate(null);
					}
					
					pfdBonusInvoice.setBonusType(bonusType);
					pfdBonusInvoice.setDebitMoney(debitMoney);
					
					if(StringUtils.isNotEmpty(debitDate)){
						pfdBonusInvoice.setDebitDate(DateValueUtil.getInstance().stringToDate(debitDate));	
					} else{
						pfdBonusInvoice.setDebitDate(null);
					}
					
					pfdBonusInvoice.setBillNote(billNote);
					pfdBonusInvoice.setUpdateDate(new Date());
					
					pfdBonusInvoiceService.saveOrUpdate(pfdBonusInvoice);
				}
				
				SimpleDateFormat boardDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				
				//狀態更變為：已付PChome廣告費/需寄回佣金發票與請款確認單
				if(StringUtils.equals(EnumPfdBonusBill.APPLY.getStatus(), billStatus)){
					Date createDate = new Date();
			        Date startDate = sdf.parse(sdf.format(createDate));
			        Calendar c = Calendar.getInstance();
					c.add(Calendar.MONTH, 6);
					Date endDate = sdf.parse(sdf.format(c.getTime()));
					
					String content = "帳務公告： 已收到您的PChome廣告費用，謝謝；";
	
					content += "可隨時查看您的<a href=\"./bonusBill.html\">請款進度狀態</a>。";
					
					addPfdBoard(pfdCustomerInfoId,startDate,endDate,content,null);
				}
				
				//狀態更變為：佣金請款失敗
				if(StringUtils.equals(EnumPfdBonusBill.PAY_FAIL.getStatus(), billStatus)){
					Date createDate = new Date();
			        Date startDate = sdf.parse(sdf.format(createDate));
			        Calendar c = Calendar.getInstance();
					c.add(Calendar.MONTH, 6);
					Date endDate = sdf.parse(sdf.format(c.getTime()));
					
					String content = "帳務公告： 佣金請款失敗；";
	
					content += "可隨時查看您的<a href=\"./bonusBill.html\">請款進度狀態</a>。";
					
					addPfdBoard(pfdCustomerInfoId,startDate,endDate,content,null);
				}
				
				//狀態更變為：佣金請款成功
				if(StringUtils.equals(EnumPfdBonusBill.PAY_MONEY.getStatus(), billStatus)){
					Date createDate = new Date();
			        Date startDate = sdf.parse(sdf.format(createDate));
			        Calendar c = Calendar.getInstance();
					c.add(Calendar.MONTH, 6);
					Date endDate = sdf.parse(sdf.format(c.getTime()));
					
					String content = "帳務公告： 佣金請款成功；";
	
					content += "可隨時查看您的<a href=\"./bonusBill.html\">請款進度狀態</a>。";
					
					addPfdBoard(pfdCustomerInfoId,startDate,endDate,content,null);
				}
	
				
				/*此段邏輯取消-請款進度為請款中時，要把之前未付的佣金合併最新的一張單
				Integer payId = pfdBonusInvoice.getId();
				String pfdId = pfdBonusInvoice.getPfdContract().getPfdCustomerInfo().getCustomerInfoId();
				if(StringUtils.equals(EnumPfdBonusBill.APPLY.getStatus(), billStatus)){
					pfdBonusInvoice.setPayId(payId);
					
					StringBuffer bonusCloseDate = new StringBuffer();
					bonusCloseDate.append(pfdBonusInvoice.getCloseYear()).append("-");
					bonusCloseDate.append(pfdBonusInvoice.getCloseMonth()).append("-");
					bonusCloseDate.append(EnumCloseDay.CLOSE_DAY.getDay());
					String checkDate = DateValueUtil.getInstance().dateToString(DateValueUtil.getInstance().getDateForStartDateAddDay(bonusCloseDate.toString(), 1));
					
					List<PfdBonusInvoice> nonCloseBonusList = pfdBonusInvoiceService.findNonCloseBonus(pfdBonusInvoice.getPfdContract().getPfdCustomerInfo().getCustomerInfoId(), pfdBonusInvoice.getPayType(), checkDate);
					
					if(!nonCloseBonusList.isEmpty()){
						List<PfdBonusInvoice> updateList = new ArrayList<PfdBonusInvoice>();
						for(PfdBonusInvoice data:nonCloseBonusList){
							data.setPayId(payId);
							updateList.add(data);
						}
						pfdBonusInvoiceService.saveOrUpdateAll(updateList);
					}
				}
				*/
			}
		} catch (Exception e) {
			log.error("Error: "+e);
		}
		
		return SUCCESS;
	}
	
	
	public String updateBalanceInvoiceAjax() {
		
		try {
			log.info(">>> balancePfdYearMonth = "+balancePfdYearMonth);
			
			Date today = new Date();
			
			int laterTotalAdClkPrice = 0;
			
			String[] balanceInfo = balancePfdYearMonth.split("_");
			
			String pfdId = balanceInfo[0];
			int closeYear = Integer.parseInt(balanceInfo[1]);
			int closeMonth =  Integer.parseInt(balanceInfo[2]);
			
			List<PfdBonusInvoice> laterBonusInvoices = pfdBonusInvoiceService.findLaterTotalAdClkPrice(pfdId, closeYear, closeMonth);
			
			if(!laterBonusInvoices.isEmpty()){
				//計算當月後付廣告花費
				for (PfdBonusInvoice pfdBonusInvoice : laterBonusInvoices) {
					laterTotalAdClkPrice =  laterTotalAdClkPrice + (int)pfdBonusInvoice.getTotalAdClkPrice();
					//壓上後付的沖帳日期
					pfdBonusInvoice.setBalanceDate(today);
					pfdBonusInvoice.setUpdateDate(today);
					pfdBonusInvoiceService.saveOrUpdate(pfdBonusInvoice);
				}
				
				//將廣告花費加回帳戶餘額中
				PfdCustomerInfo pfdCustomerInfo = laterBonusInvoices.get(0).getPfdContract().getPfdCustomerInfo();
				int remainQuota = (int) (pfdCustomerInfo.getRemainQuota() + laterTotalAdClkPrice);
				pfdCustomerInfo.setRemainQuota(remainQuota);
				pfdCustomerInfo.setUpdateDate(today);
				pfdCustomerInfoService.saveOrUpdate(pfdCustomerInfo);
			}
		} catch (Exception e) {
			log.error("Error: "+e);
		}
		
		return SUCCESS;
	}

	public void addPfdBoard(String pfdId, Date startDate, Date endDate, String content, String id) throws Exception{
		
		String pfdUserId = "";
		List<PfdUser> PfdUsers = pfdUserService.findRootPfdUser(pfdId);
		for(PfdUser user:PfdUsers){
			if(EnumPrivilegeModel.ROOT_USER.getPrivilegeId() == user.getPrivilegeId()){
				pfdUserId = user.getUserId();
			}
		}
		
		PfdBoard board = new PfdBoard();
		board.setBoardType(EnumPfdBoardType.FINANCE.getType());
		board.setBoardContent(content);
		board.setPfdCustomerInfoId(pfdId);
		board.setPfdUserId(pfdUserId);
		board.setStartDate(startDate);
		board.setEndDate(endDate);
		board.setIsSysBoard("n");
		board.setHasUrl("n");
		board.setUrlAddress(null);
		
		//觀看權限(總管理者/帳戶管理/行政管理/帳務管理)
		String msgPrivilege = EnumPfdPrivilege.ROOT_USER.getPrivilege() + "||" + EnumPfdPrivilege.ACCOUNT_MANAGER.getPrivilege()
				 + "||" + EnumPfdPrivilege.REPORT_MANAGER.getPrivilege() + "||" + EnumPfdPrivilege.BILL_MANAGER.getPrivilege();
		board.setMsgPrivilege(msgPrivilege);
		
		board.setCreateDate(new Date());
		pfdBoardService.save(board);
	}
	
	//加總PFD該月的禮金+回饋金
	public float pfdFreeMoneyByMonth(String pfdId, int year, int month)throws Exception{
		
		float pfdFreeMoney = 0;
		
		Calendar closeDate = Calendar.getInstance(); 
		closeDate.set(Calendar.YEAR,year);
		closeDate.set(Calendar.MONTH,month);

		//開始日期
		closeDate.set(Calendar.DATE, 1);// 設為當前月的1號   
        Date freeStartDate =  closeDate.getTime();
        
        //結束日期
        closeDate.add(Calendar.MONTH, 1);// 加一個月，變為下月的1號   
        closeDate.add(Calendar.DATE, -1);// 減去一天，變為當月最後一天   
        Date freeEndDate = closeDate.getTime();  
		
        pfdFreeMoney = admRecognizeDetailService.findPfdAdPvClkPriceOrderTypeForFree(pfdId, freeStartDate, freeEndDate);
		  
		return pfdFreeMoney;
	}
	
	
	public void setPfdBonusInvoiceService(
			IPfdBonusInvoiceService pfdBonusInvoiceService) {
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}
	
	public void setPfdBonusRecordService(IPfdBonusRecordService pfdBonusRecordService) {
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setPfdUserService(IPfdUserService pfdUserService) {
		this.pfdUserService = pfdUserService;
	}

	public void setPfdCustomerInfoService(
			IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
	}

	public void setInvoiceStrDate(String invoiceStrDate) {
		this.invoiceStrDate = invoiceStrDate;
	}

	public void setInvoiceEndDate(String invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public void setCloseYear(int closeYear) {
		this.closeYear = closeYear;
	}

	public void setCloseMonth(int closeMonth) {
		this.closeMonth = closeMonth;
	}

	public List<PfdBonusInvoiceVO> getPfdBonusInvoices() {
		return pfdBonusInvoices;
	}

	public PfdBonusInvoice getPfdBonusInvoice() {
		return pfdBonusInvoice;
	}

	public EnumPfdAccountPayType[] getEnumPfdAccountPayType() {
		return enumPfdAccountPayType;
	}
	
	public EnumInvoicPayType[] getEnumInvoicPayType() {
		return enumInvoicPayType;
	}

	public void setBonusInvoiceId(String bonusInvoiceId) {
		this.bonusInvoiceId = bonusInvoiceId;
	}

	public void setBalancePfdYearMonth(String balancePfdYearMonth) {
		this.balancePfdYearMonth = balancePfdYearMonth;
	}

	public void setFinanceInvoiceSno(String financeInvoiceSno) {
		this.financeInvoiceSno = financeInvoiceSno;
	}

	public void setFinanceInvoiceDate(String financeInvoiceDate) {
		this.financeInvoiceDate = financeInvoiceDate;
	}

	public void setFinanceInvoiceMoney(float financeInvoiceMoney) {
		this.financeInvoiceMoney = financeInvoiceMoney;
	}

	public void setFinancePayDate(String financePayDate) {
		this.financePayDate = financePayDate;
	}

	public void setPfdInvoiceSno(String pfdInvoiceSno) {
		this.pfdInvoiceSno = pfdInvoiceSno;
	}

	public void setPfdInvoiceDate(String pfdInvoiceDate) {
		this.pfdInvoiceDate = pfdInvoiceDate;
	}

	public void setPfdInvoiceMoney(float pfdInvoiceMoney) {
		this.pfdInvoiceMoney = pfdInvoiceMoney;
	}

	public void setPfdPayCategory(String pfdPayCategory) {
		this.pfdPayCategory = pfdPayCategory;
	}

	public void setPfdCheckSno(String pfdCheckSno) {
		this.pfdCheckSno = pfdCheckSno;
	}

	public void setPfdCheckCloseDate(String pfdCheckCloseDate) {
		this.pfdCheckCloseDate = pfdCheckCloseDate;
	}

	public double getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(double totalBonus) {
		this.totalBonus = totalBonus;
	}

	public double getTotalAdClkPrice() {
		return totalAdClkPrice;
	}

	public void setTotalAdClkPrice(double totalAdClkPrice) {
		this.totalAdClkPrice = totalAdClkPrice;
	}

	public Map<String,String> getPfdBnousBillMap() {
		return pfdBnousBillMap;
	}

	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}

	public void setDebitMoney(float debitMoney) {
		this.debitMoney = debitMoney;
	}

	public void setDebitDate(String debitDate) {
		this.debitDate = debitDate;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public void setBillNote(String billNote) {
		this.billNote = billNote;
	}

	public void setPfdBoardService(IPfdBoardService pfdBoardService) {
		this.pfdBoardService = pfdBoardService;
	}

	public void setAdmRecognizeDetailService(IAdmRecognizeDetailService admRecognizeDetailService) {
		this.admRecognizeDetailService = admRecognizeDetailService;
	}
	
}
