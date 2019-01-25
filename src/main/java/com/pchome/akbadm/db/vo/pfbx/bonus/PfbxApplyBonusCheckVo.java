package com.pchome.akbadm.db.vo.pfbx.bonus;

import java.util.Date;

public class PfbxApplyBonusCheckVo {

	private String pfbxApplyBonusId;			// pfb 請款單流水號
	private String pfbxApplyStatus;				// pfb 請款單狀態
	private String pfbCategory;					// pfb 帳戶類型
	
	private String bankId;						// 銀行流水號
	private String bankCode;					// 銀行代碼	
	private String bankName;					// 銀行名稱	
	private String bankBranchCode;				// 分行代碼
	private String bankBranchName;				// 分行名稱
	private String bankAccountNumber;			// 匯款銀行帳戶
	private String bankAccountName;				// 匯款銀行戶名	
	private String bankImgPath;					// 匯款銀行影本
	private String bankCheckStatus;				// 銀行審核狀態
	private String bankCheckNote;				// 銀行退回原因	
	
	private String personalId;					// 個人資料流水號
	private Date personalBirthday;				// 出生年月日
	private String personalAddress;				// 聯絡地址
	private String personalIdCardImgPath;		// 身分證影本
	private String personalCheckStatus;			// 身份審核狀態
	private String personalCheckNote;			// 身份退回原因
	
	private Date applyDate;						// 請款日期
	private float applyMoney;					// 請款金額
	private String applyImgPath;				// 請款收據影本
	private String applyCheckStatus;			// 請款審核狀態
	private String applyCheckNote;				// 請款退回原因
	
	
	public String getPfbxApplyBonusId() {
		return pfbxApplyBonusId;
	}
	public void setPfbxApplyBonusId(String pfbxApplyBonusId) {
		this.pfbxApplyBonusId = pfbxApplyBonusId;
	}
	public String getPfbxApplyStatus() {
		return pfbxApplyStatus;
	}
	public void setPfbxApplyStatus(String pfbxApplyStatus) {
		this.pfbxApplyStatus = pfbxApplyStatus;
	}
	public String getPfbCategory() {
		return pfbCategory;
	}
	public void setPfbCategory(String pfbCategory) {
		this.pfbCategory = pfbCategory;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankBranchCode() {
		return bankBranchCode;
	}
	public void setBankBranchCode(String bankBranchCode) {
		this.bankBranchCode = bankBranchCode;
	}
	public String getBankBranchName() {
		return bankBranchName;
	}
	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBankImgPath() {
		return bankImgPath;
	}
	public void setBankImgPath(String bankImgPath) {
		this.bankImgPath = bankImgPath;
	}
	public String getBankCheckStatus() {
		return bankCheckStatus;
	}
	public void setBankCheckStatus(String bankCheckStatus) {
		this.bankCheckStatus = bankCheckStatus;
	}
	public String getBankCheckNote() {
		return bankCheckNote;
	}
	public void setBankCheckNote(String bankCheckNote) {
		this.bankCheckNote = bankCheckNote;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
	public Date getPersonalBirthday() {
		return personalBirthday;
	}
	public void setPersonalBirthday(Date personalBirthday) {
		this.personalBirthday = personalBirthday;
	}
	public String getPersonalAddress() {
		return personalAddress;
	}
	public void setPersonalAddress(String personalAddress) {
		this.personalAddress = personalAddress;
	}
	public String getPersonalIdCardImgPath() {
		return personalIdCardImgPath;
	}
	public void setPersonalIdCardImgPath(String personalIdCardImgPath) {
		this.personalIdCardImgPath = personalIdCardImgPath;
	}
	public String getPersonalCheckStatus() {
		return personalCheckStatus;
	}
	public void setPersonalCheckStatus(String personalCheckStatus) {
		this.personalCheckStatus = personalCheckStatus;
	}
	public String getPersonalCheckNote() {
		return personalCheckNote;
	}
	public void setPersonalCheckNote(String personalCheckNote) {
		this.personalCheckNote = personalCheckNote;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public float getApplyMoney() {
		return applyMoney;
	}
	public void setApplyMoney(float applyMoney) {
		this.applyMoney = applyMoney;
	}
	public String getApplyImgPath() {
		return applyImgPath;
	}
	public void setApplyImgPath(String applyImgPath) {
		this.applyImgPath = applyImgPath;
	}
	public String getApplyCheckStatus() {
		return applyCheckStatus;
	}
	public void setApplyCheckStatus(String applyCheckStatus) {
		this.applyCheckStatus = applyCheckStatus;
	}
	public String getApplyCheckNote() {
		return applyCheckNote;
	}
	public void setApplyCheckNote(String applyCheckNote) {
		this.applyCheckNote = applyCheckNote;
	}
	
	
	
}
