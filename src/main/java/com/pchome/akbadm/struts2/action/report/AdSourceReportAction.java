package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.service.report.IAdSourceReportService;
import com.pchome.akbadm.db.vo.report.PfpAdReportVO;
import com.pchome.akbadm.struts2.BaseAction;

@SuppressWarnings("serial")
public class AdSourceReportAction extends BaseAction {

	private IAdSourceReportService adSourceReportService;
	
	//輸入參數
	private String startDate;
	private String endDate;
	private String style;
	private String searchCategory;
	private String searchOption;
	private String searchText;
	private String pfbCustomerInfoId;
	private String pfbCustomerInfoName;
	private String adPvclkDate;
	private String searchAdDevice;
	private String allowUrl;
	private String allowUrlName;
	
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
	private float totalBonus = 0;				//預估收益
	
	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	public String execute() throws Exception {
		setDeviceMap();
    	return SUCCESS;
    }
	
	public String getAdSourceReport() throws Exception {
		setDeviceMap();
		
		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchOption = " + searchOption);
		log.info(">>> searchText = " + searchText);
		log.info(">>> category = " + searchCategory);
		log.info(">>> adDevice = " + searchAdDevice);
		
		Map<String, String> conditionMap = new HashMap<String, String>();
    	
    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

    	conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);
    	
    	if(StringUtils.isNotBlank(searchCategory)){
    		conditionMap.put("category", searchCategory);
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
    	voList = adSourceReportService.getAdSourceReportByCondition(conditionMap);
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
    			if(vo.getTotalBonus() != null){
    				totalBonus += Double.parseDouble(vo.getTotalBonus().replaceAll(",", ""));
    			}
				
				if (totalClk==0 || totalPv==0) {
					totalClkRate = 0;
				} else {
					totalClkRate = (float) ((double) totalClk*100/totalPv);
				}
				
				if (totalBonus==0 || totalClk==0) {
					totalAdClkAvgPrice = 0;
				} else {
					totalAdClkAvgPrice = (float) totalBonus/totalClk;
				}
				
				if(totalBonus==0 || totalPv==0){
					totalAdPvRate = 0;
				} else {
					totalAdPvRate = (float) totalBonus*1000/totalPv;
				}
				
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
		log.info(">>> searchCategory = " + searchCategory);
		log.info(">>> pfbCustomerInfoId = " + pfbCustomerInfoId);
		log.info(">>> adPvclkDate = " + adPvclkDate);
		log.info(">>> allowUrl = " + allowUrl);
		log.info(">>> allowUrlName = " + allowUrlName);
		
		Map<String, String> conditionMap = new HashMap<String, String>();
    	
    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);
	    	
	    	if(StringUtils.isNotBlank(searchCategory)){
	    		conditionMap.put("category", searchCategory);
	    	}
	    	
	    	if(StringUtils.isNotBlank(pfbCustomerInfoId)){
	    		conditionMap.put("pfbCustomerInfoId", pfbCustomerInfoId);
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
	    	
	    	if(StringUtils.isNotBlank(allowUrl)){
	    		conditionMap.put("allowUrl", allowUrl);
	    	}
	    	
	    	if(StringUtils.isNotBlank(allowUrlName)){
	    		conditionMap.put("allowUrlName", allowUrlName);
	    	}
		}
    	
    	voList = new ArrayList<PfpAdReportVO>();
    	
    	voList = adSourceReportService.getAdSourceDetalReportByCondition(conditionMap);
    	
    	log.info("voList: "+voList.size());
    	
    	if(voList.size()==0){
    		
    		this.message = "查無資料！";
    	}else{
    		
    		totalCount = voList.size();
    		totalSize = totalCount;
    		
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
					totalAdClkAvgPrice = (float) totalAdClkPriceSum/totalClk;
				}
				
			}
    	}
		
    	
    	
    	if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
    	
		return SUCCESS;
	}
	
	private void makeDownloadReportData() throws Exception {
    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告來源成效報表_" + dformat.format(new Date()) + ".csv";
    	DecimalFormat df = new DecimalFormat("0.00");
    	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    	String[] tableHeadArray = {"期間","帳戶編號","帳戶類別","帳戶名稱","主網站名稱","網站名稱","廣告客戶","時段區間","廣告曝光數","廣告點擊數","廣告點擊率","平均點擊費用","廣告費用"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,廣告來源成效");
		content.append("\n\n");
		
		content.append("帳戶類別:,");
		if(StringUtils.isNotBlank(searchCategory)){
			if("1".equals(searchCategory)){
				content.append("個人戶");
			} else {
				content.append("公司戶");
			}
		} else {
			content.append("全部");
		}
		content.append("\n\n");	
		
		if(StringUtils.isNotBlank(searchOption)){
    		if (StringUtils.isNotBlank(searchText)){
    			if("customerInfoId".equals(searchOption)){
    				content.append("帳戶編號:,");
    			} else {
    				content.append("網站名稱:,");
    			}
    			content.append(searchText);
    			content.append("\n\n");
    		}		
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			content.append("帳戶編號/網站名稱:,");
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
			content.append("\"" + vo.getPfbCustomerInfoId() + "\",");
			content.append("\"" + vo.getPfbCategory() + "\",");
			content.append("\"" + vo.getPfbCustomerInfoName() + "\",");
			content.append("\"" + vo.getPfbDefaultWebsiteChineseName() + "\",");
			content.append("\"" + vo.getPfbWebsiteChineseName() + "\",");
			content.append("\"" + vo.getPfpCustomerInfoName() + "\",");
			content.append("\"" + vo.getTimeCode() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdClkPriceSum() + "\"");
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
		content.append("\"" + df2.format(totalPv) + "\",");
		content.append("\"" + df2.format(totalClk) + "\",");
		content.append("\"" + df.format(totalClkRate) + "%\",");
		content.append("\"$ " + df3.format(totalAdClkAvgPrice) + "\",");
		content.append("\"$ " + df2.format(totalAdClkPriceSum) + "\"");
		
		
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

	public float getTotalBonus() {
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

	public String getPfbCustomerInfoId() {
		return pfbCustomerInfoId;
	}

	public void setPfbCustomerInfoId(String pfbCustomerInfoId) {
		this.pfbCustomerInfoId = pfbCustomerInfoId;
	}

	public String getAdPvclkDate() {
		return adPvclkDate;
	}

	public void setAdPvclkDate(String adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
	}

	public Map<String, String> getAdDeviceMap() {
		return adDeviceMap;
	}

	public String getSearchAdDevice() {
		return searchAdDevice;
	}

	public void setSearchAdDevice(String searchAdDevice) {
		this.searchAdDevice = searchAdDevice;
	}

	public String getAllowUrl() {
		return allowUrl;
	}

	public void setAllowUrl(String allowUrl) {
		this.allowUrl = allowUrl;
	}

	public String getAllowUrlName() {
		return allowUrlName;
	}

	public void setAllowUrlName(String allowUrlName) {
		this.allowUrlName = allowUrlName;
	}

	public String getPfbCustomerInfoName() {
		return pfbCustomerInfoName;
	}

	public void setPfbCustomerInfoName(String pfbCustomerInfoName) {
		this.pfbCustomerInfoName = pfbCustomerInfoName;
	}
	
}
