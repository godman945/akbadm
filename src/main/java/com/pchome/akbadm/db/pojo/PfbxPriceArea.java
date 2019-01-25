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
 * PfbxPriceArea generated by hbm2java
 */
@Entity
@Table(name = "pfbx_price_area")
public class PfbxPriceArea implements java.io.Serializable {

	private String AId;
	private PfbxUserPrice pfbxUserPrice;
	private PfbxArea pfbxArea;

	public PfbxPriceArea() {
	}

	public PfbxPriceArea(String AId, PfbxUserPrice pfbxUserPrice, PfbxArea pfbxArea) {
		this.AId = AId;
		this.pfbxUserPrice = pfbxUserPrice;
		this.pfbxArea = pfbxArea;
	}

	@Id

	@Column(name = "a_id", unique = true, nullable = false, length = 20)
	public String getAId() {
		return this.AId;
	}

	public void setAId(String AId) {
		this.AId = AId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "p_id", nullable = false)
	public PfbxUserPrice getPfbxUserPrice() {
		return this.pfbxUserPrice;
	}

	public void setPfbxUserPrice(PfbxUserPrice pfbxUserPrice) {
		this.pfbxUserPrice = pfbxUserPrice;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_ref", nullable = false)
	public PfbxArea getPfbxArea() {
		return this.pfbxArea;
	}

	public void setPfbxArea(PfbxArea pfbxArea) {
		this.pfbxArea = pfbxArea;
	}

}
