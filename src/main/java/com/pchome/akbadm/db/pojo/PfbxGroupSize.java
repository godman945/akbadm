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
 * PfbxGroupSize generated by hbm2java
 */
@Entity
@Table(name = "pfbx_group_size")
public class PfbxGroupSize implements java.io.Serializable {

	private String id;
	private PfbxUserGroup pfbxUserGroup;
	private PfbxSize pfbxSize;

	public PfbxGroupSize() {
	}

	public PfbxGroupSize(String id, PfbxUserGroup pfbxUserGroup, PfbxSize pfbxSize) {
		this.id = id;
		this.pfbxUserGroup = pfbxUserGroup;
		this.pfbxSize = pfbxSize;
	}

	@Id

	@Column(name = "id", unique = true, nullable = false, length = 20)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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
	@JoinColumn(name = "size_ref", nullable = false)
	public PfbxSize getPfbxSize() {
		return this.pfbxSize;
	}

	public void setPfbxSize(PfbxSize pfbxSize) {
		this.pfbxSize = pfbxSize;
	}

}
