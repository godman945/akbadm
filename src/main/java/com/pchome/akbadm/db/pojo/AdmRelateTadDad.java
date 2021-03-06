package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AdmRelateTadDad generated by hbm2java
 */
@Entity
@Table(name = "adm_relate_tad_dad")
public class AdmRelateTadDad implements java.io.Serializable {

	private String relateTadDadSeq;
	private AdmAdPool admAdPool;
	private AdmDefineAd admDefineAd;
	private AdmTemplateAd admTemplateAd;
	private int defineAdOrder;

	public AdmRelateTadDad() {
	}

	public AdmRelateTadDad(String relateTadDadSeq, AdmAdPool admAdPool, AdmDefineAd admDefineAd,
			AdmTemplateAd admTemplateAd, int defineAdOrder) {
		this.relateTadDadSeq = relateTadDadSeq;
		this.admAdPool = admAdPool;
		this.admDefineAd = admDefineAd;
		this.admTemplateAd = admTemplateAd;
		this.defineAdOrder = defineAdOrder;
	}

	@Id

	@Column(name = "relate_tad_dad_seq", unique = true, nullable = false, length = 20)
	public String getRelateTadDadSeq() {
		return this.relateTadDadSeq;
	}

	public void setRelateTadDadSeq(String relateTadDadSeq) {
		this.relateTadDadSeq = relateTadDadSeq;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ad_pool_seq", nullable = false)
	public AdmAdPool getAdmAdPool() {
		return this.admAdPool;
	}

	public void setAdmAdPool(AdmAdPool admAdPool) {
		this.admAdPool = admAdPool;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "define_ad_seq", nullable = false)
	public AdmDefineAd getAdmDefineAd() {
		return this.admDefineAd;
	}

	public void setAdmDefineAd(AdmDefineAd admDefineAd) {
		this.admDefineAd = admDefineAd;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_ad_seq", nullable = false)
	public AdmTemplateAd getAdmTemplateAd() {
		return this.admTemplateAd;
	}

	public void setAdmTemplateAd(AdmTemplateAd admTemplateAd) {
		this.admTemplateAd = admTemplateAd;
	}

	@Column(name = "define_ad_order", nullable = false)
	public int getDefineAdOrder() {
		return this.defineAdOrder;
	}

	public void setDefineAdOrder(int defineAdOrder) {
		this.defineAdOrder = defineAdOrder;
	}

}
