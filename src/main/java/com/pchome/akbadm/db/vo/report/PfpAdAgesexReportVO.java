package com.pchome.akbadm.db.vo.report;

import java.math.BigInteger;

public class PfpAdAgesexReportVO {

	private String reportDate; //報表日期
	
	private String age;		//年齡
	private String sex;		//性別
	
	private String adGroupSeq; //廣告群組序號
	private String adActionSeq; //廣告活動序號

	private String adPvSum; //廣告PV總和
	private String adClkSum; //廣告Click總和
	private String adPriceSum; //廣告價格總和
	private String adInvClkSum; //廣告無效點擊總和
	private BigInteger count; //資料筆數(用於計算平均每日花費上限)
	private String adDevice; //裝置
	private String adType;	 //廣告類型
	private String adClkRate;	//點擊率
	private String adPvAvgPrice;//千次曝光費用
	private String AdClkAvgPrice;	//平均點擊費用

	public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAdGroupSeq() {
		return adGroupSeq;
	}

	public void setAdGroupSeq(String adGroupSeq) {
		this.adGroupSeq = adGroupSeq;
	}

	public String getAdActionSeq() {
		return adActionSeq;
	}

	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
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

	public String getAdPriceSum() {
		return adPriceSum;
	}

	public void setAdPriceSum(String adPriceSum) {
		this.adPriceSum = adPriceSum;
	}

	public String getAdInvClkSum() {
		return adInvClkSum;
	}

	public void setAdInvClkSum(String adInvClkSum) {
		this.adInvClkSum = adInvClkSum;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public String getAdDevice() {
		return adDevice;
	}

	public void setAdDevice(String adDevice) {
		this.adDevice = adDevice;
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}

	public String getAdClkRate() {
		return adClkRate;
	}

	public void setAdClkRate(String adClkRate) {
		this.adClkRate = adClkRate;
	}

	public String getAdPvAvgPrice() {
		return adPvAvgPrice;
	}

	public void setAdPvAvgPrice(String adPvAvgPrice) {
		this.adPvAvgPrice = adPvAvgPrice;
	}

	public String getAdClkAvgPrice() {
		return AdClkAvgPrice;
	}

	public void setAdClkAvgPrice(String adClkAvgPrice) {
		AdClkAvgPrice = adClkAvgPrice;
	}

}
