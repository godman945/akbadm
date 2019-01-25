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

import com.pchome.akbadm.db.pojo.PfbxWebsiteCategory;
import com.pchome.akbadm.db.service.customerInfo.IPfpCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.IPfbxWebsiteCategoryService;
import com.pchome.akbadm.db.service.report.IAdWebsiteReportService;
import com.pchome.akbadm.db.vo.report.PfpAdWebsiteReportVO;
import com.pchome.akbadm.struts2.BaseAction;

@SuppressWarnings("serial")
public class AdWebsiteReportAction extends BaseAction {

	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.000");

	private IAdWebsiteReportService adWebsiteReportService;
	private IPfbxWebsiteCategoryService pfbxWebsiteCategoryService;
	private IPfpCustomerInfoService pfpCustomerInfoService;

	//輸入參數
	private String startDate;
	private String endDate;
	private String adPvclkDate;
	private String searchAdDevice;
	private String searchCategory;
	private String websiteCategoryCode;
	private String websiteCategoryName;

	private Map<String,String> adDeviceMap;
	private Map<String,String> websiteCategoryMap;
	private List<PfpAdWebsiteReportVO> voList = new ArrayList<PfpAdWebsiteReportVO>();
	private PfpAdWebsiteReportVO totalVO;
	private List<PfpAdWebsiteReportVO> sumList;
	//訊息
	private String message = "";

	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 50;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private long totalCount = 0;   				// 初始化共幾筆

	private long totalSize = 0;					//總比數

	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名

	@Override
    public String execute() throws Exception {
		setDeviceMap();
		setCategoryMap();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

		Map<String, String> conditionMap = new HashMap<String, String>();

		conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);

    	if(StringUtils.isNotBlank(searchCategory)){
    		conditionMap.put("searchCategory", searchCategory);
    	}

    	if(StringUtils.isNotBlank(searchAdDevice) && !StringUtils.equals("allDevice", searchAdDevice)){
    		conditionMap.put("searchAdDevice", searchAdDevice);
    	}

		voList = new ArrayList<PfpAdWebsiteReportVO>();
		sumList = new ArrayList<PfpAdWebsiteReportVO>();

    	voList = adWebsiteReportService.getAdWebsiteReportByCondition(conditionMap);
    	sumList = adWebsiteReportService.getAdWebsiteReportSumByCondition(conditionMap);
    	log.info("voList: "+voList.size());

    	if(voList.size()==0){

    		this.message = "查無資料！";
    	}else{
    		List<PfpAdWebsiteReportVO> viewList = new ArrayList<PfpAdWebsiteReportVO>();

    		totalCount = voList.size();
    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
    		totalSize = totalCount;

    		for (int i=0; i<voList.size(); i++) {
    			PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();
    			vo = voList.get(i);

    			String WebsiteCategoryName = "未分類";
    			if(StringUtils.isNotEmpty(vo.getWebsiteCategoryCode()) && websiteCategoryMap.get(vo.getWebsiteCategoryCode()) != null){
    				WebsiteCategoryName = websiteCategoryMap.get(vo.getWebsiteCategoryCode());
    			}
    			vo.setWebsiteCategoryName(WebsiteCategoryName);

    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
    				viewList.add(vo);
    			}
    		}

