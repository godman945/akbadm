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

import com.pchome.akbadm.db.service.report.IAdmPortalBonusReportService;
import com.pchome.akbadm.db.vo.report.AdmPortalBonusReportVO;
import com.pchome.akbadm.struts2.BaseAction;

public class AdmPortalBonusReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private IAdmPortalBonusReportService admPortalBonusReportService;
	
	//private DecimalFormat df1 = new DecimalFormat("###,###,###,###");
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
	private AdmPortalBonusReportVO totalVO;			//總計
	private List<AdmPortalBonusReportVO> dataList;
	
	public String execute() throws Exception {
		
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		totalVO = new AdmPortalBonusReportVO();
		dataList = admPortalBonusReportService.findReportData(startDate, endDate);
		
		log.info("dataList: "+dataList.size());
		
		if(dataList.size()==0){
    		this.message = "查無資料！";
    		return SUCCESS;
    	}
		
		List<AdmPortalBonusReportVO> viewList = new ArrayList<AdmPortalBonusReportVO>();
		
		totalCount = dataList.size();
		pageCount = (int) Math.ceil(((float)totalCount / pageSize));
		totalSize = totalCount;
		
		Double portalValidPriceSum = new Double(0);			//PChome直客經銷商有效花費
		Double portalPfdBonusSum = new Double(0);			//PChome直客經銷商獎金
		Double ddTestValidPriceSum = new Double(0);			//DD測試經銷商有效花費
		Double ddTestPfdBonusSum = new Double(0);			//DD測試經銷商獎金
		Double salesValidPriceSum = new Double(0);			//PORTA業務用有效花費
		Double salesPfdBonusSum = new Double(0);			//PORTA業務用獎金
		Double portalPfbBonusSum = new Double(0);			//PChome網站總分潤
		Double portalOperatingIncomeSum = new Double(0);	//PChome營運收入
		Double incomeSumSum = new Double(0);				//收入總計
		
		for (int i=0; i<dataList.size(); i++) {
			AdmPortalBonusReportVO vo = dataList.get(i);
			
			portalValidPriceSum += new Double(vo.getPortalValidPrice().replaceAll(",", ""));
			portalPfdBonusSum += new Double(vo.getPortalPfdBonus().replaceAll(",", ""));
			ddTestValidPriceSum += new Double(vo.getDdTestValidPrice().replaceAll(",", ""));
			ddTestPfdBonusSum += new Double(vo.getDdTestPfdBonus().replaceAll(",", ""));
			salesValidPriceSum += new Double(vo.getSalesValidPrice().replaceAll(",", ""));
			salesPfdBonusSum += new Double(vo.getSalesPfdBonus().replaceAll(",", ""));
			portalPfbBonusSum += new Double(vo.getPortalPfbBonus().replaceAll(",", ""));
			portalOperatingIncomeSum += new Double(vo.getPortalOperatingIncome().replaceAll(",", ""));
			incomeSumSum += new Double(vo.getIncomeSum().replaceAll(",", ""));
			
			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
				viewList.add(vo);
			}
		}
		
		totalVO.setPortalValidPrice(df2.format(portalValidPriceSum));
		totalVO.setPortalPfdBonus(df2.format(portalPfdBonusSum));
		totalVO.setDdTestValidPrice(df2.format(ddTestValidPriceSum));
		totalVO.setDdTestPfdBonus(df2.format(ddTestPfdBonusSum));
		totalVO.setSalesValidPrice(df2.format(salesValidPriceSum));
		totalVO.setSalesPfdBonus(df2.format(salesPfdBonusSum));
		totalVO.setPortalPfbBonus(df2.format(portalPfbBonusSum));
		totalVO.setPortalOperatingIncome(df2.format(portalOperatingIncomeSum));
		totalVO.setIncomeSum(df2.format(incomeSumSum));
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		dataList = viewList;
		
		return SUCCESS;
	}
	
	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="付費刊登-PORTAL收益報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"日期","星期","PChome直客管理經銷商廣告執行","PChome直客管理經銷商廣告客戶數","PChome直客管理經銷商獎金","DD-測試經銷商廣告執行",
    			"DD-測試經銷商廣告客戶數","DD-測試經銷商獎金","PORTAL業務用廣告執行","PORTAL業務用廣告客戶數","PORTAL業務用經銷商獎金","PChome網站總分潤","PChome營運收入","PChome收入總計"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,付費刊登-PORTAL收益報表");
		content.append("\n\n");
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
    	
		for (int i=0; i<dataList.size(); i++) {
			AdmPortalBonusReportVO vo = dataList.get(i);
			
			content.append("\"" + vo.getAdPvclkDate() + "\",");
			content.append("\"" + vo.getWeek() + "\",");
			content.append("\"$ " + vo.getPortalValidPrice() + "\",");
			content.append("\"" + vo.getPortalPfpClientCount() + "\",");
			content.append("\"$ " + vo.getPortalPfdBonus() + "\",");
			content.append("\"$ " + vo.getDdTestValidPrice() + "\",");
			content.append("\"" + vo.getDdTestPfpClientCount() + "\",");
			content.append("\"$ " + vo.getDdTestPfdBonus() + "\",");
			content.append("\"$ " + vo.getSalesValidPrice() + "\",");
			content.append("\"" + vo.getSalesPfpClientCount() + "\",");
			content.append("\"$ " + vo.getSalesPfdBonus() + "\",");
			content.append("\"$ " + vo.getPortalPfbBonus() + "\",");
			content.append("\"$ " + vo.getPortalOperatingIncome() + "\",");
			content.append("\"$ " + vo.getIncomeSum() + "\",");
			content.append("\n");
		}
    	
		content.append("\n");
		
		//加總
		content.append("\"加總\",");
		content.append("\"" + "\",");
		content.append("\"$ " + totalVO.getPortalValidPrice() + "\",");
		content.append("\"" + "\",");
		content.append("\"$ " + totalVO.getPortalPfdBonus() + "\",");
		content.append("\"$ " + totalVO.getDdTestValidPrice() + "\",");
		content.append("\"" + "\",");
		content.append("\"$ " + totalVO.getDdTestPfdBonus() + "\",");
		content.append("\"$ " + totalVO.getSalesValidPrice() + "\",");
		content.append("\"" + "\",");
		content.append("\"$ " + totalVO.getSalesPfdBonus() + "\",");
		content.append("\"$ " + totalVO.getPortalPfbBonus() + "\",");
		content.append("\"$ " + totalVO.getPortalOperatingIncome() + "\",");
		content.append("\"$ " + totalVO.getIncomeSum() + "\",");
		content.append("\n");
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		
	}
	
	public void setAdmPortalBonusReportService(IAdmPortalBonusReportService admPortalBonusReportService) {
		this.admPortalBonusReportService = admPortalBonusReportService;
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

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public AdmPortalBonusReportVO getTotalVO() {
		return totalVO;
	}

	public List<AdmPortalBonusReportVO> getDataList() {
		return dataList;
	}
	
}
