package com.pchome.akbadm.db.vo.pfbx.report;

public class PfpAdClickVO {
	
	private String pfbxCustomerInfoId;			//pfb_id
	private String tproId;						//商品樣板序號
	private String width;						//廣告尺寸寬度
	private String height;						//廣告尺寸高度
	private String mouseMoveFlag;				//滑鼠移動判斷註記
	private String mouseDownX;					//滑鼠點擊X軸座標
	private String mouseDownY;					//滑鼠點擊Y軸座標
	
	//以下惡意點擊查詢才用到
	private String pfbxPositionId;				//pfb版位
	private String maliceType;					//惡意點擊型態
	private String memId;						//會員編號
	private String uuid;						//uuid
	private String remoteIp;					//ip
	private String referer;						//referer
	private String userAgent;					//userAgent
	private String mouseAreaWidth;				//滑鼠移動區域:寬
	private String mouseAreaHeight;				//滑鼠移動區域:高
	private String recordTime;
	private String recordDate;
	private String adPrice;
	
	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public String getTproId() {
		return tproId;
	}
	public void setTproId(String tproId) {
		this.tproId = tproId;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getMouseMoveFlag() {
		return mouseMoveFlag;
	}
	public void setMouseMoveFlag(String mouseMoveFlag) {
		this.mouseMoveFlag = mouseMoveFlag;
	}
	public String getMouseDownX() {
		return mouseDownX;
	}
	public void setMouseDownX(String mouseDownX) {
		this.mouseDownX = mouseDownX;
	}
	public String getMouseDownY() {
		return mouseDownY;
	}
	public void setMouseDownY(String mouseDownY) {
		this.mouseDownY = mouseDownY;
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
	public String getMemId() {
		return memId;
	}
	public void setMemId(String memId) {
		this.memId = memId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRemoteIp() {
		return remoteIp;
	}
	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getMouseAreaWidth() {
		return mouseAreaWidth;
	}
	public void setMouseAreaWidth(String mouseAreaWidth) {
		this.mouseAreaWidth = mouseAreaWidth;
	}
	public String getMouseAreaHeight() {
		return mouseAreaHeight;
	}
	public void setMouseAreaHeight(String mouseAreaHeight) {
		this.mouseAreaHeight = mouseAreaHeight;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getAdPrice() {
		return adPrice;
	}
	public void setAdPrice(String adPrice) {
		this.adPrice = adPrice;
	}
	
}
