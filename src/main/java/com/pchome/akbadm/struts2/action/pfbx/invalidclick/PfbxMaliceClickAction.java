package com.pchome.akbadm.struts2.action.pfbx.invalidclick;

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

import com.pchome.akbadm.db.service.ad.IPfpAdClickService;
import com.pchome.akbadm.db.vo.pfbx.report.PfpAdClickVO;
import com.pchome.akbadm.struts2.BaseCookieAction;

public class PfbxMaliceClickAction extends BaseCookieAction {

	private static final long serialVersionUID = 1L;
	
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	
	private IPfpAdClickService pfpAdClickService;

	//訊息
	private String message = "";
	
	//頁數
	private int pageNo = 1;       					// 初始化目前頁數
	private int pageSize = 50;     					// 初始化每頁幾筆
	private int pageCount = 0;    					// 初始化共幾頁
	private long totalCount = 0;   					// 初始化共幾筆
	private long totalSize = 0;						//總比數
	
	
	//輸入參數
	private String startDate;
	private String endDate;
	private String pfbxCustomerInfoId;
	private String pfbxPositionId;
	private String maliceType = "";
	
	private Map<String,String> maliceTypeMap;
	private List<PfpAdClickVO> voList;
	private PfpAdClickVO totalVO;
	
	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	public String execute() throws Exception {
		
		totalVO = new PfpAdClickVO();
		
		getSelectMap();
		
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		if(StringUtils.isNotEmpty(startDate)){
			conditionMap.put("startDate", startDate);
		}
		
		if(StringUtils.isNotEmpty(endDate)){
			conditionMap.put("endDate", endDate);
		}
		
		if(StringUtils.isNotEmpty(pfbxCustomerInfoId)){
			conditionMap.put("pfbxCustomerInfoId", pfbxCustomerInfoId);
		}
		
		if(StringUtils.isNotEmpty(pfbxPositionId)){
			conditionMap.put("pfbxPositionId", pfbxPositionId);
		}
		
		if(StringUtils.isNotEmpty(maliceType)){
			conditionMap.put("maliceType", maliceType);
		}
		
		voList = new ArrayList<PfpAdClickVO>();
		voList = pfpAdClickService.findMaliceClick(conditionMap);
		
		double totalAdPrice = new Double(0);
		List<PfpAdClickVO> dataList = new ArrayList<PfpAdClickVO>();
		if(!voList.isEmpty()){
			totalCount = voList.size();
			pageCount = (int) Math.ceil(((float)totalCount / pageSize));
			totalSize = totalCount;
			
			
			for(int i=0; i<voList.size(); i++){
				PfpAdClickVO vo = voList.get(i);
				String maliceName = maliceTypeMap.get(vo.getMaliceType());
				vo.setMaliceType(maliceName);
				if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
					dataList.add(vo);
				}
				totalAdPrice += Double.parseDouble(vo.getAdPrice().replaceAll(",", ""));
			}
		}
		
		totalVO.setAdPrice(df.format(totalAdPrice));
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		voList = dataList;
		
		return SUCCESS;
	}
	
	private void makeDownloadReportData() throws Exception {
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="PFB惡意點擊統計報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"日期","時間","類別","帳戶編號","版位","會員帳號","uuid","ip","referer","user_agent","滑鼠移動判斷","滑鼠移動區域:寬","滑鼠移動區域:高","滑鼠點擊位置:X	","滑鼠點擊位置:Y","無效點擊費用"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,惡意點擊統計報表");
		content.append("\n\n");
		
		if(StringUtils.isNotEmpty(pfbxCustomerInfoId)){
			content.append("查詢帳戶編號:," + pfbxCustomerInfoId);	
			content.append("\n\n");	
		}
		
		if(StringUtils.isNotEmpty(pfbxPositionId)){
			content.append("查詢版位編號:," + pfbxPositionId);	
			content.append("\n\n");	
		} 
		
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
		
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<voList.size(); i++) {
			PfpAdClickVO vo = new PfpAdClickVO();
			vo = voList.get(i);
			content.append("\"" + vo.getRecordDate() + "\",");
			content.append("\"" + vo.getRecordTime() + "\",");
			content.append("\"" + vo.getMaliceType() + "\",");
			content.append("\"" + vo.getPfbxCustomerInfoId() + "\",");
			content.append("\"" + vo.getPfbxPositionId() + "\",");
			content.append("\"" + vo.getMemId() + "\",");
			content.append("\"" + vo.getUuid() + "\",");
			content.append("\"" + vo.getRemoteIp() + "\",");
			content.append("\"" + vo.getReferer() + "\",");
			content.append("\"" + vo.getUserAgent() + "\",");
			content.append("\"" + vo.getMouseMoveFlag() + "\",");
			content.append("\"" + vo.getMouseAreaWidth() + "\",");
			content.append("\"" + vo.getMouseAreaHeight() + "\",");
			content.append("\"" + vo.getMouseDownX() + "\",");
			content.append("\"" + vo.getMouseDownY() + "\",");
			content.append("\"$ " + vo.getAdPrice() + "\"");
			
			content.append("\n");
		}
		content.append("\n");
		content.append("\n");
		
		content.append("\"總計：" + totalCount + "筆\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"$ " + totalVO.getAdPrice() + "\"");
		content.append("\n");
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}
	
	public void getSelectMap(){
		maliceTypeMap = new LinkedHashMap<String,String>();
		
		maliceTypeMap.put("1", "uuid 空白");
		maliceTypeMap.put("2", "ip 黑名單");
		maliceTypeMap.put("3", "url 空白");
		maliceTypeMap.put("4", "userAgent 黑名單");
		maliceTypeMap.put("5", "同廣告點擊20次 ");
		maliceTypeMap.put("9", "同版位 uuid 點擊10次");
		maliceTypeMap.put("10", "同版位 ip 點擊10次");
		maliceTypeMap.put("11", "同版位 pcid 點擊10次");
		maliceTypeMap.put("12", "PFB管理人員點擊無效");
		maliceTypeMap.put("99", "手動判定為無效點擊數");
		
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

	public String getMessage() {
		return message;
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

	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}

	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}

	public String getPfbxPositionId() {
		return pfbxPositionId;
	}

	public void setPfbxPositionId(String pfbxPositionId) {
		this.pfbxPositionId = pfbxPositionId;
	}

	public String getMaliceType() {
		return maliceType;
	}

	public void setMaliceType(String maliceType) {
		this.maliceType = maliceType;
	}

	public Map<String, String> getMaliceTypeMap() {
		return maliceTypeMap;
	}

	public List<PfpAdClickVO> getVoList() {
		return voList;
	}

	public PfpAdClickVO getTotalVO() {
		return totalVO;
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

	public void setPfpAdClickService(IPfpAdClickService pfpAdClickService) {
		this.pfpAdClickService = pfpAdClickService;
	}

}
