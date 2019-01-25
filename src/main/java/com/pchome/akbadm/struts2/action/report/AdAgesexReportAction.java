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

import com.pchome.akbadm.db.service.report.IAdAgesexReportService;
import com.pchome.akbadm.db.vo.report.PfpAdAgesexReportVO;
import com.pchome.akbadm.struts2.BaseAction;

@SuppressWarnings("serial")
public class AdAgesexReportAction extends BaseAction {

	private IAdAgesexReportService adAgesexReportService;

	//輸入參數
	private String startDate;
	private String endDate;
	private String searchAgesex = "A";
	private String searchDevice;

	private List<PfpAdAgesexReportVO> voList = new ArrayList<PfpAdAgesexReportVO>();
	private List<PfpAdAgesexReportVO> totalList = new ArrayList<PfpAdAgesexReportVO>();
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

    	return SUCCESS;
    }

	public String getAdAgesexReport() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchAgesex = " + searchAgesex);
		log.info(">>> searchDevice = " + searchDevice);

		Map<String, String> conditionMap = new HashMap<String, String>();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);

	    	if(StringUtils.isNotBlank(searchAgesex)){
	    		conditionMap.put("searchAgesex", searchAgesex);
	    	}

	    	if(StringUtils.isNotBlank(searchDevice)){
	    		conditionMap.put("searchDevice", searchDevice);
	    	}

	    	voList = new ArrayList<PfpAdAgesexReportVO>();
	    	totalList = new ArrayList<PfpAdAgesexReportVO>();
	    	voList = adAgesexReportService.getAdAgesexReportByCondition(conditionMap);
	    	totalList = adAgesexReportService.getAdAgesexReportTotalByCondition(conditionMap);

	    	log.info("voList: "+voList.size());

	    	if(voList.size()==0){

	    		this.message = "查無資料！";
	    	}else{
	    		List<PfpAdAgesexReportVO> viewList = new ArrayList<PfpAdAgesexReportVO>();

	    		totalCount = voList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;

	    		for (int i=0; i<voList.size(); i++) {
	    			PfpAdAgesexReportVO vo = new PfpAdAgesexReportVO();
	    			vo = voList.get(i);
	    			log.info("getAdPvSum: "+vo.getAdPvSum());

	    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
	    				viewList.add(vo);
	    			}
	    		}

	    		if(downloadFlag.trim().equals("yes")){
	    			log.info("makeDownloadReportData");
	    			makeDownloadReportData();
	    		}

	    		voList = viewList;
	    	}

		}

		return SUCCESS;
	}

	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告族群成效報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"裝置","廣告曝光數","廣告互動數","廣告互動率","平均互動費用","千次曝光費用","廣告費用"};

    	StringBuffer content=new StringBuffer();

    	content.append("報表名稱:,廣告族群成效");
		content.append("\n\n");

		content.append("查詢類別:,");
		if(StringUtils.equals(searchAgesex, "S")){
			content.append("性別");
		} else {
			content.append("年齡");
		}
		content.append("\n\n");

		content.append("裝置:,");
		if(StringUtils.equals(searchDevice, "PC")){
			content.append("電腦");
		} else if(StringUtils.equals(searchDevice, "mobile")){
			content.append("行動裝置");
		} else {
			content.append("全部");
		}
		content.append("\n\n");

		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");

		content.append("\"日期\"");
		content.append(",");
		if(StringUtils.equals(searchAgesex, "S")){
			content.append("\"性別\"");
		} else {
			content.append("\"年齡\"");
		}
		content.append(",");

		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");

		for (int i=0; i<voList.size(); i++) {
			PfpAdAgesexReportVO vo = new PfpAdAgesexReportVO();
			vo = voList.get(i);
			content.append("\"" + vo.getReportDate() + "\",");
			if(StringUtils.equals(searchAgesex, "S")){
				content.append("\"" + vo.getSex() + "\",");
			} else {
				content.append("\"" + vo.getAge() + "\",");
			}
			content.append("\"" + vo.getAdDevice() + "\",");
			content.append("\"" + vo.getAdPvSum() + "\",");
			content.append("\"" + vo.getAdClkSum() + "\",");
			content.append("\"" + vo.getAdClkRate() + "\",");
			content.append("\"$ " + vo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPvAvgPrice() + "\",");
			content.append("\"$ " + vo.getAdPriceSum() + "\"");
			content.append("\n");
		}
		content.append("\n");
		//總計
		content.append("\"總計:" + totalSize + "\"");
		content.append("\n");

		for (int i=0; i<totalList.size(); i++) {
			PfpAdAgesexReportVO totalVo = new PfpAdAgesexReportVO();
			totalVo = totalList.get(i);
			content.append("\"\",");
			if(StringUtils.equals(searchAgesex, "S")){
				content.append("\"" + totalVo.getSex() + "\",");
			} else {
				content.append("\"" + totalVo.getAge() + "\",");
			}
			content.append("\"" + totalVo.getAdDevice() + "\",");
			content.append("\"" + totalVo.getAdPvSum() + "\",");
			content.append("\"" + totalVo.getAdClkSum() + "\",");
			content.append("\"" + totalVo.getAdClkRate() + "\",");
			content.append("\"$ " + totalVo.getAdClkAvgPrice() + "\",");
			content.append("\"$ " + totalVo.getAdPvAvgPrice() + "\",");
			content.append("\"$ " + totalVo.getAdPriceSum() + "\"");
			content.append("\n");
		}

		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}

	public void setAdAgesexReportService(IAdAgesexReportService adAgesexReportService) {
		this.adAgesexReportService = adAgesexReportService;
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

	public String getSearchAgesex() {
		return searchAgesex;
	}

	public void setSearchAgesex(String searchAgesex) {
		this.searchAgesex = searchAgesex;
	}

	public String getSearchDevice() {
		return searchDevice;
	}

	public void setSearchDevice(String searchDevice) {
		this.searchDevice = searchDevice;
	}

	public List<PfpAdAgesexReportVO> getVoList() {
		return voList;
	}

	public List<PfpAdAgesexReportVO> getTotalList() {
		return totalList;
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

	public long getTotalSize() {
		return totalSize;
	}

}
