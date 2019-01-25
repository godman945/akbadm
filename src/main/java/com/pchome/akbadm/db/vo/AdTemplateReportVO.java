package com.pchome.akbadm.db.vo;

public class AdTemplateReportVO {

	private String templateProdSeq; //廣告樣式序號
	private String templateProdName; //廣告樣式名稱
	private String pvSum; //PV總和
	private String clkSum; //Click總和
	private String clkPriceAvg; //平均點選出價
	private String clkRate; //點選率 = Click總和 / PV總和
	private String priceSum; //價格總和

	public String getTemplateProdSeq() {
		return templateProdSeq;
	}

	public void setTemplateProdSeq(String templateProdSeq) {
		this.templateProdSeq = templateProdSeq;
	}

	public String getTemplateProdName() {
		return templateProdName;
	}

	public void setTemplateProdName(String templateProdName) {
		this.templateProdName = templateProdName;
	}

	public String getClkPriceAvg() {
		return clkPriceAvg;
	}

	public void setClkPriceAvg(String clkPriceAvg) {
		this.clkPriceAvg = clkPriceAvg;
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

	public String getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(String priceSum) {
		this.priceSum = priceSum;
	}
}
