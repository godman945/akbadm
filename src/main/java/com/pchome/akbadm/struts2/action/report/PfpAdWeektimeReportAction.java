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

import com.pchome.akbadm.db.service.report.IPfpAdWeektimeReportService;
import com.pchome.akbadm.db.vo.report.PfpAdTimeReportVO;
import com.pchome.akbadm.struts2.BaseAction;

@SuppressWarnings("serial")
public class PfpAdWeektimeReportAction extends BaseAction {

	private IPfpAdWeektimeReportService pfpAdWeektimeReportService;

	//輸入參數
	private String startDate;
	private String endDate;
	private String searchTime = "W";
	private String searchDevice;

	private List<PfpAdTimeReportVO> voList = new ArrayList<PfpAdTimeReportVO>();
	private List<PfpAdTimeReportVO> totalList = new ArrayList<PfpAdTimeReportVO>();
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

	public String getPfpAdWeektimeReport() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);
		log.info(">>> searchTime = " + searchTime);
		log.info(">>> searchDevice = " + searchDevice);

		Map<String, String> conditionMap = new HashMap<String, String>();

		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			conditionMap = new HashMap<String, String>();
	    	conditionMap.put("startDate", startDate);
	    	conditionMap.put("endDate", endDate);

	    	if(StringUtils.isNotBlank(searchTime)){
	    		conditionMap.put("searchTime", searchTime);
	    	}

	    	if(StringUtils.isNotBlank(searchDevice)){
	    		conditionMap.put("searchDevice", searchDevice);
	    	}

	    	voList = new ArrayList<PfpAdTimeReportVO>();
	    	totalList = new ArrayList<PfpAdTimeReportVO>();
	    	voList = pfpAdWeektimeReportService.getAdWeektimeReportByCondition(conditionMap);
	    	totalList = pfpAdWeektimeReportService.getAdWeektimeReportTotalByCondition(conditionMap);

	    	log.info("voList: "+voList.size());

	    	if(voList.size()==0){

	    		this.message = "查無資料！";
	    	}else{
	    		List<PfpAdTimeReportVO> viewList = new ArrayList<PfpAdTimeReportVO>();

	    		totalCount = voList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
	    		totalSize = totalCount;

	    		for (int i=0; i<voList.size(); i++) {
	    			PfpAdTimeReportVO vo = new PfpAdTimeReportVO();
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
    	String filename="廣告播放時段成效報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"裝置","廣告曝光數","廣告互動數","廣告互動率","平均互動費用","千次曝光費用","廣告費用"};

    	StringBuffer content=new StringBuffer();

    	content.append("報表名稱:,廣告播放時段成效");
		content.append("\n\n");

		content.append("查詢類別:,");
		if(StringUtils.equals(searchTime, "T")){
			content.append("時段");
		} else {
			content.append("星期");
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
		if(StringUtils.equals(searchTime, "T")){
			content.append("\"時段\"");
		} else {
			content.append("\"星期\"");
		}
		content.append(",");

		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");

		for (int i=0; i<voList.size(); i++) {
			PfpAdTimeReportVO vo = new PfpAdTimeReportVO();
			vo = voList.get(i);
			content.append("\"" + vo.getReportDate() + "\",");
			if(StringUtils.equals(searchTime, "T")){
				content.append("\"" + vo.getTime() + "\",");
			} else {
				content.append("\"" + vo.getWeek() + "\",");
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
			PfpAdTimeReportVO totalVo = new PfpAdTimeReportVO();
			totalVo = totalList.get(i);
			content.append("\"\",");
			if(StringUtils.equals(searchTime, "T")){
				content.append("\"" + totalVo.getTime() + "\",");
			} else {
				content.append("\"" + totalVo.getWeek() + "\",");
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

	public void setPfpAdWeektimeReportService(IPfpAdWeektimeReportService pfpAdWeektimeReportService) {
		this.pfpAdWeektimeReportService = pfpAdWeektimeReportService;
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

	public String getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}

	public String getSearchDevice() {
		return searchDevice;
	}

	public void setSearchDevice(String searchDevice) {
		this.searchDevice = searchDevice;
	}

	public List<PfpAdTimeReportVO> getVoList() {
		return voList;
	}

	public List<PfpAdTimeReportVO> getTotalList() {
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
