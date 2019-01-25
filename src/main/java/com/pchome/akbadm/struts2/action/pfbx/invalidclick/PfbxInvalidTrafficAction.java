package com.pchome.akbadm.struts2.action.pfbx.invalidclick;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.config.session.SessionConstants;
import com.pchome.akbadm.db.pojo.PfbxInvalidTraffic;
import com.pchome.akbadm.db.pojo.PfbxInvalidTrafficDetail;
import com.pchome.akbadm.db.pojo.PfpAdClick;
import com.pchome.akbadm.db.service.accesslog.IAdmAccesslogService;
import com.pchome.akbadm.db.service.ad.IPfpAdClickService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidTrafficDetailService;
import com.pchome.akbadm.db.service.pfbx.invalidclick.IPfbxInvalidTrafficService;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficDetailVO;
import com.pchome.akbadm.db.vo.pfbx.invalidclick.PfbxInvalidTrafficVO;
import com.pchome.akbadm.factory.malice.EnumMaliceType;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.pfbx.invalidclick.EnumPfbInvalidTrafficType;
import com.pchome.rmi.accesslog.EnumAccesslogAction;
import com.pchome.rmi.accesslog.EnumAccesslogChannel;
import com.pchome.rmi.accesslog.EnumAccesslogEmailStatus;

public class PfbxInvalidTrafficAction extends BaseCookieAction {

	private static final long serialVersionUID = 1L;
	
	DecimalFormat df = new DecimalFormat("###,###,###,###");
	DecimalFormat df2 = new DecimalFormat("###,###,###,##0.00");
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	
	private IPfbxInvalidTrafficService pfbxInvalidTrafficService;
	private IPfbxInvalidTrafficDetailService pfbxInvalidTrafficDetailService;
	private IPfpAdClickService pfpAdClickService;
	private IAdmAccesslogService admAccesslogService;
	
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
	private String closeDate;
	private String selectType = "";
	private String invId;
	
	private Map<String,String> selectTypeMap;
	
	private List<PfbxInvalidTrafficVO> voList;
	private List<PfbxInvalidTrafficDetailVO> detailVoList;
	
	private PfbxInvalidTrafficVO totalTrafficVO;
	
	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	public String execute() throws Exception {
		
		totalTrafficVO = new PfbxInvalidTrafficVO();
		
		getMapData();
		
		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate) && StringUtils.isEmpty(closeDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		/*if (StringUtils.isEmpty(pfbxCustomerInfoId)) {
			this.message = "PFB帳號不可空白！";
			return SUCCESS;
		}*/
		
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
		
		if(StringUtils.isNotEmpty(closeDate)){
			String closeEndDate = closeDate + "-26";
			String closeStartDate = getCloseStartDay(closeEndDate);
			conditionMap.put("closeStartDate", closeStartDate);
			conditionMap.put("closeEndDate", closeEndDate);
		}
		
		if(StringUtils.isNotEmpty(selectType)){
			conditionMap.put("selectType", selectType);
		}
		
		voList = new ArrayList<PfbxInvalidTrafficVO>();
		voList = pfbxInvalidTrafficService.getInvalidTrafficByCondition(conditionMap);
		
		double totalInvPrice = new Double(0);
		double totalInvPfbBonus = new Double(0);
		
		List<PfbxInvalidTrafficVO> dataList = new ArrayList<PfbxInvalidTrafficVO>();
		if(!voList.isEmpty()){
			totalCount = voList.size();
			pageCount = (int) Math.ceil(((float)totalCount / pageSize));
			totalSize = totalCount;
			
			for(int i=0; i<voList.size(); i++){
				PfbxInvalidTrafficVO vo = voList.get(i);
				if((pageNo -1)*pageSize <= i && pageNo*pageSize > i){
					dataList.add(vo);
				}
				
				totalInvPrice += Double.parseDouble(vo.getInvPrice().replaceAll(",", ""));
				totalInvPfbBonus += Double.parseDouble(vo.getInvPfbBonus().replaceAll(",", ""));
			}
		}
		
		totalTrafficVO.setInvPrice(df.format(totalInvPrice));
		totalTrafficVO.setInvPfbBonus(df2.format(totalInvPfbBonus));
		
		voList = dataList;
		
		return SUCCESS;
	}
	
