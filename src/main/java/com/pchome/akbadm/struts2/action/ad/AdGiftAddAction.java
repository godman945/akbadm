package com.pchome.akbadm.struts2.action.ad;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.pchome.akbadm.db.pojo.AdmFreeAction;
import com.pchome.akbadm.db.pojo.AdmFreeGift;
import com.pchome.akbadm.db.pojo.AdmFreeRecord;
import com.pchome.akbadm.db.service.free.IAdmFreeActionService;
import com.pchome.akbadm.db.service.free.IAdmFreeGiftService;
import com.pchome.akbadm.db.service.sequence.ISequenceService;
import com.pchome.akbadm.db.vo.ad.AdmFreeGiftVO;
import com.pchome.akbadm.db.vo.ad.AdmFreeRecordVO;
import com.pchome.akbadm.struts2.BaseCookieAction;
import com.pchome.enumerate.freeAction.EnumGiftStyle;
import com.pchome.enumerate.freeAction.EnumPayment;
import com.pchome.rmi.sequence.EnumSequenceTableName;

public class AdGiftAddAction extends BaseCookieAction {

	private static final long serialVersionUID = 1L;
	
	SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	NumberFormat intFormat = new DecimalFormat("###,###,###,###");
	
	private ISequenceService sequenceService;
	private IAdmFreeActionService admFreeActionService;
	private IAdmFreeGiftService admFreeGiftService;
	
	private EnumGiftStyle[] searchGiftStyle;
	private EnumPayment[] searchPayment;
	
	private String payment;					//禮金動作
	private String giftStyle;				//序號輸入頁面
	private String actionId;				//活動編號
	private String actionName;				//活動名稱
	private String actionStartDate;			//活動開始日期
	private String actionEndDate;			//活動結束日期
	private String inviledDate;				//禮金失效日期
	private float giftCondition;			//贈送條件
	private float giftMoney;				//贈送金額
	private String note;					//說明
	private String giftSnoHead;				//禮金序號開頭
	private int snoCount = 0;				//新增序號數量
	private String shared;					//是否共用序號
	
	private String updateFlag;				//修改註記
	
	private List<AdmFreeGiftVO> giftList;	//禮金序號明細
	private List<AdmFreeRecordVO> recordList;	//開通記錄明細
	private String totalFreeUseMoney = "0";		//總儲值金額
	
	private String downloadFlag = "";//download report 旗標
	private InputStream downloadFileStream;//下載報表的 input stream
	private String downloadFileName;//下載顯示名
	
	/**
	 * 新增禮金活動頁面
	 */
	public String addView() throws Exception {
		giftStyle = "";
		payment = "";
		searchGiftStyle = EnumGiftStyle.values();
		searchPayment = EnumPayment.values();
		snoCount = 1;
		
		return SUCCESS;
	}
	
	/**
	 * 新增禮金活動
	 */
	public String addGiftAction() throws Exception {
		
		log.info("---------------addGiftAction--------------");
		
		//取得活動編號
		actionId = sequenceService.getSerialNumber(EnumSequenceTableName.ADM_FREE_ACTION);
		
		//新增活動
		AdmFreeAction admFreeAction = new AdmFreeAction();
		
		admFreeAction.setActionId(actionId);
		admFreeAction.setActionName(actionName);
		admFreeAction.setPayment(payment);
		admFreeAction.setGiftStyle(giftStyle);
		admFreeAction.setGiftCondition(giftCondition);
		admFreeAction.setGiftMoney(giftMoney);
		admFreeAction.setActionStartDate(dateFormate2.parse(actionStartDate + " 00:00:00"));
		admFreeAction.setActionEndDate(dateFormate2.parse(actionEndDate + " 23:59:59"));
		admFreeAction.setInviledDate(dateFormate.parse(inviledDate));
		admFreeAction.setRetrievedFlag("n");
		admFreeAction.setNote(note);
		if(StringUtils.equals(shared, "Y")){
			admFreeAction.setShared(shared);
		} else {
			admFreeAction.setShared(null);
		}
		admFreeAction.setUpdateDate(new Date());
		admFreeAction.setCreateDate(new Date());
		admFreeActionService.save(admFreeAction);
		
		//新增禮金序號
		addFreeGift(admFreeAction);
		
		return SUCCESS;
	}
	
