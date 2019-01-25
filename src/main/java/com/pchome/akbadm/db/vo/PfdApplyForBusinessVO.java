package com.pchome.akbadm.db.vo;

import java.util.List;

public class PfdApplyForBusinessVO {

	private String applyForSeq;
	private String pfdCustomerInfoTitle;
	private String taxId;
	private String adUrl;
	private String status;
	private String applyForTime;
	private List<String> illegalReasonList;

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplyForTime() {
		return applyForTime;
	}

	public void setApplyForTime(String applyForTime) {
		this.applyForTime = applyForTime;
	}

	public String getApplyForSeq() {
		return applyForSeq;
	}

	public void setApplyForSeq(String applyForSeq) {
		this.applyForSeq = applyForSeq;
	}

	public List<String> getIllegalReasonList() {
		return illegalReasonList;
	}

	public void setIllegalReasonList(List<String> illegalReasonList) {
		this.illegalReasonList = illegalReasonList;
	}

	public String getPfdCustomerInfoTitle() {
		return pfdCustomerInfoTitle;
	}

	public void setPfdCustomerInfoTitle(String pfdCustomerInfoTitle) {
		this.pfdCustomerInfoTitle = pfdCustomerInfoTitle;
	}
}
