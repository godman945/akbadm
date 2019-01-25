package com.pchome.akbadm.db.vo.pfbx.bonus;

import java.util.Date;

public class PfbxApplyBonusVo
{
	private Date pfbxApplyDate; // pfb 請款日期
	private String pfbxApplyBonusId; // pfb 請款單編號
	private String pfbxCustomerInfoId; // pfb 帳戶編號
	private String pfbxCustomerInfoName; // pfb 帳戶名稱
	private String pfbxCustomerCategory; // 帳戶類型(Code)
	private String pfbxCustomerCategoryName; // 帳戶類型(名稱)
	private String pfbxCustomerTaxId; // 統一編號
	private String pfbxCustomerMemberId; // 申請帳號
	private String statusId; // 請款狀態Id
	private String statusName; // 請款狀態
	private String pfbxApplyStatus; // 請款狀態
	private String pfbxApplyNote; // 請款失敗原因
	private float applyMoney; // pfb 請款金額

	// 20150722新增銀行、領款人
	private String bankName; // 銀行名稱
	private String bankCheckStatus; // 銀行狀態
	private String personalName; // 領款人名稱
	private String personalCheckStatus; // 領款人狀態
	private String pfbApplyInvoiceStatus; // 發票/收據審核狀態
	private String pfbApplyInvoiceNote; // 發票/收據審核失敗原因
	private String pfbApplyInvoiceCheckStatus; // 發票/收據確認狀態

	public Date getPfbxApplyDate()
	{
		return pfbxApplyDate;
	}

	public void setPfbxApplyDate(Date pfbxApplyDate)
	{
		this.pfbxApplyDate = pfbxApplyDate;
	}

	public String getPfbxApplyBonusId()
	{
		return pfbxApplyBonusId;
	}

	public void setPfbxApplyBonusId(String pfbxApplyBonusId)
	{
		this.pfbxApplyBonusId = pfbxApplyBonusId;
	}

	public String getPfbxCustomerInfoId()
	{
		return pfbxCustomerInfoId;
	}

	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId)
	{
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}

	public String getPfbxCustomerInfoName()
	{
		return pfbxCustomerInfoName;
	}

	public void setPfbxCustomerInfoName(String pfbxCustomerInfoName)
	{
		this.pfbxCustomerInfoName = pfbxCustomerInfoName;
	}

	public String getPfbxCustomerCategoryName()
	{
		return pfbxCustomerCategoryName;
	}

	public void setPfbxCustomerCategoryName(String pfbxCustomerCategoryName)
	{
		this.pfbxCustomerCategoryName = pfbxCustomerCategoryName;
	}

	public String getPfbxCustomerTaxId()
	{
		return pfbxCustomerTaxId;
	}

	public void setPfbxCustomerTaxId(String pfbxCustomerTaxId)
	{
		this.pfbxCustomerTaxId = pfbxCustomerTaxId;
	}

	public String getStatusName()
	{
		return statusName;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public String getPfbxCustomerMemberId()
	{
		return pfbxCustomerMemberId;
	}

	public void setPfbxCustomerMemberId(String pfbxCustomerMemberId)
	{
		this.pfbxCustomerMemberId = pfbxCustomerMemberId;
	}

	public String getStatusId()
	{
		return statusId;
	}

	public void setStatusId(String statusId)
	{
		this.statusId = statusId;
	}

	public float getApplyMoney()
	{
		return applyMoney;
	}

	public void setApplyMoney(float applyMoney)
	{
		this.applyMoney = applyMoney;
	}

	public String getBankName()
	{
		return bankName;
	}

	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}

	public String getBankCheckStatus()
	{
		return bankCheckStatus;
	}

	public void setBankCheckStatus(String bankCheckStatus)
	{
		this.bankCheckStatus = bankCheckStatus;
	}

	public String getPersonalName()
	{
		return personalName;
	}

	public void setPersonalName(String personalName)
	{
		this.personalName = personalName;
	}

	public String getPersonalCheckStatus()
	{
		return personalCheckStatus;
	}

	public void setPersonalCheckStatus(String personalCheckStatus)
	{
		this.personalCheckStatus = personalCheckStatus;
	}

	public String getPfbxApplyStatus()
	{
		return pfbxApplyStatus;
	}

	public void setPfbxApplyStatus(String pfbxApplyStatus)
	{
		this.pfbxApplyStatus = pfbxApplyStatus;
	}

	public String getPfbApplyInvoiceStatus()
	{
		return pfbApplyInvoiceStatus;
	}

	public void setPfbApplyInvoiceStatus(String pfbApplyInvoiceStatus)
	{
		this.pfbApplyInvoiceStatus = pfbApplyInvoiceStatus;
	}

	public String getPfbxApplyNote()
	{
		return pfbxApplyNote;
	}

	public void setPfbxApplyNote(String pfbxApplyNote)
	{
		this.pfbxApplyNote = pfbxApplyNote;
	}

	public String getPfbApplyInvoiceNote()
	{
		return pfbApplyInvoiceNote;
	}

	public void setPfbApplyInvoiceNote(String pfbApplyInvoiceNote)
	{
		this.pfbApplyInvoiceNote = pfbApplyInvoiceNote;
	}

	public String getPfbxCustomerCategory() {
		return pfbxCustomerCategory;
	}

	public void setPfbxCustomerCategory(String pfbxCustomerCategory) {
		this.pfbxCustomerCategory = pfbxCustomerCategory;
	}

	public String getPfbApplyInvoiceCheckStatus() {
		return pfbApplyInvoiceCheckStatus;
	}

	public void setPfbApplyInvoiceCheckStatus(String pfbApplyInvoiceCheckStatus) {
		this.pfbApplyInvoiceCheckStatus = pfbApplyInvoiceCheckStatus;
	}
}