	/**
	 * 修改禮金活動頁面
	 */
	public String updateView(){
		
		searchGiftStyle = EnumGiftStyle.values();
		searchPayment = EnumPayment.values();
		
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		//檢查該活動禮金序號是否有被使用，若有被使用則無法修改禮金活動
		updateFlag = "y";
		List<AdmFreeGift> giftList = admFreeGiftService.findAdmFreeGiftToBeUsed(actionId);
		
		if(giftList.size() > 0){
			updateFlag = "n";
		}
		
		if(admFreeAction != null){
			actionName = admFreeAction.getActionName();
			payment = admFreeAction.getPayment();
			giftStyle = admFreeAction.getGiftStyle();
			if(admFreeAction.getActionStartDate() != null){
				actionStartDate = dateFormate.format(admFreeAction.getActionStartDate());	
			}
			if(admFreeAction.getActionEndDate() != null){
				actionEndDate = dateFormate.format(admFreeAction.getActionEndDate());
			}
			if(admFreeAction.getInviledDate() != null){
				inviledDate = dateFormate.format(admFreeAction.getInviledDate());		
			}
			giftCondition = admFreeAction.getGiftCondition();
			giftMoney = admFreeAction.getGiftMoney();
			note = admFreeAction.getNote();	
			shared = "N";
			if(admFreeAction.getShared() != null){
				shared = admFreeAction.getShared();
			}
			
			if(StringUtils.equals(shared, "Y")){
				Set<AdmFreeRecord> freeRecords = admFreeAction.getAdmFreeRecords();
				if(freeRecords.size() > 0){
					updateFlag = "n";
				}
			}
			
		}
		
		return SUCCESS;
	}
	
