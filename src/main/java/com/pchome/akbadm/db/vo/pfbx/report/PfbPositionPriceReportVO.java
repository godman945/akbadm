package com.pchome.akbadm.db.vo.pfbx.report;

import java.util.Date;

public class PfbPositionPriceReportVO {

	private String pId;                  //版位id
	private String pName;                //版位名稱
	private String templateProductXType; //版位編號
	private Integer sId;                 //版位尺寸id
	private String customerInfoId;       //廣告帳號
	private String pfpCustInfoId;        //僅撥pfpid廣告
	private String pfdCustInfoId;        //僅撥pfdid廣告
	private int deleteFlag;              //是否刪除 0:未刪除 1:已刪除
	private String pType;                //版位類型  N:正常  S:出價  P:私人
	private Integer pPrice;              //版位出價
	private Date updateDate;             //更新時間
	private Date createDate;             //建立日期
	
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getTemplateProductXType() {
		return templateProductXType;
	}
	public void setTemplateProductXType(String templateProductXType) {
		this.templateProductXType = templateProductXType;
	}
	public Integer getsId() {
		return sId;
	}
	public void setsId(Integer sId) {
		this.sId = sId;
	}
	public String getCustomerInfoId() {
		return customerInfoId;
	}
	public void setCustomerInfoId(String customerInfoId) {
		this.customerInfoId = customerInfoId;
	}
	public String getPfpCustInfoId() {
		return pfpCustInfoId;
	}
	public void setPfpCustInfoId(String pfpCustInfoId) {
		this.pfpCustInfoId = pfpCustInfoId;
	}
	public String getPfdCustInfoId() {
		return pfdCustInfoId;
	}
	public void setPfdCustInfoId(String pfdCustInfoId) {
		this.pfdCustInfoId = pfdCustInfoId;
	}
	public int getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getpType() {
		return pType;
	}
	public void setpType(String pType) {
		this.pType = pType;
	}
	public Integer getpPrice() {
		return pPrice;
	}
	public void setpPrice(Integer pPrice) {
		this.pPrice = pPrice;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
