package com.pchome.akbadm.db.vo.report;

public class AdmCountReportVO {

	private String countDate;			//日期
	private String week;				//星期
	private String adClkPrice;			//廣告點擊數費用
	private String clientCount;			//客戶數
	private String pfdCount;			//經銷商家數
	private String pfdBonus;			//PFD獎金
	private String pfbBonus;			//PFB分潤
	private String pfpBonus;			//PFP收入
	
	public String getCountDate() {
		return countDate;
	}
	
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	
	public String getWeek() {
		return week;
	}
	
	public void setWeek(String week) {
		this.week = week;
	}
	
	public String getAdClkPrice() {
		return adClkPrice;
	}
	
	public void setAdClkPrice(String adClkPrice) {
		this.adClkPrice = adClkPrice;
	}
	
	public String getClientCount() {
		return clientCount;
	}
	
	public void setClientCount(String clientCount) {
		this.clientCount = clientCount;
	}
	
	public String getPfdCount() {
		return pfdCount;
	}
	
	public void setPfdCount(String pfdCount) {
		this.pfdCount = pfdCount;
	}
	
	public String getPfdBonus() {
		return pfdBonus;
	}
	
	public void setPfdBonus(String pfdBonus) {
		this.pfdBonus = pfdBonus;
	}
	
	public String getPfbBonus() {
		return pfbBonus;
	}
	
	public void setPfbBonus(String pfbBonus) {
		this.pfbBonus = pfbBonus;
	}
	
	public String getPfpBonus() {
		return pfpBonus;
	}
	
	public void setPfpBonus(String pfpBonus) {
		this.pfpBonus = pfpBonus;
	}
	
}
