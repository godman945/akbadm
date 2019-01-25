package com.pchome.akbadm.db.vo.ad;

public class AdmFreeGiftVO {

	private String numberId;		//筆數
	private String giftSno;			//禮金序號
	private String customerInfoId;	//帳戶編號
	private String openDate;		//序號開啟日期
	private String orderId;			//訂單編號
	private String giftSnoStatus;	//序號啟用狀態
	
	public String getNumberId() {
		return numberId;
	}
	public void setNumberId(String numberId) {
		this.numberId = numberId;
	}
	public String getGiftSno() {
		return giftSno;
	}
	public void setGiftSno(String giftSno) {
		this.giftSno = giftSno;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGiftSnoStatus() {
		return giftSnoStatus;
	}
	public void setGiftSnoStatus(String giftSnoStatus) {
		this.giftSnoStatus = giftSnoStatus;
	}
}
