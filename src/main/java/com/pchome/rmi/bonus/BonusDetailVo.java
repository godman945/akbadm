package com.pchome.rmi.bonus;

import java.io.Serializable;

public class BonusDetailVo implements Serializable{
	
	private String pfpId;				// 廣告商Id
	private String pfpName;				// 廣告商名稱
	private String bonusItemName;		// 獎金名稱
	private float adClkPrice;			// 廣告點擊費用
	private float bonusPercent;			// 獎金百分比
	private float bonusMoney;			// 領取獎金
	private String payType;
	
	public String getPfpId() {
		return pfpId;
	}
	
	public void setPfpId(String pfpId) {
		this.pfpId = pfpId;
	}
	
	public String getPfpName() {
		return pfpName;
	}
	
	public void setPfpName(String pfpName) {
		this.pfpName = pfpName;
	}
	
	public String getBonusItemName() {
		return bonusItemName;
	}
	
	public void setBonusItemName(String bonusItemName) {
		this.bonusItemName = bonusItemName;
	}
	
	public float getAdClkPrice() {
		return adClkPrice;
	}
	
	public void setAdClkPrice(float adClkPrice) {
		this.adClkPrice = adClkPrice;
	}
	
	public float getBonusPercent() {
		return bonusPercent;
	}
	
	public void setBonusPercent(float bonusPercent) {
		this.bonusPercent = bonusPercent;
	}
	
	public float getBonusMoney() {
		return bonusMoney;
	}
	
	public void setBonusMoney(float bonusMoney) {
		this.bonusMoney = bonusMoney;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
}
