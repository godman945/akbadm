package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdReport2Service;
import com.pchome.akbadm.db.vo.AdReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.akbadm.utils.ComponentUtils;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;

public class AdReport2Action extends BaseAction  {

	private static final long serialVersionUID = 1L;

	private IAdReport2Service adReport2Service;
	private IPfdAccountService pfdAccountService;

    private String akbPfpServer;

	//查詢參數
	private String startDate; 			//起始日期
	private String endDate; 			//結束日期
	private String adType; 				//廣告形式("":全選, "1":找東西廣告, "2":PChome頻道廣告)
	private String pfdCustomerInfoId; 	//PFD帳戶
	private String searchType;			//查詢方式
	private String searchText;			//查詢文字
	private String searchAdSeq;			//查詢序號
	private String adTitle;				//廣告名稱

	private List<AdReportVO> voList = new ArrayList<AdReportVO>();
	private AdReportVO totalVO = new AdReportVO();
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

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> searchType = " + searchType);
		log.info(">>> searchText = " + searchText);

		Map<String, String> conditionMap = new HashMap<String, String>();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);

	    	if(StringUtils.isNotBlank(adType)){
	    		conditionMap.put("adType", adType);
	    	}

	    	if(StringUtils.isNotBlank(pfdCustomerInfoId)){
	    		conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
	    	}

	    	if(StringUtils.isNotBlank(searchType)){
	    		conditionMap.put("searchType", searchType);
	    	}

	    	if(StringUtils.isNotBlank(searchText)){
	    		conditionMap.put("searchText", searchText);
	    	}

	    	List<AdReportVO> dataList = adReport2Service.getAdReportList(conditionMap, -1, -1);
	    	totalVO = adReport2Service.getAdReportListSum(conditionMap);

	    	if(dataList.size()==0){
	    		this.message = "查無資料！";
	    	}else{
	    		totalCount = dataList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;

	    		for (int i=0; i<dataList.size(); i++) {
	    			AdReportVO vo = new AdReportVO();
	    			vo = dataList.get(i);

	    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
	    				voList.add(vo);
	    			}
	    		}
	    	}

		}

    	return SUCCESS;
    }

	public List<PfdCustomerInfo> getCompanyList() {

		List<PfdCustomerInfo> pfdCustomerInfos = new ArrayList<PfdCustomerInfo>();

		try {

			String status = "'" + EnumPfdAccountStatus.START.getStatus() +
					"','" + EnumPfdAccountStatus.CLOSE.getStatus() +
					"','" + EnumPfdAccountStatus.STOP.getStatus() + "'";

			log.info(">>> status = " + status);

			Map<String, String> conditionMap = new HashMap<String, String>();
			conditionMap.put("status", status);

			pfdCustomerInfos = pfdAccountService.getPfdCustomerInfoByCondition(conditionMap);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return pfdCustomerInfos;
	}

	public String findDetailData() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> searchType = " + searchType);
		log.info(">>> searchText = " + searchText);
		log.info(">>> searchAdSeq = " + searchAdSeq);

		Map<String, String> conditionMap = new HashMap<String, String>();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);

	    	if(StringUtils.isNotBlank(adType)){
	    		conditionMap.put("adType", adType);
	    	}

	    	if(StringUtils.isNotBlank(pfdCustomerInfoId)){
	    		conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
	    	}

	    	if(StringUtils.isNotBlank(searchType)){
	    		conditionMap.put("searchType", searchType);
	    	}

	    	if(StringUtils.isNotBlank(searchText)){
	    		conditionMap.put("searchText", searchText);
	    	}

	    	if(StringUtils.isNotBlank(searchAdSeq)){
	    		conditionMap.put("searchAdSeq", searchAdSeq);
	    	}

	    	voList = adReport2Service.getAdReportDetailList(conditionMap);
	    	totalVO = adReport2Service.getAdReportListSum(conditionMap);

	    	if(voList.size()==0){
	    		this.message = "查無資料！";
	    	}else{

	    		totalCount = voList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;
	    	}

		}

		return SUCCESS;
	}

	public String downloadAd() throws Exception {
		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> adType = " + adType);
		log.info(">>> pfdCustomerInfoId = " + pfdCustomerInfoId);
		log.info(">>> searchType = " + searchType);
		log.info(">>> searchText = " + searchText);

		Map<String, String> conditionMap = new HashMap<String, String>();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);

	    	if(StringUtils.isNotBlank(adType)){
	    		conditionMap.put("adType", adType);
	    	}

	    	if(StringUtils.isNotBlank(pfdCustomerInfoId)){
	    		conditionMap.put("pfdCustomerInfoId", pfdCustomerInfoId);
	    	}

	    	if(StringUtils.isNotBlank(searchType)){
	    		conditionMap.put("searchType", searchType);
	    	}

	    	if(StringUtils.isNotBlank(searchText)){
	    		conditionMap.put("searchText", searchText);
	    	}

	    	voList = adReport2Service.getAdReportDetailList(conditionMap);
	    	totalVO = adReport2Service.getAdReportListSum(conditionMap);

	    	totalCount = voList.size();
	    	totalSize = totalCount;

	    	SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
	    	String filename="廣告明細成效報表_" + dformat.format(new Date()) + ".csv";
	    	String[] tableHeadArray = {"日期","帳戶名稱","廣告title","廣告尺寸","廣告活動","廣告群組","廣告曝光數","互動數","互動率","平均互動出價","千次曝光出價","廣告費用"};

            String title = null;
            StringBuffer size = null;
	    	StringBuffer content=new StringBuffer();

	    	content.append("報表名稱:,廣告明細成效報表");
			content.append("\n\n");

			String adTypeName = "全部";
			if(StringUtils.equals("1", adType)){
				adTypeName = "找東西廣告";
			}
			if(StringUtils.equals("2", adType)){
				adTypeName = "PChome頻道廣告";
			}
			content.append("廣告型式:,");
			content.append(adTypeName);
			content.append("\n\n");

			content.append("查詢日期:,");
			content.append(startDate);
			content.append(" 到 ");
			content.append(endDate);
			content.append("\n\n");

			String pfdName = "全部";
			List<PfdCustomerInfo> PfdCustomerInfoList = getCompanyList();
			if(StringUtils.isNotEmpty(pfdCustomerInfoId)){
				for(PfdCustomerInfo pfdCustomerInfo:PfdCustomerInfoList){
					if(StringUtils.equals(pfdCustomerInfoId, pfdCustomerInfo.getCustomerInfoId())){
						pfdName = pfdCustomerInfo.getCompanyName();
						break;
					}
				}
			}

			content.append("經銷歸屬:,");
			content.append(pfdName);
			content.append("\n\n");

			for(String s:tableHeadArray){
				content.append("\"");
				content.append(s);
				content.append("\",");
			}
			content.append("\n");

			for (int i=0; i<voList.size(); i++) {
				AdReportVO vo = new AdReportVO();
				vo = voList.get(i);

				// title
				title = "";
                if ("IMG".equals(vo.getAdStyle())) {
                    title = vo.getTitle();
                }
                else if ("VIDEO".equals(vo.getAdStyle())) {
                    title = vo.getAdDetailContent();
                }

	            // size
				size = new StringBuffer();
				if ("IMG".equals(vo.getAdStyle())) {
                    size.append(vo.getImgWidth()).append("x").append(vo.getImgHeight());
				}
				else if ("VIDEO".equals(vo.getAdStyle())) {
	                size.append(vo.getAdDetailVideoWidth()).append("x").append(vo.getAdDetailVideoHeight());
	            }

				content.append("\"");
				content.append(vo.getAdPvclkDate());
				content.append("\",");
				content.append("\"");
				content.append(vo.getCustomerName());
				content.append("(");
				content.append(vo.getCustomerId());
				content.append(")");
				content.append("\",");
				content.append("\"");
				content.append(title);
				content.append("\",");
                content.append("\"");
                content.append(size);
                content.append("\",");
				content.append("\"");
				content.append(vo.getAdAction());
				content.append("\",");
				content.append("\"");
				content.append(vo.getAdGroup());
				content.append("\",");
				content.append("\"");
				content.append(vo.getKwPvSum());
				content.append("\",");
				content.append("\"");
				content.append(vo.getKwClkSum());
				content.append("\",");
				content.append("\"");
				content.append(vo.getKwClkRate());
				content.append("\",");
				content.append("\"$ ");
				content.append(vo.getClkPriceAvg());
				content.append("\",");
                content.append("\"$ ");
                content.append(vo.getPvPriceAvg());
                content.append("\",");
				content.append("\"$ ");
				content.append(vo.getKwPriceSum());
				content.append("\",");
				content.append("\n");
			}

			//總計
			content.append("\n");
			content.append("\"總計:");
			content.append(totalSize);
			content.append("筆\",");
			content.append("\"\",");
			content.append("\"\",");
			content.append("\"\",");
			content.append("\"\",");
			content.append("\"");
			content.append(totalVO.getKwPvSum());
			content.append("\",");
			content.append("\"");
			content.append(totalVO.getKwClkSum());
			content.append("\",");
			content.append("\"");
			content.append(totalVO.getKwClkRate());
			content.append("\",");
			content.append("\"$ ");
			content.append(totalVO.getClkPriceAvg());
			content.append("\",");
            content.append("\"$ ");
            content.append(totalVO.getPvPriceAvg());
            content.append("\",");
			content.append("\"$ ");
			content.append(totalVO.getKwPriceSum());
			content.append("\",");
			content.append("\n");



			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
				downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
			} else {
				downloadFileName = URLEncoder.encode(filename, "UTF-8");
			}

			downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		}

		return SUCCESS;
	}

	public Map<String, String> getAdTypeSelectOptionsMap() {
		return ComponentUtils.getAdTypeSelectOptionsMap();
	}

	public void setAdReport2Service(IAdReport2Service adReport2Service) {
		this.adReport2Service = adReport2Service;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
	}

    public String getAkbPfpServer() {
        return akbPfpServer;
    }

    public void setAkbPfpServer(String akbPfpServer) {
        this.akbPfpServer = akbPfpServer;
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

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSearchAdSeq() {
		return searchAdSeq;
	}

	public void setSearchAdSeq(String searchAdSeq) {
		this.searchAdSeq = searchAdSeq;
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

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public List<AdReportVO> getVoList() {
		return voList;
	}

	public AdReportVO getTotalVO() {
		return totalVO;
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

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

}
