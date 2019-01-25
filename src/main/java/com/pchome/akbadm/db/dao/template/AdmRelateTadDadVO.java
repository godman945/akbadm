package com.pchome.akbadm.db.dao.template;

public class AdmRelateTadDadVO {
	private int relateTadDadId;
	private String relateTadDadSeq;
	private String templateAdSeq;
	private int defineAdOrder; //資料庫讀取出來的舊權限
	private String defineAdSeq;
	private String adPoolSeq;

	public int getRelateTadDadId() {
		return relateTadDadId;
	}
	public void setRelateTadDadId(int relateTadDadId) {
		this.relateTadDadId = relateTadDadId;
	}
	public String getRelateTadDadSeq() {
		return relateTadDadSeq;
	}
	public void setRelateTadDadSeq(String relateTadDadSeq) {
		this.relateTadDadSeq = relateTadDadSeq;
	}
	public String getTemplateAdSeq() {
		return templateAdSeq;
	}
	public void setTemplateAdSeq(String templateAdSeq) {
		this.templateAdSeq = templateAdSeq;
	}
	public int getDefineAdOrder() {
		return defineAdOrder;
	}
	public void setDefineAdOrder(int defineAdOrder) {
		this.defineAdOrder = defineAdOrder;
	}
	public String getDefineAdSeq() {
		return defineAdSeq;
	}
	public void setDefineAdSeq(String defineAdSeq) {
		this.defineAdSeq = defineAdSeq;
	}
	public String getAdPoolSeq() {
		return adPoolSeq;
	}
	public void setAdPoolSeq(String adPoolSeq) {
		this.adPoolSeq = adPoolSeq;
	}
}
