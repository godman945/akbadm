package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.report.IAdmClientCountReportService;
import com.pchome.akbadm.db.vo.report.AdmClientCountReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class AdmClientCountDayReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdmClientCountReportService admClientCountReportService;

	private DecimalFormat df1 = new DecimalFormat("###,###,###,###");
	private DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");

	//輸入參數
	private String startDate;
	private String endDate;
	private String downloadFlag;
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名

	//訊息
	private String message = "";

	//頁數
	private int pageNo = 1;       					// 初始化目前頁數
	private int pageSize = 50;     					// 初始化每頁幾筆
	private int pageCount = 0;    					// 初始化共幾頁
	private long totalCount = 0;   					// 初始化共幾筆
	private long totalSize = 0;						//總比數

	//輸出參數
	private AdmClientCountReportVO totalVO;			//總計
	private AdmClientCountReportVO avgVO;			//平均
	private List<AdmClientCountReportVO> dataList;

	@Override
    public String execute() throws Exception {

		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

		totalVO = new AdmClientCountReportVO();
		avgVO = new AdmClientCountReportVO();

		dataList = admClientCountReportService.findReportData(startDate, endDate);

		log.info("dataList: "+dataList.size());

		if(dataList.size()==0){
    		this.message = "查無資料！";
    		return SUCCESS;
    	}

		List<AdmClientCountReportVO> viewList = new ArrayList<AdmClientCountReportVO>();

		totalCount = dataList.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		totalSize = totalCount;

		Double dataSize = new Double(dataList.size());	//總筆數
		Double pfpClientCountSum = new Double(0);		//直客客戶數
		Double pfdClientCountSum = new Double(0);		//經銷商客戶數
		Double salesClientCountSum = new Double(0);		//業務客戶數
		Double pfpAdClkPriceSum = new Double(0);		//直客廣告點擊數費用
		Double pfdAdClkPriceSum = new Double(0);		//經銷商廣告點擊數費用
		Double salesAdClkPriceSum = new Double(0);		//業務廣告點擊數費用
		Double pfpAdActionMaxPriceSum = new Double(0);	//直客廣告每日預算
		Double pfdAdActionMaxPriceSum = new Double(0);	//經銷商廣告每日預算
		Double salesAdActionMaxPriceSum = new Double(0);//業務廣告每日預算
		Double pfpSpendRateSum = new Double(0);			//直客預算達成率
		Double pfdSpendRateSum = new Double(0);			//經銷商預算達成率
		Double salesSpendRateSum = new Double(0);		///業務預算達成率
		Double totalSpendRateSum = new Double(0);		///總預算達成率
		
		Double pfpAdCountSum = new Double(0);			//直客廣告數
		Double pfdAdCountSum = new Double(0);			//經銷商廣告數
		Double salesAdCountSum = new Double(0);			//業務廣告數
		Double adPvSum = new Double(0);					//廣告曝光數
		Double adClkSum = new Double(0);				//廣告點擊數
		Double adInvalidClkSum = new Double(0);			//無效點擊數
		Double adClkPriceSum = new Double(0);			//廣告點擊數總費用
		Double adInvalidClkPriceSum = new Double(0);	//無效點擊數總費用
		Double clkRateSum = new Double(0);				//廣告點擊率
		Double adClkPriceAvgSum = new Double(0);		//單次點擊費用
		Double adPvPriceSum = new Double(0);			//每千次曝光費用
		Double lossCostSum = new Double(0);				//超播金額
		Double pfpSaveSum = new Double(0);				//pchome營運儲值金
		Double pfpFreeSum = new Double(0);				//pchome營運贈送金
		Double pfpPostpaidSum = new Double(0);			//pchome營運後付費
		Double pfbSaveSum = new Double(0);				//PFB分潤儲值金
		Double pfbFreeSum = new Double(0);				//PFB分潤贈送金
		Double pfbPostpaidSum = new Double(0);			//PFB分潤後付費
		Double pfdSaveSum = new Double(0);				//PFD佣金儲值金
		Double pfdFreeSum = new Double(0);				//PFD佣金贈送金
		Double pfdPostpaidSum = new Double(0);			//PFD佣金後付費
		Double totalSaveCountSum = new Double(0);		//成功儲值總筆數
		Double totalSavePriceSum = new Double(0);		//成功儲值金額
		

		for (int i=0; i<dataList.size(); i++) {
			AdmClientCountReportVO vo = dataList.get(i);

			pfpClientCountSum += new Double(vo.getPfpClientCount().replaceAll(",", ""));
			pfdClientCountSum += new Double(vo.getPfdClientCount().replaceAll(",", ""));
			salesClientCountSum += new Double(vo.getSalesAdCount().replaceAll(",", ""));
			pfpAdClkPriceSum += new Double(vo.getPfpAdClkPrice().replaceAll(",", ""));
			pfdAdClkPriceSum += new Double(vo.getPfdAdClkPrice().replaceAll(",", ""));
			salesAdClkPriceSum += new Double(vo.getSalesAdClkPrice().replaceAll(",", ""));
			pfpAdActionMaxPriceSum += new Double(vo.getPfpAdActionMaxPrice().replaceAll(",", ""));
			pfdAdActionMaxPriceSum += new Double(vo.getPfdAdActionMaxPrice().replaceAll(",", ""));
			salesAdActionMaxPriceSum += new Double(vo.getSalesAdActionMaxPrice().replaceAll(",", ""));
			pfpAdCountSum += new Double(vo.getPfpAdCount().replaceAll(",", ""));
			pfdAdCountSum += new Double(vo.getPfdAdCount().replaceAll(",", ""));
			adPvSum += new Double(vo.getAdPv().replaceAll(",", ""));
			adClkSum += new Double(vo.getAdClk().replaceAll(",", ""));
			adInvalidClkSum += new Double(vo.getAdInvalidClk().replaceAll(",", ""));
			adClkPriceSum += new Double(vo.getAdClkPrice().replaceAll(",", ""));
			adInvalidClkPriceSum += new Double(vo.getAdInvalidClkPrice().replaceAll(",", ""));
			lossCostSum += new Double(vo.getLossCost().replaceAll(",", ""));
			pfpSaveSum += new Double(vo.getPfpSave().replaceAll(",", ""));
			pfpFreeSum += new Double(vo.getPfpFree().replaceAll(",", ""));
			pfpPostpaidSum += new Double(vo.getPfpPostpaid().replaceAll(",", ""));
			pfbSaveSum += new Double(vo.getPfbSave().replaceAll(",", ""));
			pfbFreeSum += new Double(vo.getPfbFree().replaceAll(",", ""));
			pfbPostpaidSum += new Double(vo.getPfbPostpaid().replaceAll(",", ""));
			pfdSaveSum += new Double(vo.getPfdSave().replaceAll(",", ""));
			pfdFreeSum += new Double(vo.getPfdFree().replaceAll(",", ""));
			pfdPostpaidSum += new Double(vo.getPfdPostpaid().replaceAll(",", ""));
			totalSaveCountSum += new Double(vo.getTotalSaveCount().replaceAll(",", ""));
			totalSavePriceSum += new Double(vo.getTotalSavePrice().replaceAll(",", ""));
			
			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
				viewList.add(vo);
			}
		}

		
		//廣告客戶數總計
		Double totalClientCountSum = new Double(0);
		totalClientCountSum =  salesClientCountSum + pfpClientCountSum + pfdClientCountSum;
		
		//總廣告互動費用總計
		Double totalAdClkPriceSum = new Double(0);
		totalAdClkPriceSum =  pfpAdClkPriceSum + pfdAdClkPriceSum + salesAdClkPriceSum;
		
		
		//總廣告客戶數總計
		Double totalAdActionMaxPriceSum = new Double(0);
		totalAdActionMaxPriceSum =  salesAdActionMaxPriceSum + pfpAdActionMaxPriceSum + pfdAdActionMaxPriceSum;
		
		
		//直客預算達成率
		if(pfpAdClkPriceSum != 0 && pfpAdActionMaxPriceSum != 0){
			pfpSpendRateSum = (pfpAdClkPriceSum / pfpAdActionMaxPriceSum)*100;
		}

		//經銷商預算達成率
		if(pfdAdClkPriceSum != 0 && pfdAdActionMaxPriceSum != 0){
			pfdSpendRateSum = (pfdAdClkPriceSum / pfdAdActionMaxPriceSum)*100;
		}

		//業務預算達成率
		if(salesAdClkPriceSum != 0 && salesAdActionMaxPriceSum != 0){
			salesSpendRateSum = (salesAdClkPriceSum / salesAdActionMaxPriceSum)*100;
		}
		
		
		//總預算達成率
		if(totalAdClkPriceSum != 0 && totalAdActionMaxPriceSum != 0){
			totalSpendRateSum = (totalAdClkPriceSum / totalAdActionMaxPriceSum)*100;
		}
		
		//點擊率
		if(adPvSum != 0 && adClkSum != 0){
			clkRateSum = (adClkSum / adPvSum)*100;
		}

		//單次點擊費用
		if(adClkSum != 0 && adClkPriceSum != 0){
			adClkPriceAvgSum = adClkPriceSum / adClkSum;
		}

		//千次曝光費用
		if(adPvSum != 0 && adClkPriceSum != 0){
			adPvPriceSum = (adClkPriceSum / adPvSum)*1000;
		}
		
		//總計
		totalVO.setPfpClientCount(df1.format(pfpClientCountSum));
		totalVO.setSalesClientCount(df1.format(salesClientCountSum));
		totalVO.setPfdClientCount(df1.format(pfdClientCountSum));
		totalVO.setTotalClientCount(df1.format(totalClientCountSum));
		totalVO.setPfpAdClkPrice(df1.format(pfpAdClkPriceSum));
		totalVO.setSalesAdClkPrice(df2.format(salesAdClkPriceSum));
		totalVO.setPfdAdClkPrice(df1.format(pfdAdClkPriceSum));
		totalVO.setTotalAdClkPriceCount(df1.format(totalAdClkPriceSum));
		totalVO.setPfpAdActionMaxPrice(df1.format(pfpAdActionMaxPriceSum));
		totalVO.setSalesAdActionMaxPrice(df1.format(salesAdActionMaxPriceSum));
		totalVO.setPfdAdActionMaxPrice(df1.format(pfdAdActionMaxPriceSum));
		totalVO.setTotalAdActionMaxPrice(df1.format(totalAdActionMaxPriceSum));
		totalVO.setPfpSpendRate(df2.format(pfpSpendRateSum));
		totalVO.setPfdSpendRate(df2.format(pfdSpendRateSum));
		totalVO.setSalesSpendRate(df2.format(salesSpendRateSum));
		totalVO.setTotalSpendRate(df2.format(totalSpendRateSum));
		totalVO.setTotalSaveCount(df1.format(totalSaveCountSum));
		totalVO.setTotalSavePrice(df2.format(totalSavePriceSum));
		totalVO.setAdPv(df1.format(adPvSum));
		totalVO.setAdClk(df1.format(adClkSum));
		totalVO.setAdInvalidClk(df1.format(adInvalidClkSum));
		totalVO.setAdClkPrice(df1.format(adClkPriceSum));
		totalVO.setAdInvalidClkPrice(df1.format(adInvalidClkPriceSum));
		totalVO.setClkRate(df2.format(clkRateSum));
		totalVO.setAdClkPriceAvg(df2.format(adClkPriceAvgSum));
		totalVO.setAdPvPrice(df2.format(adPvPriceSum));
		totalVO.setLossCost(df1.format(lossCostSum));
		totalVO.setPfpSave(df2.format(pfpSaveSum));
		totalVO.setPfpFree(df2.format(pfpFreeSum));
		totalVO.setPfpPostpaid(df2.format(pfpPostpaidSum));
		totalVO.setPfbSave(df2.format(pfbSaveSum));
		totalVO.setPfbFree(df2.format(pfbFreeSum));
		totalVO.setPfbPostpaid(df2.format(pfbPostpaidSum));
		totalVO.setPfdSave(df2.format(pfdSaveSum));
		totalVO.setPfdFree(df2.format(pfdFreeSum));
		totalVO.setPfdPostpaid(df2.format(pfdPostpaidSum));
		