	/**
	 * 修改禮金活動
	 */
	public String updateGiftAction() throws Exception {
		
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		if(admFreeAction != null){
			admFreeAction.setActionName(actionName);
			admFreeAction.setPayment(payment);
			admFreeAction.setGiftStyle(giftStyle);
			admFreeAction.setGiftCondition(giftCondition);
			admFreeAction.setGiftMoney(giftMoney);
			admFreeAction.setActionStartDate(dateFormate2.parse(actionStartDate + " 00:00:00"));
			admFreeAction.setActionEndDate(dateFormate2.parse(actionEndDate + " 23:59:59"));
			admFreeAction.setInviledDate(dateFormate.parse(inviledDate));
			admFreeAction.setNote(note);
			
			admFreeAction.setUpdateDate(new Date());
			
			admFreeActionService.saveOrUpdate(admFreeAction);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 禮金序號明細頁面
	 * @throws Exception 
	 */
	public String giftDetalView() throws Exception{
		
		giftList = new ArrayList<AdmFreeGiftVO>();
		List<AdmFreeGift> dataList = admFreeGiftService.findAdmFreeGift(actionId);
		
		if(!dataList.isEmpty()){
			int i = 1;
			for(AdmFreeGift admFreeGift:dataList){
				
				AdmFreeGiftVO vo = new AdmFreeGiftVO();
				
				vo.setNumberId(String.valueOf(i));
				vo.setGiftSno(admFreeGift.getGiftSno());
				vo.setCustomerInfoId(admFreeGift.getCustomerInfoId());
				
				if(admFreeGift.getOpenDate() != null){
					vo.setOpenDate(dateFormate.format(admFreeGift.getOpenDate()));
				}
				
				vo.setOrderId(admFreeGift.getOrderId());
				
				String giftSnoStatus = "未啟用";
				if(StringUtils.equals(admFreeGift.getGiftSnoStatus(), "Y")){
					giftSnoStatus = "已啟用";
				}
				vo.setGiftSnoStatus(giftSnoStatus);
				
				giftList.add(vo);
				i++;
			}
		}
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadReportData();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 下載禮金序號明細
	 */
	private void makeDownloadReportData() throws Exception {
		
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告禮金序號明細報表_" + dformat.format(new Date()) + ".csv";
		
    	String[] tableHeadArray = {"禮金筆數","禮金序號","帳戶編號","序號開啟日期","訂單編號","序號狀態"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,廣告禮金序號明細");
		content.append("\n\n");
    	
		content.append("廣告禮金活動編號:," + actionId);
		content.append("\n\n");
		
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		content.append("廣告禮金活動名稱:," + admFreeAction.getActionName());
		content.append("\n\n");
		
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<giftList.size(); i++) {
			AdmFreeGiftVO vo = new AdmFreeGiftVO();
			vo = giftList.get(i);
			content.append("\"" + vo.getNumberId() + "\",");
			content.append("\"" + vo.getGiftSno() + "\",");
			String customerInfoId = " ";
			if(StringUtils.isNotEmpty(vo.getCustomerInfoId())){
				customerInfoId = vo.getCustomerInfoId();
			}
			content.append("\"" + customerInfoId + "\",");
			String openDate = " ";
			if(StringUtils.isNotEmpty(vo.getOpenDate())){
				openDate = vo.getOpenDate();
			}
			content.append("\"" + openDate + "\",");
			String orderId = " ";
			if(StringUtils.isNotEmpty(vo.getOrderId())){
				orderId = vo.getOrderId();
			}
			content.append("\"" + orderId + "\",");
			content.append("\"" + vo.getGiftSnoStatus() + "\",");
			content.append("\n");	
		}
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}
	
	/**
	 * 禮金序號帳戶開通記錄頁面
	 * @throws Exception 
	 */
	public String giftRecord() throws Exception{
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		List<AdmFreeRecord> recordData = new ArrayList<AdmFreeRecord>(admFreeAction.getAdmFreeRecords());
		
		recordList = new ArrayList<AdmFreeRecordVO>();
		if(!recordData.isEmpty()){
			int i = 1;
			totalFreeUseMoney = intFormat.format(admFreeAction.getGiftMoney()*recordData.size());
			for(AdmFreeRecord admFreeRecord:recordData){
				AdmFreeRecordVO vo = new AdmFreeRecordVO();
				vo.setNumberId(String.valueOf(i));
				vo.setCustomerInfoId(admFreeRecord.getCustomerInfoId());
				vo.setOrderId(admFreeRecord.getOrderId());
				vo.setRecordDate(dateFormate.format(admFreeRecord.getRecordDate()));
				recordList.add(vo);
				i++;
			}
		}
		
		if(downloadFlag.trim().equals("yes")){
			log.info("makeDownloadReportData");
			makeDownloadRecordData();
		}
		
		return SUCCESS;
	}
	
	/**
	 * 下載帳戶開通記錄明細
	 */
	private void makeDownloadRecordData() throws Exception {
		
		SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss");
    	String filename="廣告禮金帳戶開通明細報表_" + dformat.format(new Date()) + ".csv";
		
    	String[] tableHeadArray = {"開通筆數","帳戶編號","訂單編號","序號開啟日期"};
    	
    	StringBuffer content=new StringBuffer();
    	
    	content.append("報表名稱:,廣告禮金帳戶開通明細");
		content.append("\n\n");
    	
		content.append("廣告禮金活動編號:," + actionId);
		content.append("\n\n");
		
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		content.append("廣告禮金活動名稱:," + admFreeAction.getActionName());
		content.append("\n\n");
		
		content.append("實際領用總金額:,\"NT$ " + totalFreeUseMoney + "\"");
		content.append("\n\n");
		
		for(String s:tableHeadArray){
			content.append("\"" + s + "\"");
			content.append(",");
		}
		content.append("\n");
		
		for (int i=0; i<recordList.size(); i++) {
			AdmFreeRecordVO vo = new AdmFreeRecordVO();
			vo = recordList.get(i);
			content.append("\"" + vo.getNumberId() + "\",");
			String customerInfoId = " ";
			if(StringUtils.isNotEmpty(vo.getCustomerInfoId())){
				customerInfoId = vo.getCustomerInfoId();
			}
			content.append("\"" + customerInfoId + "\",");
			
			String orderId = " ";
			if(StringUtils.isNotEmpty(vo.getOrderId())){
				orderId = vo.getOrderId();
			}
			content.append("\"" + orderId + "\",");
			
			String recordDate = " ";
			if(StringUtils.isNotEmpty(vo.getRecordDate())){
				recordDate = vo.getRecordDate();
			}
			content.append("\"" + recordDate + "\",");
			content.append("\n");	
		}
		
		if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			downloadFileName = new String(filename.getBytes("UTF-8"), "ISO8859-1");
		} else {
			downloadFileName = URLEncoder.encode(filename, "UTF-8");			
		}

		downloadFileStream = new ByteArrayInputStream(content.toString().getBytes("big5"));
	}
	
	/**
	 * 禮金序號明細頁面新增序號
	 */
	public String giftDetalViewAdd(){
		
		AdmFreeAction admFreeAction = admFreeActionService.findFreeAction(actionId);
		
		addFreeGift(admFreeAction);
		
		return SUCCESS;
	}
	
	/**
	 * 新增禮金序號
	 */
	private void addFreeGift(AdmFreeAction admFreeAction){
		
		List<AdmFreeGift> giftList = new ArrayList<AdmFreeGift>();
		
		for(int i=1;i<=snoCount;i++){
			
			AdmFreeGift data = new AdmFreeGift();
			
			String giftSno = giftSnoHead + getRandomPassword();
			
			data.setAdmFreeAction(admFreeAction);
			data.setGiftSno(giftSno);
			data.setGiftSnoStatus("N");
			data.setUpdateDate(new Date());
			data.setCreateDate(new Date());
			
			giftList.add(data);
		}
		
		admFreeGiftService.saveOrUpdateAll(giftList);
	}
	
	/**
	 * 取得禮金序號後八碼序號(數字加大寫英文隨機八碼)
	 */
	private String getRandomPassword() {
		int z;
		char string;
	    StringBuilder sb = new StringBuilder();

	    for (int i = 0; i < 8; i++) {
	      z = (int) ((Math.random() * 3) % 2);
	      if (z == 1) { // 放數字
	    	  string = (char) ((Math.random() * 10) + 48);
	      } else { // 放大寫英文
	    	  string = (char) (((Math.random() * 26) + 65));
	      }
	      
	      //排除0、1、I、O
	      String word = String.valueOf(string);
	      if(StringUtils.equals("0", word) || StringUtils.equals("1", word) || 
	    		  StringUtils.equals("I", word) || StringUtils.equals("O", word)){
	    	  i--;
	    	  continue;
	      }
	      
	      sb.append(string);
	    }
	    return sb.toString();
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EnumGiftStyle[] getSearchGiftStyle() {
		return searchGiftStyle;
	}

	public EnumPayment[] getSearchPayment() {
		return searchPayment;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getGiftStyle() {
		return giftStyle;
	}

	public void setGiftStyle(String giftStyle) {
		this.giftStyle = giftStyle;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionStartDate() {
		return actionStartDate;
	}

	public void setActionStartDate(String actionStartDate) {
		this.actionStartDate = actionStartDate;
	}

	public String getActionEndDate() {
		return actionEndDate;
	}

	public void setActionEndDate(String actionEndDate) {
		this.actionEndDate = actionEndDate;
	}

	public String getInviledDate() {
		return inviledDate;
	}

	public void setInviledDate(String inviledDate) {
		this.inviledDate = inviledDate;
	}

	public float getGiftCondition() {
		return giftCondition;
	}

	public void setGiftCondition(float giftCondition) {
		this.giftCondition = giftCondition;
	}

	public float getGiftMoney() {
		return giftMoney;
	}

	public void setGiftMoney(float giftMoney) {
		this.giftMoney = giftMoney;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getSnoCount() {
		return snoCount;
	}

	public void setSnoCount(int snoCount) {
		this.snoCount = snoCount;
	}

	public void setGiftSnoHead(String giftSnoHead) {
		this.giftSnoHead = giftSnoHead;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public List<AdmFreeGiftVO> getGiftList() {
		return giftList;
	}

	public void setAdmFreeActionService(IAdmFreeActionService admFreeActionService) {
		this.admFreeActionService = admFreeActionService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public void setAdmFreeGiftService(IAdmFreeGiftService admFreeGiftService) {
		this.admFreeGiftService = admFreeGiftService;
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

	public String getShared() {
		return shared;
	}

	public void setShared(String shared) {
		this.shared = shared;
	}

	public List<AdmFreeRecordVO> getRecordList() {
		return recordList;
	}

	public String getTotalFreeUseMoney() {
		return totalFreeUseMoney;
	}
		
}
