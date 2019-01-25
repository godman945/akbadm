package com.pchome.akbadm.db.vo;

public class AdActionReportVO {

	private String reportDate; //日期
	private String pvSum; //PV總和(到期報表)
	private String clkSum; //Click總和(到期報表)
	private String clkRate; //點選率 = Click總和 / PV總和(到期報表)
	private String invalidClkSum; //無效點擊Click總和(到期報表)
	private String priceSum; //價格總和(到期報表)
	private String clkPriceAvg; //平均點選出價(到期報表)
	private String pvPriceAvg; // 千次曝光費用
	private String customerInfoId; //客戶ID(明細用)
	private String customerInfoName; //客戶姓名(明細用)(到期報表)(未達每日花費上限報表)
	private String adActionSeq; //廣告活動序號(明細用)(到期報表)(未達每日花費上限報表)
	private String adActionName; //廣告活動(明細用)(到期報表)(未達每日花費上限報表)
	private String overPriceSum; //超播金額
	private String pfdCustomerInfoId; //經銷商ID
	private String pfdCustomerInfoTitle; //經銷商名字
	private String dealer; //經銷商
	private String pfdUserId; //業務ID
	private String pfdUserName; //業務名字
	private String sales;		//業務
	private String spendRate;  // 消耗比率=廣告累積花費/(每日預算*實際廣告天數)
	private String adActionControlPrice;	//調控金額

	// 行動裝置
	private int rowspan = 0;
	private String adDevice; // 裝置
	private String adOs; // 作業系統
	private String adGroupSeq; // 廣告分類序號
	private String adGroupName; // 廣告分類序號
	private String adSeq; // 廣告序號
	private String templateProductSeq; // 商品樣板序號

	//到期報表
	private String startDate; //廣告開始日
	private String endDate; //廣告結束日
	private String taxId; //統編
	private String maxPrice; //每日花費上限
	private String arrivalRate; //達成率
	private int count; //筆數
	private String nowMaxPrice; //目前每日花費上限

	//未達每日花費上限報表
	private String unReachPrice; //未達每日花費上限費用
	private String searchDate;		// 查詢日期

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getPvSum() {
		return pvSum;
	}

	public void setPvSum(String pvSum) {
		this.pvSum = pvSum;
	}

	public String getClkSum() {
		return clkSum;
	}

	public void setClkSum(String clkSum) {
		this.clkSum = clkSum;
	}

	public String getClkRate() {
		return clkRate;
	}

	public void setClkRate(String clkRate) {
		this.clkRate = clkRate;
	}

	public String getInvalidClkSum() {
		return invalidClkSum;
	}

	public void setInvalidClkSum(String invalidClkSum) {
		this.invalidClkSum = invalidClkSum;
	}

	public String getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(String priceSum) {
		this.priceSum = priceSum;
	}

	public String getClkPriceAvg() {
		return clkPriceAvg;
	}

	public void setClkPriceAvg(String clkPriceAvg) {
		this.clkPriceAvg = clkPriceAvg;
	}

    public String getPvPriceAvg() {
        return pvPriceAvg;
    }

    public void setPvPriceAvg(String pvPriceAvg) {
        this.pvPriceAvg = pvPriceAvg;
    }

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getCustomerInfoName() {
		return customerInfoName;
	}

	public void setCustomerInfoName(String customerInfoName) {
		this.customerInfoName = customerInfoName;
	}

	public String getAdActionSeq() {
		return adActionSeq;
	}

	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}

	public String getAdActionName() {
		return adActionName;
	}

	public void setAdActionName(String adActionName) {
		this.adActionName = adActionName;
	}

	public String getOverPriceSum() {
		return overPriceSum;
	}

	public void setOverPriceSum(String overPriceSum) {
		this.overPriceSum = overPriceSum;
	}

	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}

	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}

	public String getDealer() {
		return dealer;
	}

	public void setDealer(String dealer) {
		this.dealer = dealer;
	}

	public String getPfdUserId() {
		return pfdUserId;
	}

	public void setPfdUserId(String pfdUserId) {
		this.pfdUserId = pfdUserId;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public String getAdDevice() {
		return adDevice;
	}

	public void setAdDevice(String adDevice) {
		this.adDevice = adDevice;
	}

	public String getAdOs() {
		return adOs;
	}

	public void setAdOs(String adOs) {
		this.adOs = adOs;
	}

	public String getAdGroupSeq() {
		return adGroupSeq;
	}

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}

	public String getAdGroupName() {
		return adGroupName;
	}

	public void setAdGroupName(String adGroupName) {
		this.adGroupName = adGroupName;
	}

	public String getAdSeq() {
		return adSeq;
	}

	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}

	public String getTemplateProductSeq() {
		return templateProductSeq;
	}

	public void setTemplateProductSeq(String templateProductSeq) {
		this.templateProductSeq = templateProductSeq;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getArrivalRate() {
		return arrivalRate;
	}

	public void setArrivalRate(String arrivalRate) {
		this.arrivalRate = arrivalRate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUnReachPrice() {
		return unReachPrice;
	}

	public void setUnReachPrice(String unReachPrice) {
		this.unReachPrice = unReachPrice;
	}

	public String getPfdCustomerInfoTitle() {
		return pfdCustomerInfoTitle;
	}

	public void setPfdCustomerInfoTitle(String pfdCustomerInfoTitle) {
		this.pfdCustomerInfoTitle = pfdCustomerInfoTitle;
	}

	public String getPfdUserName() {
		return pfdUserName;
	}

	public void setPfdUserName(String pfdUserName) {
		this.pfdUserName = pfdUserName;
	}

	public String getSpendRate() {
		return spendRate;
	}

	public void setSpendRate(String spendRate) {
		this.spendRate = spendRate;
	}

	public String getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public String getAdActionControlPrice() {
		return adActionControlPrice;
	}

	public void setAdActionControlPrice(String adActionControlPrice) {
		this.adActionControlPrice = adActionControlPrice;
	}

	public String getNowMaxPrice() {
		return nowMaxPrice;
	}

	public void setNowMaxPrice(String nowMaxPrice) {
		this.nowMaxPrice = nowMaxPrice;
	}

}