	public String getDetailData(){
		
		Map<String, String> conditionMap = new HashMap<String, String>();
		
		conditionMap.put("invId", invId);
		
		detailVoList = new ArrayList<PfbxInvalidTrafficDetailVO>();
		detailVoList = pfbxInvalidTrafficDetailService.getInvalidTrafficDetailByCondition(conditionMap);
		
		return SUCCESS;
	}
	
	public String deleteInvalidTraffic(){
		
		getMapData();
		
		PfbxInvalidTraffic pfbxInvalidTraffic = pfbxInvalidTrafficService.findInvalidTrafficById(invId);
		
		if(pfbxInvalidTraffic != null){
			
			List<Long> adClickIdList = new ArrayList<Long>();
			
			List<PfbxInvalidTrafficDetail> detailList = new ArrayList<PfbxInvalidTrafficDetail>(pfbxInvalidTraffic.getPfbxInvalidTrafficDetails());
			Integer detailSize = detailList.size();
			
			//刪除無效流量記錄
			String pfbxPositionId = detailList.get(0).getPfbxPositionId();
			for(PfbxInvalidTrafficDetail data:detailList){
				adClickIdList.add(data.getAdClickId());
				
				if(!StringUtils.equals(pfbxPositionId, data.getPfbxPositionId())){
					pfbxPositionId = "不分版位";
				}
				
				pfbxInvalidTrafficDetailService.delete(data);
			}
			
			//刪除無效流量主檔
			pfbxInvalidTrafficService.delete(pfbxInvalidTraffic);
			
			
			List<PfpAdClick> clickList = pfpAdClickService.findPfpAdClickByTraffic(adClickIdList);
			
			for(PfpAdClick pfpAdClick:clickList){
				pfpAdClick.setMaliceType(Integer.parseInt(EnumMaliceType.OK.getType()));
				pfpAdClick.setUpdateDate(new Date());
			}
			
			pfpAdClickService.saveOrUpdateAll(clickList);
			
			//access log
		    String logMsg = "【扣款退回】    無效類別：" + selectTypeMap.get(pfbxInvalidTraffic.getInvType()) + 
		    				";紀錄日期：" + dateFormate.format(pfbxInvalidTraffic.getInvDate()) + 
		    				";PFB帳號：" + pfbxInvalidTraffic.getPfbId() + 
		    				";PFB版位：" + pfbxPositionId + 
		    				";數量/花費金額：" + detailSize + "/$" + df.format(pfbxInvalidTraffic.getInvPrice());
		    admAccesslogService.addAdmAccesslog(EnumAccesslogChannel.ADM, EnumAccesslogAction.PFB_INVALID_TRAFFIC, logMsg, 
			    super.getSession().get(SessionConstants.SESSION_USER_ID).toString(), null, null, 
			    null, request.getRemoteAddr(), EnumAccesslogEmailStatus.NO);
		}
		
		return SUCCESS;
	}
	
