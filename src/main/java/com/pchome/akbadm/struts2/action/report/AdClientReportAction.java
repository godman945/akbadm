package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.customerInfo.IPfdCustomerInfoService;
import com.pchome.akbadm.db.service.report.IAdClientReportService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;
import com.pchome.akbadm.struts2.BaseAction;

@SuppressWarnings("serial")
public class AdClientReportAction extends BaseAction {

	private IAdClientReportService adClientReportService;
	private IPfdCustomerInfoService pfdCustomerInfoService;

	//輸入參數
	private String startDate;
	private String endDate;
	private String style;
	private String searchCategory;
	private String searchOption;
	private String searchText;
	private String pfdCustomerInfoId;
	private String pfpCustomerInfoId;
	private String adPvclkDate;
	private String pfpCustomerInfoName;
	private String searchAdDevice;

	private Map<String, String> pfdCustomerInfoIdMap;
	private Map<String,String> adDeviceMap;

	private List<PfpAdReportVO> voList = new ArrayList<PfpAdReportVO>();
	//訊息
	private String message = "";

	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 50;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private long totalCount = 0;   				// 初始化共幾筆

	private long totalSize = 0;					//總比數
	private long totalPv = 0;					//曝光數
	private long totalClk = 0;					//點擊數
	private float totalClkRate = 0;				//點擊率
	private float totalAdClkAvgPrice = 0;		//單次點擊收益
	private float totalAdPvRate = 0;			//千次曝光收益
	private float totalAdClkPriceSum = 0;		//總廣告花費
	private BigDecimal totalBonus = new BigDecimal(0);				//預估收益
	private double totalConvertCountSum =0;				//轉換次數總計
	private double totalConvertPriceCountSum =0;		//總轉換價值總計	
	private double totalConvertCVR = 0;					//轉換率總計	
	private double totalConvertCost = 0;				//平均轉換成本總計
	private double totalConvertInvestmentCost = 0;		//廣告投資報酬率總計

	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名

	@Override
    public String execute() throws Exception {

		pfdCustomerInfoIdMap();
		setDeviceMap();
    	return SUCCESS;
    }

	public String getAdClientReport() throws Exception {
		pfdCustomerInfoIdMap();
		setDeviceMap();
		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchOption = " + searchOption);
		log.info(">>> searchText = " + searchText);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> searchAdDevice = " + searchAdDevice);

		Map<String, String> conditionMap = new HashMap<String, String>();

    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

		conditionMap = new HashMap<String, String>();
    	conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);

    	if(StringUtils.isNotBlank(pfdCustomerInfoId)){
    		conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
    	}

