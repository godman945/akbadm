package com.pchome.akbadm.struts2.action.pfbx.report;

import com.pchome.akbadm.db.pojo.PfbxCustomerInfo;
import com.pchome.akbadm.db.service.pfbx.account.IPfbxCustomerInfoService;
import com.pchome.akbadm.db.service.pfbx.report.IPfbxTimeReportService;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxCustomerReportVo;
import com.pchome.akbadm.db.vo.pfbx.report.PfbxReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PfbTimeReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IPfbxCustomerInfoService pfbxCustomerInfoService;
	private IPfbxTimeReportService pfbxTimeReportService;

    //輸入參數
	private String startDate;
	private String endDate;
	private String period;
	private String searchCategory;
	private String searchOption;
	private String searchText;
	
	private PfbxCustomerReportVo pfbxCustomerReportVo;
	
	private List<PfbxCustomerInfo> list = new ArrayList<PfbxCustomerInfo>();
	private List<PfbxReportVO> voList = new ArrayList<PfbxReportVO>();
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
    	
    	log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> period = " + period);
		log.info(">>> searchText = " + searchText);
    	
    	Map<String, String> conditionMap = new HashMap<String, String>();
    	Map<String, String> conditionMap1 = new HashMap<String, String>();
    	List<PfbxReportVO> viewList = new ArrayList<PfbxReportVO>();
    	
    	if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			list = new ArrayList<PfbxCustomerInfo>();
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);
	    	conditionMap.put("period", period);
	    	
	    	if(StringUtils.isNotBlank(searchCategory)){
	    		conditionMap.put("searchCategory", searchCategory);
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
	    	voList = new ArrayList<PfbxReportVO>();
	    	voList = pfbxTimeReportService.getPfbxTimeReportByCondition(conditionMap);
	    	log.info("voList: "+voList.size());
	    	
	    	if(voList.size()==0){
	    		
	    		this.message = "查無資料！";
	    	}else{
	    		
	    		totalCount = voList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;
	    		
		    	for (int i=0; i<voList.size(); i++) {
		    		PfbxReportVO vo = new PfbxReportVO();
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
	    			if(vo.getAdClkPriceSum() != null){
	    				totalAdClkPriceSum += Double.parseDouble(vo.getAdClkPriceSum().replaceAll(",", ""));	
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
					
					
//	    			vo.setWebsiteDisplayUrl(info.getWebsiteDisplayUrl());
//	    			log.info("CustomerInfoId: "+vo.getCustomerInfoId());
//	    			log.info("chineseName: "+vo.getWebsiteChineseName());
//	    			log.info("url: "+vo.getWebsiteDisplayUrl());
	    			
				}
		    	
	    	}
    	
		}
    	
    	if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
    	
    	voList = viewList;
		return SUCCESS;
	}

    private void makeDownloadReportData() throws Exception {
    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="時間成效報表_" + dformat.format(new Date()) + ".csv";
    	DecimalFormat df = new DecimalFormat("0.00");
    	DecimalFormat df2 = new DecimalFormat("###,###,###,###");
    	DecimalFormat df3 = new DecimalFormat("###,###,###,##0.00");
    	String[] tableHeadArray = {"期間","帳戶編號","帳戶類別","網站名稱","廣告撥放網址","廣告曝光數","廣告點擊數","廣告點擊率","單次點擊收益","千次曝光收益","預估收益"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,時間成效");
		content.append("\n\n");
		content.append("期間:,");
		if (StringUtils.isNotBlank(period)){
			if("Day".equals(period)){
				content.append("天");
			} else if("Week".equals(period)){
				content.append("週");
			} else {
				content.append("月");
			}
		} 
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
    			} else if("websiteChineseName".equals(searchOption)) {
    				content.append("網站名稱:,");
    			} else {
    				content.append("廣告播放網址:,");
    			}
    			content.append(searchText);
    			content.append("\n\n");
    		}		
    	} else {
    		if (StringUtils.isNotBlank(searchText)){
    			content.append("帳戶編號/網站名稱/廣告播放網址:,");
    			content.append(searchText);
    			content.append("\n\n");
    		}
    	}
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<voList.size(); i++) {
    		PfbxReportVO vo = new PfbxReportVO();
			vo = voList.get(i);
			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getCustomerInfoId() + "\",");
			content.append("\"" + vo.getWebsiteCategory() + "\",");
			content.append("\"" + vo.getWebsiteChineseName() + "\",");
			content.append("\"" + vo.getWebsiteDisplayUrl() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvRate() + "\",");
			content.append("\"$ " + vo.getTotalBonus() + "\"");
			content.append("\n");
		}
		
		//總計
		content.append("\"總計:" + totalSize + "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" +  "\",");
		content.append("\"" + df2.format(totalPv) + "\",");
		content.append("\"" + df2.format(totalClk) + "\",");
		content.append("\"" + df.format(totalClkRate) + "%\",");
		content.append("\"$ " + df3.format(totalAdClkAvgPrice) + "\",");
		content.append("\"$ " + df3.format(totalAdPvRate) + "\",");
		content.append("\"$ " + df3.format(totalBonus) + "\"");
		
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
    }
    
	public void setPfbxCustomerInfoService(IPfbxCustomerInfoService pfbxCustomerInfoService) {
		this.pfbxCustomerInfoService = pfbxCustomerInfoService;
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

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(String searchOption) {
		this.searchOption = searchOption;
	}

	public PfbxCustomerReportVo getPfbxCustomerReportVo() {
		return pfbxCustomerReportVo;
	}

	public void setPfbxCustomerReportVo(PfbxCustomerReportVo pfbxCustomerReportVo) {
		this.pfbxCustomerReportVo = pfbxCustomerReportVo;
	}

	public List<PfbxCustomerInfo> getList() {
		return list;
	}

	public void setList(List<PfbxCustomerInfo> list) {
		this.list = list;
	}

	public IPfbxCustomerInfoService getPfbxCustomerInfoService() {
		return pfbxCustomerInfoService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public IPfbxTimeReportService getPfbxTimeReportService() {
		return pfbxTimeReportService;
	}

	public void setPfbxTimeReportService(
			IPfbxTimeReportService pfbxTimeReportService) {
		this.pfbxTimeReportService = pfbxTimeReportService;
	}

	public List<PfbxReportVO> getVoList() {
		return voList;
	}

	public void setVoList(List<PfbxReportVO> voList) {
		this.voList = voList;
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

	public float getTotalBonus() {
		return totalBonus;
	}

	public float getTotalAdClkPriceSum() {
		return totalAdClkPriceSum;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
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

}