	public String makeDownloadReportData() throws Exception {
		
		getMapData();
		
		if (StringUtils.isEmpty(startDate) && StringUtils.isEmpty(endDate) && StringUtils.isEmpty(closeDate)) {
			this.message = "請選擇日期開始查詢！";
			return SUCCESS;
		}
		
		/*if (StringUtils.isEmpty(pfbxCustomerInfoId)) {
			this.message = "PFB帳號不可空白！";
			return SUCCESS;
		}*/
		
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
		
		if(StringUtils.isNotEmpty(closeDate)){
			String closeEndDate = closeDate + "-26";
			String closeStartDate = getCloseStartDay(closeEndDate);
			conditionMap.put("closeStartDate", closeStartDate);
			conditionMap.put("closeEndDate", closeEndDate);
		}
		
		if(StringUtils.isNotEmpty(selectType)){
			conditionMap.put("selectType", selectType);
		}
		
		detailVoList = new ArrayList<PfbxInvalidTrafficDetailVO>();
		detailVoList = pfbxInvalidTrafficDetailService.getInvalidTrafficDetailByDownload(conditionMap);
		
		totalCount = detailVoList.size();
		
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="PFB無效流量扣款明細報表_" + dformat.format(new Date()) + ".csv";
    	String[] tableHeadArray = {"日期","帳戶編號","版位編號","點擊區間","無效點擊數","無效點擊費用","無效分潤","無效類別","無效原因"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,無效流量扣款明細報表");
		content.append("\n\n");
		
		content.append("帳戶編號:,");
		if(StringUtils.isNotEmpty(pfbxCustomerInfoId)){
			content.append(pfbxCustomerInfoId);	
		} else {
			content.append("全部");
		}
		
		content.append("\n\n");
		
		
		content.append("日期範圍:," + startDate + " 到 " + endDate);
		content.append("\n\n");
    	
		if(StringUtils.isNotEmpty(closeDate)){
			String[] dateArray = closeDate.split("-");
			content.append("結算月份:," + dateArray[0] + "年" + dateArray[1] + "月份");	
			content.append("\n\n");	
		} 
		
		content.append("無效類別:,");
		if(StringUtils.isNotEmpty(selectType)){
			content.append(selectTypeMap.get(selectType));	
		} else {
			content.append("全部");
		}
		content.append("\n\n");	
		
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		double totalAdPrice = new Double(0);
		double totalInvPfbBonus = new Double(0);
		
		for (int i=0; i<detailVoList.size(); i++) {
			PfbxInvalidTrafficDetailVO vo = new PfbxInvalidTrafficDetailVO();
			vo = detailVoList.get(i);
			
			content.append("\"" + vo.getInvDate() + "\",");
			content.append("\"" + vo.getPfbId() + "\",");
			content.append("\"" + vo.getPfbxPositionId() + "\",");
			content.append("\"" + vo.getRecordTime() + "\",");
			content.append("\"" + vo.getAdClk() + "\",");
			content.append("\"$ " + vo.getAdPrice() + "\",");
			content.append("\"$ " + vo.getInvPfbBonus() + "\",");
			content.append("\"" + vo.getInvType() + "\",");
			content.append("\"" + vo.getInvNote() + "\"");
			
			content.append("\n");
			
			totalAdPrice += Double.parseDouble(vo.getAdPrice().replaceAll(",", ""));
			totalInvPfbBonus += Double.parseDouble(vo.getInvPfbBonus().replaceAll(",", ""));
		}
		
		content.append("\n");
		content.append("\n");
		
		content.append("\"總計：" + totalCount + "筆\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\"$ " + df.format(totalAdPrice) + "\",");
		content.append("\"$ " + df2.format(totalInvPfbBonus) + "\",");
		content.append("\"\",");
		content.append("\"\",");
		content.append("\n");
		
		
    	if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
		
		
		return SUCCESS;
	}
	
	private void getMapData(){
		selectTypeMap = new LinkedHashMap<String,String>();
		for(EnumPfbInvalidTrafficType enumPfbInvalidTrafficType:EnumPfbInvalidTrafficType.values()){
			selectTypeMap.put(enumPfbInvalidTrafficType.getType(), enumPfbInvalidTrafficType.getChName());
		}
	}
	
	//取指定日期前一個月
	private String getCloseStartDay(String searchDate) throws Exception {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
        c.setTime(format.parse(searchDate));
        c.add(Calendar.MONTH, -1);
        String firstDay = format.format(c.getTime());

        return firstDay;
	}
	
	public void setPfbxInvalidTrafficService(IPfbxInvalidTrafficService pfbxInvalidTrafficService) {
		this.pfbxInvalidTrafficService = pfbxInvalidTrafficService;
	}
	
	public void setPfbxInvalidTrafficDetailService(IPfbxInvalidTrafficDetailService pfbxInvalidTrafficDetailService) {
		this.pfbxInvalidTrafficDetailService = pfbxInvalidTrafficDetailService;
	}
	
	public void setPfpAdClickService(IPfpAdClickService pfpAdClickService) {
		this.pfpAdClickService = pfpAdClickService;
	}

	public void setAdmAccesslogService(IAdmAccesslogService admAccesslogService) {
		this.admAccesslogService = admAccesslogService;
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

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getSelectType() {
		return selectType;
	}

	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public Map<String, String> getSelectTypeMap() {
		return selectTypeMap;
	}

	public List<PfbxInvalidTrafficVO> getVoList() {
		return voList;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public List<PfbxInvalidTrafficDetailVO> getDetailVoList() {
		return detailVoList;
	}

	public PfbxInvalidTrafficVO getTotalTrafficVO() {
		return totalTrafficVO;
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
