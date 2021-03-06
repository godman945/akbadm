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
 * PfbxOptionSize generated by hbm2java
 */
@Entity
@Table(name = "pfbx_option_size")
public class PfbxOptionSize implements java.io.Serializable {

	private String id;
	private PfbxUserOption pfbxUserOption;
	private PfbxSize pfbxSize;

	public PfbxOptionSize() {
	}

	public PfbxOptionSize(String id, PfbxUserOption pfbxUserOption, PfbxSize pfbxSize) {
		this.id = id;
		this.pfbxUserOption = pfbxUserOption;
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
	@JoinColumn(name = "o_id", nullable = false)
	public PfbxUserOption getPfbxUserOption() {
		return this.pfbxUserOption;
	}

	public void setPfbxUserOption(PfbxUserOption pfbxUserOption) {
		this.pfbxUserOption = pfbxUserOption;
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
