package com.pchome.akbadm.struts2.action.pfd.bonus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusInvoiceVO;
import com.pchome.akbadm.db.vo.pfd.bonus.PfdBonusRecordInvoiceVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.bonus.EnumInvoicPayType;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

public class BalanceInvoiceAction extends BaseCookieAction{

	private final String FILE_TYPE = ".csv";
	
	private IPfdBonusInvoiceService pfdBonusInvoiceService;
	private List<PfdBonusInvoice> pfdBonusInvoices;
	private List<PfdBonusInvoiceVO> pfdBonusInvoicesVO;
	private EnumPfdAccountPayType[] enumPfdAccountPayType = EnumPfdAccountPayType.values();				// 帳戶付款方式
	private EnumInvoicPayType[] enumInvoicPayType = EnumInvoicPayType.values();							// 發票付款方式
	
	
	private String invoiceStrDate;
	private String invoiceEndDate;
	private String pfdCustomerInfoId;
	private String bonusInvoiceId;
	
	private String downloadFileName;								// 下載檔案名稱
	private InputStream downloadFileStream;							// 下載檔案
	
	public String balanceInvoiceReportAction(){
		
		this.queryBalanceInvoiceReport();
		
		return SUCCESS;
	}
	
	public String donwnloadBalanceReportAction() throws Exception{
		
		this.queryBalanceInvoiceReport();
		
		StringBuffer fileName = new StringBuffer();
		
		fileName.append("沖帳報表");
		fileName.append("_");
		fileName.append(DateValueUtil.getInstance().getDateValue(DateValueUtil.TODAY, DateValueUtil.DBPATH));
		fileName.append(FILE_TYPE);
		
		StringBuffer content = new StringBuffer();									
		
		content.append("帳務月份,經銷商編號,經銷商名稱,統一編號,付款狀態,廣告點擊費用,應付獎金,");
		content.append("發票號碼,發票日期,發票金額(含稅),付款方式,支票號碼,支票到期日期,");
		content.append("發票號碼,發票日期,發票金額(含稅),付款日期,");
		content.append("折讓金額(含稅),折讓日期,沖帳日期");
		content.append("\n");	  
		    
		for(PfdBonusInvoiceVO invoice:pfdBonusInvoicesVO){
			content.append(invoice.getCloseYear()+"-"+invoice.getCloseMonth()).append(",");
			content.append(invoice.getPfdCustomerInfoId()).append(",");
			content.append(invoice.getCompanyName()).append(",");
			content.append(invoice.getCompanyTaxId()).append(",");
			for(EnumPfdAccountPayType payType:enumPfdAccountPayType){
				if(payType.getPayType().equals(invoice.getPayType())){
					content.append(payType.getPayName()).append(",");
				}
			}	
			content.append(invoice.getTotalAdClkPrice()).append(",");
			content.append(invoice.getTotalBonus()).append(",");
			
			//應收廣告費用
			content.append(invoice.getPfdInvoiceSno()).append(",");
			content.append(invoice.getPfdInvoiceDate()).append(",");
			content.append(invoice.getPfdInvoiceMoney()).append(",");
			for(EnumInvoicPayType payType:enumInvoicPayType){
				if(payType.getType().equals(invoice.getPfdPayCategory())){
					content.append(payType.getChName()).append(",");
				}
			}			
			content.append(invoice.getPfdCheckSno()).append(",");
			content.append(invoice.getPfdCheckCloseDate()).append(",");
			
			//應付獎金
			content.append(invoice.getFinanceInvoiceSno()).append(",");
			content.append(invoice.getFinanceInvoiceDate()).append(",");
			content.append(invoice.getFinanceInvoiceMoney()).append(",");
			content.append(invoice.getFinancePayDate()).append(",");
			
			//折讓金額
			content.append(invoice.getDebitMoney()).append(",");
			content.append(invoice.getDebitDate()).append(",");
			content.append(invoice.getBalanceDate());
			content.append("\n");
		}
		
		downloadFileName = URLEncoder.encode(fileName.toString(), "UTF-8");
		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));	
		
		return SUCCESS;
	}
	
	private void queryBalanceInvoiceReport(){
		try{
			log.info("1. invoiceStrDate: "+invoiceStrDate);
			log.info("2. invoiceEndDate: "+invoiceEndDate);
			log.info("3. pfdCustomerInfoId: "+pfdCustomerInfoId);
			log.info("4. pfdBonusInvoiceService: "+pfdBonusInvoiceService);
			
			String strYear = invoiceStrDate.split("-")[0];
			String strMonth = invoiceStrDate.split("-")[1];
			String endYear = invoiceEndDate.split("-")[0];
			String endMonth = invoiceEndDate.split("-")[1];
			
			String strDate = strYear+strMonth;
			String endDate = endYear+endMonth;
			
			log.info(" strYear: "+strYear+"  strMonth: "+strMonth);
			log.info(" endYear: "+endYear+"  endMonth: "+endMonth);
			log.info(" strDate: "+strDate);
			log.info(" endDate: "+endDate);
			
			pfdBonusInvoicesVO = new ArrayList<PfdBonusInvoiceVO>();
			Map<String,PfdBonusInvoiceVO> closeMap = new HashMap<String,PfdBonusInvoiceVO>();
	
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
					Calendar calendar2 = Calendar.getInstance();
					calendar2.setTime(recordDate);
					
					//如果資料年月為當下前1個月，即可請款
					if(calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)){
						thisMonth = "Y";
					}						
					
					//如果已無生效合約，即打開該PFD最後一筆可請款的年月資料
					if(closeMap.get(data.getValue().getPfdCustomerInfoId()) != null){
						PfdBonusInvoiceVO closeVo = new PfdBonusInvoiceVO();
						closeVo = closeMap.get(data.getValue().getPfdCustomerInfoId());
						
						if(closeVo.getCloseYear() == vo.getCloseYear() && closeVo.getCloseMonth() == vo.getCloseMonth()){
							thisMonth = "Y";
						}
					}
					
					//如果為上個月或是無生效合約且該筆為最後一筆資料，即可下載PDF
					vo.setThisMonth(thisMonth);
					
					//是否到請款時間
					Calendar currentDate = Calendar.getInstance();
					currentDate.setTime(new Date());
					//java.util.Calendar之get MONTH會少1個月(Calendar的0月等於1月)
					if (vo.getCloseYear() > currentDate.get(Calendar.YEAR) || (vo.getCloseYear() == currentDate.get(Calendar.YEAR)&&vo.getCloseMonth()-1 >=currentDate.get(Calendar.MONTH) ) ){
						vo.setDownload("N");	//尚未到請款時間
					}else{
						vo.setDownload("Y");	//已過請款時間
					}
					
					pfdBonusInvoicesVO.add(vo);
				}
			}
			
			log.info(" pfdBonusInvoicesVO: "+pfdBonusInvoicesVO);
		
		}catch (Exception e){
			log.error("Error: "+e);
		}
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
						bonusInvoice.setPayType(data.getPfpPayType());
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
	
	public void setPfdBonusInvoiceService(
			IPfdBonusInvoiceService pfdBonusInvoiceService) {
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}
	
	
	public void setInvoiceStrDate(String invoiceStrDate) {
		this.invoiceStrDate = invoiceStrDate;
	}

	public void setInvoiceEndDate(String invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	public String getInvoiceStrDate() {
		return invoiceStrDate;
	}

	public String getInvoiceEndDate() {
		return invoiceEndDate;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setBonusInvoiceId(String bonusInvoiceId) {
		this.bonusInvoiceId = bonusInvoiceId;
	}

	public List<PfdBonusInvoice> getPfdBonusInvoices() {
		return pfdBonusInvoices;
	}
	
	public List<PfdBonusInvoiceVO> getPfdBonusInvoicesVO() {
		return pfdBonusInvoicesVO;
	}

	public EnumPfdAccountPayType[] getEnumPfdAccountPayType() {
		return enumPfdAccountPayType;
	}

	public EnumInvoicPayType[] getEnumInvoicPayType() {
		return enumInvoicPayType;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}
	
}
