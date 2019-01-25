package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.report.IAdSourceReportService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class AdmAdSourceDayReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private IAdSourceReportService adSourceReportService;
	
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

	private PfpAdReportVO totalVO;					//總計
	private PfpAdReportVO avgVO;					//平均
	private List<PfpAdReportVO> dataList;
	private List<PfpAdReportVO> sumList;
	
	public String execute() throws Exception {
    	
		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		totalVO = new PfpAdReportVO();
		avgVO = new PfpAdReportVO();
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);
    	conditionMap.put("emailReport", "yes");
		
		dataList = adSourceReportService.getAdSourceReportByCondition(conditionMap);
		sumList = adSourceReportService.getAdSourceReportSumByCondition(conditionMap);
		
		log.info("dataList: "+dataList.size());
		
		if(dataList.size()==0){
    		this.message = "查無資料！";
    		return SUCCESS;
    	}
		
		List<PfpAdReportVO> viewList = new ArrayList<PfpAdReportVO>();
		totalCount = dataList.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		totalSize = totalCount;
		
		for (int i=0; i<dataList.size(); i++) {
			PfpAdReportVO vo = dataList.get(i);
			
			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
				viewList.add(vo);
			}
		}
		
		Double dataSize = new Double(sumList.size());	//總比數
		Double adClkPriceSum = new Double(0);			//廣告點擊費用
		Double adPvSum = new Double(0);					//廣告曝光數
		Double adClkSum = new Double(0);				//廣告點擊數 
		Double clkRateSum = new Double(0);				//廣告點擊率
		Double adClkAvgPriceSum = new Double(0);		//單次點擊收益
		Double adPvRateSum = new Double(0);				//千次曝光收益
		Double invalidBonusSum = new Double(0);			//惡意點擊收益
		Double totalBonusSum = new Double(0);			//PFB分潤
		
		if(!sumList.isEmpty()){
			for(PfpAdReportVO vo:sumList){
				adClkPriceSum += new Double(vo.getAdClkPriceSum().replaceAll(",", ""));
				adPvSum += new Double(vo.getAdPvSum().replaceAll(",", ""));
				adClkSum += new Double(vo.getAdClkSum().replaceAll(",", ""));
				invalidBonusSum += new Double(vo.getInvalidBonus().replaceAll(",", ""));
				totalBonusSum += new Double(vo.getTotalBonus().replaceAll(",", ""));
			}
		}
		
		//點擊率
		if(adPvSum != 0 && adClkSum != 0){
			clkRateSum = (adClkSum / adPvSum)*100;
		}
		
		//單次點擊收益
		if(adClkSum != 0 && totalBonusSum != 0){
			adClkAvgPriceSum = totalBonusSum / adClkSum;
		}
		
		//千次曝光收益
		if(adPvSum != 0 && totalBonusSum != 0){
			adPvRateSum = (totalBonusSum / adPvSum)*1000;
		}
		
		//總計
		totalVO.setAdClkPriceSum(df1.format(adClkPriceSum));
		totalVO.setAdPvSum(df1.format(adPvSum));
		totalVO.setAdClkSum(df1.format(adClkSum));
		totalVO.setAdClkRate(df2.format(clkRateSum));
		totalVO.setAdClkAvgPrice(df2.format(adClkAvgPriceSum));
		totalVO.setAdPvRate(df2.format(adPvRateSum));
		totalVO.setInvalidBonus(df2.format(invalidBonusSum));
		totalVO.setTotalBonus(df2.format(totalBonusSum));
		
		//平均
		avgVO.setAdClkPriceSum(df1.format(adClkPriceSum/dataSize));
		avgVO.setAdPvSum(df1.format(adPvSum/dataSize));
		avgVO.setAdClkSum(df1.format(adClkSum/dataSize));
		avgVO.setAdClkRate(df2.format(clkRateSum));
		avgVO.setAdClkAvgPrice(df2.format(adClkAvgPriceSum));
		avgVO.setAdPvRate(df2.format(adPvRateSum));
		avgVO.setInvalidBonus(df2.format(invalidBonusSum/dataSize));
		avgVO.setTotalBonus(df2.format(totalBonusSum/dataSize));
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		dataList = viewList;
		
		return SUCCESS;
	}
	
	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="付費刊登-聯播網網站-統計日報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"日期","聯播網帳戶名稱","廣告聯播網網站","廣告客戶數","廣告點擊費用","廣告曝光數","廣告點擊數","點擊率","單次點擊收益","每千次曝光收益","惡意點擊收益","預估收益"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,付費刊登-聯播網網站-統計日報表");
		content.append("\n\n");
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
    	
		for (int i=0; i<dataList.size(); i++) {
			PfpAdReportVO vo = dataList.get(i);
			
			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getPfbCustomerInfoName() + "\",");
			content.append("\"" + vo.getPfbWebsiteChineseName() + "\",");
			content.append("\"" + vo.getPfpCustomerInfoId() + "\",");
			content.append("\"$ " + vo.getAdClkPriceSum() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvRate() + "\",");
			content.append("\"$ " + vo.getInvalidBonus() + "\",");
			content.append("\"$ " + vo.getTotalBonus() + "\",");
			content.append("\n");
		}
		
		content.append("\n");
		
		//小計
		content.append("\"小計\",");
		for(int j=1;j<tableHeadArray.length;j++){
			String s = tableHeadArray[j];
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<sumList.size(); i++) {
			PfpAdReportVO vo = sumList.get(i);
			
			content.append("\"\",");
			content.append("\"" + vo.getPfbCustomerInfoName() + "\",");
			content.append("\"\",");
			content.append("\"\",");
			content.append("\"$ " + vo.getAdClkPriceSum() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvRate() + "\",");
			content.append("\"$ " + vo.getInvalidBonus() + "\",");
			content.append("\"$ " + vo.getTotalBonus() + "\",");
			content.append("\n");
		}
		
		content.append("\n");
		
		//加總
		content.append("\"加總\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"$ " + totalVO.getAdClkPriceSum() + "\",");
		content.append("\"" + totalVO.getAdPvSum() + "\",");
		content.append("\"" + totalVO.getAdClkSum() + "\",");
		content.append("\"" + totalVO.getAdClkRate() + "%\",");
		content.append("\"$ " + totalVO.getAdClkAvgPrice() + "\",");
		content.append("\"$ " + totalVO.getAdPvRate() + "\",");
		content.append("\"$ " + totalVO.getInvalidBonus() + "\",");
		content.append("\"$ " + totalVO.getTotalBonus() + "\",");
		content.append("\n");
		
		//平均
		content.append("\"平均\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"$ " + avgVO.getAdClkPriceSum() + "\",");
		content.append("\"" + avgVO.getAdPvSum() + "\",");
		content.append("\"" + avgVO.getAdClkSum() + "\",");
		content.append("\"" + avgVO.getAdClkRate() + "%\",");
		content.append("\"$ " + avgVO.getAdClkAvgPrice() + "\",");
		content.append("\"$ " + avgVO.getAdPvRate() + "\",");
		content.append("\"$ " + avgVO.getInvalidBonus() + "\",");
		content.append("\"$ " + avgVO.getTotalBonus() + "\",");
		content.append("\n");
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		
	}

	public void setAdSourceReportService(IAdSourceReportService adSourceReportService) {
		this.adSourceReportService = adSourceReportService;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
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

	public PfpAdReportVO getTotalVO() {
		return totalVO;
	}

	public PfpAdReportVO getAvgVO() {
		return avgVO;
	}

	public List<PfpAdReportVO> getDataList() {
		return dataList;
	}

	public List<PfpAdReportVO> getSumList() {
		return sumList;
	}
	
}
