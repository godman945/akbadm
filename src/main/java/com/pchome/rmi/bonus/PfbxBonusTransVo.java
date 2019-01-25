package com.pchome.rmi.bonus;

import java.io.Serializable;

public class PfbxBonusTransVo implements Serializable{

	private String pfbxCustomerInfoId;		// pfb 帳戶編號
	private String transDate;				// 交易日期
	private String transDateDesc;			// 收益日期說明
	private String transDesc;				// 交易項目
	private String transType;				// 交易類型
	private float waitBonus;				// 待結款項	
	private float payBonus;				// 已付款項
	private float remain;					// 收益餘額
	
	private String pfbApplyId;				// 請款編號
	private String pfbApplyStatus;			// 請款狀態Id
	private String pfbApplyStatusChName;	// 請款狀態名稱
	
	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public String getTransDate() {
		return transDate;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public String getTransDateDesc() {
		return transDateDesc;
	}
	public void setTransDateDesc(String transDateDesc) {
		this.transDateDesc = transDateDesc;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public float getWaitBonus() {
		return waitBonus;
	}
	public void setWaitBonus(float waitBonus) {
		this.waitBonus = waitBonus;
	}
	public float getPayBonus() {
		return payBonus;
	}
	public void setPayBonus(float payBonus) {
		this.payBonus = payBonus;
	}
	public float getRemain() {
		return remain;
	}
	public void setRemain(float remain) {
		this.remain = remain;
	}
	public String getPfbApplyId() {
		return pfbApplyId;
	}
	public void setPfbApplyId(String pfbApplyId) {
		this.pfbApplyId = pfbApplyId;
	}
	public String getPfbApplyStatus() {
		return pfbApplyStatus;
	}
	public void setPfbApplyStatus(String pfbApplyStatus) {
		this.pfbApplyStatus = pfbApplyStatus;
	}
	public String getPfbApplyStatusChName() {
		return pfbApplyStatusChName;
	}
	public void setPfbApplyStatusChName(String pfbApplyStatusChName) {
		this.pfbApplyStatusChName = pfbApplyStatusChName;
	}	
	
}
