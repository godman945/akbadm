package com.pchome.akbadm.db.vo.pfbx.bonus;

public class PfbxBonusBillVo {

	private String transDate;				// 交易年月
	private String transDesc;				// 交易項目
	private float transMoney;				// 交易金額
	
	public String getTransDate() {
		return transDate;
	}
	
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	
	public String getTransDesc() {
		return transDesc;
	}
	
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	
	public float getTransMoney() {
		return transMoney;
	}
	
	public void setTransMoney(float transMoney) {
		this.transMoney = transMoney;
	}
	
}