    	if(StringUtils.isNotBlank(searchOption)){
    		if (StringUtils.isNotBlank(searchText)){
    			log.info("searchOption: "+searchOption);
    			log.info("searchText: "+searchText);
    			conditionMap.put(searchOption, searchText);
    		}
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			conditionMap.put("searchAll", searchText);
    		}
    	}

    	if(StringUtils.isNotBlank(searchAdDevice) && !StringUtils.equals("allDevice", searchAdDevice)){
    		conditionMap.put("searchAdDevice", searchAdDevice);
    	}

    	voList = new ArrayList<PfpAdReportVO>();
    	voList = adClientReportService.getAdClientReportByCondition(conditionMap);
    	log.info("voList: "+voList.size());

    	if(voList.size()==0){

    		this.message = "查無資料！";
    	}else{
    		List<PfpAdReportVO> viewList = new ArrayList<PfpAdReportVO>();

    		totalCount = voList.size();
    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
    		totalSize = totalCount;
    		
    		
    		
    		for (int i=0; i<voList.size(); i++) {
    			PfpAdReportVO vo = new PfpAdReportVO();
    			vo = voList.get(i);
    			log.info("getAdPvSum: "+vo.getAdPvSum());

    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
    				viewList.add(vo);
    			}

    			if(vo.getAdPvSum() != null){
    				totalPv += Integer.parseInt(vo.getAdPvSum().replaceAll(",", ""));
    			}
    			if(vo.getAdClkSum() != null){
    				totalClk += Integer.parseInt(vo.getAdClkSum().replaceAll(",", ""));
    			}
    			if(vo.getAdClkPriceSum() != null){
    				totalAdClkPriceSum += Double.parseDouble(vo.getAdClkPriceSum().replaceAll(",", ""));
    			}

				if (totalClk==0 || totalPv==0) {
					totalClkRate = 0;
				} else {
					totalClkRate = (float) ((double) totalClk*100/totalPv);
				}

				if (totalAdClkPriceSum==0 || totalClk==0) {
					totalAdClkAvgPrice = 0;
				} else {
					totalAdClkAvgPrice = totalAdClkPriceSum/totalClk;
				}

                if(totalAdClkPriceSum==0 || totalPv==0){
                    totalAdPvRate = 0;
                } else {
                    totalAdPvRate = totalAdClkPriceSum*1000/totalPv;
                }
                
                
                //總轉換數
                BigDecimal convertCountBigDecimal =  new BigDecimal(vo.getConvertCountSum());
                totalConvertCountSum += convertCountBigDecimal.doubleValue();
                
                //總轉換價值
                BigDecimal convertPriceCountBigDecimal =  new BigDecimal(vo.getConvertPriceCountSum());
                totalConvertPriceCountSum += convertPriceCountBigDecimal.doubleValue();
			}
    		
    		//轉換率=轉換次數/互動數*100%
            if(totalConvertCountSum > 0 && totalClk > 0){
            	totalConvertCVR  = (totalConvertCountSum / totalClk) * 100;
			}
            //平均轉換成本=費用/轉換次數
            if(totalAdClkPriceSum > 0 && totalConvertCountSum > 0){
            	totalConvertCost = totalAdClkPriceSum / totalConvertCountSum;
			}
            //廣告投資報酬率=總轉換價值/費用*100%
			if(totalConvertPriceCountSum > 0 && totalAdClkPriceSum > 0){
				totalConvertInvestmentCost = (totalConvertPriceCountSum / totalAdClkPriceSum) * 100;
			}
			
			

	    	voList = viewList;
    	}

		return SUCCESS;
	}

	public String detalReportData() throws Exception {
		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchOption = " + searchOption);
		log.info(">>> searchText = " + searchText);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> pfpCustomerInfoId = " + pfpCustomerInfoId);
		log.info(">>> adPvclkDate = " + adPvclkDate);
		log.info(">>> searchAdDevice = " + searchAdDevice);

		Map<String, String> conditionMap = new HashMap<String, String>();

    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

		conditionMap = new HashMap<String, String>();
    	conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);

    	if(StringUtils.isNotBlank(pfdCustomerInfoId)){
    		conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
    	}

    	if(StringUtils.isNotBlank(pfpCustomerInfoId)){
    		conditionMap.put("pfpCustomerInfoId", pfpCustomerInfoId);
    	}

    	if(StringUtils.isNotBlank(adPvclkDate)){
    		conditionMap.put("adPvclkDate", adPvclkDate);
    	}

    	if(StringUtils.isNotBlank(searchOption)){
    		if (StringUtils.isNotBlank(searchText)){
    			log.info("searchOption: "+searchOption);
    			log.info("searchText: "+searchText);
    			conditionMap.put(searchOption, searchText);
    		}
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			conditionMap.put("searchAll", searchText);
    		}
    	}

    	if(StringUtils.isNotBlank(searchAdDevice) && !StringUtils.equals("allDevice", searchAdDevice)){
    		conditionMap.put("searchAdDevice", searchAdDevice);
    	}

    	voList = new ArrayList<PfpAdReportVO>();
    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");

    	voList = adClientReportService.getAdClientDetalReportByCondition(conditionMap);

    	log.info("voList: "+voList.size());

    	if(voList.size()==0){

    		this.message = "查無資料！";
    	}else{

    		totalCount = voList.size();
    		totalSize = totalCount;
    		double doubleTotalBonus = 0;

    		for (int i=0; i<voList.size(); i++) {
    			PfpAdReportVO vo = new PfpAdReportVO();
    			vo = voList.get(i);
    			log.info("getAdPvSum: "+vo.getAdPvSum());

    			if(vo.getAdPvSum() != null){
    				totalPv += Integer.parseInt(vo.getAdPvSum().replaceAll(",", ""));
    			}
    			if(vo.getAdClkSum() != null){
    				totalClk += Integer.parseInt(vo.getAdClkSum().replaceAll(",", ""));
    			}
    			if(vo.getTotalBonus() != null){
    				totalBonus = totalBonus.add(new BigDecimal(vo.getTotalBonus().replaceAll(",", "")));
    				doubleTotalBonus = Double.parseDouble(totalBonus.toString());
    				vo.setTotalBonus(df3.format(Double.parseDouble(vo.getTotalBonus())));
    			}

				if (totalClk==0 || totalPv==0) {
					totalClkRate = 0;
				} else {
					totalClkRate = (float) ((double) totalClk*100/totalPv);
				}

				if (doubleTotalBonus==0 || totalClk==0) {
					totalAdClkAvgPrice = 0;
				} else {
					totalAdClkAvgPrice = (float) doubleTotalBonus/totalClk;
				}

				if(doubleTotalBonus==0 || totalPv==0){
					totalAdPvRate = 0;
				} else {
					totalAdPvRate = (float) doubleTotalBonus*1000/totalPv;
				}
				
				//總轉換數
	            BigDecimal convertCountBigDecimal =  new BigDecimal(vo.getConvertCountSum());
	            totalConvertCountSum += convertCountBigDecimal.doubleValue();
	            
	            //總轉換價值
	            BigDecimal convertPriceCountBigDecimal =  new BigDecimal(vo.getConvertPriceCountSum());
	            totalConvertPriceCountSum += convertPriceCountBigDecimal.doubleValue();
			}
		
			//轉換率=轉換次數/互動數*100%
	        if(totalConvertCountSum > 0 && totalClk > 0){
	        	totalConvertCVR  = (totalConvertCountSum / totalClk) * 100;
			}
	        //平均轉換成本=費用/轉換次數
	        if(doubleTotalBonus > 0 && totalConvertCountSum > 0){
	        	totalConvertCost = doubleTotalBonus / totalConvertCountSum;
			}
	        //廣告投資報酬率=總轉換價值/費用*100%
			if(totalConvertPriceCountSum > 0 && doubleTotalBonus > 0){
				totalConvertInvestmentCost = (totalConvertPriceCountSum / doubleTotalBonus) * 100;
			}
    		
    	}



    	if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}

		return SUCCESS;
	}

	public void pfdCustomerInfoIdMap() {
		List<PfdCustomerInfo> list = pfdCustomerInfoService.loadAll();

		pfdCustomerInfoIdMap = new TreeMap<String, String>();
		for(PfdCustomerInfo data : list){
			pfdCustomerInfoIdMap.put(data.getCustomerInfoId(), data.getCompanyName());
		}
	}

	private void makeDownloadReportData() throws Exception {
    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告客戶成效報表_" + dformat.format(new Date()) + ".csv";
    	DecimalFormat df = new DecimalFormat("0.00");
    	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    	String[] tableHeadArray = {"期間","帳戶編號","經銷商","廣告客戶","帳戶名稱","主網站名稱","網站名稱","時段區間","廣告曝光數","廣告點擊數","廣告點擊率","單次點擊收益","千次曝光收益","預估收益","轉換次數","轉換率","總轉換價值","平均轉換成本","廣告投資報酬率"};

    	StringBuffer content=new StringBuffer();

    	content.append("報表名稱:,廣告客戶成效");
		content.append("\n\n");

		content.append("經銷商:,");
		if(StringUtils.isNotBlank(pfdCustomerInfoId)){
			PfdCustomerInfo pfdCustomerInfo = pfdCustomerInfoService.get(pfdCustomerInfoId);
			content.append(pfdCustomerInfo.getCompanyName());
		} else {
			content.append("全部");
		}
		content.append("\n\n");

		if(StringUtils.isNotBlank(searchOption)){
    		if (StringUtils.isNotBlank(searchText)){
    			if("customerInfoId".equals(searchOption)){
    				content.append("帳戶編號:,");
    			} else {
    				content.append("廣告客戶:,");
    			}
    			content.append(searchText);
    			content.append("\n\n");
    		}
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			content.append("帳戶編號/廣告客戶:,");
    			content.append(searchText);
    			content.append("\n\n");
    		}
    	}
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");

		content.append("裝置查詢:,");
		if(StringUtils.isNotBlank(searchAdDevice) && !StringUtils.equals("allDevice", searchAdDevice)){
			if(StringUtils.equals("PC", searchAdDevice)){
				content.append("電腦");
			} else{
				content.append("行動裝置");
			}
		} else {
			content.append("全部裝置");
		}
		content.append("\n\n");

		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");

		for (int i=0; i<voList.size(); i++) {
			PfpAdReportVO vo = new PfpAdReportVO();
			vo = voList.get(i);
			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getPfpCustomerInfoId() + "\",");
			content.append("\"" + vo.getPfdCustomerInfoName() + "\",");
			content.append("\"" + vo.getPfpCustomerInfoName() + "\",");
			content.append("\"" + vo.getPfbCustomerInfoName() + "\",");
			content.append("\"" + vo.getPfbDefaultWebsiteChineseName() + "\",");
			content.append("\"" + vo.getPfbWebsiteChineseName() + "\",");
			content.append("\"" + vo.getTimeCode() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvRate() + "\",");
			content.append("\"$ " + vo.getTotalBonus() + "\",");
			content.append("\"" + vo.getConvertCountSum() + "\",");//轉換次數
			content.append("\"" + vo.getConvertCVR() + "\",");//轉換率
			content.append("\"" + vo.getConvertPriceCountSum() + "\",");//總轉換價值
			content.append("\"" + vo.getConvertCost() + "\","); //平均轉換成本
			content.append("\"" + vo.getConvertInvestmentCost() + "\"");//廣告投資報酬率
			content.append("\n");
		}

		//總計
		content.append("\"總計:" + totalSize + "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" + df2.format(totalPv) + "\",");
		content.append("\"" + df2.format(totalClk) + "\",");
		content.append("\"" + df.format(totalClkRate) + "%\",");
		content.append("\"$ " + df3.format(totalAdClkAvgPrice) + "\",");
		content.append("\"$ " + df3.format(totalAdPvRate) + "\",");
		content.append("\"$ " + df3.format(totalBonus) + "\",");
		
		content.append("\"" + df2.format(totalConvertCountSum) + "\",");		//轉換次數總計
		content.append("\"" + df.format(totalConvertCVR) + "%\",");				//轉換率總計	
		content.append("\"" + df2.format(totalConvertPriceCountSum) + "\",");	//總轉換價值總計
		content.append("\"" + df.format(totalConvertCost) + "\",");				//平均轉換成本總計
		content.append("\"" + df.format(totalConvertInvestmentCost) + "%\"");	//廣告投資報酬率總計


		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
    }

	private void setDeviceMap(){
		adDeviceMap = new LinkedHashMap<String,String>();

		adDeviceMap.put("allDevice", "全部裝置");
		adDeviceMap.put("PC", "電腦");
		adDeviceMap.put("mobile", "行動裝置");
	}

	public void setAdClientReportService(IAdClientReportService adClientReportService) {
		this.adClientReportService = adClientReportService;
	}

	public void setPfdCustomerInfoService(
			IPfdCustomerInfoService pfdCustomerInfoService) {
		this.pfdCustomerInfoService = pfdCustomerInfoService;
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

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public Map<String, String> getPfdCustomerInfoIdMap() {
		return pfdCustomerInfoIdMap;
	}

	public Map<String, String> getAdDeviceMap() {
		return adDeviceMap;
	}

	public List<PfpAdReportVO> getVoList() {
		return voList;
	}

	public String getMessage() {
		return message;
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

	public long getTotalSize() {
		return totalSize;
	}

	public long getTotalPv() {
		return totalPv;
	}

	public long getTotalClk() {
		return totalClk;
	}

	public float getTotalClkRate() {
		return totalClkRate;
	}

	public float getTotalAdClkAvgPrice() {
		return totalAdClkAvgPrice;
	}

	public float getTotalAdPvRate() {
		return totalAdPvRate;
	}

	public float getTotalAdClkPriceSum() {
		return totalAdClkPriceSum;
	}

	public BigDecimal getTotalBonus() {
		return totalBonus;
	}

	public String getDownloadFlag() {
		return downloadFlag;
	}

	public void setDownloadFlag(String downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}

	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}

	public String getAdPvclkDate() {
		return adPvclkDate;
	}

	public void setAdPvclkDate(String adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
	}

	public String getPfpCustomerInfoName() {
		return pfpCustomerInfoName;
	}

	public void setPfpCustomerInfoName(String pfpCustomerInfoName) {
		this.pfpCustomerInfoName = pfpCustomerInfoName;
	}

	public String getSearchAdDevice() {
		return searchAdDevice;
	}

	public void setSearchAdDevice(String searchAdDevice) {
		this.searchAdDevice = searchAdDevice;
	}

	public double getTotalConvertCountSum() {
		return totalConvertCountSum;
	}

	public void setTotalConvertCountSum(double totalConvertCountSum) {
		this.totalConvertCountSum = totalConvertCountSum;
	}

	public double getTotalConvertPriceCountSum() {
		return totalConvertPriceCountSum;
	}

	public void setTotalConvertPriceCountSum(double totalConvertPriceCountSum) {
		this.totalConvertPriceCountSum = totalConvertPriceCountSum;
	}

	public double getTotalConvertCVR() {
		return totalConvertCVR;
	}

	public void setTotalConvertCVR(double totalConvertCVR) {
		this.totalConvertCVR = totalConvertCVR;
	}

	public double getTotalConvertCost() {
		return totalConvertCost;
	}

	public void setTotalConvertCost(double totalConvertCost) {
		this.totalConvertCost = totalConvertCost;
	}

	public double getTotalConvertInvestmentCost() {
		return totalConvertInvestmentCost;
	}

	public void setTotalConvertInvestmentCost(double totalConvertInvestmentCost) {
		this.totalConvertInvestmentCost = totalConvertInvestmentCost;
	}

}
