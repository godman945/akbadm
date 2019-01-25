package com.pchome.akbadm.quartzs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pchome.akbadm.db.pojo.PfdBonusInvoice;
import com.pchome.akbadm.db.pojo.PfdBonusItemSet;
import com.pchome.akbadm.db.pojo.PfdContract;
import com.pchome.akbadm.db.service.contract.IPfdContractService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusDayReportService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusInvoiceService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusItemSetService;
import com.pchome.akbadm.db.service.pfd.bonus.IPfdBonusRecordService;
import com.pchome.akbadm.factory.pfd.bonus.APfdBonusItem;
import com.pchome.akbadm.factory.pfd.bonus.EveryDayPfdBonus;
import com.pchome.akbadm.factory.pfd.bonus.PfdBonusItemFactory;
import com.pchome.config.TestConfig;
import com.pchome.enumerate.contract.EnumContractStatus;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusBill;
import com.pchome.enumerate.pfd.bonus.EnumPfdBonusItem;
import com.pchome.enumerate.pfd.bonus.EnumPfdDownloadBill;
import com.pchome.rmi.bonus.EnumPfdAccountPayType;
import com.pchome.soft.util.DateValueUtil;

/**
 * 經銷商獎金計算
 * 1. 月獎金 = 經銷商廣告月收入 * (達到月門檻 %)
 * 2. 季獎金 = 經銷商廣告季收入 * (達到季門檻 %) - 每月獎金
 * 3. 年獎金 = 經銷商廣告年收入 * (達到年門檻 %) - 每月獎金 - 每季獎金
 * 4. 專案獎金 = 經銷商廣告年收入 * (達到年門檻 %)
 *
 * 條件
 * 1. 有合約的經銷商才計算獎金
 * 2. 綜合型經銷商計算獎金, 不理會合約到期日期
 * 3.
 */

@Transactional
public class PfdBonusJob {

	protected Log log = LogFactory.getLog(this.getClass().getName());

	private IPfdContractService pfdContractService;
	private IPfdBonusItemSetService pfdBonusItemSetService;
	private IPfdBonusInvoiceService pfdBonusInvoiceService;
	private IPfdBonusRecordService pfdBonusRecordService;
	private PfdBonusItemFactory pfdBonusItemFactory;
	private EveryDayPfdBonus everyDayPfdBonus;
	private IPfdBonusDayReportService pfdBonusDayReportService;

	public void auto(){
        log.info("====PfdBonusJob.auto() start====");

		String yesterday = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);

		// 獎金計算流程
		this.bonusProcess(DateValueUtil.getInstance().stringToDate(yesterday));

