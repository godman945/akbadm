package com.pchome.akbadm.db.dao.template;

public class AdmRelateTproTadVO {
	private int relateTproTadId;
	private String relateTproTadSeq;
	private String templateProductSeq;
	private String templateProductSeqSub;
	private int templateAdOrder; //資料庫讀取出來的舊權限
	private String templateAdSeq;

	public int getRelateTproTadId() {
		return relateTproTadId;
	}
	public void setRelateTproTadId(int relateTproTadId) {
		this.relateTproTadId = relateTproTadId;
	}
	public String getRelateTproTadSeq() {
		return relateTproTadSeq;
	}
	public void setRelateTproTadSeq(String relateTproTadSeq) {
		this.relateTproTadSeq = relateTproTadSeq;
	}
	public String getTemplateProductSeq() {
		return templateProductSeq;
	}
	public void setTemplateProductSeq(String templateProductSeq) {
		this.templateProductSeq = templateProductSeq;
	}
	public String getTemplateProductSeqSub() {
		return templateProductSeqSub;
	}
	public void setTemplateProductSeqSub(String templateProductSeqSub) {
		this.templateProductSeqSub = templateProductSeqSub;
	}
	public int getTemplateAdOrder() {
		return templateAdOrder;
	}
	public void setTemplateAdOrder(int templateAdOrder) {
		this.templateAdOrder = templateAdOrder;
	}
	public String getTemplateAdSeq() {
		return templateAdSeq;
	}
	public void setTemplateAdSeq(String templateAdSeq) {
		this.templateAdSeq = templateAdSeq;
	}
}
