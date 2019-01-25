package com.pchome.akbadm.db.vo.pfbx.invalidclick;

public class PfbxInvalidClickVO {

	private String recordDate;				//記錄日期
	private String recordTime;				//記錄時間
	private String pfbxCustomerInfoId;		//pfb帳戶編號
	private String pfbxPositionId;			//pfb版位編號
	private String selectStyle;				//選擇條件
	private String count;					//數量
	private String price;					//花費
	private String memId;					//會員帳號
	private String uuid;
	private String remoteIp;
	private String referer;
	private String mouseMoveFlag;
	private String userAgent;
	
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
	public String getSelectStyle() {
		return selectStyle;
	}
	public void setSelectStyle(String selectStyle) {
		this.selectStyle = selectStyle;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
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
	public String getMouseMoveFlag() {
		return mouseMoveFlag;
	}
	public void setMouseMoveFlag(String mouseMoveFlag) {
		this.mouseMoveFlag = mouseMoveFlag;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}
