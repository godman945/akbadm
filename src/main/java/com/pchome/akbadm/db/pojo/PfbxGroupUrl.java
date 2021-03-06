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
 * PfbxGroupUrl generated by hbm2java
 */
@Entity
@Table(name = "pfbx_group_url")
public class PfbxGroupUrl implements java.io.Serializable {

	private String UId;
	private PfbxUserGroup pfbxUserGroup;
	private PfbxUrl pfbxUrl;

	public PfbxGroupUrl() {
	}

	public PfbxGroupUrl(String UId, PfbxUserGroup pfbxUserGroup, PfbxUrl pfbxUrl) {
		this.UId = UId;
		this.pfbxUserGroup = pfbxUserGroup;
		this.pfbxUrl = pfbxUrl;
	}

	@Id

	@Column(name = "u_id", unique = true, nullable = false, length = 20)
	public String getUId() {
		return this.UId;
	}

	public void setUId(String UId) {
		this.UId = UId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "g_id", nullable = false)
	public PfbxUserGroup getPfbxUserGroup() {
		return this.pfbxUserGroup;
	}

	public void setPfbxUserGroup(PfbxUserGroup pfbxUserGroup) {
		this.pfbxUserGroup = pfbxUserGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "url_ref", nullable = false)
	public PfbxUrl getPfbxUrl() {
		return this.pfbxUrl;
	}

	public void setPfbxUrl(PfbxUrl pfbxUrl) {
		this.pfbxUrl = pfbxUrl;
	}

}
