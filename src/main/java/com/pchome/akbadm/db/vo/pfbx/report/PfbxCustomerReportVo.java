package com.pchome.akbadm.db.vo.pfbx.report;

public class PfbxCustomerReportVo {

	private String customerInfoId;		//帳戶編號
	private String websiteDisplayUrl;	//廣告網址
	private String websiteChineseName;  //網站中文名稱
	
	private String adPvClkCustomer;		//廣告客戶
	
	private String adPvclkDate;			//廣告曝光點擊數日期
	private String adPvclkUnit;			//廣告曝光點擊數單位
	private String adPv;				//曝光數
	private String adClk;				//點擊數
	private String adInvalidClk;		//無效點擊次數
	private String adPvSum;				//曝光數總和
	private String adClkSum;			//點擊數總和
	private String adInvalidClkSum;		//無效點擊次數總和
	private String adPvPrice;			//曝光數費用
	private String adClkPrice;			//點擊數費用
	private String adInvalidClkPrice;	//無效點擊費用
	private String adPvPriceSum;		//曝光數費用總和
	private String adClkPriceSum;		//點擊費用總和
	private String adInvalidClkPriceSum;//無效點擊費用總和
	private String adClkRate;			//點擊率
	private String adClkAvgPrice;		//平均點擊費用
	private String adPvRate;			//千次曝光數
	
	private String totalChargeSum;		//PChome管理費
	private String totalBonusSum;		//PFB廣告分潤


	public String getAdPvclkDate() {
		return adPvclkDate;
	}

	public void setAdPvclkDate(String adPvclkDate) {
		this.adPvclkDate = adPvclkDate;
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

	public String getAdInvalidClk() {
		return adInvalidClk;
	}

	public void setAdInvalidClk(String adInvalidClk) {
		this.adInvalidClk = adInvalidClk;
	}

	public String getAdPvSum() {
		return adPvSum;
	}

	public void setAdPvSum(String adPvSum) {
		this.adPvSum = adPvSum;
	}

	public String getAdClkSum() {
		return adClkSum;
	}

	public void setAdClkSum(String adClkSum) {
		this.adClkSum = adClkSum;
	}

	public String getAdInvalidClkSum() {
		return adInvalidClkSum;
	}

	public void setAdInvalidClkSum(String adInvalidClkSum) {
		this.adInvalidClkSum = adInvalidClkSum;
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

	public String getAdInvalidClkPrice() {
		return adInvalidClkPrice;
	}

	public void setAdInvalidClkPrice(String adInvalidClkPrice) {
		this.adInvalidClkPrice = adInvalidClkPrice;
	}

	public String getAdPvPriceSum() {
		return adPvPriceSum;
	}

	public void setAdPvPriceSum(String adPvPriceSum) {
		this.adPvPriceSum = adPvPriceSum;
	}

	public String getAdClkPriceSum() {
		return adClkPriceSum;
	}

	public void setAdClkPriceSum(String adClkPriceSum) {
		this.adClkPriceSum = adClkPriceSum;
	}

	public String getAdInvalidClkPriceSum() {
		return adInvalidClkPriceSum;
	}

	public void setAdInvalidClkPriceSum(String adInvalidClkPriceSum) {
		this.adInvalidClkPriceSum = adInvalidClkPriceSum;
	}

	public String getAdClkRate() {
		return adClkRate;
	}

	public void setAdClkRate(String adClkRate) {
		this.adClkRate = adClkRate;
	}

	public String getAdClkAvgPrice() {
		return adClkAvgPrice;
	}

	public void setAdClkAvgPrice(String adClkAvgPrice) {
		this.adClkAvgPrice = adClkAvgPrice;
	}

	public String getCustomerInfoId() {
		return customerInfoId;
	}

	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}

	public String getAdPvclkUnit() {
		return adPvclkUnit;
	}

	public void setAdPvclkUnit(String adPvclkUnit) {
		this.adPvclkUnit = adPvclkUnit;
	}

	public String getWebsiteChineseName() {
		return websiteChineseName;
	}

	public void setWebsiteChineseName(String websiteChineseName) {
		this.websiteChineseName = websiteChineseName;
	}

	public String getWebsiteDisplayUrl() {
		return websiteDisplayUrl;
	}

	public void setWebsiteDisplayUrl(String websiteDisplayUrl) {
		this.websiteDisplayUrl = websiteDisplayUrl;
	}

	public String getAdPvRate() {
		return adPvRate;
	}

	public void setAdPvRate(String adPvRate) {
		this.adPvRate = adPvRate;
	}

	public String getAdPvClkCustomer() {
		return adPvClkCustomer;
	}

	public void setAdPvClkCustomer(String adPvClkCustomer) {
		this.adPvClkCustomer = adPvClkCustomer;
	}

	public String getTotalChargeSum() {
		return totalChargeSum;
	}

	public void setTotalChargeSum(String totalChargeSum) {
		this.totalChargeSum = totalChargeSum;
	}

	public String getTotalBonusSum() {
		return totalBonusSum;
	}

	public void setTotalBonusSum(String totalBonusSum) {
		this.totalBonusSum = totalBonusSum;
	}
	
}
