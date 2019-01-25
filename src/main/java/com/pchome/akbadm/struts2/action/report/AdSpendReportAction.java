package com.pchome.akbadm.struts2.action.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.PfdCustomerInfo;
import com.pchome.akbadm.db.service.pfd.account.IPfdAccountService;
import com.pchome.akbadm.db.service.report.IAdReportService;
import com.pchome.akbadm.db.vo.AdActionReportVO;
import com.pchome.akbadm.struts2.BaseAction;
import com.pchome.enumerate.pfd.EnumPfdAccountStatus;
import com.pchome.soft.util.DateValueUtil;

/**
 * 花費成效排名
 */
public class AdSpendReportAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private IAdReportService adReportService;
	private IPfdAccountService pfdAccountService;
	private String fontPath;	// 字形檔參數

	//查詢參數
	private String startDate; //起始日期
	private String endDate; //結束日期
	private String displayCount = "50"; //顯示前幾名
	private String pfdCustomerInfoId;	// 經銷歸屬
	private int pageNo = 1;       				// 初始化目前頁數
	private int pageSize = 50;     				// 初始化每頁幾筆
	private int pageCount = 0;    				// 初始化共幾頁
	private long totalCount = 0;   				// 初始化共幾筆

	//總計
	private AdActionReportVO totalVO = new AdActionReportVO();



	//查詢結果
	private List<AdActionReportVO> dataList = new ArrayList<AdActionReportVO>();

	//下載檔案
	private String fileName = "";
	private InputStream downloadFileStream;//下載報表的 input stream
	//private InputStream pdfStream = null;

	//訊息
	private String message = "";

	@Override
    public String execute() throws Exception {

		log.info(" fontPath: "+fontPath);
		log.info(" startDate: "+startDate);
		log.info(" endDate: "+endDate);
		log.info(" pfdCustomerInfoId: "+pfdCustomerInfoId);

		if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {

			this.message = "請選擇日期開始查詢！";

		} else {
			DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
			DecimalFormat df2 = new DecimalFormat("###,###,###,###");

			long controlPriceSum = 0;		//調控金額
			long pvSum = 0;					//曝光數
			long clkSum = 0;				//點擊數
			double priceSum = 0;			//費用
			double clkRateSum = 0;			//點擊率
			double clkPriceAvgSum = 0;		//平均點擊費用
			double pvPriceAvgSum = 0;       //千次曝光收益
			double spendRateSum = 0;		//消耗率

			this.dataList = adReportService.getAdSpendReport(startDate, endDate, pfdCustomerInfoId);


			if (dataList.size()==0) {
				this.message = "查無資料！";
			} else {
				List<AdActionReportVO> viewList = new ArrayList<AdActionReportVO>();

				totalCount = dataList.size();
	    		pageCount = (int) Math.ceil(((float)totalCount / pageSize));

	    		for (int i=0; i<dataList.size(); i++) {
	    			AdActionReportVO vo = new AdActionReportVO();
	    			vo = dataList.get(i);

	    			if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
	    				viewList.add(vo);
	    			}

	    			if(vo.getPvSum() != null){
	    				pvSum += Long.parseLong(vo.getPvSum().replaceAll(",", ""));
	    			}

	    			if(vo.getAdActionControlPrice() != null){
	    				controlPriceSum += Long.parseLong(vo.getAdActionControlPrice().replaceAll(",", ""));
	    			}

	    			if(vo.getClkSum() != null){
	    				clkSum += Long.parseLong(vo.getClkSum().replaceAll(",", ""));
	    			}

	    			if(vo.getPriceSum() != null){
	    				priceSum += Double.parseDouble(vo.getPriceSum().replaceAll(",", ""));
	    			}
	    		}
	    		dataList = viewList;
			}

			//點擊率
			if(pvSum != 0 && clkSum != 0){
				clkRateSum = (double) clkSum*100 / pvSum;
			}

			//平均點選費用
			if(clkSum != 0 && priceSum != 0){
				clkPriceAvgSum = priceSum / clkSum;
			}

            //千次曝光收益
            if ((priceSum != 0) && (pvSum != 0)) {
                pvPriceAvgSum = priceSum * 1000 / pvSum;
            }

			//消耗率
			long countDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);
			if(controlPriceSum != 0 && priceSum != 0 && countDay != 0){
				spendRateSum = priceSum / (controlPriceSum * countDay) * 100;
			}

			totalVO.setPvSum(df2.format(pvSum));
			totalVO.setClkSum(df2.format(clkSum));
			totalVO.setPriceSum(df2.format(priceSum));
			totalVO.setClkRate(df.format(clkRateSum));
			totalVO.setClkPriceAvg(df.format(clkPriceAvgSum));
            totalVO.setPvPriceAvg(df.format(pvPriceAvgSum));
			totalVO.setSpendRate(df.format(spendRateSum));

		}
		return SUCCESS;
	}

	public String downloadAdSpend() throws Exception {

		log.info(">>> startDate = " + startDate);
		log.info(">>> endDate = " + endDate);

		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		String reportNo = dateFormat.format(now);

		this.fileName = "花費成效排名_" + reportNo + ".csv";
		//this.fileName =  URLEncoder.encode("花費成效排名_" + reportNo + ".pdf", "UTF-8");

		try {

			this.dataList = adReportService.getAdSpendReport(startDate, endDate, pfdCustomerInfoId);
	        log.info(">>> dataList.size() = " + dataList.size());

	        if (dataList.size()==0) {
	        	this.message = "查無資料！";
	        	return INPUT;
	        }

	        String[] colNameArray = {"經銷歸屬" ,"會員帳號", "帳戶名稱", "廣告活動", "目前花費上限", "每日花費上限", "調控金額", "曝光數", "互動數", "互動率", "平均互動出價", "千次曝光出價", "費用", "消耗率", "負責業務"};
	        DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
			DecimalFormat df2 = new DecimalFormat("###,###,###,###");

			long controlPriceSum = 0;		//調控金額
			long pvSum = 0;					//曝光數
			long clkSum = 0;				//點擊數
			double priceSum = 0;			//費用
			double clkRateSum = 0;			//點擊率
			double clkPriceAvgSum = 0;		//平均點擊費用
            double pvPriceAvgSum = 0;       //千次曝光收益
			double spendRateSum = 0;		//消耗率

	        StringBuffer content=new StringBuffer();

	        content.append("報表名稱:,花費成效");
			content.append("\n\n");

			content.append("經銷歸屬:,");
			if(StringUtils.isNotEmpty(pfdCustomerInfoId)){
				String pfdCustomerInfoName = pfdAccountService.get(pfdCustomerInfoId).getCompanyName();
				content.append(pfdCustomerInfoName);
			} else {
				content.append("全部");
			}
			content.append("\n\n");

			content.append("日期範圍:," + startDate + " 到 " + endDate);
			content.append("\n\n");

			for(String s:colNameArray){
				content.append("\"" + s + "\"");
				content.append(",");
			}
			content.append("\n");

			for (int i=0; i<dataList.size(); i++) {
    			AdActionReportVO vo = new AdActionReportVO();
    			vo = dataList.get(i);
    			content.append("\"" + vo.getDealer() + "\",");
    			content.append("\"" + vo.getCustomerInfoId() + "\",");
    			content.append("\"" + vo.getCustomerInfoName() + "\",");
    			content.append("\"" + vo.getAdActionName() + "\",");
    			content.append("\"$ " + vo.getNowMaxPrice() + "\",");
    			content.append("\"$ " + vo.getMaxPrice() + "\",");
    			content.append("\"$ " + vo.getAdActionControlPrice() + "\",");
    			content.append("\"" + vo.getPvSum() + "\",");
    			content.append("\"" + vo.getClkSum() + "\",");
    			content.append("\"" + vo.getClkRate() + "%\",");
    			content.append("\"$ " + vo.getClkPriceAvg() + "\",");
                content.append("\"$ " + vo.getPvPriceAvg() + "\",");
    			content.append("\"$ " + vo.getPriceSum() + "\",");
    			content.append("\"" + vo.getSpendRate() + "%\",");
    			content.append("\"" + vo.getPfdUserName() + "\",");
    			content.append("\n");

    			if(vo.getPvSum() != null){
    				pvSum += Long.parseLong(vo.getPvSum().replaceAll(",", ""));
    			}

    			if(vo.getAdActionControlPrice() != null){
    				controlPriceSum += Long.parseLong(vo.getAdActionControlPrice().replaceAll(",", ""));
    			}

    			if(vo.getClkSum() != null){
    				clkSum += Long.parseLong(vo.getClkSum().replaceAll(",", ""));
    			}

    			if(vo.getPriceSum() != null){
    				priceSum += Double.parseDouble(vo.getPriceSum().replaceAll(",", ""));
    			}
    		}
			content.append("\n");

			//點擊率
			if(pvSum != 0 && clkSum != 0){
				clkRateSum = (double) clkSum*100 / pvSum;;
			}

			//平均點選費用
			if(clkSum != 0 && priceSum != 0){
				clkPriceAvgSum = priceSum / clkSum;
			}

            //千次曝光收益
            if ((priceSum != 0) && (pvSum != 0)) {
                pvPriceAvgSum = priceSum * 1000 / pvSum;
            }

			//消耗率
			long countDay = DateValueUtil.getInstance().getDateDiffDay(startDate, endDate);
			if(controlPriceSum != 0 && priceSum != 0 && countDay != 0){
				spendRateSum = priceSum / (controlPriceSum * countDay) * 100;
			}

			//總計
			content.append("\"總計:" + dataList.size() + "\",");
			content.append("\"" +  "\",");
			content.append("\"" +  "\",");
			content.append("\"" +  "\",");
			content.append("\"" +  "\",");
			content.append("\"" +  "\",");
			content.append("\"" +  "\",");
			content.append("\"" + df2.format(pvSum) + "\",");
			content.append("\"" + df2.format(clkSum) + "\",");
			content.append("\"" + df.format(clkRateSum) + "%\",");
			content.append("\"$ " + df.format(clkPriceAvgSum) + "\",");
            content.append("\"$ " + df.format(pvPriceAvgSum) + "\",");
			content.append("\"$ " + df2.format(priceSum) + "\",");
			content.append("\"" + df.format(spendRateSum) + "%\",");
			content.append("\"" +  "\",");

			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			} else {
				fileName = URLEncoder.encode(fileName, "UTF-8");
			}

			downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));

	        //pdf
	        /*ByteArrayOutputStream out = new ByteArrayOutputStream();

	        Document doc = new Document(PageSize.A4.rotate(), -35, -35, 20, 20);

			PdfWriter.getInstance(doc, out);

			doc.open();

			PdfUtil pdfUtil = new PdfUtil(fontPath);

			pdfUtil.prepareAdSpendReportPdf(doc, dataList,	startDate, endDate);

			doc.close();

			this.pdfStream = new ByteArrayInputStream(out.toByteArray());;*/

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		return SUCCESS;
	}

	public Map<String, String> getDisplayCountMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("50", "50");
		map.put("100", "100");
		map.put("500", "500");
		map.put("1000", "1000");
		return map;
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

		} catch(Exception ex) {
			log.info("Exception :" + ex);
		}

		return pfdCustomerInfos;
	}

	public void setFontPath(String fontPath) {
		this.fontPath = fontPath;
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

	public String getDisplayCount() {
		return displayCount;
	}

	public void setDisplayCount(String displayCount) {
		this.displayCount = displayCount;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public List<AdActionReportVO> getDataList() {
		return dataList;
	}

	public void setDataList(List<AdActionReportVO> dataList) {
		this.dataList = dataList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*public InputStream getPdfStream() {
		return pdfStream;
	}

	public void setPdfStream(InputStream pdfStream) {
		this.pdfStream = pdfStream;
	}*/

	public InputStream getDownloadFileStream() {
		return downloadFileStream;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setAdReportService(IAdReportService adReportService) {
		this.adReportService = adReportService;
	}

	public void setPfdAccountService(IPfdAccountService pfdAccountService) {
		this.pfdAccountService = pfdAccountService;
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

	public AdActionReportVO getTotalVO() {
		return totalVO;
	}

}
