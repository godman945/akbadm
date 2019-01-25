package com.pchome.akbadm.db.vo;

public class AdQueryConditionVO {

	private String[] status; //廣告狀態
	private String style; //廣告型態
	private String sendVerifyStartTime; //廣告送審起始時間
	private String sendVerifyEndTime; //廣告送審結束時間
	private String pfdCustomerInfoId;

	public String[] getStatus() {
		return status;
	}

	public void setStatus(String[] status) {
		this.status = status;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getSendVerifyStartTime() {
		return sendVerifyStartTime;
	}

	public void setSendVerifyStartTime(String sendVerifyStartTime) {
		this.sendVerifyStartTime = sendVerifyStartTime;
	}

	public String getSendVerifyEndTime() {
		return sendVerifyEndTime;
	}

	public void setSendVerifyEndTime(String sendVerifyEndTime) {
		this.sendVerifyEndTime = sendVerifyEndTime;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	
	
	
}
