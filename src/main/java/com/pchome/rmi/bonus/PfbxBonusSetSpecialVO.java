package com.pchome.rmi.bonus;

import java.io.Serializable;

public class PfbxBonusSetSpecialVO implements Serializable {
	
	private Integer id;
	private String pfbId;
	private String specialName;
	private String startDate;
	private String endDate;
	private float pfbPercent;
	private float pchomeChargePercent;
	private Integer deleteFlag;
	private String updateDate;
	private String createDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPfbId() {
		return pfbId;
	}
	public void setPfbId(String pfbId) {
		this.pfbId = pfbId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public float getPfbPercent() {
		return pfbPercent;
	}
	public void setPfbPercent(float pfbPercent) {
		this.pfbPercent = pfbPercent;
	}
	public float getPchomeChargePercent() {
		return pchomeChargePercent;
	}
	public void setPchomeChargePercent(float pchomeChargePercent) {
		this.pchomeChargePercent = pchomeChargePercent;
	}
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	
}
