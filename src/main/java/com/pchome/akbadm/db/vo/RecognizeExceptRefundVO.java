package com.pchome.akbadm.db.vo;
 
public class RecognizeExceptRefundVO {
	
	private String recognizeOrderId;
    private String costDate;
    private float costPrice;
    private float tax;
    private String customerInfoId;
    
    
	public String getRecognizeOrderId() {
		return recognizeOrderId;
	}
	public void setRecognizeOrderId(String recognizeOrderId) {
		this.recognizeOrderId = recognizeOrderId;
	}
	public String getCostDate() {
		return costDate;
	}
	public void setCostDate(String costDate) {
		this.costDate = costDate;
	}
	public float getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(float costPrice) {
		this.costPrice = costPrice;
	}
	public float getTax() {
		return tax;
	}
	public void setTax(float tax) {
		this.tax = tax;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
    
}
