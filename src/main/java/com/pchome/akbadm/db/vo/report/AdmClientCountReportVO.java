package com.pchome.akbadm.db.vo.report;

public class AdmClientCountReportVO {
	
	private String countDate;			//日期
	private String week;				//星期
	private String pfpClientCount;		//直客客戶數
	private String pfdClientCount;		//經銷商客戶數
	private String salesClientCount; 	//業務客戶數
	private String pfpAdClkPrice;		//直客廣告點擊數費用
	private String pfdAdClkPrice;		//經銷商廣告點擊數費用
	private String salesAdClkPrice;		//業務廣告點擊費用 
	private String pfpAdActionMaxPrice;	//直客廣告每日預算
	private String pfdAdActionMaxPrice;	//經銷商廣告每日預算
	private String salesAdActionMaxPrice; //業務廣告每日預算
	private String pfpSpendRate;		//直客預算達成率
	private String pfdSpendRate;		//經銷商預算達成率
	private String salesSpendRate;		//業務預算達成率
	private String pfpAdCount;			//直客廣告數
	private String pfdAdCount;			//經銷商廣告數
	private String salesAdCount;		//業務廣告數
	private String adPv;				//廣告曝光數
	private String adClk;				//廣告點擊數
	private String clkRate;				//廣告點擊率
	private String adClkPriceAvg;		//單次點擊費用
	private String adPvPrice;			//每千次曝光費用
	private String adClkPrice;			//廣告點擊數總費用
	private String lossCost;			//超播金額
	private String adInvalidClk;		//無效點擊數
	private String adInvalidClkPrice;	//無效點擊數總費用
	private String pfpSave;				//pchome營運儲值金
	private String pfpFree;				//pchome營運贈送金
	private String pfpPostpaid;			//pchome營運後付費
	private String pfbSave;				//PFB分潤儲值金
	private String pfbFree;				//PFB分潤贈送金
	private String pfbPostpaid;			//PFB分潤後付費
	private String pfdSave;				//PFD佣金儲值金
	private String pfdFree;				//PFD佣金贈送金
	private String pfdPostpaid;			//PFD佣金後付費
	private String totalSavePrice;		//成功儲值金額
	private String totalSaveCount;		//儲值總筆數
	