/* 		2018-03-27 線上廣告數暫時註解		
		totalVO.setSalesAdCount(df1.format(salesAdCountSum));
		totalVO.setPfpAdCount(df1.format(pfpAdCountSum));
		totalVO.setPfdAdCount(df1.format(pfdAdCountSum));
*/		
		
		//平均
		avgVO.setPfpClientCount(df2.format(pfpClientCountSum/dataSize));
		avgVO.setSalesClientCount(df2.format(salesClientCountSum/dataSize));
		avgVO.setPfdClientCount(df2.format(pfdClientCountSum/dataSize));
		avgVO.setTotalClientCount(df2.format(totalClientCountSum/dataSize));
		avgVO.setPfpAdClkPrice(df2.format(pfpAdClkPriceSum/dataSize));
		avgVO.setSalesAdClkPrice(df2.format(salesAdClkPriceSum/dataSize));
		avgVO.setPfdAdClkPrice(df2.format(pfdAdClkPriceSum/dataSize));
		avgVO.setTotalAdClkPriceCount(df1.format(totalAdClkPriceSum/dataSize));
		avgVO.setPfpAdActionMaxPrice(df2.format(pfpAdActionMaxPriceSum/dataSize));
		avgVO.setSalesAdActionMaxPrice(df2.format(salesAdActionMaxPriceSum/dataSize));
		avgVO.setPfdAdActionMaxPrice(df2.format(pfdAdActionMaxPriceSum/dataSize));
		avgVO.setTotalAdActionMaxPrice(df2.format(totalAdActionMaxPriceSum/dataSize));
		avgVO.setPfpSpendRate(df2.format(pfpSpendRateSum));
		avgVO.setSalesSpendRate(df2.format(salesSpendRateSum));
		avgVO.setPfdSpendRate(df2.format(pfdSpendRateSum));
		avgVO.setTotalSpendRate(df2.format(totalSpendRateSum));
		avgVO.setTotalSaveCount(df2.format(totalSaveCountSum/dataSize));
		avgVO.setTotalSavePrice(df2.format(totalSavePriceSum/dataSize));
		avgVO.setPfpAdCount(df2.format(pfpAdCountSum/dataSize));
		avgVO.setPfdAdCount(df2.format(pfdAdCountSum/dataSize));
		avgVO.setAdPv(df2.format(adPvSum/dataSize));
		avgVO.setAdClk(df2.format(adClkSum/dataSize));
		avgVO.setAdInvalidClk(df2.format(adInvalidClkSum/dataSize));
		avgVO.setAdClkPrice(df2.format(adClkPriceSum/dataSize));
		avgVO.setAdInvalidClkPrice(df2.format(adInvalidClkPriceSum/dataSize));
		avgVO.setClkRate(df2.format(clkRateSum));
		avgVO.setAdClkPriceAvg(df2.format(adClkPriceAvgSum));
		avgVO.setAdPvPrice(df2.format(adPvPriceSum));
		avgVO.setLossCost(df2.format(lossCostSum/dataSize));
		avgVO.setPfpSave(df2.format(pfpSaveSum/dataSize));
		avgVO.setPfpFree(df2.format(pfpFreeSum/dataSize));
		avgVO.setPfpPostpaid(df2.format(pfpPostpaidSum/dataSize));
		avgVO.setPfbSave(df2.format(pfbSaveSum/dataSize));
		avgVO.setPfbFree(df2.format(pfbFreeSum/dataSize));
		avgVO.setPfbPostpaid(df2.format(pfbPostpaidSum/dataSize));
		avgVO.setPfdSave(df2.format(pfdSaveSum/dataSize));
		avgVO.setPfdFree(df2.format(pfdFreeSum/dataSize));
		avgVO.setPfdPostpaid(df2.format(pfdPostpaidSum/dataSize));


		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}

		dataList = viewList;

    	return SUCCESS;
    }

	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="付費刊登-聯播網客戶-統計日報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {
    			"日期","星期",
    			"廣告曝光數","廣告互動數","互動率","單次互動費用","每千次曝光費用",
    			"直客廣告客戶數","業務廣告客戶數","經銷商廣告客戶數",
    			"直客互動費用","業務互動費用","經銷商互動費用",
    			"直客每日預算","業務每日預算","經銷商每日預算",
    			"直客預算達成率","業務預算達成率","經銷商預算達成率",
    			"營運費用(超播)","惡意點擊數","惡意點擊費用",
    			"預付儲值廣告金(儲值筆數)","預付儲值廣告金(儲值金額)",
    			"PChome 營運費用(儲值金)","PChome 營運費用(贈送金)","PChome 營運費用(後付費)",
    			"PFB 分潤(儲值金)","PFB 分潤(贈送金)","PFB 分潤(後付費)","PFD 佣金(儲值金)","PFD 佣金(贈送金)","PFD 佣金(後付費)"
    			};

    	StringBuffer content=new StringBuffer();

    	content.append("報表名稱:,付費刊登-聯播網客戶-統計日報表");
		content.append("\n\n");
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");

		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");

		for (int i=0; i<dataList.size(); i++) {
			AdmClientCountReportVO vo = dataList.get(i);

			content.append("\"" + vo.getCountDate() + "\",");
			content.append("\"" + vo.getWeek() + "\",");
			content.append("\"" + vo.getAdPv() + "\",");
			content.append("\"" + vo.getAdClk() + "\",");
			content.append("\"" + vo.getClkRate() + "%\",");
			content.append("\"$ " + vo.getAdClkPriceAvg() + "\",");
			content.append("\"$ " + vo.getAdPvPrice() + "\",");
			content.append("\"" + vo.getPfpClientCount() + "\",");
			content.append("\"" + vo.getSalesClientCount() + "\",");
			content.append("\"" + vo.getPfdClientCount() + "\",");
			content.append("\"$ " + vo.getPfpAdClkPrice() + "\",");
			content.append("\"$ " + vo.getSalesAdClkPrice() + "\",");
			content.append("\"$ " + vo.getPfdAdClkPrice() + "\",");
			content.append("\"$ " + vo.getPfpAdActionMaxPrice() + "\",");
			content.append("\"$ " + vo.getSalesAdActionMaxPrice() + "\",");
			content.append("\"$ " + vo.getPfdAdActionMaxPrice() + "\",");
			content.append("\"" + vo.getPfpSpendRate() + "%\",");
			content.append("\"" + vo.getSalesSpendRate() + "%\",");
			content.append("\"" + vo.getPfdSpendRate() + "%\",");
			content.append("\"$ " + vo.getLossCost() + "\",");
			content.append("\"" + vo.getAdInvalidClk() + "\",");
			content.append("\"$ " + vo.getAdInvalidClkPrice() + "\",");
			content.append("\"" + vo.getTotalSaveCount() + "\",");
			content.append("\"$ " + vo.getTotalSavePrice() + "\",");
			content.append("\"$ " + vo.getPfpSave() + "\",");
			content.append("\"$ " + vo.getPfpFree() + "\",");
			content.append("\"$ " + vo.getPfpPostpaid() + "\",");
			content.append("\"$ " + vo.getPfbSave() + "\",");
			content.append("\"$ " + vo.getPfbFree() + "\",");
			content.append("\"$ " + vo.getPfbPostpaid() + "\",");
			content.append("\"$ " + vo.getPfdSave() + "\",");
			content.append("\"$ " + vo.getPfdFree() + "\",");
			content.append("\"$ " + vo.getPfdPostpaid() + "\",");
//			content.append("\"" + vo.getPfpAdCount() + "\",");
//			content.append("\"" + vo.getPfdAdCount() + "\",");
			content.append("\n");
		}
		content.append("\n");

		//加總
		content.append("\"加總\",");
		content.append("\"" + "\",");
		content.append("\"" + totalVO.getAdPv() + "\",");
		content.append("\"" + totalVO.getAdClk() + "\",");
		content.append("\"" + totalVO.getClkRate() + "%\",");
		content.append("\"$ " + totalVO.getAdClkPriceAvg() + "\",");
		content.append("\"$ " + totalVO.getAdPvPrice() + "\",");
		content.append("\"" + totalVO.getPfpClientCount() + "\",");
		content.append("\"" + totalVO.getSalesClientCount() + "\",");
		content.append("\"" + totalVO.getPfdClientCount() + "\",");
		content.append("\"$ " + totalVO.getPfpAdClkPrice() + "\",");
		content.append("\"$ " + totalVO.getSalesAdClkPrice() + "\",");
		content.append("\"$ " + totalVO.getPfdAdClkPrice() + "\",");
		content.append("\"$ " + totalVO.getPfpAdActionMaxPrice() + "\",");
		content.append("\"$ " + totalVO.getSalesAdActionMaxPrice() + "\",");
		content.append("\"$ " + totalVO.getPfdAdActionMaxPrice() + "\",");
		content.append("\"" + totalVO.getPfpSpendRate() + "%\",");
		content.append("\"" + totalVO.getSalesSpendRate() + "%\",");
		content.append("\"" + totalVO.getPfdSpendRate() + "%\",");
		content.append("\"$ " + totalVO.getLossCost() + "\",");
		content.append("\"" + totalVO.getAdInvalidClk() + "\",");
		content.append("\"$ " + totalVO.getAdInvalidClkPrice() + "\",");
		content.append("\"" + totalVO.getTotalSaveCount() + "\",");
		content.append("\"$ " + totalVO.getTotalSavePrice() + "\",");
		content.append("\"$ " + totalVO.getPfpSave() + "\",");
		content.append("\"$ " + totalVO.getPfpFree() + "\",");
		content.append("\"$ " + totalVO.getPfpPostpaid() + "\",");
		content.append("\"$ " + totalVO.getPfbSave() + "\",");
		content.append("\"$ " + totalVO.getPfbFree() + "\",");
		content.append("\"$ " + totalVO.getPfbPostpaid() + "\",");
		content.append("\"$ " + totalVO.getPfdSave() + "\",");
		content.append("\"$ " + totalVO.getPfdFree() + "\",");
		content.append("\"$ " + totalVO.getPfdPostpaid() + "\",");
		content.append("\n");
