package com.pchome.akbadm.db.vo.ad;

public class AdmFreeRecordVO {
	
	private String numberId;		//筆數
	private String customerInfoId;	//帳戶編號
	private String orderId;			//訂單編號
	private String recordDate;		//記錄日期
	
	public String getNumberId() {
		return numberId;
	}
	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
