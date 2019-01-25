package com.pchome.akbadm.db.vo.pfbx.account;

import java.util.Date;


//領款人資料
public class PfbxPersonalVo {
	
	private Integer id;
	private String pfbxCustomerInfoId;	//連播網帳戶
	private String name;				//姓名
	private Date birthday;				//生日
	private String zip;					//郵遞區號
	private String contactAddress;		//連絡地址
	private String idCard;				//身分證字號
	private String idCardImg;			//身分證影本
	private String isMainUse;			//是否為主要使用
	
	private String deleteFlag;			//是否刪除
	private String checkStatus;			//審核狀態
	private String checkNote;			//退回原因
	
	private Date createDate;
	private Date updatedate;
	
	
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getPfbxCustomerInfoId()
	{
		return pfbxCustomerInfoId;
	}
	public void setPfbxCustomerInfoId(String pfbxCustomerInfoId)
	{
		this.pfbxCustomerInfoId = pfbxCustomerInfoId;
	}
	public Date getBirthday()
	{
		return birthday;
	}
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	public String getContactAddress()
	{
		return contactAddress;
	}
	public void setContactAddress(String contactAddress)
	{
		this.contactAddress = contactAddress;
	}
	public String getIdCard()
	{
		return idCard;
	}
	public void setIdCard(String idCard)
	{
		this.idCard = idCard;
	}
	public String getIdCardImg()
	{
		return idCardImg;
	}
	public void setIdCardImg(String idCardImg)
	{
		this.idCardImg = idCardImg;
	}
	public String getIsMainUse()
	{
		return isMainUse;
	}
	public void setIsMainUse(String isMainUse)
	{
		this.isMainUse = isMainUse;
	}
	public String getDeleteFlag()
	{
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag)
	{
		this.deleteFlag = deleteFlag;
	}
	public String getCheckStatus()
	{
		return checkStatus;
	}
	public void setCheckStatus(String checkStatus)
	{
		this.checkStatus = checkStatus;
	}
	public String getCheckNote()
	{
		return checkNote;
	}
	public void setCheckNote(String checkNote)
	{
		this.checkNote = checkNote;
	}
	public Date getCreateDate()
	{
		return createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public Date getUpdatedate()
	{
		return updatedate;
	}
	public void setUpdatedate(Date updatedate)
	{
		this.updatedate = updatedate;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	
}
