package com.pchome.rmi.manager;

import java.io.Serializable;

public class PfdAccountVO implements Serializable{

	private String pfdCustomerInfoId;
	private String pfdCustomerInfoName;
	private String pfdCustomerInfoStatus;
	private int developAmount;
	private float advanceTotalRemain;
	private float laterRemainQuota;
	private float laterTotalRemain;
	private float oneWeekAdCost;
	
	public String getPfdCustomerInfoId() {
		return pfdCustomerInfoId;
	}
	
	public void setPfdCustomerInfoId(String pfdCustomerInfoId) {
		this.pfdCustomerInfoId = pfdCustomerInfoId;
	}
	
	public String getPfdCustomerInfoName() {
		return pfdCustomerInfoName;
	}
	
	public void setPfdCustomerInfoName(String pfdCustomerInfoName) {
		this.pfdCustomerInfoName = pfdCustomerInfoName;
	}
	
	public String getPfdCustomerInfoStatus() {
		return pfdCustomerInfoStatus;
	}
	
	public void setPfdCustomerInfoStatus(String pfdCustomerInfoStatus) {
		this.pfdCustomerInfoStatus = pfdCustomerInfoStatus;
	}
	
	public int getDevelopAmount() {
		return developAmount;
	}

	public void setDevelopAmount(int developAmount) {
		this.developAmount = developAmount;
	}

	public float getAdvanceTotalRemain() {
		return advanceTotalRemain;
	}
	
	public void setAdvanceTotalRemain(float advanceTotalRemain) {
		this.advanceTotalRemain = advanceTotalRemain;
	}
	
	public float getLaterRemainQuota() {
		return laterRemainQuota;
	}

	public void setLaterRemainQuota(float laterRemainQuota) {
		this.laterRemainQuota = laterRemainQuota;
	}

	public float getLaterTotalRemain() {
		return laterTotalRemain;
	}
	
	public void setLaterTotalRemain(float laterTotalRemain) {
		this.laterTotalRemain = laterTotalRemain;
	}
	
	public float getOneWeekAdCost() {
		return oneWeekAdCost;
	}
	
	public void setOneWeekAdCost(float oneWeekAdCost) {
		this.oneWeekAdCost = oneWeekAdCost;
	}
	
}