        log.info("====PfdBonusJob.auto() end====");
	}

	public void manual(String startDate){
        log.info("====PfdBonusJob.manual() start====");

		this.countPfdBonus(startDate);

        log.info("====PfdBonusJob.manual() end====");
	}

	private void countPfdBonus(String startDate){
		// 指定日期計算到前一天
		String yesterday = DateValueUtil.getInstance().getDateValue(DateValueUtil.YESTERDAY, DateValueUtil.DBPATH);
		//yesterday = "2015-06-09";
		long diffyDay = DateValueUtil.getInstance().getDateDiffDay(startDate, yesterday);

		Date recordDate = null;
		int countDay = 0;

		// 逐天處理
		while(diffyDay > 0){

			recordDate = DateValueUtil.getInstance().getDateForStartDateAddDay(startDate, countDay);

			// 獎金計算流程
			this.bonusProcess(recordDate);

			diffyDay--;
			countDay++;

		}
	}

	private void bonusProcess(Date recordDate){

		// 刪除日期之後的資料
		this.deleteProcess(recordDate);

		// 獎金計算流程
		log.info(" ---------------------------- ");
		log.info(" recordDate: "+DateValueUtil.getInstance().dateToString(recordDate));
		List<PfdContract> pfdContracts = pfdContractService.findValidPfdContract(recordDate);

		if(!pfdContracts.isEmpty()){

			for(PfdContract contract:pfdContracts){
				
				// 檢查合約
				if(this.checkPfdContract(contract,recordDate)){

					log.info(contract.getPfdCustomerInfo().getCompanyName());

					// 更新 pfd_bonus_day_report(計算 pfd 的每日月佣金)
					this.updatePfdBonusDayReport(recordDate,contract);

					// 計算更新獎金記錄
					this.updatePfdBonusRecord(recordDate, contract);

					// 更新預付發票金額，如為BOTH混合型預+後付都有
					if ( (StringUtils.equals(contract.getPfpPayType(), EnumPfdAccountPayType.ADVANCE.getPayType())) ||
							 (StringUtils.equals(contract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType()))	){
						this.updatePfdBonusInvoice(recordDate, contract, EnumPfdAccountPayType.ADVANCE.getPayType());
					}

					// 更新後付發票金額，如為BOTH混合型預+後付都有
					if ( (StringUtils.equals(contract.getPfpPayType(), EnumPfdAccountPayType.LATER.getPayType())) ||
							 (StringUtils.equals(contract.getPfpPayType(), EnumPfdAccountPayType.BOTH.getPayType()))	){
						this.updatePfdBonusInvoice(recordDate, contract, EnumPfdAccountPayType.LATER.getPayType());
					}
//					System.out.println("流程結束");
				}
			}
		}
	}


    private void deleteProcess(Date deleteDate){

		// 刪除 pfd_bonus_day_report
		this.deletePfdBonusDayReport(deleteDate);

	}


    private void deletePfdBonusDayReport(Date deleteDate){
		pfdBonusDayReportService.deletePfdBonusDayReport(deleteDate);
	}


    private void updatePfdBonusDayReport(Date startDate,PfdContract contract){

		everyDayPfdBonus.pfdBonusProcess(startDate,contract);

	}

	private boolean checkPfdContract(PfdContract contract, Date recordDate){
		// 檢查合約
		boolean ok = false;
		log.info("check");
		log.info("id="+contract.getPfdCustomerInfo().getCompanyName());

		log.info("status="+contract.getStatus());


		if(contract.getStatus().equals(EnumContractStatus.USE.getStatusId())){
			ok = true;
			log.info("true id="+contract.getPfdCustomerInfo().getCompanyName());
		}

		if(contract.getStatus().equals(EnumContractStatus.OVERTIME.getStatusId())){
			// 混合型經銷商
			if(contract.getPfdCustomerInfo().getMixFlag().equals("y")){
				ok = true;
				log.info("true id="+contract.getPfdCustomerInfo().getCompanyName());
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



	private void updatePfdBonusRecord(Date recordDate, PfdContract contract){
		// 計算更新獎金記錄
		List<PfdBonusItemSet> pfdBonusItemSets = pfdBonusItemSetService.findPfdBonusItemSets(contract.getPfdContractId());

		//經銷商所含獎金
		for(PfdBonusItemSet set:pfdBonusItemSets){

			for(EnumPfdBonusItem enumPfdBonusItem:EnumPfdBonusItem.values()){
				//計算某一種獎金
				if(set.getBonusItem().equals(enumPfdBonusItem.getItemType())){
					log.info(" BonusItemId: "+set.getBonusItem());
					// 獎金計算項目
					APfdBonusItem bonusItem = pfdBonusItemFactory.getPfdBonusItem(enumPfdBonusItem);

					if(bonusItem != null){

						// 獎金計算更新
						bonusItem.process(recordDate, contract, set.getSubItemId(),set.getBonusItem());
					}
				}

			}
		}

	}

	private void updatePfdBonusInvoice(Date recordDate, PfdContract contract, String payType){
		// 更新發票金額
		String recordDateStr = DateValueUtil.getInstance().dateToString(recordDate);

		// 獎金屬於那個0 日,1 月,2 年
		List<Integer> dateList = this.getYearAndMonth(recordDate);
		int month = dateList.get(1);
		int year = dateList.get(2);

		int day = Integer.valueOf(recordDateStr.substring(8, 10));
		String pfdId = contract.getPfdCustomerInfo().getCustomerInfoId();

		// 當月該領取獎金
		float totalBonus = pfdBonusRecordService.findPfdBonusMoneyByContract(pfdId, payType, year, month, contract.getPfdContractId());

		// 當月廣告費用
		float totalAdClkPrice = pfdBonusRecordService.findPfdBonusAdClkPriceByContract(pfdId, payType, year, month, EnumPfdBonusItem.EVERY_MONTH_BONUS.getItemType(), contract.getPfdContractId());

			PfdBonusInvoice pfdBonusInvoice = pfdBonusInvoiceService.findPfdBonusInvoiceByContract(pfdId,
					Integer.valueOf(year),Integer.valueOf(month), payType, contract.getPfdContractId());

			if(pfdBonusInvoice == null){
				pfdBonusInvoice = this.createNewPfdBonusInvoice(recordDate, contract, payType, year, month);
			}

			pfdBonusInvoice.setTotalAdClkPrice(totalAdClkPrice);
			pfdBonusInvoice.setTotalBonus(totalBonus);

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
	        try {
				c.setTime(format.parse(recordDateStr));
			} catch (ParseException e) {
				log.info(e);
			}
			
			if(day == c.getActualMaximum(Calendar.DATE)){
				pfdBonusInvoice.setDownload(EnumPfdDownloadBill.YES.getStatus());
			}

			pfdBonusInvoice.setRecordDate(recordDate);
			pfdBonusInvoice.setUpdateDate(new Date());
			pfdBonusInvoiceService.saveOrUpdate(pfdBonusInvoice);

	}

	private PfdBonusInvoice createNewPfdBonusInvoice(Date recordDate, PfdContract contract, String payType, int year, int month){

		Date today = new Date();
		PfdBonusInvoice pfdBonusInvoice = new PfdBonusInvoice();

		pfdBonusInvoice.setPfdContract(contract);
		pfdBonusInvoice.setCloseYear(year);
		pfdBonusInvoice.setCloseMonth(month);
		pfdBonusInvoice.setBillStatus(EnumPfdBonusBill.NOT_APPLY.getStatus());
		pfdBonusInvoice.setDownload(EnumPfdDownloadBill.NO.getStatus());
		pfdBonusInvoice.setPayType(payType);
		pfdBonusInvoice.setTotalAdClkPrice(0);
		pfdBonusInvoice.setTotalBonus(0);
		pfdBonusInvoice.setUpdateDate(today);
		pfdBonusInvoice.setCreateDate(today);

		return pfdBonusInvoice;
	}


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

	public void setPfdContractService(IPfdContractService pfdContractService) {
		this.pfdContractService = pfdContractService;
	}

	public void setPfdBonusItemSetService(
			IPfdBonusItemSetService pfdBonusItemSetService) {
		this.pfdBonusItemSetService = pfdBonusItemSetService;
	}

	public void setPfdBonusInvoiceService(
			IPfdBonusInvoiceService pfdBonusInvoiceService) {
		this.pfdBonusInvoiceService = pfdBonusInvoiceService;
	}

	public void setPfdBonusRecordService(
			IPfdBonusRecordService pfdBonusRecordService) {
		this.pfdBonusRecordService = pfdBonusRecordService;
	}

	public void setPfdBonusItemFactory(PfdBonusItemFactory pfdBonusItemFactory) {
		this.pfdBonusItemFactory = pfdBonusItemFactory;
	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = null;
		String date = null;

		if(args.length > 0){
			context = new FileSystemXmlApplicationContext(TestConfig.getPath(args));
			date = args[1].toString();
		}else{
			String[] test = {"local"};
			context = new FileSystemXmlApplicationContext(TestConfig.getPath(test));
			date = "2016-12-01";
		}

		PfdBonusJob job = context.getBean(PfdBonusJob.class);

		job.manual(date);

		//System.out.print("  "+DateValueUtil.getInstance().getQuarterOfYear("2015-01-25"));
	}

	public void setEveryDayPfdBonus(EveryDayPfdBonus everyDayPfdBonus) {
		this.everyDayPfdBonus = everyDayPfdBonus;
	}

	public void setPfdBonusDayReportService(
			IPfdBonusDayReportService pfdBonusDayReportService) {
		this.pfdBonusDayReportService = pfdBonusDayReportService;
	}


}
