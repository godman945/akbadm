package com.pchome.rmi.bonus;

import java.io.Serializable;

public class PfdBonusDetailVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String bonusItemName;				// 獎金名稱
	private String bonusDetailDesc;				// 獎金描述
	private double nowClkPrice;					// 總廣告點擊數
	private double nowClkTax;					// 廣告點擊數營業稅
	private double nowClkPriceSum;				// 總廣告點擊數(含稅)
	private double nowBonus;						// 總應得獎金
	private double nowBonusTax;					// 總應得獎金營業稅
	private double nowBonusSum;					// 總應得獎金(含稅)
	private int count;							// 筆數
	private String contractId;					// 合約編號	
	
	public String getBonusItemName() {
		return bonusItemName;
	}
	public void setBonusItemName(String bonusItemName) {
		this.bonusItemName = bonusItemName;
	}
	public String getBonusDetailDesc() {
		return bonusDetailDesc;
	}
	public void setBonusDetailDesc(String bonusDetailDesc) {
		this.bonusDetailDesc = bonusDetailDesc;
	}
	public double getNowClkPrice() {
		return nowClkPrice;
	}
	public void setNowClkPrice(double nowClkPrice) {
		this.nowClkPrice = nowClkPrice;
	}
	public double getNowClkTax() {
		return nowClkTax;
	}
	public void setNowClkTax(double nowClkTax) {
		this.nowClkTax = nowClkTax;
	}
	public double getNowClkPriceSum() {
		return nowClkPriceSum;
	}
	public void setNowClkPriceSum(double nowClkPriceSum) {
		this.nowClkPriceSum = nowClkPriceSum;
	}
	public double getNowBonus() {
		return nowBonus;
	}
	public void setNowBonus(double nowBonus) {
		this.nowBonus = nowBonus;
	}
	public double getNowBonusTax() {
		return nowBonusTax;
	}
	public void setNowBonusTax(double nowBonusTax) {
		this.nowBonusTax = nowBonusTax;
	}
	public double getNowBonusSum() {
		return nowBonusSum;
	}
	public void setNowBonusSum(double nowBonusSum) {
		this.nowBonusSum = nowBonusSum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
}
