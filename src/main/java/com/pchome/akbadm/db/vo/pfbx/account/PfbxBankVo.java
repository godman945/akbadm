package com.pchome.akbadm.db.vo.pfbx.account;

import java.util.Date;

//PfbxBank 銀行列表
public class PfbxBankVo {
	private int id;
	private String pfbxCustomerInfoId;	//連播網帳戶
	private String bankName;			//銀行名稱
	private String bankCode;			//銀行代碼
	private String branchName;			//分行名稱
	private String branchCode;			//分行代碼
	private String accountname;			//銀行戶名
	private String accountNumber;		//銀行帳號
	private String accountImg;			//存摺影本
	
	private String isMainBank;			//是否為主要銀行
	private String checkStatus;			//審核狀態
	private String checkNote;			//退回原因
	
	private Date createDate;
	private Date updateDate;
	
	
	public String getPfbxCustomerInfoId()
	{
		return pfbxCustomerInfoId;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId)
	{
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public String getIsMainBank()
	{
		return isMainBank;
	}
	public void setIsMainBank(String isMainBank)
	{
		this.isMainBank = isMainBank;
	}
	public String getCheckNote()
	{
		return checkNote;
	}
	public void setCheckNote(String checkNote)
	{
		this.checkNote = checkNote;
	}
	public String getBranchName()
	{
		return branchName;
	}
	public void setBranchName(String branchName)
	{
		this.branchName = branchName;
	}
	public String getBranchCode()
	{
		return branchCode;
	}
	public void setBranchCode(String branchCode)
	{
		this.branchCode = branchCode;
	}
	public String getAccountname()
	{
		return accountname;
	}
	public void setAccountname(String accountname)
	{
		this.accountname = accountname;
	}
	public String getAccountNumber()
	{
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber)
	{
		this.accountNumber = accountNumber;
	}
	public String getAccountImg()
	{
		return accountImg;
	}
	public void setAccountImg(String accountImg)
	{
		this.accountImg = accountImg;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public Date getUpdateDate()
	{
		return updateDate;
	}
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
	public String getCheckStatus()
	{
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus)
	{
		this.checkStatus = checkStatus;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getBankName()
	{
		return bankName;
	}
	public void setBankName(String bankName)
	{
		this.bankName = bankName;
	}
	public String getBankCode()
	{
		return bankCode;
	}
	public void setBankCode(String bankCode)
	{
		this.bankCode = bankCode;
	}
	
}
