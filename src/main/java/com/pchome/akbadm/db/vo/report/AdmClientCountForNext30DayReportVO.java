package com.pchome.akbadm.db.vo.report;

public class AdmClientCountForNext30DayReportVO {

	private String countDate;			//日期
	private String week;				//星期
	private String pfpCount;			//廣告客戶數
	private String pfpAdActionCount;	//廣告活動數
	private String pfpAdGroupCount;		//廣告分類數
	private String pfpAdCount;			//廣告明細數
	private String pfpAdActionMaxPrice;	//廣告每日預算
	
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
	public String getPfpCount() {
		return pfpCount;
	}
	public void setPfpCount(String pfpCount) {
		this.pfpCount = pfpCount;
	}
	public String getPfpAdActionCount() {
		return pfpAdActionCount;
	}
	public void setPfpAdActionCount(String pfpAdActionCount) {
		this.pfpAdActionCount = pfpAdActionCount;
	}
	public String getPfpAdGroupCount() {
		return pfpAdGroupCount;
	}
	public void setPfpAdGroupCount(String pfpAdGroupCount) {
		this.pfpAdGroupCount = pfpAdGroupCount;
	}
	public String getPfpAdCount() {
		return pfpAdCount;
	}
	public void setPfpAdCount(String pfpAdCount) {
		this.pfpAdCount = pfpAdCount;
	}
	public String getPfpAdActionMaxPrice() {
		return pfpAdActionMaxPrice;
	}
	public void setPfpAdActionMaxPrice(String pfpAdActionMaxPrice) {
		this.pfpAdActionMaxPrice = pfpAdActionMaxPrice;
	}
	
}