//		content.append("\"" + totalVO.getPfpAdCount() + "\",");
//		content.append("\"" + totalVO.getPfdAdCount() + "\",");
		
		//平均
		content.append("\"平均\",");
		content.append("\"" + "\",");
		content.append("\"" + avgVO.getAdPv() + "\",");
		content.append("\"" + avgVO.getAdClk() + "\",");
		content.append("\"" + avgVO.getClkRate() + "%\",");
		content.append("\"$ " + avgVO.getAdClkPriceAvg() + "\",");
		content.append("\"$ " + avgVO.getAdPvPrice() + "\",");
		content.append("\"" + avgVO.getPfpClientCount() + "\",");
		content.append("\"" + avgVO.getSalesClientCount() + "\",");
		content.append("\"" + avgVO.getPfdClientCount() + "\",");
		content.append("\"$ " + avgVO.getPfpAdClkPrice() + "\",");
		content.append("\"$ " + avgVO.getSalesAdClkPrice() + "\",");
		content.append("\"$ " + avgVO.getPfdAdClkPrice() + "\",");
		content.append("\"$ " + avgVO.getPfpAdActionMaxPrice() + "\",");
		content.append("\"$ " + avgVO.getSalesAdActionMaxPrice() + "\",");
		content.append("\"$ " + avgVO.getPfdAdActionMaxPrice() + "\",");
		content.append("\"" + avgVO.getPfpSpendRate() + "%\",");
		content.append("\"" + avgVO.getSalesSpendRate() + "%\",");
		content.append("\"" + avgVO.getPfdSpendRate() + "%\",");
		content.append("\"$ " + avgVO.getLossCost() + "\",");
		content.append("\"" + avgVO.getAdInvalidClk() + "\",");
		content.append("\"$ " + avgVO.getAdInvalidClkPrice() + "\",");
		content.append("\"" + avgVO.getTotalSaveCount() + "\",");
		content.append("\"$ " + avgVO.getTotalSavePrice() + "\",");
		content.append("\"$ " + avgVO.getPfpSave() + "\",");
		content.append("\"$ " + avgVO.getPfpFree() + "\",");
		content.append("\"$ " + avgVO.getPfpPostpaid() + "\",");
		content.append("\"$ " + avgVO.getPfbSave() + "\",");
		content.append("\"$ " + avgVO.getPfbFree() + "\",");
		content.append("\"$ " + avgVO.getPfbPostpaid() + "\",");
		content.append("\"$ " + avgVO.getPfdSave() + "\",");
		content.append("\"$ " + avgVO.getPfdFree() + "\",");
		content.append("\"$ " + avgVO.getPfdPostpaid() + "\",");
		content.append("\n");

		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));

	}

	public void setAdmClientCountReportService(IAdmClientCountReportService admClientCountReportService) {
		this.admClientCountReportService = admClientCountReportService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public AdmClientCountReportVO getTotalVO() {
		return totalVO;
	}

	public void setTotalVO(AdmClientCountReportVO totalVO) {
		this.totalVO = totalVO;
	}

	public IAdmClientCountReportService getAdmClientCountReportService() {
		return admClientCountReportService;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public AdmClientCountReportVO getAvgVO() {
		return avgVO;
	}

	public List<AdmClientCountReportVO> getDataList() {
		return dataList;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFlag(String downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

}
