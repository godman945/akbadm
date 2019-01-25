package com.pchome.akbadm.db.pojo;
// Generated 2018/7/30 �U�� 06:36:43 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * PfbxAssignPrice generated by hbm2java
 */
@Entity
@Table(name = "pfbx_assign_price")
public class PfbxAssignPrice implements java.io.Serializable {

	private String assignPriceId;
	private PfbxAssignBuyer pfbxAssignBuyer;
	private String positionType;
	private String priceType;
	private BigDecimal assignPositionPrice;
	private BigDecimal assignPrice;
	private Set<PfbxUserPrice> pfbxUserPrices = new HashSet<PfbxUserPrice>(0);

	public PfbxAssignPrice() {
	}

	public PfbxAssignPrice(String assignPriceId, PfbxAssignBuyer pfbxAssignBuyer, String positionType,
			String priceType) {
		this.assignPriceId = assignPriceId;
		this.pfbxAssignBuyer = pfbxAssignBuyer;
		this.positionType = positionType;
		this.priceType = priceType;
	}

	public PfbxAssignPrice(String assignPriceId, PfbxAssignBuyer pfbxAssignBuyer, String positionType, String priceType,
			BigDecimal assignPositionPrice, BigDecimal assignPrice, Set<PfbxUserPrice> pfbxUserPrices) {
		this.assignPriceId = assignPriceId;
		this.pfbxAssignBuyer = pfbxAssignBuyer;
		this.positionType = positionType;
		this.priceType = priceType;
		this.assignPositionPrice = assignPositionPrice;
		this.assignPrice = assignPrice;
		this.pfbxUserPrices = pfbxUserPrices;
	}

	@Id

	@Column(name = "assign_price_id", unique = true, nullable = false, length = 20)
	public String getAssignPriceId() {
		return this.assignPriceId;
	}

	public void setAssignPriceId(String assignPriceId) {
		this.assignPriceId = assignPriceId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ad_request_ref", nullable = false)
	public PfbxAssignBuyer getPfbxAssignBuyer() {
		return this.pfbxAssignBuyer;
	}

	public void setPfbxAssignBuyer(PfbxAssignBuyer pfbxAssignBuyer) {
		this.pfbxAssignBuyer = pfbxAssignBuyer;
	}

	@Column(name = "position_type", nullable = false, length = 1)
	public String getPositionType() {
		return this.positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	@Column(name = "price_type", nullable = false, length = 1)
	public String getPriceType() {
		return this.priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	@Column(name = "assign_position_price", precision = 10)
	public BigDecimal getAssignPositionPrice() {
		return this.assignPositionPrice;
	}

	public void setAssignPositionPrice(BigDecimal assignPositionPrice) {
		this.assignPositionPrice = assignPositionPrice;
	}

	@Column(name = "assign_price", precision = 10)
	public BigDecimal getAssignPrice() {
		return this.assignPrice;
	}

	public void setAssignPrice(BigDecimal assignPrice) {
		this.assignPrice = assignPrice;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfbxAssignPrice")
	public Set<PfbxUserPrice> getPfbxUserPrices() {
		return this.pfbxUserPrices;
	}

	public void setPfbxUserPrices(Set<PfbxUserPrice> pfbxUserPrices) {
		this.pfbxUserPrices = pfbxUserPrices;
	}

}
