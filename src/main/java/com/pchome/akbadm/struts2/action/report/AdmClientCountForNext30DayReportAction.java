package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.pchome.akbadm.db.service.report.IAdmClientCountReportService;
import com.pchome.akbadm.db.vo.report.AdmClientCountForNext30DayReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class AdmClientCountForNext30DayReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private IAdmClientCountReportService admClientCountReportService;
	
	private DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
	private SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
	//輸入參數
	private String startDate;
	private String endDate;
	private String downloadFlag = "";
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	//訊息
	private String message = "";
	
	private AdmClientCountForNext30DayReportVO avgVO;			//平均
	private List<AdmClientCountForNext30DayReportVO> dataList;
	
	public String execute() throws Exception {
		
		avgVO = new AdmClientCountForNext30DayReportVO();
		dataList = new ArrayList<AdmClientCountForNext30DayReportVO>();
		
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		for(int i=0;i<=29;i++){
			if(i>0){
				c.add(Calendar.DATE, 1);
			}
			String searchDay = dateFormate.format(c.getTime());
			
			if(i==0){
				startDate = searchDay;
			}
			
			if(i==29){
				endDate = searchDay;
			}
			
			List<AdmClientCountForNext30DayReportVO> data = admClientCountReportService.findReportDataFpr30Day(searchDay);
			dataList.addAll(data);
		}
		
		if(dataList.size()==0){
    		this.message = "查無資料！";
    		return SUCCESS;
    	}
		
		Double dataSize = new Double(30);				//總比數
		Double pfpCountSum = new Double(0);				//廣告客戶數
		Double pfpAdActionCountSum = new Double(0);		//廣告活動數
		Double pfpAdGroupCountSum = new Double(0);		//廣告分類數
		Double pfpAdCountSum = new Double(0);			//廣告明細數
		Double pfpAdActionMaxPriceSum = new Double(0);	//廣告每日預算
		
		if(!dataList.isEmpty()){
			for(AdmClientCountForNext30DayReportVO vo:dataList){
				pfpCountSum += new Double(vo.getPfpCount().replaceAll(",", ""));
				pfpAdActionCountSum += new Double(vo.getPfpAdActionCount().replaceAll(",", ""));
				pfpAdGroupCountSum += new Double(vo.getPfpAdGroupCount().replaceAll(",", ""));
				pfpAdCountSum += new Double(vo.getPfpAdCount().replaceAll(",", ""));
				pfpAdActionMaxPriceSum += new Double(vo.getPfpAdActionMaxPrice().replaceAll(",", ""));
			}
		}
		
		//平均
		avgVO.setPfpCount(df2.format(pfpCountSum/dataSize));
		avgVO.setPfpAdActionCount(df2.format(pfpAdActionCountSum/dataSize));
		avgVO.setPfpAdGroupCount(df2.format(pfpAdGroupCountSum/dataSize));
		avgVO.setPfpAdCount(df2.format(pfpAdCountSum/dataSize));
		avgVO.setPfpAdActionMaxPrice(df2.format(pfpAdActionMaxPriceSum/dataSize));
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		return SUCCESS;
	}
	
	
	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="付費刊登-未來30天統計日報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"日期","廣告客戶數","廣告活動數","廣告明細數","廣告花費"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,付費刊登-未來30天統計日報表");
		content.append("\n\n");
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
    	
		for (int i=0; i<dataList.size(); i++) {
			AdmClientCountForNext30DayReportVO vo = dataList.get(i);
			
			content.append("\"" + vo.getCountDate() + "(" + vo.getWeek() + ")" + "\",");
			content.append("\"" + vo.getPfpCount() + "\",");
			content.append("\"" + vo.getPfpAdActionCount() + "\",");
			content.append("\"" + vo.getPfpAdCount() + "\",");
			content.append("\"$ " + vo.getPfpAdActionMaxPrice() + "\",");
			content.append("\n");
		}
    	
		content.append("\n");
		
		//平均
		content.append("\"平均\",");
		content.append("\"" + avgVO.getPfpCount() + "\",");
		content.append("\"" + avgVO.getPfpAdActionCount() + "\",");
		content.append("\"" + avgVO.getPfpAdCount() + "\",");
		content.append("\"$ " + avgVO.getPfpAdActionMaxPrice() + "\",");
		content.append("\n");
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		
	}

	public void setAdmClientCountReportService(
			IAdmClientCountReportService admClientCountReportService) {
		this.admClientCountReportService = admClientCountReportService;
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

	public void setDownloadFlag(String downloadFlag) {
		this.downloadFlag = downloadFlag;
	}

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public String getMessage() {
		return message;
	}

	public AdmClientCountForNext30DayReportVO getAvgVO() {
		return avgVO;
	}

	public List<AdmClientCountForNext30DayReportVO> getDataList() {
		return dataList;
	}
	
}
