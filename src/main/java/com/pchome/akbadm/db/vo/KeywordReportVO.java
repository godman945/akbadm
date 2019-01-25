package com.pchome.akbadm.db.vo;

public class KeywordReportVO {

	private String keywordSeq; //關鍵字序號
	private String keyword; //關鍵字
	private String customerId; //客戶帳號
	private String adAction; //廣告活動
	private String adGroup; //廣告群組
	
	//廣泛比對
	private String kwOpen;	//開啟狀態
	private String kwPvSum; //PV總和
	private String kwClkSum; //Click總和
	private String kwPriceSum; //價格總和
	private String clkPriceAvg; //平均點選出價
	private String kwClkRate; //點選率 = Click總和 / PV總和
	
	//詞組比對
	private String kwPhrOpen;	//開啟狀態
	private String kwPhrPvSum; //PV總和
	private String kwPhrClkSum; //Click總和
	private String kwPhrPriceSum; //價格總和
	private String phrClkPriceAvg; //平均點選出價
	private String kwPhrClkRate; //點選率 = Click總和 / PV總和
	
	//精準比對
	private String kwPreOpen;	//開啟狀態
	private String kwPrePvSum; //PV總和
	private String kwPreClkSum; //Click總和
	private String kwPrePriceSum; //價格總和
	private String preClkPriceAvg; //平均點選出價
	private String kwPreClkRate; //點選率 = Click總和 / PV總和
	
	//總計比對
	private String kwPvTotal; //PV總和
	private String kwClkTotal; //Click總和
	private String kwPriceTotal; //價格總和
	private String clkPriceTotalAvg; //平均點選出價
	private String kwClkRateTotal; //點選率 = Click總和 / PV總和
	
	//暫時沒在用
	private String customerName; //客戶姓名
	private String offerPrice; //廣告出價

	public String getKeywordSeq() {
		return keywordSeq;
	}

	public void setKeywordSeq(String keywordSeq) {
		this.keywordSeq = keywordSeq;
	}

	public String getKwOpen() {
		return kwOpen;
	}

	public void setKwOpen(String kwOpen) {
		this.kwOpen = kwOpen;
	}

	public String getKwPvSum() {
		return kwPvSum;
	}

	public void setKwPvSum(String kwPvSum) {
		this.kwPvSum = kwPvSum;
	}

	public String getKwClkSum() {
		return kwClkSum;
	}

	public void setKwClkSum(String kwClkSum) {
		this.kwClkSum = kwClkSum;
	}

	public String getKwPriceSum() {
		return kwPriceSum;
	}

	public void setKwPriceSum(String kwPriceSum) {
		this.kwPriceSum = kwPriceSum;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getClkPriceAvg() {
		return clkPriceAvg;
	}

	public void setClkPriceAvg(String clkPriceAvg) {
		this.clkPriceAvg = clkPriceAvg;
	}

	public String getKwClkRate() {
		return kwClkRate;
	}

	public void setKwClkRate(String kwClkRate) {
		this.kwClkRate = kwClkRate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAdAction() {
		return adAction;
	}

	public void setAdAction(String adAction) {
		this.adAction = adAction;
	}

	public String getAdGroup() {
		return adGroup;
	}

	public void setAdGroup(String adGroup) {
		this.adGroup = adGroup;
	}

	public String getOfferPrice() {
		return offerPrice;
	}

	public void setOfferPrice(String offerPrice) {
		this.offerPrice = offerPrice;
	}

	public String getKwPhrOpen() {
		return kwPhrOpen;
	}

	public void setKwPhrOpen(String kwPhrOpen) {
		this.kwPhrOpen = kwPhrOpen;
	}

	public String getKwPhrPvSum() {
		return kwPhrPvSum;
	}

	public void setKwPhrPvSum(String kwPhrPvSum) {
		this.kwPhrPvSum = kwPhrPvSum;
	}

	public String getKwPhrClkSum() {
		return kwPhrClkSum;
	}

	public void setKwPhrClkSum(String kwPhrClkSum) {
		this.kwPhrClkSum = kwPhrClkSum;
	}

	public String getKwPhrPriceSum() {
		return kwPhrPriceSum;
	}

	public void setKwPhrPriceSum(String kwPhrPriceSum) {
		this.kwPhrPriceSum = kwPhrPriceSum;
	}

	public String getPhrClkPriceAvg() {
		return phrClkPriceAvg;
	}

	public void setPhrClkPriceAvg(String phrClkPriceAvg) {
		this.phrClkPriceAvg = phrClkPriceAvg;
	}

	public String getKwPhrClkRate() {
		return kwPhrClkRate;
	}

	public void setKwPhrClkRate(String kwPhrClkRate) {
		this.kwPhrClkRate = kwPhrClkRate;
	}

	public String getKwPreOpen() {
		return kwPreOpen;
	}

	public void setKwPreOpen(String kwPreOpen) {
		this.kwPreOpen = kwPreOpen;
	}

	public String getKwPrePvSum() {
		return kwPrePvSum;
	}

	public void setKwPrePvSum(String kwPrePvSum) {
		this.kwPrePvSum = kwPrePvSum;
	}

	public String getKwPreClkSum() {
		return kwPreClkSum;
	}

	public void setKwPreClkSum(String kwPreClkSum) {
		this.kwPreClkSum = kwPreClkSum;
	}

	public String getKwPrePriceSum() {
		return kwPrePriceSum;
	}

	public void setKwPrePriceSum(String kwPrePriceSum) {
		this.kwPrePriceSum = kwPrePriceSum;
	}

	public String getPreClkPriceAvg() {
		return preClkPriceAvg;
	}

	public void setPreClkPriceAvg(String preClkPriceAvg) {
		this.preClkPriceAvg = preClkPriceAvg;
	}

	public String getKwPreClkRate() {
		return kwPreClkRate;
	}

	public void setKwPreClkRate(String kwPreClkRate) {
		this.kwPreClkRate = kwPreClkRate;
	}

	public String getKwPvTotal() {
		return kwPvTotal;
	}

	public void setKwPvTotal(String kwPvTotal) {
		this.kwPvTotal = kwPvTotal;
	}

	public String getKwClkTotal() {
		return kwClkTotal;
	}

	public void setKwClkTotal(String kwClkTotal) {
		this.kwClkTotal = kwClkTotal;
	}

	public String getKwPriceTotal() {
		return kwPriceTotal;
	}

	public void setKwPriceTotal(String kwPriceTotal) {
		this.kwPriceTotal = kwPriceTotal;
	}

	public String getClkPriceTotalAvg() {
		return clkPriceTotalAvg;
	}

	public void setClkPriceTotalAvg(String clkPriceTotalAvg) {
		this.clkPriceTotalAvg = clkPriceTotalAvg;
	}

	public String getKwClkRateTotal() {
		return kwClkRateTotal;
	}

	public void setKwClkRateTotal(String kwClkRateTotal) {
		this.kwClkRateTotal = kwClkRateTotal;
	}
	
}
