package com.pchome.akbadm.db.vo.ad;

public class AdmFreeActionVO {

	private String actionId;		//優惠活動編號
	private String actionName;		//優惠活動名稱
	private String payment;			//需要付款
	private String giftCondition;	//贈送條件
	private String giftMoney;		//贈送金額
	private String actionStartDate;	//活動開始日期
	private String actionEndDate;	//活動結束日期
	private String inviledDate;		//表定的失效日期
	private String retrievedFlag;	//是否已回收
	private String note;			//備註說明
	private String giftStyle;		//儲值頁面位置
	private String totalGiftMoney;	//禮金發放總金額
	private String totalRecordMoney;//實際領用總金額
	private String shared;			//是否共用序號
	private String sharedNote;		//是否共用序號註記
	
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
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getGiftCondition() {
		return giftCondition;
	}
	public void setGiftCondition(String giftCondition) {
		this.giftCondition = giftCondition;
	}
	public String getGiftMoney() {
		return giftMoney;
	}
	public void setGiftMoney(String giftMoney) {
		this.giftMoney = giftMoney;
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
	public String getRetrievedFlag() {
		return retrievedFlag;
	}
	public void setRetrievedFlag(String retrievedFlag) {
		this.retrievedFlag = retrievedFlag;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getGiftStyle() {
		return giftStyle;
	}
	public void setGiftStyle(String giftStyle) {
		this.giftStyle = giftStyle;
	}
	public String getTotalGiftMoney() {
		return totalGiftMoney;
	}
	public void setTotalGiftMoney(String totalGiftMoney) {
		this.totalGiftMoney = totalGiftMoney;
	}
	public String getShared() {
		return shared;
	}
	public void setShared(String shared) {
		this.shared = shared;
	}
	public String getSharedNote() {
		return sharedNote;
	}
	public void setSharedNote(String sharedNote) {
		this.sharedNote = sharedNote;
	}
	public String getTotalRecordMoney() {
		return totalRecordMoney;
	}
	public void setTotalRecordMoney(String totalRecordMoney) {
		this.totalRecordMoney = totalRecordMoney;
	}
	
}
