package com.pchome.akbadm.db.vo;

public class ControlPriceVO {
	
	private String adActionSeq;
	private float adActionMax = 0;
	private float adActionCost = 0;
	private float adActionInvalidCost = 0;
	private float adActionRealCost = 0;
	private float adActionControlPrice = 0;
	private String changeMax;
	
	
	public String getAdActionSeq() {
		return adActionSeq;
	}
	public void setAdActionSeq(String adActionSeq) {
		this.adActionSeq = adActionSeq;
	}
	public float getAdActionMax() {
		return adActionMax;
	}
	public void setAdActionMax(float adActionMax) {
		this.adActionMax = adActionMax;
	}
	public float getAdActionCost() {
		return adActionCost;
	}
	public void setAdActionCost(float adActionCost) {
		this.adActionCost = adActionCost;
	}
	public float getAdActionInvalidCost() {
		return adActionInvalidCost;
	}
	public void setAdActionInvalidCost(float adActionInvalidCost) {
		this.adActionInvalidCost = adActionInvalidCost;
	}
	public float getAdActionRealCost() {
		return adActionRealCost;
	}
	public void setAdActionRealCost(float adActionRealCost) {
		this.adActionRealCost = adActionRealCost;
	}
	public float getAdActionControlPrice() {
		return adActionControlPrice;
	}
	public void setAdActionControlPrice(float adActionControlPrice) {
		this.adActionControlPrice = adActionControlPrice;
	}
	public String getChangeMax() {
		return changeMax;
	}
	public void setChangeMax(String changeMax) {
		this.changeMax = changeMax;
	}
}