    		voList = viewList;
    		setTotalVO();
    	}

    	return SUCCESS;
    }

	public String detalReportData() throws Exception {
		setDeviceMap();
		setCategoryMap();

		Map<String, String> conditionMap = new HashMap<String, String>();

    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}

    	conditionMap.put("startDate", startDate);
    	conditionMap.put("endDate", endDate);

    	if(StringUtils.isNotBlank(searchCategory)){
    		conditionMap.put("searchCategory", searchCategory);
    	}

    	if(StringUtils.isNotBlank(searchAdDevice) && !StringUtils.equals("allDevice", searchAdDevice)){
    		conditionMap.put("searchAdDevice", searchAdDevice);
    	}

    	Map<String,String> pfpCustomerInfoMap = new LinkedHashMap<String,String>();
    	voList = new ArrayList<PfpAdWebsiteReportVO>();
		sumList = new ArrayList<PfpAdWebsiteReportVO>();

    	voList = adWebsiteReportService.getAdWebsiteDetalReportByCondition(conditionMap);
    	sumList = adWebsiteReportService.getAdWebsiteReportSumByCondition(conditionMap);
    	log.info("voList: "+voList.size());

    	if(voList.size()==0){

    		this.message = "查無資料！";
    	}else{
    		totalCount = voList.size();
    		totalSize = totalCount;


    		pfpCustomerInfoMap = pfpCustomerInfoService.findPfpCustomerInfoNameMap();

    		for (int i=0; i<voList.size(); i++) {
    			PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();
    			vo = voList.get(i);

    			String WebsiteCategoryName = "未分類";
    			if(StringUtils.isNotEmpty(vo.getWebsiteCategoryCode()) && websiteCategoryMap.get(vo.getWebsiteCategoryCode()) != null){
    				WebsiteCategoryName = websiteCategoryMap.get(vo.getWebsiteCategoryCode());
    			}
    			vo.setWebsiteCategoryName(WebsiteCategoryName);

    			String pfpCustomerInfoName = "";
    			if(pfpCustomerInfoMap.get(vo.getPfpCustomerInfoId()) != null){
    				pfpCustomerInfoName = pfpCustomerInfoMap.get(vo.getPfpCustomerInfoId());
    			}
    			vo.setPfpCustomerInfoName(pfpCustomerInfoName);

    		}

    		setTotalVO();
    	}


    	if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}

		return SUCCESS;
	}

	private void makeDownloadReportData() throws Exception {
    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="網站類型成效報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"期間","網站類型","帳戶名稱","裝置","時段區間","廣告曝光數","廣告互動數","廣告互動率","平均互動費用","千次曝光費用","廣告費用"};

    	StringBuffer content=new StringBuffer();

    	content.append("報表名稱:,網站類型成效");
		content.append("\n\n");

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

		content.append("網站類型查詢:,");
		if(StringUtils.isNotBlank(searchCategory)) {
			content.append(websiteCategoryMap.get(searchCategory));
		} else {
			content.append("全部類型");
		}
		content.append("\n\n");

		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");

		for (int i=0; i<voList.size(); i++) {
			PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();
			vo = voList.get(i);

			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getWebsiteCategoryName() + "\",");
			content.append("\"" + vo.getPfpCustomerInfoName() + "\",");
			content.append("\"" + vo.getAdDevice() + "\",");
			content.append("\"" + vo.getTimeCode() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdClkPriceSum() + "\",");

			content.append("\n");
		}

		//總計
		content.append("\"總計:" + totalSize + "筆\",");
		content.append("\n");

		for (int i=0; i<sumList.size(); i++) {
			PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();
			vo = sumList.get(i);

			content.append("\"" + "\",");
			content.append("\"" + vo.getWebsiteCategoryName() + "\",");
			content.append("\"" + "\",");
			content.append("\"" + "\",");
			content.append("\"" + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdClkPriceSum() + "\",");

			content.append("\n");
		}

		content.append("\"共計\",");
		content.append("\"" + "\",");
		content.append("\"" + "\",");
		content.append("\"" + "\",");
		content.append("\"" + "\",");
		content.append("\"" + totalVO.getAdPvSum() + "\",");
		content.append("\"" + totalVO.getAdClkSum() + "\",");
		content.append("\"" + totalVO.getAdClkRate() + "\",");
		content.append("\"$ " + totalVO.getAdClkAvgPrice() + "\",");
		content.append("\"$ " + totalVO.getAdPvAvgPrice() + "\",");
		content.append("\"$ " + totalVO.getAdClkPriceSum() + "\",");


		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}

	private void setTotalVO(){

		totalVO = new PfpAdWebsiteReportVO();

		Long pvSum = new Long(0);
		Long clkSum = new Long(0);
		Double price = new Double(0);

		for (int i=0; i<sumList.size(); i++) {
			PfpAdWebsiteReportVO vo = new PfpAdWebsiteReportVO();
			vo = sumList.get(i);

			String WebsiteCategoryName = "未分類";
			if(StringUtils.isNotEmpty(vo.getWebsiteCategoryCode()) && websiteCategoryMap.get(vo.getWebsiteCategoryCode()) != null){
				WebsiteCategoryName = websiteCategoryMap.get(vo.getWebsiteCategoryCode());
			}
			vo.setWebsiteCategoryName(WebsiteCategoryName);

			pvSum += Long.parseLong(vo.getAdPvSum().replace(",", ""));
			clkSum += Long.parseLong(vo.getAdClkSum().replace(",", ""));
			price += Double.parseDouble(vo.getAdClkPriceSum().replace(",", ""));
		}

		//點擊率
		if (clkSum==0 || pvSum==0) {
			totalVO.setAdClkRate(df.format(0) + "%");
		} else {
			totalVO.setAdClkRate(df.format((clkSum.doubleValue() / pvSum.doubleValue())*100) + "%");
		}

		//平均點擊費用
		if (price==0 || clkSum==0) {
			totalVO.setAdClkAvgPrice(df3.format(0));
		} else {
			totalVO.setAdClkAvgPrice(df3.format(price.doubleValue() / clkSum.doubleValue()));
		}

		//千次曝光費用
		if (price==0 || pvSum==0) {
			totalVO.setAdPvAvgPrice(df3.format(0));
		} else {
			totalVO.setAdPvAvgPrice(df3.format(price.doubleValue()*1000 / pvSum) );
		}

		totalVO.setAdPvSum(df2.format(pvSum));
		totalVO.setAdClkSum(df2.format(clkSum));
		totalVO.setAdClkPriceSum(df3.format(price));
	}


	private void setDeviceMap(){
		adDeviceMap = new LinkedHashMap<String,String>();

		adDeviceMap.put("allDevice", "全部裝置");
		adDeviceMap.put("PC", "電腦");
		adDeviceMap.put("mobile", "行動裝置");
	}

	private void setCategoryMap(){
		websiteCategoryMap = new LinkedHashMap<String,String>();

		List<PfbxWebsiteCategory> list = new ArrayList<PfbxWebsiteCategory>();

		list = pfbxWebsiteCategoryService.getFirstLevelPfpPfbxWebsiteCategory();

		if(!list.isEmpty()){
			for(PfbxWebsiteCategory data:list){
				websiteCategoryMap.put(data.getCode(), data.getName());
			}
		}
	}

	public void setAdWebsiteReportService(IAdWebsiteReportService adWebsiteReportService) {
		this.adWebsiteReportService = adWebsiteReportService;
	}

	public void setPfbxWebsiteCategoryService(IPfbxWebsiteCategoryService pfbxWebsiteCategoryService) {
		this.pfbxWebsiteCategoryService = pfbxWebsiteCategoryService;
	}

	public void setPfpCustomerInfoService(IPfpCustomerInfoService pfpCustomerInfoService) {
		this.pfpCustomerInfoService = pfpCustomerInfoService;
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

	public List<PfpAdWebsiteReportVO> getVoList() {
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

	public String getWebsiteCategoryCode() {
		return websiteCategoryCode;
	}

	public void setWebsiteCategoryCode(String websiteCategoryCode) {
		this.websiteCategoryCode = websiteCategoryCode;
	}

	public String getWebsiteCategoryName() {
		return websiteCategoryName;
	}

	public void setWebsiteCategoryName(String websiteCategoryName) {
		this.websiteCategoryName = websiteCategoryName;
	}

	public PfpAdWebsiteReportVO getTotalVO() {
		return totalVO;
	}

	public List<PfpAdWebsiteReportVO> getSumList() {
		return sumList;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	public Map<String, String> getWebsiteCategoryMap() {
		return websiteCategoryMap;
	}

}
