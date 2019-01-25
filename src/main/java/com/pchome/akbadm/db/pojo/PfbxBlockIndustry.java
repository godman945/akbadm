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
 * PfbxBlockIndustry generated by hbm2java
 */
@Entity
@Table(name = "pfbx_block_industry")
public class PfbxBlockIndustry implements java.io.Serializable {

	private String id;
	private PfpAdCategoryNew pfpAdCategoryNew;
	private PfbxUserOption pfbxUserOption;

	public PfbxBlockIndustry() {
	}

	public PfbxBlockIndustry(String id, PfpAdCategoryNew pfpAdCategoryNew, PfbxUserOption pfbxUserOption) {
		this.id = id;
		this.pfpAdCategoryNew = pfpAdCategoryNew;
		this.pfbxUserOption = pfbxUserOption;
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
	@JoinColumn(name = "industry_ref", nullable = false)
	public PfpAdCategoryNew getPfpAdCategoryNew() {
		return this.pfpAdCategoryNew;
	}

	public void setPfpAdCategoryNew(PfpAdCategoryNew pfpAdCategoryNew) {
		this.pfpAdCategoryNew = pfpAdCategoryNew;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "o_id", nullable = false)
	public PfbxUserOption getPfbxUserOption() {
		return this.pfbxUserOption;
	}

	public void setPfbxUserOption(PfbxUserOption pfbxUserOption) {
		this.pfbxUserOption = pfbxUserOption;
	}

}
