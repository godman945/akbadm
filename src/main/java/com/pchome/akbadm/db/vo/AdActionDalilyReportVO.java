package com.pchome.akbadm.db.vo;
 
public class AdActionDalilyReportVO {
    // 客戶id
    private String customerInfoId;
    // 訂單編號
    private String recognizeOrderId;
    // 帳戶名稱
    private String customerInfoTitle;
    // 訂單類型
    private String orderType;
    // 帳戶建立日期
    private String createDate;
    // 儲值金額
    private String totalAddMoney;
    // 攤提日期
    private String costDate;
    //花費
    private String costPrice;
    // 儲值日
    private String saveDate;
    // 儲值金額
    private String orderPrice;
    // 花費日與建立帳戶天數間格
    private String lagTime;
    // 帳戶餘額
    private String remain;
    
    //相減後剩餘金額
    private String minusMoney;
    
    
    public String getCustomerInfoId() {
	return customerInfoId;
    }

    public void setCustomerInfoId(String customerInfoId) {
	this.customerInfoId = customerInfoId;
    }

    public String getRecognizeOrderId() {
	return recognizeOrderId;
    }

    public void setRecognizeOrderId(String recognizeOrderId) {
	this.recognizeOrderId = recognizeOrderId;
    }

    public String getCustomerInfoTitle() {
	return customerInfoTitle;
    }

    public void setCustomerInfoTitle(String customerInfoTitle) {
	this.customerInfoTitle = customerInfoTitle;
    }

    public String getOrderType() {
	return orderType;
    }

    public void setOrderType(String orderType) {
	this.orderType = orderType;
    }

    public String getCreateDate() {
	return createDate;
    }

    public void setCreateDate(String createDate) {
	this.createDate = createDate;
    }

    public String getTotalAddMoney() {
	return totalAddMoney;
    }

    public void setTotalAddMoney(String totalAddMoney) {
	this.totalAddMoney = totalAddMoney;
    }

    public String getCostDate() {
	return costDate;
    }

    public void setCostDate(String costDate) {
	this.costDate = costDate;
    }

    public String getSaveDate() {
	return saveDate;
    }

    public void setSaveDate(String saveDate) {
	this.saveDate = saveDate;
    }

    public String getOrderPrice() {
	return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
	this.orderPrice = orderPrice;
    }

    public String getLagTime() {
	return lagTime;
    }

    public void setLagTime(String lagTime) {
	this.lagTime = lagTime;
    }

    public String getRemain() {
	return remain;
    }

    public void setRemain(String remain) {
	this.remain = remain;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getMinusMoney() {
        return minusMoney;
    }

    public void setMinusMoney(String minusMoney) {
        this.minusMoney = minusMoney;
    }

}
