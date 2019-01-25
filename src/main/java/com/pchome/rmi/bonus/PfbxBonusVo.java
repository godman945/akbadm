package com.pchome.rmi.bonus;

import java.io.Serializable;

public class PfbxBonusVo implements Serializable{

	private String pfbxCustomerInfoId;			// pfb 帳戶編號
	private String pfbxCustomerInfoName;		// pfb 帳戶名稱
	private String pfbxCustomerInfoContactName;	// pfb 帳戶聯絡人
	private String pfbxCustomerCategoryName;			// 帳戶類型
	private String pfbxCustomerTaxId;					// 統一編號
	private String pfbxCustomerMemberId;				// 申請帳號
	private String statusId;							// 分潤狀態
	private String statusName;							// 分潤狀態名稱
	private String author;								// 分潤設定者
	private String ip;									// 分潤設定 IP
	private int pfbxCustomerBonusPercent;				// pfb 收益百分比
	private float totalBonus;							// pfb 總金額
	private float applyBonus;							// pfb 請款金額
	private float totalPayBonus;						// pfb 累計已付金額
	private float waitBonus;							// pfb 待結金額
	private float lastPayBonus;							// pfb 上次已付金額
	private float applyMinLimit;						// pfb 請款最低限制
	private int pfbxCustomerBonusPchomeChargePercent;	// pfb PChome分潤百分比
	private int pfbxCustomerBonusTotalPercent;			// pfb 分潤總計百分比
	private int totalSize;								// 筆數

	public String getPfbxCustomerInfoId() {
		return pfbxCustomerInfoId;
	}
	
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId) {
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}

	public String getPfbxCustomerInfoName() {
		return pfbxCustomerInfoName;
	}

	public void setPfbxCustomerInfoName(String pfbxCustomerInfoName) {
		this.pfbxCustomerInfoName = pfbxCustomerInfoName;
	}

	public String getPfbxCustomerInfoContactName() {
		return pfbxCustomerInfoContactName;
	}

	public void setPfbxCustomerInfoContactName(String pfbxCustomerInfoContactName) {
		this.pfbxCustomerInfoContactName = pfbxCustomerInfoContactName;
	}

	public String getPfbxCustomerCategoryName() {
		return pfbxCustomerCategoryName;
	}

	public void setPfbxCustomerCategoryName(String pfbxCustomerCategoryName) {
		this.pfbxCustomerCategoryName = pfbxCustomerCategoryName;
	}

	public String getPfbxCustomerTaxId() {
		return pfbxCustomerTaxId;
	}

	public void setPfbxCustomerTaxId(String pfbxCustomerTaxId) {
		this.pfbxCustomerTaxId = pfbxCustomerTaxId;
	}

	public String getPfbxCustomerMemberId() {
		return pfbxCustomerMemberId;
	}

	public void setPfbxCustomerMemberId(String pfbxCustomerMemberId) {
		this.pfbxCustomerMemberId = pfbxCustomerMemberId;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPfbxCustomerBonusPercent() {
		return pfbxCustomerBonusPercent;
	}

	public void setPfbxCustomerBonusPercent(int pfbxCustomerBonusPercent) {
		this.pfbxCustomerBonusPercent = pfbxCustomerBonusPercent;
	}

	public float getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(float totalBonus) {
		this.totalBonus = totalBonus;
	}

	public float getApplyBonus() {
		return applyBonus;
	}

	public void setApplyBonus(float applyBonus) {
		this.applyBonus = applyBonus;
	}

	public float getTotalPayBonus() {
		return totalPayBonus;
	}

	public void setTotalPayBonus(float totalPayBonus) {
		this.totalPayBonus = totalPayBonus;
	}

	public float getWaitBonus() {
		return waitBonus;
	}

	public void setWaitBonus(float waitBonus) {
		this.waitBonus = waitBonus;
	}

	public float getLastPayBonus() {
		return lastPayBonus;
	}

	public void setLastPayBonus(float lastPayBonus) {
		this.lastPayBonus = lastPayBonus;
	}

	public float getApplyMinLimit() {
		return applyMinLimit;
	}

	public void setApplyMinLimit(float applyMinLimit) {
		this.applyMinLimit = applyMinLimit;
	}

	public int getPfbxCustomerBonusPchomeChargePercent() {
		return pfbxCustomerBonusPchomeChargePercent;
	}

	public void setPfbxCustomerBonusPchomeChargePercent(
			int pfbxCustomerBonusPchomeChargePercent) {
		this.pfbxCustomerBonusPchomeChargePercent = pfbxCustomerBonusPchomeChargePercent;
	}

	public int getPfbxCustomerBonusTotalPercent() {
		return pfbxCustomerBonusTotalPercent;
	}

	public void setPfbxCustomerBonusTotalPercent(int pfbxCustomerBonusTotalPercent) {
		this.pfbxCustomerBonusTotalPercent = pfbxCustomerBonusTotalPercent;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
}