	private String totalClientCount; 	//廣告客戶數總計
	private String totalAdClkPriceCount;//廣告互動費用總計
	private String totalAdActionMaxPrice;//廣告每日預算總計
	private String totalSpendRate;		//預算達成率總計
	
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public String getPfpClientCount() {
		return pfpClientCount;
	}
	public void setPfpClientCount(String pfpClientCount) {
		this.pfpClientCount = pfpClientCount;
	}
	public String getPfdClientCount() {
		return pfdClientCount;
	}
	public void setPfdClientCount(String pfdClientCount) {
		this.pfdClientCount = pfdClientCount;
	}
	public String getPfpAdClkPrice() {
		return pfpAdClkPrice;
	}
	public void setPfpAdClkPrice(String pfpAdClkPrice) {
		this.pfpAdClkPrice = pfpAdClkPrice;
	}
	public String getPfdAdClkPrice() {
		return pfdAdClkPrice;
	}
	public void setPfdAdClkPrice(String pfdAdClkPrice) {
		this.pfdAdClkPrice = pfdAdClkPrice;
	}
	public String getPfpAdActionMaxPrice() {
		return pfpAdActionMaxPrice;
	}
	public void setPfpAdActionMaxPrice(String pfpAdActionMaxPrice) {
		this.pfpAdActionMaxPrice = pfpAdActionMaxPrice;
	}
	public String getPfdAdActionMaxPrice() {
		return pfdAdActionMaxPrice;
	}
	public void setPfdAdActionMaxPrice(String pfdAdActionMaxPrice) {
		this.pfdAdActionMaxPrice = pfdAdActionMaxPrice;
	}
	public String getPfpSpendRate() {
		return pfpSpendRate;
	}
	public void setPfpSpendRate(String pfpSpendRate) {
		this.pfpSpendRate = pfpSpendRate;
	}
	public String getPfdSpendRate() {
		return pfdSpendRate;
	}
	public void setPfdSpendRate(String pfdSpendRate) {
		this.pfdSpendRate = pfdSpendRate;
	}
	public String getPfpAdCount() {
		return pfpAdCount;
	}
	public void setPfpAdCount(String pfpAdCount) {
		this.pfpAdCount = pfpAdCount;
	}
	public String getPfdAdCount() {
		return pfdAdCount;
	}
	public void setPfdAdCount(String pfdAdCount) {
		this.pfdAdCount = pfdAdCount;
	}
	public String getAdPv() {
		return adPv;
	}
	public void setAdPv(String adPv) {
		this.adPv = adPv;
	}
	public String getAdClk() {
		return adClk;
	}
	public void setAdClk(String adClk) {
		this.adClk = adClk;
	}
	public String getClkRate() {
		return clkRate;
	}
	public void setClkRate(String clkRate) {
		this.clkRate = clkRate;
	}
	public String getAdClkPriceAvg() {
		return adClkPriceAvg;
	}
	public void setAdClkPriceAvg(String adClkPriceAvg) {
		this.adClkPriceAvg = adClkPriceAvg;
	}
	public String getAdPvPrice() {
		return adPvPrice;
	}
	public void setAdPvPrice(String adPvPrice) {
		this.adPvPrice = adPvPrice;
	}
	public String getAdClkPrice() {
		return adClkPrice;
	}
	public void setAdClkPrice(String adClkPrice) {
		this.adClkPrice = adClkPrice;
	}
	public String getLossCost() {
		return lossCost;
	}
	public void setLossCost(String lossCost) {
		this.lossCost = lossCost;
	}
	public String getAdInvalidClk() {
		return adInvalidClk;
	}
	public void setAdInvalidClk(String adInvalidClk) {
		this.adInvalidClk = adInvalidClk;
	}
	public String getAdInvalidClkPrice() {
		return adInvalidClkPrice;
	}
	public void setAdInvalidClkPrice(String adInvalidClkPrice) {
		this.adInvalidClkPrice = adInvalidClkPrice;
	}
	public String getPfpSave() {
		return pfpSave;
	}
	public void setPfpSave(String pfpSave) {
		this.pfpSave = pfpSave;
	}
	public String getPfpFree() {
		return pfpFree;
	}
	public void setPfpFree(String pfpFree) {
		this.pfpFree = pfpFree;
	}
	public String getPfpPostpaid() {
		return pfpPostpaid;
	}
	public void setPfpPostpaid(String pfpPostpaid) {
		this.pfpPostpaid = pfpPostpaid;
	}
	public String getPfbSave() {
		return pfbSave;
	}
	public void setPfbSave(String pfbSave) {
		this.pfbSave = pfbSave;
	}
	public String getPfbFree() {
		return pfbFree;
	}
	public void setPfbFree(String pfbFree) {
		this.pfbFree = pfbFree;
	}
	public String getPfbPostpaid() {
		return pfbPostpaid;
	}
	public void setPfbPostpaid(String pfbPostpaid) {
		this.pfbPostpaid = pfbPostpaid;
	}
	public String getPfdSave() {
		return pfdSave;
	}
	public void setPfdSave(String pfdSave) {
		this.pfdSave = pfdSave;
	}
	public String getPfdFree() {
		return pfdFree;
	}
	public void setPfdFree(String pfdFree) {
		this.pfdFree = pfdFree;
	}
	public String getPfdPostpaid() {
		return pfdPostpaid;
	}
	public void setPfdPostpaid(String pfdPostpaid) {
		this.pfdPostpaid = pfdPostpaid;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getTotalSavePrice() {
		return totalSavePrice;
	}
	public void setTotalSavePrice(String totalSavePrice) {
		this.totalSavePrice = totalSavePrice;
	}
	public String getTotalSaveCount() {
		return totalSaveCount;
	}
	public void setTotalSaveCount(String totalSaveCount) {
		this.totalSaveCount = totalSaveCount;
	}
	public String getSalesClientCount() {
		return salesClientCount;
	}
	public void setSalesClientCount(String salesClientCount) {
		this.salesClientCount = salesClientCount;
	}
	public String getSalesAdClkPrice() {
		return salesAdClkPrice;
	}
	public void setSalesAdClkPrice(String salesAdClkPrice) {
		this.salesAdClkPrice = salesAdClkPrice;
	}
	public String getSalesAdActionMaxPrice() {
		return salesAdActionMaxPrice;
	}
	public void setSalesAdActionMaxPrice(String salesAdActionMaxPrice) {
		this.salesAdActionMaxPrice = salesAdActionMaxPrice;
	}
	public String getSalesAdCount() {
		return salesAdCount;
	}
	public void setSalesAdCount(String salesAdCount) {
		this.salesAdCount = salesAdCount;
	}
	public String getSalesSpendRate() {
		return salesSpendRate;
	}
	public void setSalesSpendRate(String salesSpendRate) {
		this.salesSpendRate = salesSpendRate;
	}
	public String getTotalClientCount() {
		return totalClientCount;
	}
	public void setTotalClientCount(String totalClientCount) {
		this.totalClientCount = totalClientCount;
	}
	public String getTotalAdClkPriceCount() {
		return totalAdClkPriceCount;
	}
	public void setTotalAdClkPriceCount(String totalAdClkPriceCount) {
		this.totalAdClkPriceCount = totalAdClkPriceCount;
	}
	public String getTotalAdActionMaxPrice() {
		return totalAdActionMaxPrice;
	}
	public void setTotalAdActionMaxPrice(String totalAdActionMaxPrice) {
		this.totalAdActionMaxPrice = totalAdActionMaxPrice;
	}
	public String getTotalSpendRate() {
		return totalSpendRate;
	}
	public void setTotalSpendRate(String totalSpendRate) {
		this.totalSpendRate = totalSpendRate;
	}
	
}
