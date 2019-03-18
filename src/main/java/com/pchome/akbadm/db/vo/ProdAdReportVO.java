package com.pchome.akbadm.db.vo;

public class ProdAdReportVO {

	private String pfpCustomerInfoId;	//客戶帳號
	private String adSeq;				//廣告序號
	private String catalogSeq;			//商品目錄ID
	private String catalogProdSeq; 		//商品ID
	private String clk; 				//商品點選數
	private String pv;					//曝光次數
	private String clkRate;				//(商品點選數/曝光次數)=商品點選率
	private String prodName; 			//商品名稱
	private String prodImg;				//商品圖
	private String sumClk; 				//商品點選數總數
	private String sumPv;				//曝光次數總數
	private String sumClkRate;			//(商品點選數總數/曝光次數總數)=商品點選率總數
	private String rowTotal;			//總筆數
	
	public String getPfpCustomerInfoId() {
		return pfpCustomerInfoId;
	}
	public void setPfpCustomerInfoId(String pfpCustomerInfoId) {
		this.pfpCustomerInfoId = pfpCustomerInfoId;
	}
	public String getAdSeq() {
		return adSeq;
	}
	public void setAdSeq(String adSeq) {
		this.adSeq = adSeq;
	}
	public String getCatalogSeq() {
		return catalogSeq;
	}
	public void setCatalogSeq(String catalogSeq) {
		this.catalogSeq = catalogSeq;
	}
	public String getCatalogProdSeq() {
		return catalogProdSeq;
	}
	public void setCatalogProdSeq(String catalogProdSeq) {
		this.catalogProdSeq = catalogProdSeq;
	}
	public String getClk() {
		return clk;
	}
	public void setClk(String clk) {
		this.clk = clk;
	}
	public String getPv() {
		return pv;
	}
	public void setPv(String pv) {
		this.pv = pv;
	}
	public String getClkRate() {
		return clkRate;
	}
	public void setClkRate(String clkRate) {
		this.clkRate = clkRate;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getProdImg() {
		return prodImg;
	}
	public void setProdImg(String prodImg) {
		this.prodImg = prodImg;
	}
	public String getSumClk() {
		return sumClk;
	}
	public void setSumClk(String sumClk) {
		this.sumClk = sumClk;
	}
	public String getSumPv() {
		return sumPv;
	}
	public void setSumPv(String sumPv) {
		this.sumPv = sumPv;
	}
	public String getSumClkRate() {
		return sumClkRate;
	}
	public void setSumClkRate(String sumClkRate) {
		this.sumClkRate = sumClkRate;
	}
	public String getRowTotal() {
		return rowTotal;
	}
	public void setRowTotal(String rowTotal) {
		this.rowTotal = rowTotal;
	}
	
}
